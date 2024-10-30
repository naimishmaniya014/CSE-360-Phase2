package models;

public class RoleTest {

    public static void main(String[] args) {
        RoleTest tester = new RoleTest();
        tester.runTests();
    }

    public void runTests() {
        System.out.println("Running Role enum tests...");

        testRoleAdmin();
        testRoleStudent();
        testRoleInstructor();

        System.out.println("Tests completed.");
    }

    public void testRoleAdmin() {
        System.out.println("\nTest: Role ADMIN");
        Role role = Role.ADMIN;

        if ("A".equals(role.getCode())) {
            System.out.println("Passed: ADMIN role code is correct.");
        } else {
            System.out.println("Failed: ADMIN role code is incorrect.");
        }
    }

    public void testRoleStudent() {
        System.out.println("\nTest: Role STUDENT");
        Role role = Role.STUDENT;

        if ("S".equals(role.getCode())) {
            System.out.println("Passed: STUDENT role code is correct.");
        } else {
            System.out.println("Failed: STUDENT role code is incorrect.");
        }
    }

    public void testRoleInstructor() {
        System.out.println("\nTest: Role INSTRUCTOR");
        Role role = Role.INSTRUCTOR;

        if ("I".equals(role.getCode())) {
            System.out.println("Passed: INSTRUCTOR role code is correct.");
        } else {
            System.out.println("Failed: INSTRUCTOR role code is incorrect.");
        }
    }
}
