package Utilities;

import models.User;
import models.Role;

public class SessionManagerTest {

    public static void main(String[] args) {
        SessionManagerTest tester = new SessionManagerTest();
        tester.runTests();
    }

    public void runTests() {
        System.out.println("Running SessionManager tests...");

        testSetCurrentUser();
        testClearSession();
        testSetCurrentRole();

        System.out.println("Tests completed.");
    }

    public void testSetCurrentUser() {
        System.out.println("\nTest: Set Current User");
        SessionManager sessionManager = SessionManager.getInstance();
        User user = new User("testUser", "password123");

        sessionManager.setCurrentUser(user);

        if (user.equals(sessionManager.getCurrentUser())) {
            System.out.println("Passed: Current user is set correctly.");
        } else {
            System.out.println("Failed: Current user is not set correctly.");
        }
    }

    public void testClearSession() {
        System.out.println("\nTest: Clear Session");
        SessionManager sessionManager = SessionManager.getInstance();
        User user = new User("testUser", "password123");

        sessionManager.setCurrentUser(user);
        sessionManager.clearSession();

        if (sessionManager.getCurrentUser() == null) {
            System.out.println("Passed: Session cleared successfully.");
        } else {
            System.out.println("Failed: Session not cleared.");
        }
    }

    public void testSetCurrentRole() {
        System.out.println("\nTest: Set Current Role");
        SessionManager sessionManager = SessionManager.getInstance();
        User user = new User("testUser", "password123");

        sessionManager.setCurrentUser(user);
        sessionManager.setCurrentRole(Role.ADMIN);

        if (Role.ADMIN.equals(sessionManager.getCurrentRole())) {
            System.out.println("Passed: Current role is set correctly.");
        } else {
            System.out.println("Failed: Current role is not set correctly.");
        }
    }
}
