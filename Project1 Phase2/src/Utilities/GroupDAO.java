package Utilities;

import models.Group;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GroupDAO {
    private Connection connection;

    public GroupDAO() throws SQLException {
        connection = DatabaseManager.getInstance().getConnection();
    }

    /**
     * Adds a new Group to the database.
     * 
     * @param group The Group to add.
     * @return The generated ID of the new group.
     * @throws SQLException if database operation fails.
     */
    public long addGroup(Group group) throws SQLException {
        String insertGroupSQL = "INSERT INTO Groups (name) VALUES (?);";
        try (PreparedStatement pstmt = connection.prepareStatement(insertGroupSQL, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, group.getName());
            pstmt.executeUpdate();

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    long id = generatedKeys.getLong(1);
                    group.setId(id);
                    return id;
                } else {
                    throw new SQLException("Creating group failed, no ID obtained.");
                }
            }
        }
    }

    /**
     * Updates an existing Group in the database.
     * 
     * @param group The Group with updated information.
     * @throws SQLException if database operation fails.
     */
    public void updateGroup(Group group) throws SQLException {
        String updateGroupSQL = "UPDATE Groups SET name=? WHERE id=?;";
        try (PreparedStatement pstmt = connection.prepareStatement(updateGroupSQL)) {
            pstmt.setString(1, group.getName());
            pstmt.setLong(2, group.getId());
            pstmt.executeUpdate();
        }
    }

    /**
     * Deletes a Group from the database.
     * 
     * @param id The ID of the Group to delete.
     * @throws SQLException if database operation fails.
     */
    public void deleteGroup(long id) throws SQLException {
        String deleteGroupSQL = "DELETE FROM Groups WHERE id=?;";
        try (PreparedStatement pstmt = connection.prepareStatement(deleteGroupSQL)) {
            pstmt.setLong(1, id);
            pstmt.executeUpdate();
        }
    }

    /**
     * Retrieves a Group by its ID.
     * 
     * @param id The ID of the Group.
     * @return The Group if found, else null.
     * @throws SQLException if database operation fails.
     */
    public Group getGroupById(long id) throws SQLException {
        String selectGroupSQL = "SELECT * FROM Groups WHERE id=?;";
        try (PreparedStatement pstmt = connection.prepareStatement(selectGroupSQL)) {
            pstmt.setLong(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return extractGroupFromResultSet(rs);
                }
            }
        }
        return null;
    }

    /**
     * Retrieves a Group by its name.
     * 
     * @param name The name of the Group.
     * @return The Group if found, else null.
     * @throws SQLException if database operation fails.
     */
    public Group getGroupByName(String name) throws SQLException {
        String selectGroupSQL = "SELECT * FROM Groups WHERE name=?;";
        try (PreparedStatement pstmt = connection.prepareStatement(selectGroupSQL)) {
            pstmt.setString(1, name);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return extractGroupFromResultSet(rs);
                }
            }
        }
        return null;
    }

    /**
     * Retrieves all Groups from the database.
     * 
     * @return A list of all Groups.
     * @throws SQLException if database operation fails.
     */
    public List<Group> getAllGroups() throws SQLException {
        List<Group> groups = new ArrayList<>();
        String selectAllSQL = "SELECT * FROM Groups;";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(selectAllSQL)) {
            while (rs.next()) {
                groups.add(extractGroupFromResultSet(rs));
            }
        }
        return groups;
    }

    /**
     * Extracts a Group object from the current row of the ResultSet.
     * 
     * @param rs The ResultSet positioned at the desired row.
     * @return A Group object.
     * @throws SQLException if data extraction fails.
     */
    private Group extractGroupFromResultSet(ResultSet rs) throws SQLException {
        long id = rs.getLong("id");
        String name = rs.getString("name");
        return new Group(id, name);
    }
}
