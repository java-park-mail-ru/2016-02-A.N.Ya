package main;

import rest.UserProfile;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by morev on 03.03.16.
 */
public class SessionService {
    private Map<String, UserProfile> sessions = new HashMap<>();

    public SessionService() {

    }

    public void newSession(String id, UserProfile user) {
        sessions.put(id, user);
    }

    public UserProfile getUserById(String id) {
        return sessions.get(id);
    }

    public void deleteSession(String id) {
        sessions.remove(id);
    }


}
