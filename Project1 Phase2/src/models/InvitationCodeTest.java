package models;

import java.util.Arrays;

public class InvitationCodeTest {

    public static void main(String[] args) {
        InvitationCodeTest tester = new InvitationCodeTest();
        tester.runTests();
    }

    public void runTests() {
        System.out.println("Running InvitationCode class tests...");
        
        testInvitationCode();
        testSetUsed();

        System.out.println("Tests completed.");
    }

    public void testInvitationCode() {
        System.out.println("\nTest: InvitationCode Constructor");
        InvitationCode invitation = new InvitationCode("ABC123", Arrays.asList(Role.ADMIN, Role.STUDENT));

        if ("ABC123".equals(invitation.getCode()) && invitation.getRoles().contains(Role.ADMIN)) {
            System.out.println("Passed: InvitationCode constructor works.");
        } else {
            System.out.println("Failed: InvitationCode constructor failed.");
        }
    }

    public void testSetUsed() {
        System.out.println("\nTest: Set InvitationCode Used");
        InvitationCode invitation = new InvitationCode("XYZ456", Arrays.asList(Role.INSTRUCTOR));
        invitation.setUsed(true);

        if (invitation.isUsed()) {
            System.out.println("Passed: InvitationCode is marked as used.");
        } else {
            System.out.println("Failed: InvitationCode is not marked as used.");
        }
    }
}
