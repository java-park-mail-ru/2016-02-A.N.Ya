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

    }

    public Collection<UserProfile> getAllUsers() {
        System.out.println("AccountServiceOnHashMap - getAllUsers");
        return usersById.values();
    }

    public UserProfile getUser(long userID) {
        System.out.println("AccountServiceOnHashMap - getUser " + userID);
        return usersById.get(userID);
    }

    public UserProfile getUser(String login) {
        System.out.println("AccountServiceOnHashMap - getUser \"" + login + "\"");
        return usersByName.get(login);
    }

    public long addUser(UserProfile user) {
        System.out.println("AccountServiceOnHashMap - addUser \"" + user.getLogin() + "\"");
        if (usersById.containsValue(user)) {
            System.out.println("    already exists");
            return -1;
        } else {
            idCounter++;
            user.setId(idCounter);
            usersById.put(idCounter, user);
            usersByName.put(user.getLogin(), user);
            System.out.println("    added user with id " + idCounter);
            return idCounter;
        }
    }

    @SuppressWarnings("UnusedReturnValue")
    public boolean modifyUser(long userID, UserProfile user) {
        System.out.println("AccountServiceOnHashMap - modifyUser");
        if (!usersById.containsKey(userID)) {
            System.err.println("    no such user");
            return false;
        }
        if (!user.getLogin().equals(usersById.get(userID).getLogin())) {
            System.err.println("    attempt to change login");
            return false;
        }
        user.setId(userID);
        usersById.replace(userID, user);
        usersByName.replace(user.getLogin(), user);
        System.err.println("    modyfied");
        return true;
    }

    public boolean deleteUser(long userID) {
        System.out.println("AccountServiceOnHashMap - deleteUser " + userID);
        if (usersById.containsKey(userID)) {
            usersByName.remove(usersById.get(userID).getLogin());
            usersById.remove(userID);
            System.out.println("    user removed");
            return true;
        } else {
            System.err.println("    no such user");
            return false;
        }
    }
}
