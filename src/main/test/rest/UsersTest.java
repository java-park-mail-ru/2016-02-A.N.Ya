package rest;

import main.RestApplication;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.core.Application;

import static org.junit.Assert.assertEquals;

/**
 * Created by morev on 17.03.16.
 */
public class UsersTest extends JerseyTest {
    @Override
    protected Application configure()
    {
        return new RestApplication();
    }


    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void getAdmin() {
        final String adminJson = target("user").path("admin").request().get(String.class);
        assertEquals("{\"login\":\"admin\",\"password\":\"admin\"}", adminJson);
    }

    @Test
    public void testCreateUser() throws Exception {

    }

    @Test
    public void testGetUserById() {
        final String userJson = target("user").path("admin").request().get(String.class);
        assertEquals("{\"login\":\"user\",\"password\":\"12345\"}", userJson);
    }

    @Test
    public void testModifyUser() throws Exception {

    }

    @Test
    public void testDeleteUser() throws Exception {

    }

    @Test
    public void testGetAllUsers() throws Exception {

    }
}