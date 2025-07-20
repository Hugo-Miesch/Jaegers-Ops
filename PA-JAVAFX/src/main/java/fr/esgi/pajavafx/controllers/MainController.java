package fr.esgi.pajavafx.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class MainController {

    @FXML
    private Label displayLabel;

    @FXML
    private Button btnClient;

    @FXML
    private Button btnTechnicien;

    @FXML
    private Button btnAdmin;

    @FXML
    private Button btnEntrepot;

    @FXML
    private Button btnRobot;

    @FXML
    private Button btnTicket;

    @FXML
    public void initialize() {
        // Assurez-vous que l'événement est bien configuré
        btnClient.setOnAction(event -> showClientList());
        // Ajoutez des événements pour les autres boutons si nécessaire
    }

    private void showClientList() {
        // Modifiez le texte du label
        displayLabel.setText("Liste des clients");
    }

    // Méthodes pour les autres boutons...
}
