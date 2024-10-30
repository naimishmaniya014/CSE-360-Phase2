package Controllers;

import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import models.Role;
import Utilities.UserManager;

public class ManageRolesDialog extends Dialog<Void> {

    /**
     * <p> Title: Manage Roles Dialog. </p>
     * 
     * <p> Description: This class provides a dialog to manage the roles of a user.
     * The admin can add or remove roles by specifying the username and role modifications.
     * Roles are added using a '+' sign and removed using a '-' sign (e.g., +ADMIN,-STUDENT). </p>
     * 
     * @author Naimish
     * 
     * @version 1.00   2024-10-09  Initial version.
     */

    private TextField usernameField;
    private TextField rolesField;

    /**
     * Constructor that initializes the dialog for managing user roles.
     * The dialog allows the admin to enter a username and modify the roles
     * by adding or removing roles with the specified format.
     */
    public ManageRolesDialog() {
        setTitle("Manage User Roles");
        setHeaderText("Add or remove roles from a user.");

        ButtonType applyButtonType = new ButtonType("Apply", ButtonBar.ButtonData.OK_DONE);
        getDialogPane().getButtonTypes().addAll(applyButtonType, ButtonType.CANCEL);

        usernameField = new TextField();
        rolesField = new TextField();

        GridPane grid = new GridPane();
        grid.add(new Label("Username:"), 0, 0);
        grid.add(usernameField, 1, 0);
        grid.add(new Label("Roles (e.g., +ADMIN,-STUDENT):"), 0, 1);
        grid.add(rolesField, 1, 1);

        getDialogPane().setContent(grid);

        /**
         * Processes the role changes when the "Apply" button is clicked.
         * It adds roles with a '+' sign and removes roles with a '-' sign.
         * 
         * @return Always returns null, as the dialog is for applying changes only.
         */
        setResultConverter(dialogButton -> {
            if (dialogButton == applyButtonType) {
                String username = usernameField.getText().trim();
                String rolesInput = rolesField.getText().trim();
                if (!username.isEmpty() && !rolesInput.isEmpty()) {
                    UserManager userManager = UserManager.getInstance();
                    models.User user = userManager.getUserByUsername(username);
                    if (user != null) {
                        for (String roleOp : rolesInput.split(",")) {
                            roleOp = roleOp.trim();
                            if (roleOp.length() > 1) {
                                char op = roleOp.charAt(0);
                                String roleName = roleOp.substring(1).toUpperCase();
                                try {
                                    Role role = Role.valueOf(roleName);
                                    if (op == '+') {
                                        user.addRole(role);
                                    } else if (op == '-') {
                                        user.removeRole(role);
                                    }
                                } catch (IllegalArgumentException e) {
                                }
                            }
                        }
                        Alert info = new Alert(Alert.AlertType.INFORMATION, "Roles updated for user " + username + ".");
                        info.showAndWait();
                    } else {
                        Alert error = new Alert(Alert.AlertType.ERROR, "User not found.");
                        error.showAndWait();
                    }
                }
            }
            return null;
        });
    }
}
