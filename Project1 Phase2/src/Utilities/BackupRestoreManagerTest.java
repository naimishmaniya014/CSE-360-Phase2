package Utilities;

import models.Group;
import models.HelpArticle;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class BackupRestoreManagerTest {

    public static void main(String[] args) {
        BackupRestoreManagerTest tester = new BackupRestoreManagerTest();
        tester.runTests();
    }

    public void runTests() {
        System.out.println("Running BackupRestoreManager class tests...");

        try {
            BackupRestoreManager manager = new BackupRestoreManager();
            testBackupAll(manager);
            testBackupByGroup(manager);
            testRestore(manager);
        } catch (SQLException e) {
            System.out.println("Failed: Unable to initialize BackupRestoreManager.");
        }
    }

    public void testBackupAll(BackupRestoreManager manager) {
        System.out.println("\nTest: Backup All Groups with Articles");
        try {
            manager.backupAllGroups("backup_all_groups.bak");
            System.out.println("Passed: Backup all groups and articles successfully.");
        } catch (IOException | SQLException e) {
            System.out.println("Failed: Backup all groups and articles failed.");
            e.printStackTrace();
        }
    }

    public void testBackupByGroup(BackupRestoreManager manager) {
        System.out.println("\nTest: Backup Specific Groups with Articles");
        try {
            List<String> groupsToBackup = Arrays.asList("cs", "ai"); // Example group names
            manager.backupGroups(groupsToBackup, "backup_cs_ai_groups.bak");
            System.out.println("Passed: Backup specific groups and their articles successfully.");
        } catch (IOException | SQLException e) {
            System.out.println("Failed: Backup specific groups and their articles failed.");
            e.printStackTrace();
        }
    }

    public void testRestore(BackupRestoreManager manager) {
        System.out.println("\nTest: Restore Groups and Articles from Backup");
        try {
            manager.restoreGroups("backup_all_groups.bak", true); // Remove existing before restoring
            System.out.println("Passed: Restore groups and articles successfully.");
        } catch (IOException | SQLException | ClassNotFoundException e) {
            System.out.println("Failed: Restore groups and articles failed.");
            e.printStackTrace();
        }
    }
}
