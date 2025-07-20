package fr.esgi.pajavafx.controllers;

import fr.esgi.pajavafx.ApiClient;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loginButton.setOnAction(event -> loginSuperAdmin());
    }

    private void loginSuperAdmin() {
        String email = emailField.getText().trim();
        String password = passwordField.getText().trim();

        // Validation des champs
        if (email.isEmpty() || password.isEmpty()) {
            System.out.println("Veuillez remplir tous les champs");
            return;
        }

        // Appel à l'API pour la connexion
        ApiClient.loginSuperAdmin(email, password, responseBody -> {
            Platform.runLater(() -> {
                System.out.println("Response Body: " + responseBody.trim());

                // Vérifiez si la réponse indique une connexion réussie
                if (responseBody.equals("Connexion réussie !")) {
                    System.out.println("Connexion réussie pour " + email);

                    // Charger la page d'accueil
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fr/esgi/pajavafx/Home.fxml"));
                        Parent root = loader.load();
                        Stage stage = (Stage) loginButton.getScene().getWindow();
                        stage.setScene(new Scene(root));
                        stage.show();
                    } catch (IOException e) {
                        System.out.println("Erreur lors du chargement de la page Home : " + e.getMessage());
                    }
                } else {
                    System.out.println("Erreur lors de la connexion : " + responseBody);
                }
            });
        });
    }
}
