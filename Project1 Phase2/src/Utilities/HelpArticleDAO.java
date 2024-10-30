package Utilities;

import models.Group;
import models.HelpArticle;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HelpArticleDAO {
    private Connection connection;

    public HelpArticleDAO() throws SQLException {
        connection = DatabaseManager.getInstance().getConnection();
    }

    /**
     * Adds a new help article to the database.
     *
     * @param article The HelpArticle object to add.
     * @throws SQLException if database operation fails.
     */
    public void addHelpArticle(HelpArticle article) throws SQLException {
        String insertSQL = "INSERT INTO HelpArticles (header, title, shortDescription, keywords, body, referenceLinks) " +
                           "VALUES (?, ?, ?, ?, ?, ?);";
        try (PreparedStatement pstmt = connection.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, article.getHeader());
            pstmt.setString(2, article.getTitle());
            pstmt.setString(3, article.getShortDescription());
            pstmt.setString(4, String.join(",", article.getKeywords()));
            pstmt.setString(5, article.getBody());
            pstmt.setString(6, String.join(",", article.getReferenceLinks()));
            pstmt.executeUpdate();
            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    article.setId(rs.getLong(1));
                }
            }
        }
    }

    /**
     * Retrieves all help articles from the database.
     *
     * @return A list of all HelpArticle objects.
     * @throws SQLException if database operation fails.
     */
    public List<HelpArticle> getAllHelpArticles() throws SQLException {
        List<HelpArticle> articles = new ArrayList<>();
        String selectSQL = "SELECT * FROM HelpArticles;";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(selectSQL)) {
            while (rs.next()) {
                articles.add(extractHelpArticleFromResultSet(rs));
            }
        }
        return articles;
    }

    /**
     * Retrieves a help article by its ID.
     *
     * @param articleId The ID of the help article.
     * @return The HelpArticle object, or null if not found.
     * @throws SQLException if database operation fails.
     */
    public HelpArticle getHelpArticleById(long articleId) throws SQLException {
        String query = "SELECT * FROM HelpArticles WHERE id = ?;";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setLong(1, articleId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return extractHelpArticleFromResultSet(rs);
                }
            }
        }
        return null;
    }

    /**
     * Updates an existing help article in the database.
     *
     * @param article The HelpArticle object with updated information.
     * @throws SQLException if database operation fails.
     */
    public void updateHelpArticle(HelpArticle article) throws SQLException {
        String updateSQL = "UPDATE HelpArticles SET header = ?, title = ?, shortDescription = ?, " +
                           "keywords = ?, body = ?, referenceLinks = ? WHERE id = ?;";
        try (PreparedStatement pstmt = connection.prepareStatement(updateSQL)) {
            pstmt.setString(1, article.getHeader());
            pstmt.setString(2, article.getTitle());
            pstmt.setString(3, article.getShortDescription());
            pstmt.setString(4, String.join(",", article.getKeywords()));
            pstmt.setString(5, article.getBody());
            pstmt.setString(6, String.join(",", article.getReferenceLinks()));
            pstmt.setLong(7, article.getId());
            pstmt.executeUpdate();
        }
    }

    /**
     * Deletes a help article from the database.
     *
     * @param articleId The ID of the help article to delete.
     * @throws SQLException if database operation fails.
     */
    public void deleteHelpArticle(long articleId) throws SQLException {
        String deleteSQL = "DELETE FROM HelpArticles WHERE id = ?;";
        try (PreparedStatement pstmt = connection.prepareStatement(deleteSQL)) {
            pstmt.setLong(1, articleId);
            pstmt.executeUpdate();
        }
    }

    /**
     * Deletes all help articles from the database.
     *
     * @throws SQLException if database operation fails.
     */
    public void deleteAllHelpArticles() throws SQLException {
        String deleteSQL = "DELETE FROM HelpArticles;";
        try (PreparedStatement pstmt = connection.prepareStatement(deleteSQL)) {
            pstmt.executeUpdate();
        }
    }

    /**
     * Associates a HelpArticle with a Group.
     *
     * @param articleId The ID of the HelpArticle.
     * @param groupId   The ID of the Group.
     * @throws SQLException if database operation fails.
     */
    public void associateArticleWithGroup(long articleId, long groupId) throws SQLException {
        String insertAssociationSQL = "MERGE INTO ArticleGroups (article_id, group_id) KEY (article_id, group_id) VALUES (?, ?);";
        try (PreparedStatement pstmt = connection.prepareStatement(insertAssociationSQL)) {
            pstmt.setLong(1, articleId);
            pstmt.setLong(2, groupId);
            pstmt.executeUpdate();
        }
    }

    /**
     * Removes the association between a HelpArticle and a Group.
     *
     * @param articleId The ID of the HelpArticle.
     * @param groupId   The ID of the Group.
     * @throws SQLException if database operation fails.
     */
    public void dissociateArticleFromGroup(long articleId, long groupId) throws SQLException {
        String deleteAssociationSQL = "DELETE FROM ArticleGroups WHERE article_id = ? AND group_id = ?;";
        try (PreparedStatement pstmt = connection.prepareStatement(deleteAssociationSQL)) {
            pstmt.setLong(1, articleId);
            pstmt.setLong(2, groupId);
            pstmt.executeUpdate();
        }
    }

    /**
     * Retrieves HelpArticles belonging to a specific group.
     *
     * @param groupId The ID of the group.
     * @return A list of HelpArticles associated with the group.
     * @throws SQLException if database operation fails.
     */
    public List<HelpArticle> getArticlesByGroupId(long groupId) throws SQLException {
        List<HelpArticle> articles = new ArrayList<>();
        String query = "SELECT ha.* FROM HelpArticles ha " +
                       "JOIN ArticleGroups ag ON ha.id = ag.article_id " +
                       "WHERE ag.group_id = ?;";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setLong(1, groupId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    articles.add(extractHelpArticleFromResultSet(rs));
                }
            }
        }
        return articles;
    }

    /**
     * Retrieves Groups associated with a specific HelpArticle.
     *
     * @param articleId The ID of the HelpArticle.
     * @return A list of Groups associated with the article.
     * @throws SQLException if database operation fails.
     */
    public List<Group> getGroupsByArticleId(long articleId) throws SQLException {
        List<Group> groups = new ArrayList<>();
        String query = "SELECT g.* FROM Groups g " +
                       "JOIN ArticleGroups ag ON g.id = ag.group_id " +
                       "WHERE ag.article_id = ?;";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setLong(1, articleId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Group group = new Group();
                    group.setId(rs.getLong("id"));
                    group.setName(rs.getString("name"));
                    groups.add(group);
                }
            }
        }
        return groups;
    }

    /**
     * Clears all group associations for a specific group.
     *
     * @param groupId The ID of the Group.
     * @throws SQLException if database operation fails.
     */
    public void clearAssociationsForGroup(long groupId) throws SQLException {
        String deleteSQL = "DELETE FROM ArticleGroups WHERE group_id = ?;";
        try (PreparedStatement pstmt = connection.prepareStatement(deleteSQL)) {
            pstmt.setLong(1, groupId);
            pstmt.executeUpdate();
        }
    }

    /**
     * Clears all group associations for a specific article.
     *
     * @param articleId The ID of the HelpArticle.
     * @throws SQLException if database operation fails.
     */
    public void clearAssociationsForArticle(long articleId) throws SQLException {
        String deleteSQL = "DELETE FROM ArticleGroups WHERE article_id = ?;";
        try (PreparedStatement pstmt = connection.prepareStatement(deleteSQL)) {
            pstmt.setLong(1, articleId);
            pstmt.executeUpdate();
        }
    }

    /**
     * Clears all group associations.
     *
     * @throws SQLException if database operation fails.
     */
    public void clearAllAssociations() throws SQLException {
        String deleteSQL = "DELETE FROM ArticleGroups;";
        try (PreparedStatement pstmt = connection.prepareStatement(deleteSQL)) {
            pstmt.executeUpdate();
        }
    }

    /**
     * Extracts a HelpArticle object from the current row of the ResultSet.
     *
     * @param rs The ResultSet positioned at the desired row.
     * @return A HelpArticle object.
     * @throws SQLException if data extraction fails.
     */
    private HelpArticle extractHelpArticleFromResultSet(ResultSet rs) throws SQLException {
        long id = rs.getLong("id");
        String header = rs.getString("header");
        String title = rs.getString("title");
        String shortDescription = rs.getString("shortDescription");
        List<String> keywords = Arrays.asList(rs.getString("keywords").split(","));
        String body = rs.getString("body");
        List<String> referenceLinks = Arrays.asList(rs.getString("referenceLinks").split(","));
        return new HelpArticle(id, header, title, shortDescription, keywords, body, referenceLinks);
    }

    /**
     * Provides access to the database connection.
     *
     * @return The current database connection.
     */
    public Connection getConnection() {
        return connection;
    }
}
