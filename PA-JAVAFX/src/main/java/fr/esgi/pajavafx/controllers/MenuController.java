package fr.esgi.pajavafx.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MenuController implements Initializable {
    @FXML
    private void handleUserManager(javafx.event.ActionEvent event) throws IOException {
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/fr/esgi/pajavafx/UserManager.fxml"));
        stage.setTitle("Gestion des utilisateurs");
        stage.setScene(new Scene(root, 800, 600));
    }

    @FXML
    private void handleTechManager(javafx.event.ActionEvent event) throws IOException {
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/fr/esgi/pajavafx/TechManager.fxml"));
        stage.setTitle("Gestion des techniciens");
        stage.setScene(new Scene(root, 800, 600));
    }

    @FXML
    private void handleWarehouseManager(javafx.event.ActionEvent event) throws IOException {
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/fr/esgi/pajavafx/WarehouseManager.fxml"));
        stage.setTitle("Gestion des entrepôts");
        stage.setScene(new Scene(root, 800, 600));
    }

    @FXML
    private void handleRobotManager(javafx.event.ActionEvent event) throws IOException {
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/fr/esgi/pajavafx/RobotManager.fxml"));
        stage.setTitle("Gestion des robots");
        stage.setScene(new Scene(root, 800, 600));
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Initialisation
    }

}
