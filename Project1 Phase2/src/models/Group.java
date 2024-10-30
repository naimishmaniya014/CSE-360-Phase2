package models;

public class Group {
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

    // ID is set by the database
    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
