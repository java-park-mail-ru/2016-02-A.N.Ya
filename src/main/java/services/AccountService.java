package services;

import rest.UserProfile;

import java.util.Collection;

/**
 * Created by morev on 01.04.16.
 */
public interface AccountService {

    UserProfile getUser(long userID);

    UserProfile getUser(String login);

    long addUser(UserProfile user);

    boolean modifyUser(long userID, UserProfile user);

    boolean deleteUser(long userID);

    Collection<UserProfile> getAllUsers();
}
