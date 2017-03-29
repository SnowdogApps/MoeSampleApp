package pl.snowdog.model;

public class Repository {

    private int id;
    private String name;
    private String description;
    private String language;
    private Owner owner;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getLanguage() {
        return language;
    }

    public Owner getOwner() {
        return owner;
    }
}
