package services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;
import main.UserProfile;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


public class AccountServiceOnHashMap implements AccountService {
    private static final Logger logger = LogManager.getLogger(SessionService.class);
    private volatile long idCounter = 0;

    private final Map<Long, UserProfile> usersById = new HashMap<>();
    private final Map<String, UserProfile> usersByName = new HashMap<>();


    public AccountServiceOnHashMap() {
        System.out.println("AccountService - AccountService");
    }

    @Override
    public Collection<UserProfile> getAllUsers() {
        logger.info("AccountServiceOnHashMap - getAllUsers");
        return usersById.values();
    }

    @Override
    @Nullable
    public UserProfile getUser(long userID) {
        logger.info("AccountServiceOnHashMap - getUser " + userID);
        return usersById.get(userID);
    }

    @Override
    @Nullable
    public UserProfile getUser(String login) {
        logger.info("AccountServiceOnHashMap - getUser \"" + login + '"');
        return usersByName.get(login);
    }

    @Override
    public long addUser(UserProfile user) {
        logger.info("AccountServiceOnHashMap - addUser \"" + user.getLogin() + '"');
        if (usersById.containsValue(user)) {
            logger.info("    already exists");
            return -1;
        } else {
            idCounter++;
            user.setId(idCounter);
            usersById.put(idCounter, user);
            usersByName.put(user.getLogin(), user);
            logger.info("    added user with id " + idCounter);
            return idCounter;
        }
    }

    @Override
    public boolean modifyUser(long userID, UserProfile user) {
        logger.info("AccountServiceOnHashMap - modifyUser");
        if (!usersById.containsKey(userID)) {
            logger.error("    no such user");
            return false;
        }
        if (!user.getLogin().equals(usersById.get(userID).getLogin())) {
            logger.error("    attempt to change login");
            return false;
        }
        user.setId(userID);
        usersById.replace(userID, user);
        usersByName.replace(user.getLogin(), user);
        logger.error("    modyfied");
        return true;
    }

    @Override
    public boolean deleteUser(long userID) {
        logger.info("AccountServiceOnHashMap - deleteUser " + userID);
        if (usersById.containsKey(userID)) {
            usersByName.remove(usersById.get(userID).getLogin());
            usersById.remove(userID);
            logger.info("    user removed");
            return true;
        } else {
            logger.error("    no such user");
            return false;
        }
    }
}