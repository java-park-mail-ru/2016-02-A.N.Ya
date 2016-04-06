package rest;

import main.Context;
import org.mockito.internal.exceptions.ExceptionIncludingMockitoWarnings;
import services.AccountService;
import services.AccountServiceOnHashMap;
import services.SessionService;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.json.Json;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by morev on 17.03.16.
 */
public class UsersTest extends JerseyTest {
    private AccountService accountService;
    private SessionService sessionService;


    private UserProfile adminUser;
    private UserProfile testUser;



    @Override
    protected Application configure()
    {
        adminUser = new UserProfile("admin", "admin", "best@awesome_admins.com");
        testUser = new UserProfile("test", "12345", "sg@sg.com");

        accountService = new AccountServiceOnHashMap();
        accountService.addUser(adminUser);
        accountService.addUser(testUser);
        sessionService = new SessionService();


        final ResourceConfig config = ResourceConfig.forApplication(new RestApplication());
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
    }




    @Test
    public void getAdmin() {
        //final String adminJson = target("user").path("1").request().get(String.class);
        //assertEquals("{\"login\":\"test\",\"password\":\"admin\"}", adminJson);
    }

    @Test
    public void testCreateUser() throws Exception {

    }

    @Test
    public void testGetUserById() {
        final String actual = target("user").path("1").request().get(String.class);
        final String expected = Json.createObjectBuilder()
                .add("id", testUser.getId())
                .add("login", testUser.getLogin())
                .add("email", testUser.getEmail())
                .build()
                .toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testModifyUser() throws Exception {
        //assertEquals(403, response.getStatus());
    }

    @Test
    public void testDeleteUser() throws Exception {

    }

    @Test
    public void testGetAllUsers() throws Exception {

    }

    @Test
    public void testIsLogged() throws Exception {
        final String actual = target("session").request().get(String.class);
        final String expected = Json.createObjectBuilder()
                                .add("id", 1)
                                .build()
                                .toString();
        assertEquals(expected, actual);
    }


}