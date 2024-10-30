package Controllers;

import Utilities.HelpArticleDAO;
import models.Group;
import models.HelpArticle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AssignArticlesDialog extends Dialog<List<HelpArticle>> {

    private Group selectedGroup;
    private ListView<HelpArticle> articleListView;
    private ObservableList<HelpArticle> allArticles;
    private HelpArticleDAO helpArticleDAO;

    public AssignArticlesDialog(Group group) throws SQLException {
        this.selectedGroup = group;
        helpArticleDAO = new HelpArticleDAO();

        setTitle("Assign Articles to Group");
        setHeaderText("Select articles to assign to the group: " + group.getName());

        ButtonType assignButtonType = new ButtonType("Assign", ButtonBar.ButtonData.OK_DONE);
        getDialogPane().getButtonTypes().addAll(assignButtonType, ButtonType.CANCEL);

        articleListView = new ListView<>();
        articleListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE); // Enable multiple selection

        // Load all articles
        allArticles = FXCollections.observableArrayList(helpArticleDAO.getAllHelpArticles());
        articleListView.setItems(allArticles);

        // Load already assigned articles and select them
        List<HelpArticle> assignedArticles = helpArticleDAO.getArticlesByGroupId(group.getId());
        for (HelpArticle article : assignedArticles) {
            articleListView.getSelectionModel().select(article);
        }

        VBox content = new VBox();
        content.getChildren().addAll(new Label("Select Articles:"), articleListView);
        getDialogPane().setContent(content);

        setResultConverter(dialogButton -> {
            if (dialogButton == assignButtonType) {
                return new ArrayList<>(articleListView.getSelectionModel().getSelectedItems());
            }
            return null;
        });
    }
}
