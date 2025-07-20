package fr.esgi.pajavafx.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MenuController {

    @FXML
    private void goToUser() throws IOException {
        Stage newStage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("/fr/esgi/pajavafx/UserList.fxml")); // Assurez-vous d'utiliser le bon FXML
        newStage.setTitle("Utilisateurs");
        newStage.setScene(new Scene(root, 800, 600));
        newStage.show();
    }

    // Ajoutez d'autres méthodes si nécessaire
}
