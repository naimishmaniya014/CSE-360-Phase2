package Controllers;

import Utilities.GroupDAO;
import Utilities.SessionManager;
import models.Group;
import models.Role;
import models.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class GroupPage {

    private VBox view;
    private TableView<Group> tableView;
    private ObservableList<Group> groupsList;
    private GroupDAO groupDAO;

    private Button backButton;
    private Button addButton;
    private Button editButton;
    private Button deleteButton;
    private Button refreshButton;

    public GroupPage() {
        try {
            groupDAO = new GroupDAO();
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to connect to the database.");
            return;
        }

        view = new VBox(10);
        view.setPadding(new Insets(20));

        // Initialize Back Button
        backButton = new Button("Back");
        backButton.setOnAction(e -> handleBack());

        tableView = new TableView<>();
        groupsList = FXCollections.observableArrayList();
        tableView.setItems(groupsList);

        // Define table columns
        TableColumn<Group, Long> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(data -> new javafx.beans.property.SimpleLongProperty(data.getValue().getId()).asObject());
        idCol.setPrefWidth(50);

        TableColumn<Group, String> nameCol = new TableColumn<>("Group Name");
        nameCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getName()));
        nameCol.setPrefWidth(200);

        tableView.getColumns().addAll(idCol, nameCol);

        // Buttons
        addButton = new Button("Add Group");
        addButton.setOnAction(e -> showAddGroupDialog());

        editButton = new Button("Edit Group");
        editButton.setOnAction(e -> showEditGroupDialog());

        deleteButton = new Button("Delete Group");
        deleteButton.setOnAction(e -> deleteSelectedGroup());

        refreshButton = new Button("Refresh");
        refreshButton.setOnAction(e -> loadGroups());

        ToolBar toolBar = new ToolBar(backButton, addButton, editButton, deleteButton, refreshButton);

        view.getChildren().addAll(toolBar, tableView);

        loadGroups();
    }

    public VBox getView() {
        return view;
    }

    /**
     * Loads all groups from the database into the table view.
     */
    private void loadGroups() {
        try {
            List<Group> groups = groupDAO.getAllGroups();
            groupsList.setAll(groups);
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to load groups.");
        }
    }

    /**
     * Shows a dialog to add a new group.
     */
    private void showAddGroupDialog() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Add Group");
        dialog.setHeaderText("Enter the name of the new group:");
        dialog.setContentText("Group Name:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(name -> {
            if (name.trim().isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Invalid Input", "Group name cannot be empty.");
                return;
            }
            try {
                Group existing = groupDAO.getGroupByName(name.trim());
                if (existing != null) {
                    showAlert(Alert.AlertType.WARNING, "Duplicate Group", "A group with this name already exists.");
                    return;
                }
                Group group = new Group(name.trim());
                groupDAO.addGroup(group);
                showAlert(Alert.AlertType.INFORMATION, "Success", "Group added successfully.");
                loadGroups();
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to add group.");
            }
        });
    }

    /**
     * Shows a dialog to edit the selected group.
     */
    private void showEditGroupDialog() {
        Group selected = tableView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a group to edit.");
            return;
        }

        TextInputDialog dialog = new TextInputDialog(selected.getName());
        dialog.setTitle("Edit Group");
        dialog.setHeaderText("Modify the name of the selected group:");
        dialog.setContentText("Group Name:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(name -> {
            if (name.trim().isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Invalid Input", "Group name cannot be empty.");
                return;
            }
            try {
                Group existing = groupDAO.getGroupByName(name.trim());
                if (existing != null && existing.getId() != selected.getId()) {
                    showAlert(Alert.AlertType.WARNING, "Duplicate Group", "A group with this name already exists.");
                    return;
                }
                selected.setName(name.trim());
                groupDAO.updateGroup(selected);
                showAlert(Alert.AlertType.INFORMATION, "Success", "Group updated successfully.");
                loadGroups();
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to update group.");
            }
        });
    }

    /**
     * Deletes the selected group after confirmation.
     */
    private void deleteSelectedGroup() {
        Group selected = tableView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a group to delete.");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete the selected group?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> result = confirm.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.YES) {
            try {
                groupDAO.deleteGroup(selected.getId());
                showAlert(Alert.AlertType.INFORMATION, "Success", "Group deleted successfully.");
                loadGroups();
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to delete group.");
            }
        }
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
