package fr.esgi.pajavafx.controllers;

import fr.esgi.pajavafx.models.User;
import fr.esgi.pajavafx.ApiClient;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

public class ClientTabContent extends VBox {

    private TableView<User> clientTable = new TableView<>();

    public ClientTabContent() {
        setupUI();
        fetchClients();
    }

    private void setupUI() {
        // Configuration de l'interface utilisateur
        Button addButton = new Button("Ajouter Client");
        // Ajoutez d'autres boutons ici

        getChildren().addAll(clientTable, addButton);
        setPadding(new Insets(10));
        setSpacing(10);
    }

    private void fetchClients() {
        ApiClient.getUsers(users -> {
            if (users != null) {
                clientTable.getItems().clear();
                clientTable.getItems().addAll(users);
            } else {
                showError("Erreur lors de la récupération des clients");
            }
        });
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
