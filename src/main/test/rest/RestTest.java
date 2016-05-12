package rest;

import main.Context;
import main.UserProfile;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Before;
import org.junit.Test;
import services.AccountService;
import services.AccountServiceOnHibernate;
import services.SessionService;

import javax.json.Json;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by morev on 17.03.16.
 */
public class RestTest extends JerseyTest {
    private AccountService accountService;
    private SessionService sessionService;


    private UserProfile adminUser;
    private UserProfile testUser;
    private UserProfile loggedUser;
    private UserProfile newUser;



    @Override
    protected Application configure()
    {
        adminUser = new UserProfile("admin", "admin", "best@awesome_admins.com");
        testUser = new UserProfile("test", "12345", "sg@sg.com");
        loggedUser = new UserProfile("logged", "qwerty", "loh@test.com");
        newUser = new UserProfile("new", "password", "1@newbies.com");

        accountService = new AccountServiceOnHibernate(
                "jdbc:mysql://localhost:3306/BloodyDefenceTestDB", "bloodydefender", "stargate");
        accountService.addUser(adminUser);
        accountService.addUser(testUser);
        accountService.addUser(loggedUser);

        sessionService = new SessionService();

        Context context = new Context();
        context.add(AccountService.class, accountService);
        context.add(SessionService.class, sessionService);

        Set<Class<?>> classes = new HashSet<>();
        classes.add(Users.class);
        classes.add(Sessions.class);
        final ResourceConfig config = new ResourceConfig(classes);
        //noinspection AnonymousInnerClassMayBeStatic
        config.register(new AbstractBinder() {
            @Override
            protected void configure() {
                bind(context).to(Context.class);
            }
        });

        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpSession session = mock(HttpSession.class);


        config.register(new AbstractBinder() {
            @Override
            protected void configure() {
                bind(accountService).to(AccountService.class);
                bind(sessionService).to(SessionService.class);
                bind(request).to(HttpServletRequest.class);
                bind(session).to(HttpSession.class);
                when(request.getSession()).thenReturn(session);
                when(session.getId()).thenReturn("mock_id");
            }
        });
        return config;
    }


    @Before
    public void mySetUp() {
        sessionService.newSession("mock_id", loggedUser);
    }

    @Test
    public void testGetAuthorizedUserById() {
        final String actual = target("user").path(String.valueOf(loggedUser.getId())).request().get(String.class);
        final String expected = loggedUser.toString();
        assertEquals(expected, actual);
    }


    @Test
    public void testGetUnauthorizedUserById() {
        final Response actual = target("user").path(String.valueOf(testUser.getId())).request().get();
        assertEquals(Response.Status.UNAUTHORIZED.getStatusCode(), actual.getStatus());
    }


    @Test
    public void testCreateExistingUser() {
        final Response actual = target("user").request().put(Entity.json(testUser));
        assertEquals(Response.Status.FORBIDDEN.getStatusCode(), actual.getStatus());
    }


    @Test
    public void testCreateNewUser() {
        final String expected = Json.createObjectBuilder()
                .add("id", accountService.getAllUsers().size() + 1)
                .build()
                .toString();
        final String actual = target("user").request().put(Entity.json(newUser), String.class);
        assertEquals(expected, actual);
    }

    @Test
    public void modifyUnauthorizedUser() {
        final Response actual = target("user").path(String.valueOf(testUser.getId()))
                .request().post(Entity.json(testUser));
        assertEquals(actual.getStatus(), Response.Status.FORBIDDEN.getStatusCode());
    }

    @Test
    public void modifyForeignUser() {
        final Response actual = target("user").path(String.valueOf(loggedUser.getId()))
                .request().post(Entity.json(testUser));
        assertEquals(actual.getStatus(), Response.Status.FORBIDDEN.getStatusCode());
    }

    @Test
    public void modifyUser() {
        UserProfile modifyedUser = new UserProfile(
                loggedUser.getLogin(),
                loggedUser.getPassword() + "123",
                loggedUser.getEmail() + 'm',
                loggedUser.getId()
        );
        final Response actual = target("user").path(String.valueOf(loggedUser.getId()))
                .request().post(Entity.json(modifyedUser));
        assertEquals(Response.Status.OK.getStatusCode(), actual.getStatus());
        assertEquals(modifyedUser, accountService.getUser(loggedUser.getId()));
    }

    @Test
    public void deleteUnauthorizedUser() {
        final Response actual = target("user").path(String.valueOf(testUser.getId())).request().delete();
        assertEquals(Response.Status.FORBIDDEN.getStatusCode(), actual.getStatus());
        assertEquals(testUser, accountService.getUser(testUser.getLogin()));
    }


    @Test
    public void deleteUser() {
        final Response actual = target("user").path(String.valueOf(loggedUser.getId())).request().delete();
        assertEquals(Response.Status.OK.getStatusCode(), actual.getStatus());
        assertEquals(null, accountService.getUser(loggedUser.getLogin()));
    }


    @Test
    public void testIsLogged() {
        final String actual = target("session").request().get(String.class);
        final String expected = Json.createObjectBuilder()
                                .add("id", loggedUser.getId())
                                .build()
                                .toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testAuthorizedLogin() {
        final String requestJSON = Json.createObjectBuilder()
                                .add("login", loggedUser.getLogin())
                                .add("password", loggedUser.getPassword())
                                .build()
                                .toString();
        final Response actual = target("session").request().put(Entity.json(requestJSON));
        assertEquals(Response.Status.OK.getStatusCode(), actual.getStatus());
        assertEquals(loggedUser, sessionService.getUserById("mock_id"));
    }

    @Test
    public void testNonExistingLogin() {
        final String requestJSON = Json.createObjectBuilder()
                .add("login", newUser.getLogin())
                .add("password", newUser.getPassword())
                .build()
                .toString();
        final Response actual = target("session").request().put(Entity.json(requestJSON));
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), actual.getStatus());
        assertEquals(loggedUser, sessionService.getUserById("mock_id"));
    }


    @Test
    public void testWrongPasswordLogin() {
        final String requestJSON = Json.createObjectBuilder()
                .add("login", adminUser.getLogin())
                .add("password", adminUser.getPassword() + '☺')
                .build()
                .toString();
        final Response actual = target("session").request().put(Entity.json(requestJSON));
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), actual.getStatus());
        assertEquals(loggedUser, sessionService.getUserById("mock_id"));
    }

    @Test
    public void testLogin() {
        final String requestJSON = Json.createObjectBuilder()
                .add("login", adminUser.getLogin())
                .add("password", adminUser.getPassword())
                .build()
                .toString();
        final Response actual = target("session").request().put(Entity.json(requestJSON));
        assertEquals(Response.Status.OK.getStatusCode(), actual.getStatus());
        assertEquals(adminUser, sessionService.getUserById("mock_id"));
    }

    @Test
    public void testLogout() {
        final Response actual = target("session").request().delete();
        assertEquals(Response.Status.OK.getStatusCode(), actual.getStatus());
        assertEquals(null, sessionService.getUserById("mock_id"));
    }

}