package main;

import rest.UserProfile;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


public class AccountService {
    private static long idCounter;

    private Map<Long, UserProfile> users = new HashMap<>();


    public AccountService() {
        users.put(1L, new UserProfile("admin", "admin", "a@a.ru"));
        users.get(1L).setId(1L);
        users.put(2L, new UserProfile("guest", "12345", "sg@sg.com"));
        idCounter = 2;
    }

    public Collection<UserProfile> getAllUsers() {
        return users.values();
    }

    public long addUser(UserProfile userProfile) {
        if (users.containsValue(userProfile)) {
            return -1;
        } else {
            idCounter++;
            users.put(idCounter, userProfile);
            users.get(idCounter).setId(idCounter);
            return idCounter;
        }
    }

    public UserProfile getUser(long userID) {
        return users.get(userID);
    }

    public UserProfile getUser(String login) {
        return users.get(this.getID(login));
    }

    public boolean modifyUser(long userID, UserProfile userProfile) {
        if (!users.containsKey(userID))
            return false;
        users.replace(userID, userProfile);
        return true;
    }

    public boolean deleteUser(long userID) {
        if (users.containsKey(userID)) {
            users.remove(userID);
            return true;
        }
        return false;
    }

    private long getID(String login) {
        for (Map.Entry<Long, UserProfile> user: users.entrySet())
            if (user.getValue().getLogin() == login)
                return user.getKey();
        return -1;
    }
}
