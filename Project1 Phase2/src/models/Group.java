package models;

import java.io.Serializable;

/**
 * <p> Title: Group Class </p>
 * 
 * <p> Description: This class represents a Group entity within the application.
 * It encapsulates the group's ID and name, providing constructors, getters, setters,
 * and utility methods for object comparison and representation. </p>
 * 
 * @author Naimish Maniya
 * 
 * <p> @version 1.00  2024-10-29  Initial version. </p>
 */
public class Group implements Serializable {
    private static final long serialVersionUID = 1L;

    private long id;
    private String name;

    /**
     * Default constructor for Group.
     */
    public Group() {}

    /**
     * Constructs a Group with the specified ID and name.
     *
     * @param id   The unique identifier for the group.
     * @param name The name of the group.
     */
    public Group(long id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * Constructs a Group with the specified name.
     *
     * @param name The name of the group.
     */
    public Group(String name) {
        this.name = name;
    }

    /**
     * Retrieves the ID of the group.
     *
     * @return The group's ID.
     */
    public long getId() {
        return id;
    }

    /**
     * Sets the ID of the group.
     *
     * @param id The group's ID.
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Retrieves the name of the group.
     *
     * @return The group's name.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the group.
     *
     * @param name The group's name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns a string representation of the group.
     *
     * @return The group's name.
     */
    @Override
    public String toString() {
        return name;
    }
}
