package Utilities;

import models.User;
import models.Role;

public class UserManagerTest {

    public static void main(String[] args) {
        UserManagerTest tester = new UserManagerTest();
        tester.runTests();
    }

    public void runTests() {
        System.out.println("Running UserManager tests...");
        
        testAddUser();
        testIsUsernameTaken();
        testAuthenticate();
        testResetPassword();
        
        System.out.println("Tests completed.");
    }

    public void testAddUser() {
        System.out.println("\nTest: Add User");
        UserManager userManager = UserManager.getInstance();
        User user = new User("testUser", "password123");

        userManager.addUser(user);
        if (userManager.getUserByUsername("testUser") != null) {
            System.out.println("Passed: User was added successfully.");
        } else {
            System.out.println("Failed: User was not added.");
        }
    }

    public void testIsUsernameTaken() {
        System.out.println("\nTest: Is Username Taken");
        UserManager userManager = UserManager.getInstance();
        User user = new User("testUser2", "password123");
        userManager.addUser(user);

        if (userManager.isUsernameTaken("testUser2")) {
            System.out.println("Passed: Username is correctly marked as taken.");
        } else {
            System.out.println("Failed: Username is not marked as taken.");
        }
    }

    public void testAuthenticate() {
        System.out.println("\nTest: Authenticate User");
        UserManager userManager = UserManager.getInstance();
        User user = new User("testUser3", "password123");
        userManager.addUser(user);

        if (userManager.authenticate("testUser3", "password123") != null) {
            System.out.println("Passed: User was authenticated successfully.");
        } else {
            System.out.println("Failed: Authentication failed.");
        }
    }

    public void testResetPassword() {
        System.out.println("\nTest: Reset Password");
        UserManager userManager = UserManager.getInstance();
        User user = new User("testUser4", "password123");
        userManager.addUser(user);

        userManager.resetPassword("testUser4");
        if (user.isResetRequired()) {
            System.out.println("Passed: Password reset successfully.");
        } else {
            System.out.println("Failed: Password reset did not occur.");
        }
    }
}
