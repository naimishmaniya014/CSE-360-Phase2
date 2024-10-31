package Controllers;

import Utilities.HelpArticleDAO;
import Utilities.GroupDAO;
import Utilities.SessionManager;
import models.HelpArticle;
import models.Role;
import models.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * <p> Title: HelpArticlePage Class </p>
 * 
 * <p> Description: This class represents the user interface for managing help articles within the application.
 * It provides functionalities to add, edit, delete, and refresh help articles. The page displays a table of 
 * existing help articles and allows users to perform CRUD (Create, Read, Update, Delete) operations on them. 
 * </p>
 * 
 * @author Naimish Maniya
 * 
 * <p> @version 1.00  2024-10-29  Initial version. </p>
 */
public class HelpArticlePage {

    private VBox view;
    private TableView<HelpArticle> tableView;
    private ObservableList<HelpArticle> articlesList;
    private HelpArticleDAO helpArticleDAO;
    private GroupDAO groupDAO;

    private Button backButton;
    private Button addButton;
    private Button editButton;
    private Button deleteButton;
    private Button refreshButton;

    /**
     * Constructs a HelpArticlePage instance.
     * Initializes UI components, sets up event handlers, and loads existing help articles from the database.
     */
    public HelpArticlePage() {
        try {
            helpArticleDAO = new HelpArticleDAO();
            groupDAO = new GroupDAO();
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to connect to the database.");
            return;
        }

        view = new VBox(10);
        view.setPadding(new Insets(20));

        backButton = new Button("Back");
        backButton.setOnAction(e -> handleBack());

        tableView = new TableView<>();
        articlesList = FXCollections.observableArrayList();
        tableView.setItems(articlesList);

        TableColumn<HelpArticle, Long> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(data -> new javafx.beans.property.SimpleLongProperty(data.getValue().getId()).asObject());
        idCol.setPrefWidth(50);

        TableColumn<HelpArticle, String> titleCol = new TableColumn<>("Title");
        titleCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getTitle()));
        titleCol.setPrefWidth(200);

        TableColumn<HelpArticle, String> shortDescCol = new TableColumn<>("Short Description");
        shortDescCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getShortDescription()));
        shortDescCol.setPrefWidth(300);

        tableView.getColumns().addAll(idCol, titleCol, shortDescCol);

        addButton = new Button("Add Article");
        addButton.setOnAction(e -> showAddArticleDialog());

        editButton = new Button("Edit Article");
        editButton.setOnAction(e -> showEditArticleDialog());

        deleteButton = new Button("Delete Article");
        deleteButton.setOnAction(e -> deleteSelectedArticle());

        refreshButton = new Button("Refresh");
        refreshButton.setOnAction(e -> loadArticles());

        ToolBar toolBar = new ToolBar(backButton, addButton, editButton, deleteButton, refreshButton);

        view.getChildren().addAll(toolBar, tableView);

        loadArticles();
    }

    /**
     * Retrieves the main layout of the HelpArticlePage.
     *
     * @return The {@link VBox} containing all UI components of the page.
     */
    public VBox getView() {
        return view;
    }

    /**
     * Loads all help articles from the database into the table view.
     * Fetches the list of help articles and updates the observable list.
     */
    private void loadArticles() {
        try {
            List<HelpArticle> articles = helpArticleDAO.getAllHelpArticles();
            articlesList.setAll(articles);
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to load help articles.");
        }
    }

    /**
     * Shows a dialog to add a new help article.
     * Prompts the user to enter details for the new article and handles the addition to the database.
     */
    private void showAddArticleDialog() {
        Dialog<HelpArticle> dialog = new Dialog<>();
        dialog.setTitle("Add Help Article");
        dialog.setHeaderText("Enter details for the new help article.");

        ButtonType addButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        GridPane grid = createArticleForm(null);
        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                return extractArticleFromForm(grid, null);
            }
            return null;
        });

        Optional<HelpArticle> result = dialog.showAndWait();

        result.ifPresent(article -> {
            try {
                helpArticleDAO.addHelpArticle(article);
                showAlert(Alert.AlertType.INFORMATION, "Success", "Help article added successfully.");
                loadArticles();
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to add help article.");
            }
        });
    }

    /**
     * Shows a dialog to edit the selected help article.
     * Allows the user to modify the article's details and updates it in the database.
     */
    private void showEditArticleDialog() {
        HelpArticle selected = tableView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select an article to edit.");
            return;
        }

        Dialog<HelpArticle> dialog = new Dialog<>();
        dialog.setTitle("Edit Help Article");
        dialog.setHeaderText("Modify details for the selected help article.");

        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        GridPane grid = createArticleForm(selected);
        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                return extractArticleFromForm(grid, selected.getId());
            }
            return null;
        });

        Optional<HelpArticle> result = dialog.showAndWait();

        result.ifPresent(article -> {
            try {
                helpArticleDAO.updateHelpArticle(article);
                showAlert(Alert.AlertType.INFORMATION, "Success", "Help article updated successfully.");
                loadArticles();
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to update help article.");
            }
        });
    }

    /**
     * Deletes the selected help article after user confirmation.
     * Removes the article from the database and refreshes the table view.
     */
    private void deleteSelectedArticle() {
        HelpArticle selected = tableView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select an article to delete.");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete the selected article?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> result = confirm.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.YES) {
            try {
                helpArticleDAO.deleteHelpArticle(selected.getId());
                showAlert(Alert.AlertType.INFORMATION, "Success", "Help article deleted successfully.");
                loadArticles();
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to delete help article.");
            }
        }
    }

    /**
     * Creates a form for adding or editing a help article.
     *
     * @param article The HelpArticle to edit, or null for adding a new article.
     * @return The GridPane containing the form fields.
     */
    private GridPane createArticleForm(HelpArticle article) {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField headerField = new TextField();
        headerField.setPromptText("Header");
        TextField titleField = new TextField();
        titleField.setPromptText("Title");
        TextArea shortDescArea = new TextArea();
        shortDescArea.setPromptText("Short Description");
        shortDescArea.setPrefRowCount(3);
        TextField keywordsField = new TextField();
        keywordsField.setPromptText("Keywords (comma-separated)");
        TextArea bodyArea = new TextArea();
        bodyArea.setPromptText("Body");
        bodyArea.setPrefRowCount(10);
        TextField referenceLinksField = new TextField();
        referenceLinksField.setPromptText("Reference Links (comma-separated)");

        if (article != null) {
            headerField.setText(article.getHeader());
            titleField.setText(article.getTitle());
            shortDescArea.setText(article.getShortDescription());
            keywordsField.setText(String.join(",", article.getKeywords()));
            bodyArea.setText(article.getBody());
            referenceLinksField.setText(String.join(",", article.getReferenceLinks()));
        }

        grid.add(new Label("Header:"), 0, 0);
        grid.add(headerField, 1, 0);
        grid.add(new Label("Title:"), 0, 1);
        grid.add(titleField, 1, 1);
        grid.add(new Label("Short Description:"), 0, 2);
        grid.add(shortDescArea, 1, 2);
        grid.add(new Label("Keywords:"), 0, 3);
        grid.add(keywordsField, 1, 3);
        grid.add(new Label("Body:"), 0, 4);
        grid.add(bodyArea, 1, 4);
        grid.add(new Label("Reference Links:"), 0, 5);
        grid.add(referenceLinksField, 1, 5);

        return grid;
    }

    /**
     * Extracts a HelpArticle object from the form fields.
     *
     * @param grid The GridPane containing the form fields.
     * @param id   The ID of the article being edited, or null for a new article.
     * @return A HelpArticle object with the form data.
     */
    private HelpArticle extractArticleFromForm(GridPane grid, Long id) {
        TextField headerField = (TextField) grid.getChildren().get(1);
        TextField titleField = (TextField) grid.getChildren().get(3);
        TextArea shortDescArea = (TextArea) grid.getChildren().get(5);
        TextField keywordsField = (TextField) grid.getChildren().get(7);
        TextArea bodyArea = (TextArea) grid.getChildren().get(9);
        TextField referenceLinksField = (TextField) grid.getChildren().get(11);

        HelpArticle article = new HelpArticle();
        if (id != null) {
            article.setId(id);
        }
        article.setHeader(headerField.getText().trim());
        article.setTitle(titleField.getText().trim());
        article.setShortDescription(shortDescArea.getText().trim());
        String keywordsText = keywordsField.getText().trim();
        if (!keywordsText.isEmpty()) {
            article.setKeywords(Arrays.asList(keywordsText.split(",")));
        }
        String bodyText = bodyArea.getText().trim();
        if (!bodyText.isEmpty()) {
            article.setBody(bodyText);
        }
        String referencesText = referenceLinksField.getText().trim();
        if (!referencesText.isEmpty()) {
            article.setReferenceLinks(Arrays.asList(referencesText.split(",")));
        }

        return article;
    }

    /**
     * Handles navigation back to the home page based on the current user's role.
     * Invokes the {@link Main#showHomePage(User, Role)} method to display the appropriate home page.
     */
    private void handleBack() {
        User currentUser = SessionManager.getInstance().getCurrentUser();
        Role currentRole = SessionManager.getInstance().getCurrentRole();
        Main.showHomePage(currentUser, currentRole);
    }

    /**
     * Displays an alert dialog with the specified parameters.
     *
     * @param type    The type of alert (e.g., INFORMATION, ERROR, WARNING).
     * @param title   The title of the alert dialog.
     * @param content The content message of the alert dialog.
     */
    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type, content, ButtonType.OK);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}
