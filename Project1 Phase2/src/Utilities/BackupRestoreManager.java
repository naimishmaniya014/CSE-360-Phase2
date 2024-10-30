package Utilities;

import models.HelpArticle;
import models.Group;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BackupRestoreManager {
    private Connection connection;
    private HelpArticleDAO helpArticleDAO;
    private GroupDAO groupDAO;

    public BackupRestoreManager() throws SQLException {
        connection = DatabaseManager.getInstance().getConnection();
        helpArticleDAO = new HelpArticleDAO();
        groupDAO = new GroupDAO();
    }

    /**
     * Backs up all help articles to an external file.
     * 
     * @param filePath The path to the backup file.
     * @throws IOException if file operations fail.
     * @throws SQLException if database operations fail.
     */
    public void backupAll(String filePath) throws IOException, SQLException {
        List<HelpArticle> articles = helpArticleDAO.getAllHelpArticles();
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(articles);
        }
    }

    /**
     * Backs up help articles belonging to specific groups.
     * 
     * @param groupNames The names of the groups to back up.
     * @param filePath The path to the backup file.
     * @throws IOException if file operations fail.
     * @throws SQLException if database operations fail.
     */
    public void backupByGroups(List<String> groupNames, String filePath) throws IOException, SQLException {
        List<HelpArticle> articles = new ArrayList<>();
        for (String groupName : groupNames) {
            articles.addAll(helpArticleDAO.getHelpArticlesByGroup(groupName));
        }
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(articles);
        }
    }

    /**
     * Restores help articles from a backup file.
     * 
     * @param filePath The path to the backup file.
     * @param removeExisting Whether to remove existing articles before restoring.
     * @throws IOException if file operations fail.
     * @throws SQLException if database operations fail.
     * @throws ClassNotFoundException if deserialization fails.
     */
    @SuppressWarnings("unchecked")
    public void restore(String filePath, boolean removeExisting) throws IOException, SQLException, ClassNotFoundException {
        List<HelpArticle> articles;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
            articles = (List<HelpArticle>) ois.readObject();
        }

        if (removeExisting) {
            // Remove all existing articles
            String deleteAllSQL = "DELETE FROM HelpArticles;";
            try (Statement stmt = connection.createStatement()) {
                stmt.execute(deleteAllSQL);
            }
        }

        for (HelpArticle article : articles) {
            HelpArticle existing = helpArticleDAO.getHelpArticleById(article.getId());
            if (existing == null) {
                helpArticleDAO.addHelpArticle(article);
            }
            // If exists, skip to prevent duplicates
        }
    }
}
