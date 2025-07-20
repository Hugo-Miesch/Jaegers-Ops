package fr.esgi.pajavafx.models;

public class User {
    private int id;
    private String email;
    private String name;
    private String image;

    public User(int id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.image = null; // Valeur par défaut
    }

    // Getters et Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
