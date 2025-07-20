package fr.esgi.pajavafx.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;

import java.net.URL;
import java.util.ResourceBundle;

public class HomeController implements Initializable {

    @FXML
    private BorderPane root;

    @FXML
    private Button btnClient;

    @FXML
    private TabPane tabPane;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        btnClient.setOnAction(event -> showClientTab());
        loadTabs();
    }

    private void loadTabs() {
        // Ajouter ici d'autres onglets si nécessaire
        tabPane.getTabs().clear(); // Pour éviter les doublons
        Tab clientTab = new Tab("Client", new ClientTabContent());
        tabPane.getTabs().add(clientTab);
    }

    private void showClientTab() {
        // Assurez-vous que l'onglet Client est visible
        tabPane.getSelectionModel().select(tabPane.getTabs().get(0)); // Supposons que c'est le premier onglet
    }
}