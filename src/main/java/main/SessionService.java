package main;

import org.jetbrains.annotations.Nullable;
import rest.UserProfile;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by morev on 03.03.16.
 */
public class SessionService {
    private final Map<String, UserProfile> sessions = new HashMap<>();

    public void newSession(String sessionId, UserProfile user) {
        if (sessions.containsKey(sessionId))
            System.err.println("Session already exists!");
        else
            sessions.put(sessionId, user);
    }

    @Nullable
    public UserProfile getUserById(String sessionId) {
        if (sessions.containsKey(sessionId))
            return sessions.get(sessionId);
        else {
            System.err.println("No session to get");
            return null;
        }
    }

    public void deleteSession(String sessionId) {
        if (sessions.containsKey(sessionId))
            sessions.remove(sessionId);
        else {
            System.err.println("No session to remove");
        }

    }


}
