package fr.esgi.pajavafx.models;

public class Robot {
    private int id;
    private String name;
    private int entrepotId;
    private String imagePath;

    // Getters et setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getEntrepotId() {
        return entrepotId;
    }

    public void setEntrepotId(int entrepotId) {
        this.entrepotId = entrepotId;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
