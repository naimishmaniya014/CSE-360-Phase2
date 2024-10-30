package models;

import java.io.Serializable;

public class Group implements Serializable {
    private static final long serialVersionUID = 1L;

    private long id;
    private String name;

    // Constructors
    public Group() {}

    public Group(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Group(String name) {
        this.name = name;
    }

    // Getters and Setters

    public long getId() {
        return id;
    }

    public void setId(long id) { // Ensure this method exists
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) { // Ensure this method exists
        this.name = name;
    }

    // toString method for display purposes
    @Override
    public String toString() {
        return name;
    }
}
