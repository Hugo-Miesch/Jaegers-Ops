package fr.esgi.pajavafx.models;

public class Tech {
    private int id;
    private String name;

    public Tech(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
