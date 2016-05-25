package base;

import account.UserProfile;
import org.jetbrains.annotations.Nullable;

import java.util.Map;


public interface SessionService {
    void newSession(String sessionId, UserProfile user);

    @Nullable
    UserProfile getUserById(String sessionId);

    void deleteSession(String sessionId);

    Map<String, UserProfile> getAllSessions();
}
