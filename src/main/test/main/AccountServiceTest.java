package main;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import rest.UserProfile;
import services.AccountService;
import services.AccountServiceOnHashMap;

import javax.jws.soap.SOAPBinding;
import java.util.ArrayList;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by morev on 06.04.16.
 */
public class AccountServiceTest {
    private AccountService accountService;

    Collection<UserProfile> userProfiles;

    private UserProfile adminUser;
    private UserProfile testUser;
    private UserProfile nonExistingUser;


    @Before
    public void mySetUp() {
        accountService = new AccountServiceOnHashMap();

        adminUser = new UserProfile("admin", "admin", "best@awesome_admins.com");
        testUser = new UserProfile("test", "12345", "sg@sg.com");
        nonExistingUser = new UserProfile("Schrödinger", "Erwin", "alive@dead.net");

        accountService.addUser(adminUser);
        accountService.addUser(testUser);

        userProfiles = new ArrayList<UserProfile>();
        userProfiles.add(adminUser);
        userProfiles.add(testUser);
    }

    @Test
    public void testGetAllUsers() {
        Collection<UserProfile> actual = accountService.getAllUsers();
        assertTrue( actual.containsAll(userProfiles) );
        assertTrue( userProfiles.containsAll(actual));
    }

    @Test
    public void testGetNonExistingUserById() {
        UserProfile actual = accountService.getUser(nonExistingUser.getId());
        assertEquals(null, actual);
    }

    @Test
    public void testGetExistingUserById() {
        UserProfile actual = accountService.getUser(testUser.getId());
        assertEquals(testUser, actual);
    }

    @Test
    public void testGetNonExistingUserByLogin() {
        UserProfile actual = accountService.getUser(nonExistingUser.getLogin());
        assertEquals(null, actual);
    }

    @Test
    public void testGetExistingUserByLogin() {
        UserProfile actual = accountService.getUser(testUser.getLogin());
        assertEquals(testUser, actual);
    }

    @Test
    public void testAddNewUser() {
        assertFalse(accountService.getAllUsers().contains(nonExistingUser));
        long id = accountService.addUser(nonExistingUser);
        assertEquals(userProfiles.size() + 1, id);
        assertTrue(accountService.getAllUsers().contains(nonExistingUser));
    }

    @Test
    public void testAddExistingUser() {
        assertTrue(accountService.getAllUsers().contains(testUser));
        long id = accountService.addUser(testUser);
        assertEquals(-1, id);
        assertTrue(accountService.getAllUsers().contains(testUser));
    }

    @Test
    public void modifyNonExistingUser() {
        boolean result = accountService.modifyUser(nonExistingUser.getId(), nonExistingUser);
        assertFalse(result);
    }

    @Test
    public void modifyWrongId() {
        boolean result = accountService.modifyUser(nonExistingUser.getId(), testUser);
        assertFalse(result);
    }

    @Test
    public void modifyLogin() {
        UserProfile modifyedUser = new UserProfile(
                testUser.getLogin() + "_modifyed",
                testUser.getPassword(),
                testUser.getEmail()
        );
        boolean result = accountService.modifyUser(testUser.getId(), modifyedUser);
        assertFalse(result);
        assertTrue(accountService.getAllUsers().contains(testUser));
    }

    @Test
    public void modifyUser() {
        UserProfile modifyedUser = new UserProfile(
                testUser.getLogin(),
                testUser.getPassword() + "_modifyed",
                testUser.getEmail()
        );
        boolean result = accountService.modifyUser(testUser.getId(), modifyedUser);
        assertTrue(result);
        assertTrue(accountService.getAllUsers().contains(modifyedUser));
        ///assertFalse(accountService.getAllUsers().contains(testUser)); TODO // FIXME: 06.04.16 
    }

    @Test
    public void deleteNonExistingUser() {
        assertFalse(accountService.getAllUsers().contains(nonExistingUser));
        boolean result = accountService.deleteUser(nonExistingUser.getId());
        assertFalse(result);
        assertFalse(accountService.getAllUsers().contains(nonExistingUser));
    }

    @Test
    public void deleteUser() {
        assertTrue(accountService.getAllUsers().contains(testUser));
        boolean result = accountService.deleteUser(testUser.getId());
        assertTrue(result);
        assertFalse(accountService.getAllUsers().contains(testUser));
    }
}