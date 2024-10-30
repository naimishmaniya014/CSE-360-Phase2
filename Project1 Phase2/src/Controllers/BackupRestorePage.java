package Controllers;

import Utilities.BackupRestoreManager;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import models.Role;
import models.User;
import Utilities.SessionManager;

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

        backupAllButton = new Button("Backup All Groups");
        backupAllButton.setOnAction(e -> handleBackupAllGroups());

        backupByGroupButton = new Button("Backup Selected Groups");
        backupByGroupButton.setOnAction(e -> {
			try {
				handleBackupSelectedGroups();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});

        restoreButton = new Button("Restore Groups");
        restoreButton.setOnAction(e -> handleRestoreGroups());

        ToolBar toolBar = new ToolBar(backButton, backupAllButton, backupByGroupButton, restoreButton);

        view.getChildren().addAll(toolBar);
    }

    public VBox getView() {
        return view;
    }

    /**
     * Handles backing up all groups along with their articles.
     */
    private void handleBackupAllGroups() {
        FileChooserDialog fileDialog = new FileChooserDialog("Backup All Groups with Articles", "*.bak");
        Optional<String> result = fileDialog.showSaveDialog();
        result.ifPresent(filePath -> {
            try {
                backupRestoreManager.backupAllGroups(filePath);
                showAlert(Alert.AlertType.INFORMATION, "Success", "All groups and their articles backed up successfully.");
            } catch (IOException | SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Backup Error", "Failed to backup all groups and articles.");
            }
        });
    }

    /**
     * Handles backing up selected groups along with their articles.
     * @throws SQLException 
     */
    private void handleBackupSelectedGroups() throws SQLException {
        // Fetch all groups
        GroupSelectionDialog groupDialog = new GroupSelectionDialog();
        Optional<List<String>> result = groupDialog.showAndWait();

        result.ifPresent(groups -> {
            if (groups.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "No Groups Selected", "Please select at least one group.");
                return;
            }

            FileChooserDialog fileDialog = new FileChooserDialog("Backup Selected Groups with Articles", "*.bak");
            Optional<String> filePathOpt = fileDialog.showSaveDialog();
            filePathOpt.ifPresent(filePath -> {
                try {
                    backupRestoreManager.backupGroups(groups, filePath);
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Selected groups and their articles backed up successfully.");
                } catch (IOException | SQLException e) {
                    showAlert(Alert.AlertType.ERROR, "Backup Error", "Failed to backup selected groups and articles.");
                }
            });
        });
    }

    /**
     * Handles restoring groups and their articles from a backup file.
     */
    private void handleRestoreGroups() {
        FileChooserDialog fileDialog = new FileChooserDialog("Select Backup File", "*.bak");
        Optional<String> filePathOpt = fileDialog.showOpenDialog();
        filePathOpt.ifPresent(filePath -> {
            // Ask whether to remove existing groups and articles before restoring
            ChoiceDialog<String> choiceDialog = new ChoiceDialog<>("Merge", "Merge", "Remove All");
            choiceDialog.setTitle("Restore Options");
            choiceDialog.setHeaderText("Choose how to restore the groups and articles:");
            choiceDialog.setContentText("Select an option:");

            Optional<String> choice = choiceDialog.showAndWait();
            if (choice.isPresent()) {
                boolean removeExisting = choice.get().equals("Remove All");
                try {
                    backupRestoreManager.restoreGroups(filePath, removeExisting);
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Groups and articles restored successfully.");
                } catch (IOException | SQLException | ClassNotFoundException e) {
                    showAlert(Alert.AlertType.ERROR, "Restore Error", "Failed to restore groups and articles.");
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
