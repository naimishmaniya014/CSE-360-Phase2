package Controllers;

import Utilities.GroupDAO;
import Utilities.HelpArticleDAO;
import models.Group;
import models.HelpArticle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AssignGroupsDialog extends Dialog<List<Group>> {

    private HelpArticle selectedArticle;
    private ListView<Group> groupListView;
    private ObservableList<Group> allGroups;
    private GroupDAO groupDAO;
    private HelpArticleDAO helpArticleDAO;

    public AssignGroupsDialog(HelpArticle article) throws SQLException {
        this.selectedArticle = article;
        groupDAO = new GroupDAO();
        helpArticleDAO = new HelpArticleDAO();

        setTitle("Assign Groups to Article");
        setHeaderText("Select groups to associate with the article: " + article.getTitle());

        ButtonType assignButtonType = new ButtonType("Assign", ButtonBar.ButtonData.OK_DONE);
        getDialogPane().getButtonTypes().addAll(assignButtonType, ButtonType.CANCEL);

        groupListView = new ListView<>();
        groupListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE); // Ensure multiple selection
        allGroups = FXCollections.observableArrayList();

        List<Group> groups = groupDAO.getAllGroups();
        allGroups.addAll(groups);
        groupListView.setItems(allGroups);

        // Load already associated groups and select them
        List<Group> associatedGroups = helpArticleDAO.getGroupsByArticleId(article.getId());
        for (Group group : associatedGroups) {
            groupListView.getSelectionModel().select(group);
        }

        getDialogPane().setContent(groupListView);

        setResultConverter(dialogButton -> {
            if (dialogButton == assignButtonType) {
                return new ArrayList<>(groupListView.getSelectionModel().getSelectedItems());
            }
            return null;
        });
    }
}
