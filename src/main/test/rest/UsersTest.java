package rest;

import services.AccountService;
import services.AccountServiceOnHashMap;
import services.SessionService;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.Application;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by morev on 17.03.16.
 */
public class UsersTest extends JerseyTest {
    @Override
    protected Application configure()
    {
        final AccountService accountService = new AccountServiceOnHashMap();
        final SessionService sessionService = new SessionService();

        final ResourceConfig config = ResourceConfig.forApplication(new RestApplication());
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpSession session = mock(HttpSession.class);


        config.register(new AbstractBinder() {
            @Override
            protected void configure() {
                bind(request).to(HttpServletRequest.class);
                bind(session).to(HttpSession.class);
                when(request.getSession()).thenReturn(session);
                when(session.getId()).thenReturn("mock_id");

            }
        });
        return config;
    }


    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void getAdmin() {
        final String adminJson = target("user").path("1").request().get(String.class);

        System.out.append(adminJson);
        assertEquals("{\"login\":\"admin\",\"password\":\"admin\"}", adminJson);
    }

    @Test
    public void testCreateUser() throws Exception {

    }

    @Test
    public void testGetUserById() {
        final String userJson = target("user").path("1").request().get(String.class);
        assertEquals("{\"login\":\"user\",\"password\":\"12345\"}", userJson);
    }

    @Test
    public void testModifyUser() throws Exception {
        final String userJson = target("user").path("1").request().get(String.class);
        System.out.println("TestModifyUser.request = " + userJson);
        assertEquals("{\"login\":\"user\",\"password\":\"12345\"}", userJson);
    }

    @Test
    public void testDeleteUser() throws Exception {

    }

    @Test
    public void testGetAllUsers() throws Exception {

    }
}