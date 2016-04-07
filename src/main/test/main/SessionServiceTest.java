package main;

import org.junit.Before;
import org.junit.Test;
import services.SessionService;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by morev on 06.04.16.
 */
public class SessionServiceTest {
    private SessionService sessionService;

    private Map<String, UserProfile> sessions;

    private UserProfile testUser;


    private String sessionIdAdmin;
    private String sessionId1;
    private String sessionIdNonExisting;


    @Before
    public void mySetUp() {
        sessionService = new SessionService();

        sessionIdAdmin = "sessionIdAdmin";
        sessionId1 = "sessionId1";
        sessionIdNonExisting = "sessionIdNonExisting";

        UserProfile adminUser = new UserProfile("admin", "admin", "best@awesome_admins.com");
        adminUser.setId(1);
        testUser = new UserProfile("test", "12345", "sg@sg.com");
        testUser.setId(2);



        sessions = new HashMap<>();
        sessions.put(sessionIdAdmin, adminUser);
        sessions.put(sessionId1, testUser);

        sessionService.deleteSession("mock_id");

        sessionService.newSession(sessionIdAdmin, adminUser);
        sessionService.newSession(sessionId1, testUser);
    }


    @Test
    public void testGetAllSessions() {
        Map<String, UserProfile> map = sessionService.getAllSessions();
        for (String id: map.keySet()) {
            assertTrue(sessions.containsKey(id));
            assertEquals(sessions.get(id), map.get(id));
        }
        for (String id: sessions.keySet()) {
            assertTrue(map.containsKey(id));
            assertEquals(map.get(id), sessions.get(id));
        }
    }


    @Test
    public void testNewExistingSession() {
        assertTrue(sessionService.getAllSessions().containsKey(sessionId1));
        sessionService.newSession(sessionId1, testUser);
        assertTrue(sessionService.getAllSessions().containsKey(sessionId1));
    }

    @Test
    public void testNewSession() {
        assertFalse(sessionService.getAllSessions().containsKey(sessionIdNonExisting));
        sessionService.newSession(sessionIdNonExisting, testUser);
        assertTrue(sessionService.getAllSessions().containsKey(sessionIdNonExisting));
    }

    @Test
    public void testGetUserByNonExistingId() {
        assertFalse(sessionService.getAllSessions().containsKey(sessionIdNonExisting));
        UserProfile userProfile = sessionService.getUserById(sessionIdNonExisting);
        assertEquals(null, userProfile);
    }

    @Test
    public void testGetUserBySessionId() {
        assertTrue(sessionService.getAllSessions().containsKey(sessionId1));
        assertTrue(sessionService.getAllSessions().containsKey(sessionIdAdmin));
        UserProfile userProfile = sessionService.getUserById(sessionId1);
        assertEquals(testUser, userProfile);
    }

    @Test
    public void testDeleteNonExistingSession() {
        assertFalse(sessionService.getAllSessions().containsKey(sessionIdNonExisting));
        sessionService.deleteSession(sessionIdNonExisting);
        assertFalse(sessionService.getAllSessions().containsKey(sessionIdNonExisting));
    }

    @Test
    public void testDeleteSession() {
        assertTrue(sessionService.getAllSessions().containsKey(sessionId1));
        sessionService.deleteSession(sessionId1);
        assertFalse(sessionService.getAllSessions().containsKey(sessionId1));
    }
}
