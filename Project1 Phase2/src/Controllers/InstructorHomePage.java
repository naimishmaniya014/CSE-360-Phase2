package Controllers;

import javafx.geometry.Insets;
import models.*;
import Utilities.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

public class InstructorHomePage {

	/**
     * <p> Title: Instructor Home Page Controller. </p>
     * 
     * <p> Description: This class manages the Instructor Home Page, providing basic
     * functionalities such as displaying a welcome message and allowing the
     * instructor to log out. </p>
     * 
     * @author Naimish
     * 
     * @version 1.00   2024-10-09  Initial version.
     */
	
	private VBox view;
    private User user;
    private Label welcomeLabel;
    private Button viewArticlesButton;
    private Button manageArticlesButton;
    private Button logoutButton;

    public InstructorHomePage(User user) {
        this.user = user;

        view = new VBox(10);
        view.setPadding(new Insets(20));

        welcomeLabel = new Label("Welcome, " + user.getPreferredName() + " (Instructor)");
        viewArticlesButton = new Button("View Help Articles");
        viewArticlesButton.setOnAction(e -> handleViewArticles());

        manageArticlesButton = new Button("Manage Help Articles");
        manageArticlesButton.setOnAction(e -> handleManageArticles());

        logoutButton = new Button("Log Out");
        logoutButton.setOnAction(e -> Main.showLoginPage());

        view.getChildren().addAll(welcomeLabel, viewArticlesButton, manageArticlesButton, logoutButton);
    }
    
    private void handleViewArticles() {
        HelpArticlePage helpArticlePage = new HelpArticlePage();
        Scene scene = new Scene(helpArticlePage.getView(), 800, 600);
        Main.getStage().setScene(scene);
    }

    /**
     * Navigates to the HelpArticlePage for managing articles.
     */
    private void handleManageArticles() {
        HelpArticlePage helpArticlePage = new HelpArticlePage();
        Scene scene = new Scene(helpArticlePage.getView(), 800, 600);
        Main.getStage().setScene(scene);
    }

    /**
     * Returns the view for the Instructor Home Page, which is a VBox layout.
     * 
     * @return The VBox layout of the instructor's home page.
     */
    public VBox getView() {
        return view;
    }
}
