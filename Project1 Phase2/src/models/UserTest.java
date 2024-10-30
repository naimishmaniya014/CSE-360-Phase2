package models;

public class UserTest {

    public static void main(String[] args) {
        UserTest tester = new UserTest();
        tester.runTests();
    }

    public void runTests() {
        System.out.println("Running User class tests...");

        testUserConstructor();
        testSetPassword();
        testAddRole();
        testRemoveRole();
        testOtpExpiration();

        System.out.println("Tests completed.");
    }

    public void testUserConstructor() {
        System.out.println("\nTest: User Constructor");
        User user = new User("testUser", "password123");
        
        if ("testUser".equals(user.getUsername()) && "password123".equals(user.getPassword())) {
            System.out.println("Passed: User constructor works correctly.");
        } else {
            System.out.println("Failed: User constructor failed.");
        }
    }

    public void testSetPassword() {
        System.out.println("\nTest: Set Password");
        User user = new User("testUser", "password123");
        user.setPassword("newPassword123");
        
        if ("newPassword123".equals(user.getPassword())) {
            System.out.println("Passed: Set password works.");
        } else {
            System.out.println("Failed: Set password failed.");
        }
    }

    public void testAddRole() {
        System.out.println("\nTest: Add Role");
        User user = new User("testUser", "password123");
        user.addRole(Role.ADMIN);
        
        if (user.getRoles().contains(Role.ADMIN)) {
            System.out.println("Passed: Role added successfully.");
        } else {
            System.out.println("Failed: Role was not added.");
        }
    }

    public void testRemoveRole() {
        System.out.println("\nTest: Remove Role");
        User user = new User("testUser", "password123");
        user.addRole(Role.ADMIN);
        user.removeRole(Role.ADMIN);
        
        if (!user.getRoles().contains(Role.ADMIN)) {
            System.out.println("Passed: Role removed successfully.");
        } else {
            System.out.println("Failed: Role was not removed.");
        }
    }

    public void testOtpExpiration() {
        System.out.println("\nTest: OTP Expiration");
        User user = new User("testUser", "password123");
        user.setOtpExpiration(java.time.LocalDateTime.now());
        
        if (user.getOtpExpiration() != null) {
            System.out.println("Passed: OTP expiration set correctly.");
        } else {
            System.out.println("Failed: OTP expiration not set.");
        }
    }
}
