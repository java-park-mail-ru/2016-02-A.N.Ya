package account;

import base.SessionService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by morev on 03.03.16.
 */
public class SessionServiceImpl implements SessionService {

    @SuppressWarnings("ConstantNamingConvention")
    private static final Logger logger = LogManager.getLogger(SessionServiceImpl.class);
    private final Map<String, UserProfile> sessions = new HashMap<>();

    @Override
    public void newSession(String sessionId, UserProfile user) {
        logger.debug("newSession \"" + sessionId + "\", login \"" + user.getLogin() + '"');
        sessions.put(sessionId, user);
        logger.debug("created session with id " + sessionId + "\" of login\"" + user.getLogin() + '"');
    }

    @Override
    @Nullable
    public UserProfile getUserById(String sessionId) {
        logger.debug("getUserById \"\" + sessionId + '\"'");
        if (sessions.containsKey(sessionId)) {
            final UserProfile userProfile = sessions.get(sessionId);
            logger.debug("    found user \"" + userProfile.getId() + "\" with login " + userProfile.getLogin());
            return userProfile;
        }
        else {
            logger.debug("    no such id");
            logger.debug("    num of sessions = " + sessions.size());
            return null;
        }
    }

    @Override
    public void deleteSession(String sessionId) {
        logger.debug("SessionServiceImpl - deleteSession \"" + sessionId + '"');
        if (sessions.containsKey(sessionId)) {
            logger.debug("    success");
            sessions.remove(sessionId);
        } else {
            System.err.println("    no session to remove");
        }

    }

    @Override
    public Map<String, UserProfile> getAllSessions(){
        return sessions;
    }


}
