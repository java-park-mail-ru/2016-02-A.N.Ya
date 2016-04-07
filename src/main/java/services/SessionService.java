package services;

import org.jetbrains.annotations.Nullable;
import main.UserProfile;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by morev on 03.03.16.
 */
public class SessionService {
    private final Map<String, UserProfile> sessions = new HashMap<>();

    public SessionService() {
        System.out.println("SessionService - SessionService");
    }

    public void newSession(String sessionId, UserProfile user) {
        System.out.println("SessionService - newSession \"" + sessionId + "\", login \"" + user.getLogin() + '"');
        sessions.put(sessionId, user);
        System.out.println("    created session with id \"" + sessionId + "\" of login\"" + user.getLogin() + '"');
    }

    @Nullable
    public UserProfile getUserById(String sessionId) {
        System.out.println("SessionService - getUserById \"" + sessionId + '"');
        if (sessions.containsKey(sessionId)) {
            final UserProfile userProfile = sessions.get(sessionId);
            System.out.println("    found user \"" + userProfile.getId() + "\" with login " + userProfile.getLogin());
            return userProfile;
        }
        else {
            System.out.println("    no such id");
            System.out.println("    num of sessions = " + sessions.size());
            return null;
        }
    }

    public void deleteSession(String sessionId) {
        System.out.println("SessionService - deleteSession \"" + sessionId + '"');
        if (sessions.containsKey(sessionId)) {
            System.out.println("    success");
            sessions.remove(sessionId);
        } else {
            System.err.println("    no session to remove");
        }

    }

    public Map<String, UserProfile> getAllSessions(){
        return sessions;
    }


}
