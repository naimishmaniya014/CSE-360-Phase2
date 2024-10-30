package Controllers;

import javafx.geometry.Insets;
import models.*;
import Utilities.*;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

public class StudentHomePage {

    /**
     * <p> Title: Student Home Page Controller. </p>
     * 
     * <p> Description: This class manages the Student Home Page, displaying a welcome message
     * specific to the student user and providing an option to log out. </p>
     * 
     * @author Naimish
     * 
     * @version 1.00   2024-10-09  Initial version.
     */

    private VBox view;
    private User user;
    private Label welcomeLabel;
    private Button logoutButton;

    /**
     * Constructor that sets up the Student Home Page.
     * Displays a welcome message and includes a button to log out.
     * 
     * @param user The student user for whom the home page is displayed.
     */
    public StudentHomePage(User user) {
        this.user = user;

        view = new VBox(10);
        view.setPadding(new Insets(20));

        welcomeLabel = new Label("Welcome, " + user.getPreferredName() + " (Student)");
        logoutButton = new Button("Log Out");
        logoutButton.setOnAction(e -> Main.showLoginPage());

        view.getChildren().addAll(welcomeLabel, logoutButton);
    }

    /**
     * Returns the view for the Student Home Page, which is a VBox layout.
     * 
     * @return The VBox layout of the student's home page.
     */
    public VBox getView() {
        return view;
    }
}
