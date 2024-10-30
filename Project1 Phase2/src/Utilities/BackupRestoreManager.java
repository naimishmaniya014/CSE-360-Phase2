package Utilities;

import models.Group;
import models.GroupWithArticles;
import models.HelpArticle;

import java.io.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BackupRestoreManager {
    private HelpArticleDAO helpArticleDAO;
    private GroupDAO groupDAO;

    public BackupRestoreManager() throws SQLException {
        helpArticleDAO = new HelpArticleDAO();
        groupDAO = new GroupDAO();
    }

    /**
     * Backs up all groups along with their associated articles to an external file.
     *
     * @param filePath The path to the backup file.
     * @throws IOException    if file operations fail.
     * @throws SQLException   if database operations fail.
     */
    public void backupAllGroups(String filePath) throws IOException, SQLException {
        List<Group> groups = groupDAO.getAllGroups();
        List<GroupWithArticles> backupData = new ArrayList<>();

        for (Group group : groups) {
            List<HelpArticle> articles = helpArticleDAO.getArticlesByGroupId(group.getId());
            backupData.add(new GroupWithArticles(group, articles));
        }

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(backupData);
        }
    }

    /**
     * Backs up specific groups along with their associated articles.
     *
     * @param groupNames The names of the groups to back up.
     * @param filePath   The path to the backup file.
     * @throws IOException    if file operations fail.
     * @throws SQLException   if database operations fail.
     */
    public void backupGroups(List<String> groupNames, String filePath) throws IOException, SQLException {
        List<GroupWithArticles> backupData = new ArrayList<>();

        for (String groupName : groupNames) {
            Group group = groupDAO.getGroupByName(groupName);
            if (group != null) {
                List<HelpArticle> articles = helpArticleDAO.getArticlesByGroupId(group.getId());
                backupData.add(new GroupWithArticles(group, articles));
            }
        }

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(backupData);
        }
    }

    /**
     * Restores groups and their associated articles from a backup file.
     *
     * @param filePath      The path to the backup file.
     * @param removeExisting Whether to remove existing groups and articles before restoring.
     * @throws IOException            if file operations fail.
     * @throws SQLException           if database operations fail.
     * @throws ClassNotFoundException if deserialization fails.
     */
    @SuppressWarnings("unchecked")
    public void restoreGroups(String filePath, boolean removeExisting) throws IOException, SQLException, ClassNotFoundException {
        List<GroupWithArticles> backupData;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
            backupData = (List<GroupWithArticles>) ois.readObject();
        }

        if (removeExisting) {
            // Remove all existing associations and groups
            helpArticleDAO.clearAllAssociations();
            groupDAO.deleteAllGroups();
            helpArticleDAO.deleteAllHelpArticles(); // Optionally delete all articles
        }

        for (GroupWithArticles gwa : backupData) {
            Group group = gwa.getGroup();
            Group existingGroup = groupDAO.getGroupByName(group.getName());
            if (existingGroup == null) {
                groupDAO.addGroup(group);
                existingGroup = groupDAO.getGroupByName(group.getName());
            }

            for (HelpArticle article : gwa.getArticles()) {
                HelpArticle existingArticle = helpArticleDAO.getHelpArticleById(article.getId());
                if (existingArticle == null) {
                    helpArticleDAO.addHelpArticle(article);
                }
                // Associate article with group
                helpArticleDAO.associateArticleWithGroup(article.getId(), existingGroup.getId());
            }
        }
    }
}
