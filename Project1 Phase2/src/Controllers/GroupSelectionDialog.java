package Controllers;

import Utilities.GroupDAO;
import models.Group;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GroupSelectionDialog extends Dialog<List<String>> {

    private ListView<String> groupListView;
    private ObservableList<String> groupNames;

    public GroupSelectionDialog() {
        setTitle("Select Groups");
        setHeaderText("Select groups to include in the backup:");

        ButtonType backupButtonType = new ButtonType("Backup", ButtonBar.ButtonData.OK_DONE);
        getDialogPane().getButtonTypes().addAll(backupButtonType, ButtonType.CANCEL);

        groupListView = new ListView<>();
        groupListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        groupNames = FXCollections.observableArrayList();

        try {
            GroupDAO groupDAO = new GroupDAO();
            List<Group> groups = groupDAO.getAllGroups();
            for (Group group : groups) {
                groupNames.add(group.getName());
            }
            groupListView.setItems(groupNames);
        } catch (SQLException e) {
            // Handle error
        }

        getDialogPane().setContent(groupListView);

        setResultConverter(dialogButton -> {
            if (dialogButton == backupButtonType) {
                return new ArrayList<>(groupListView.getSelectionModel().getSelectedItems());
            }
            return null;
        });
    }
}
