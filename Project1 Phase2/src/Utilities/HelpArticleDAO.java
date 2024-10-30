package Utilities;

import models.HelpArticle;
import models.Group;

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
     * Adds a new HelpArticle to the database.
     * 
     * @param article The HelpArticle to add.
     * @return The generated ID of the new article.
     * @throws SQLException if database operation fails.
     */
    public long addHelpArticle(HelpArticle article) throws SQLException {
        String insertArticleSQL = "INSERT INTO HelpArticles (header, title, shortDescription, keywords, body, referenceLinks) VALUES (?, ?, ?, ?, ?, ?);";
        try (PreparedStatement pstmt = connection.prepareStatement(insertArticleSQL, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, article.getHeader());
            pstmt.setString(2, article.getTitle());
            pstmt.setString(3, article.getShortDescription());
            pstmt.setString(4, String.join(",", article.getKeywords()));
            pstmt.setString(5, article.getBody());
            pstmt.setString(6, String.join(",", article.getReferenceLinks()));
            pstmt.executeUpdate();

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    long id = generatedKeys.getLong(1);
                    article.setId(id);
                    return id;
                } else {
                    throw new SQLException("Creating help article failed, no ID obtained.");
                }
            }
        }
    }

    /**
     * Updates an existing HelpArticle in the database.
     * 
     * @param article The HelpArticle with updated information.
     * @throws SQLException if database operation fails.
     */
    public void updateHelpArticle(HelpArticle article) throws SQLException {
        String updateArticleSQL = "UPDATE HelpArticles SET header=?, title=?, shortDescription=?, keywords=?, body=?, referenceLinks=? WHERE id=?;";
        try (PreparedStatement pstmt = connection.prepareStatement(updateArticleSQL)) {
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
     * Deletes a HelpArticle from the database.
     * 
     * @param id The ID of the HelpArticle to delete.
     * @throws SQLException if database operation fails.
     */
    public void deleteHelpArticle(long id) throws SQLException {
        String deleteArticleSQL = "DELETE FROM HelpArticles WHERE id=?;";
        try (PreparedStatement pstmt = connection.prepareStatement(deleteArticleSQL)) {
            pstmt.setLong(1, id);
            pstmt.executeUpdate();
        }
    }

    /**
     * Retrieves a HelpArticle by its ID.
     * 
     * @param id The ID of the HelpArticle.
     * @return The HelpArticle if found, else null.
     * @throws SQLException if database operation fails.
     */
    public HelpArticle getHelpArticleById(long id) throws SQLException {
        String selectArticleSQL = "SELECT * FROM HelpArticles WHERE id=?;";
        try (PreparedStatement pstmt = connection.prepareStatement(selectArticleSQL)) {
            pstmt.setLong(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return extractHelpArticleFromResultSet(rs);
                }
            }
        }
        return null;
    }

    /**
     * Retrieves all HelpArticles from the database.
     * 
     * @return A list of all HelpArticles.
     * @throws SQLException if database operation fails.
     */
    public List<HelpArticle> getAllHelpArticles() throws SQLException {
        List<HelpArticle> articles = new ArrayList<>();
        String selectAllSQL = "SELECT * FROM HelpArticles;";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(selectAllSQL)) {
            while (rs.next()) {
                articles.add(extractHelpArticleFromResultSet(rs));
            }
        }
        return articles;
    }

    /**
     * Searches for HelpArticles based on keywords in title, short description, or body.
     * 
     * @param keyword The keyword to search for.
     * @return A list of matching HelpArticles.
     * @throws SQLException if database operation fails.
     */
    public List<HelpArticle> searchHelpArticles(String keyword) throws SQLException {
        List<HelpArticle> articles = new ArrayList<>();
        String searchSQL = "SELECT * FROM HelpArticles WHERE " +
                "LOWER(title) LIKE ? OR " +
                "LOWER(shortDescription) LIKE ? OR " +
                "LOWER(body) LIKE ?;";
        String likeKeyword = "%" + keyword.toLowerCase() + "%";
        try (PreparedStatement pstmt = connection.prepareStatement(searchSQL)) {
            pstmt.setString(1, likeKeyword);
            pstmt.setString(2, likeKeyword);
            pstmt.setString(3, likeKeyword);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    articles.add(extractHelpArticleFromResultSet(rs));
                }
            }
        }
        return articles;
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
     * Retrieves HelpArticles belonging to a specific group.
     * 
     * @param groupName The name of the group.
     * @return A list of HelpArticles in the specified group.
     * @throws SQLException if database operation fails.
     */
    public List<HelpArticle> getHelpArticlesByGroup(String groupName) throws SQLException {
        List<HelpArticle> articles = new ArrayList<>();
        String query = "SELECT ha.* FROM HelpArticles ha " +
                "JOIN ArticleGroups ag ON ha.id = ag.article_id " +
                "JOIN Groups g ON ag.group_id = g.id " +
                "WHERE g.name = ?;";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, groupName);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    articles.add(extractHelpArticleFromResultSet(rs));
                }
            }
        }
        return articles;
    }
}
