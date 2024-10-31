package Utilities;

import models.Group;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * <p> Title: GroupDAO Class </p>
 * 
 * <p> Description: This class provides Data Access Object (DAO) functionalities for the {@link Group} entity.
 * It facilitates CRUD (Create, Read, Update, Delete) operations on the Groups table in the database. 
 * The class interacts with the {@link DatabaseManager} to execute SQL queries and manage group data. 
 * </p>
 * 
 * @author Naimish Maniya
 * 
 * <p> @version 1.00  2024-10-29  Initial version. </p>
 */
public class GroupDAO {
    private Connection connection;

    /**
     * Constructs a GroupDAO instance.
     * Initializes the database connection using the DatabaseManager.
     *
     * @throws SQLException If there is an error accessing the database.
     */
    public GroupDAO() throws SQLException {
        connection = DatabaseManager.getInstance().getConnection();
    }

    /**
     * Adds a new group to the database.
     *
     * @param group The Group object to add.
     * @throws SQLException If a database access error occurs.
     */
    public void addGroup(Group group) throws SQLException {
        String insertSQL = "INSERT INTO Groups (name) VALUES (?);";
        try (PreparedStatement pstmt = connection.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, group.getName());
            pstmt.executeUpdate();
            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    group.setId(rs.getLong(1));
                }
            }
        }
    }

    /**
     * Retrieves all groups from the database.
     *
     * @return A list of all Group objects.
     * @throws SQLException If a database access error occurs.
     */
    public List<Group> getAllGroups() throws SQLException {
        List<Group> groups = new ArrayList<>();
        String selectSQL = "SELECT * FROM Groups;";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(selectSQL)) {
            while (rs.next()) {
                Group group = new Group();
                group.setId(rs.getLong("id"));
                group.setName(rs.getString("name"));
                groups.add(group);
            }
        }
        return groups;
    }

    /**
     * Retrieves a group by its name.
     *
     * @param name The name of the group.
     * @return The Group object, or null if not found.
     * @throws SQLException If a database access error occurs.
     */
    public Group getGroupByName(String name) throws SQLException {
        String query = "SELECT * FROM Groups WHERE name = ?;";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, name);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Group group = new Group();
                    group.setId(rs.getLong("id"));
                    group.setName(rs.getString("name"));
                    return group;
                }
            }
        }
        return null;
    }

    /**
     * Updates an existing group in the database.
     *
     * @param group The Group object with updated information.
     * @throws SQLException If a database access error occurs.
     */
    public void updateGroup(Group group) throws SQLException {
        String updateSQL = "UPDATE Groups SET name = ? WHERE id = ?;";
        try (PreparedStatement pstmt = connection.prepareStatement(updateSQL)) {
            pstmt.setString(1, group.getName());
            pstmt.setLong(2, group.getId());
            pstmt.executeUpdate();
        }
    }

    /**
     * Deletes a group from the database.
     *
     * @param groupId The ID of the group to delete.
     * @throws SQLException If a database access error occurs.
     */
    public void deleteGroup(long groupId) throws SQLException {
        String deleteSQL = "DELETE FROM Groups WHERE id = ?;";
        try (PreparedStatement pstmt = connection.prepareStatement(deleteSQL)) {
            pstmt.setLong(1, groupId);
            pstmt.executeUpdate();
        }
    }

    /**
     * Deletes all groups from the database.
     *
     * @throws SQLException If a database access error occurs.
     */
    public void deleteAllGroups() throws SQLException {
        String deleteSQL = "DELETE FROM Groups;";
        try (PreparedStatement pstmt = connection.prepareStatement(deleteSQL)) {
            pstmt.executeUpdate();
        }
    }
}
