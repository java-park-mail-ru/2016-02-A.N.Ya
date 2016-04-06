package rest;

import main.Context;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Before;
import org.junit.Test;
import services.AccountService;
import services.AccountServiceOnHashMap;
import services.SessionService;

import javax.json.Json;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
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



    @Override
    protected Application configure()
    {
        adminUser = new UserProfile("admin", "admin", "best@awesome_admins.com");
        testUser = new UserProfile("test", "12345", "sg@sg.com");
        loggedUser = new UserProfile("logged", "qwerty", "loh@test.com");

        accountService = new AccountServiceOnHashMap();
        accountService.addUser(adminUser);
        accountService.addUser(testUser);
        accountService.addUser(loggedUser);

        sessionService = new SessionService();

        main.Context context = new Context();
        context.add(AccountService.class, accountService);
        context.add(SessionService.class, sessionService);

        Set<Class<?>> classes = new HashSet<>();
        classes.add(Users.class);
        classes.add(Sessions.class);
        final ResourceConfig config = new ResourceConfig(classes);
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
    public void mySetUp() throws Exception {
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
    public void testCreateExistingUserById() {
        final Response actual = target("user").path(String.valueOf(testUser.getId())).request().get();
        assertEquals(Response.Status.UNAUTHORIZED.getStatusCode(), actual.getStatus());
    }


    @Test
    public void testIsLogged() throws Exception {
        final String actual = target("session").request().get(String.class);
        final String expected = Json.createObjectBuilder()
                                .add("id", loggedUser.getId())
                                .build()
                                .toString();
        assertEquals(expected, actual);
    }


}