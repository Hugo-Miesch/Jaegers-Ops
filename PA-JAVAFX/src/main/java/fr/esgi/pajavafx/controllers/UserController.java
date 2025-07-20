package fr.esgi.pajavafx.controllers;

import fr.esgi.pajavafx.models.User; // Votre modèle d'utilisateur
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class UserController {

    private TableView<User> userTable;

    public UserController(TableView<User> table) {
        this.userTable = table;
    }

    private String sendRequest(String method, String endpoint, String jsonData) throws Exception {
        URL url = new URL("http://jaegers-ops.duckdns.org:54122" + endpoint); // Remplacez par l'URL de votre API
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod(method);
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);

        if (jsonData != null) {
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonData.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }
        }

        // Lire la réponse
        StringBuilder response = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
        }

        return response.toString();
    }

    public void getAllUsers() {
        try {
            String response = sendRequest("GET", "/clients", null);
            ObservableList<User> users = FXCollections.observableArrayList();
            // Parsing la réponse JSON (vous pourriez utiliser une bibliothèque comme Gson ou Jackson)
            // Ajouter les utilisateurs à userTable ici
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public User getUserById(int id) {
        User user = null;
        try {
            String response = sendRequest("GET", "/clients/" + id, null);
            // Parsing la réponse JSON et créer un objet User
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }

    public void updateUser(int id, String name, String email, String password) {
        try {
            // Créez l'objet JSON pour mettre à jour
            String jsonData = String.format("{\"name\": \"%s\", \"email\": \"%s\", \"password\": \"%s\"}", name, email, password);
            sendRequest("PUT", "/clients/" + id, jsonData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteUser(int id) {
        try {
            sendRequest("DELETE", "/clients/" + id, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
