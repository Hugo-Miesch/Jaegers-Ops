package fr.esgi.pajavafx.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    @FXML
    private TextField emailField;
    @FXML
    private TextField passwordField;
    @FXML
    private Button loginButton;

    private static final String DEFAULT_EMAIL = "admin@admin.com";
    private static final String DEFAULT_PASSWORD = "admin";

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Initialisation
    }

    @FXML
    private void handleLogin(javafx.event.ActionEvent event) throws IOException {
        String email = emailField.getText();
        String password = passwordField.getText();

        if (email.isEmpty() || password.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setContentText("Veuillez remplir tous les champs");
            alert.showAndWait();
            return;
        }

        if (email.equals(DEFAULT_EMAIL) && password.equals(DEFAULT_PASSWORD)) {
            Stage stage = (Stage) loginButton.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("/fr/esgi/pajavafx/UserList.fxml"));
            stage.setTitle("Liste des utilisateurs");
            stage.setScene(new Scene(root, 800, 600));
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setContentText("Email ou mot de passe incorrect");
            alert.showAndWait();
        }
    }
}
