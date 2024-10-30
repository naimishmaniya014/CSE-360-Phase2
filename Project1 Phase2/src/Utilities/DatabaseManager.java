package Utilities;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {
    private static DatabaseManager instance;
    private Connection connection;
    private final String DB_URL = "jdbc:h2:./helpSystemDB"; // Database stored in the project directory
    private final String USER = "sa";
    private final String PASSWORD = "";

    private DatabaseManager() throws SQLException {
        try {
            connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
            initializeDatabase();
        } catch (SQLException ex) {
            throw new SQLException("Failed to connect to H2 database.", ex);
        }
    }

    /**
     * Initializes the database by creating necessary tables if they do not exist.
     */
    private void initializeDatabase() throws SQLException {
        String createHelpArticlesTable = "CREATE TABLE IF NOT EXISTS HelpArticles (" +
                "id BIGINT AUTO_INCREMENT PRIMARY KEY," +
                "header VARCHAR(255)," +
                "title VARCHAR(255) NOT NULL," +
                "shortDescription VARCHAR(500)," +
                "keywords VARCHAR(500)," +
                "body CLOB," +
                "referenceLinks VARCHAR(1000)" +
                ");";

        String createGroupsTable = "CREATE TABLE IF NOT EXISTS Groups (" +
                "id BIGINT AUTO_INCREMENT PRIMARY KEY," +
                "name VARCHAR(255) UNIQUE NOT NULL" +
                ");";

        String createArticleGroupTable = "CREATE TABLE IF NOT EXISTS ArticleGroups (" +
                "article_id BIGINT NOT NULL," +
                "group_id BIGINT NOT NULL," +
                "PRIMARY KEY (article_id, group_id)," +
                "FOREIGN KEY (article_id) REFERENCES HelpArticles(id) ON DELETE CASCADE," +
                "FOREIGN KEY (group_id) REFERENCES Groups(id) ON DELETE CASCADE" +
                ");";

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(createHelpArticlesTable);
            stmt.execute(createGroupsTable);
            stmt.execute(createArticleGroupTable);
        } catch (SQLException e) {
            throw new SQLException("Failed to initialize database tables.", e);
        }
    }

    public Connection getConnection() {
        return connection;
    }

    /**
     * Provides the singleton instance of DatabaseManager.
     * 
     * @return DatabaseManager instance
     * @throws SQLException if connection fails
     */
    public static DatabaseManager getInstance() throws SQLException {
        if (instance == null) {
            instance = new DatabaseManager();
        } else if (instance.getConnection().isClosed()) {
            instance = new DatabaseManager();
        }
        return instance;
    }
}
