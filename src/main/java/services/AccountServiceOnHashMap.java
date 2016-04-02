package services;

import rest.UserProfile;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


public class AccountServiceOnHashMap implements AccountService {
    private volatile long idCounter = 0;

    private final Map<Long, UserProfile> usersById = new HashMap<>();
    private final Map<String, UserProfile> usersByName = new HashMap<>();


    public AccountServiceOnHashMap() {
        addUser(new UserProfile("adm    in", "admin", "a@a.ru"));
        addUser(new UserProfile("guest", "12345", "sg@sg.com"));
    }

    public Collection<UserProfile> getAllUsers() {
        return usersById.values();
    }

    public UserProfile getUser(long userID) {
        return usersById.get(userID);
    }

    public UserProfile getUser(String login) {
        return usersByName.get(login);
    }

    public long addUser(UserProfile user) {
        if (usersById.containsValue(user)) {
            return -1;
        } else {
            idCounter++;
            user.setId(idCounter);
            usersById.put(idCounter, user);
            usersByName.put(user.getLogin(), user);
            return idCounter;
        }
    }

    @SuppressWarnings("UnusedReturnValue")
    public boolean modifyUser(long userID, UserProfile user) {
        if (!usersById.containsKey(userID)) {
            System.err.println("No such user to modify!");
            return false;
        }
        if (! user.getLogin().equals(usersById.get(userID).getLogin())) {
            System.err.println("Cannot change username!");
            return false;
        }
        usersById.replace(userID, user);
        usersByName.replace(user.getLogin(), user);
        return true;
    }

    public boolean deleteUser(long userID) {
        if (usersById.containsKey(userID)) {
            usersByName.remove(usersById.get(userID).getLogin());
            usersById.remove(userID);
            return true;
        } else {
            System.err.println("No such user to delete!");
            return false;
        }
    }
}
