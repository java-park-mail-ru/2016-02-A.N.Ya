package main;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import rest.UserProfile;
import services.AccountServiceOnHashMap;
import services.SessionService;

import javax.validation.constraints.AssertTrue;
import java.util.ArrayList;
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

    private UserProfile adminUser;
    private UserProfile testUser;

    private String session_id_admin;
    private String session_id_1;
    private String session_id_non_existing;


    @Before
    public void mySetUp() {
        sessionService = new SessionService();

        session_id_admin = "session_id_admin";
        session_id_1 = "session_id_1";
        session_id_non_existing = "session_id_non_existing";

        adminUser = new UserProfile("admin", "admin", "best@awesome_admins.com");
        adminUser.setId(1);
        testUser = new UserProfile("test", "12345", "sg@sg.com");
        testUser.setId(2);



        sessions = new HashMap<String, UserProfile>();
        sessions.put(session_id_admin, adminUser);
        sessions.put(session_id_1, testUser);

        // TODO FIX
        sessionService.deleteSession("mock_id");

        sessionService.newSession(session_id_admin, adminUser);
        sessionService.newSession(session_id_1, testUser);
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
        assertTrue(sessionService.getAllSessions().containsKey(session_id_1));
        sessionService.newSession(session_id_1, testUser);
        assertTrue(sessionService.getAllSessions().containsKey(session_id_1));
    }

    @Test
    public void testNewSession() {
        assertFalse(sessionService.getAllSessions().containsKey(session_id_non_existing));
        sessionService.newSession(session_id_non_existing, testUser);
        assertTrue(sessionService.getAllSessions().containsKey(session_id_non_existing));
    }

    @Test
    public void testGetUserByNonExistingSessionId() {
        assertFalse(sessionService.getAllSessions().containsKey(session_id_non_existing));
        UserProfile userProfile = sessionService.getUserById(session_id_non_existing);
        assertEquals(null, userProfile);
    }

    @Test
    public void testGetUserBySessionId() {
        assertTrue(sessionService.getAllSessions().containsKey(session_id_1));
        UserProfile userProfile = sessionService.getUserById(session_id_1);
        assertEquals(testUser, userProfile);
    }

    @Test
    public void testDeleteNonExistingSession() {
        assertFalse(sessionService.getAllSessions().containsKey(session_id_non_existing));
        sessionService.deleteSession(session_id_non_existing);
        assertFalse(sessionService.getAllSessions().containsKey(session_id_non_existing));
    }

    @Test
    public void testDeleteSession() {
        assertTrue(sessionService.getAllSessions().containsKey(session_id_1));
        sessionService.deleteSession(session_id_1);
        assertFalse(sessionService.getAllSessions().containsKey(session_id_1));
    }
}
