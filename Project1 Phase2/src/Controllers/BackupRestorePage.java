package Controllers;

import Utilities.BackupRestoreManager;
import Utilities.SessionManager;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import models.Role;
import models.User;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class BackupRestorePage {

    private VBox view;
    private BackupRestoreManager backupRestoreManager;

    private Button backButton;
    private Button backupAllButton;
    private Button backupByGroupButton;
    private Button restoreButton;

    public BackupRestorePage() {
        try {
            backupRestoreManager = new BackupRestoreManager();
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to initialize backup/restore manager.");
            return;
        }

        view = new VBox(10);
        view.setPadding(new Insets(20));

        // Initialize Back Button
        backButton = new Button("Back");
        backButton.setOnAction(e -> handleBack());

        backupAllButton = new Button("Backup All Articles");
        backupAllButton.setOnAction(e -> handleBackupAll());

        backupByGroupButton = new Button("Backup by Group");
        backupByGroupButton.setOnAction(e -> handleBackupByGroup());

        restoreButton = new Button("Restore Articles");
        restoreButton.setOnAction(e -> handleRestore());

        ToolBar toolBar = new ToolBar(backButton, backupAllButton, backupByGroupButton, restoreButton);

        view.getChildren().addAll(toolBar);
    }

    public VBox getView() {
        return view;
    }

    /**
     * Handles backing up all help articles.
     */
    private void handleBackupAll() {
        FileChooserDialog fileDialog = new FileChooserDialog("Backup All Articles", "*.bak");
        Optional<String> result = fileDialog.showSaveDialog();
        result.ifPresent(filePath -> {
            try {
                backupRestoreManager.backupAll(filePath);
                showAlert(Alert.AlertType.INFORMATION, "Success", "All articles backed up successfully.");
            } catch (IOException | SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Backup Error", "Failed to backup articles.");
            }
        });
    }

    /**
     * Handles backing up help articles by selected groups.
     */
    private void handleBackupByGroup() {
        // Fetch all groups
        GroupSelectionDialog groupDialog = new GroupSelectionDialog();
        Optional<List<String>> result = groupDialog.showAndWait();

        result.ifPresent(groups -> {
            if (groups.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "No Groups Selected", "Please select at least one group.");
                return;
            }

            FileChooserDialog fileDialog = new FileChooserDialog("Backup Articles by Group", "*.bak");
            Optional<String> filePathOpt = fileDialog.showSaveDialog();
            filePathOpt.ifPresent(filePath -> {
                try {
                    backupRestoreManager.backupByGroups(groups, filePath);
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Articles backed up by group successfully.");
                } catch (IOException | SQLException e) {
                    showAlert(Alert.AlertType.ERROR, "Backup Error", "Failed to backup articles by group.");
                }
            });
        });
    }

    /**
     * Handles restoring help articles from a backup file.
     */
    private void handleRestore() {
        FileChooserDialog fileDialog = new FileChooserDialog("Select Backup File", "*.bak");
        Optional<String> filePathOpt = fileDialog.showOpenDialog();
        filePathOpt.ifPresent(filePath -> {
            // Ask whether to remove existing articles or merge
            ChoiceDialog<String> choiceDialog = new ChoiceDialog<>("Merge", "Merge", "Remove All");
            choiceDialog.setTitle("Restore Options");
            choiceDialog.setHeaderText("Choose how to restore the articles:");
            choiceDialog.setContentText("Select an option:");

            Optional<String> choice = choiceDialog.showAndWait();
            if (choice.isPresent()) {
                boolean removeExisting = choice.get().equals("Remove All");
                try {
                    backupRestoreManager.restore(filePath, removeExisting);
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Articles restored successfully.");
                } catch (IOException | SQLException | ClassNotFoundException e) {
                    showAlert(Alert.AlertType.ERROR, "Restore Error", "Failed to restore articles.");
                }
            }
        });
    }

    /**
     * Handles navigation back to the home page based on the current user's role.
     */
    private void handleBack() {
        User currentUser = SessionManager.getInstance().getCurrentUser();
        Role currentRole = SessionManager.getInstance().getCurrentRole();
        Main.showHomePage(currentUser, currentRole);
    }

    /**
     * Shows an alert dialog.
     *
     * @param type    The type of alert.
     * @param title   The title of the alert.
     * @param content The content message of the alert.
     */
    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type, content, ButtonType.OK);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}
