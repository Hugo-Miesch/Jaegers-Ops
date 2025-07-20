package fr.esgi.pajavafx.controllers;

import fr.esgi.pajavafx.models.User;
import fr.esgi.pajavafx.ApiClient;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

public class UserListController {

    @FXML private TableView<User> clientTable;
    @FXML private TableColumn<User, Integer> idColumn;
    @FXML private TableColumn<User, String> nameColumn;
    @FXML private TableColumn<User, String> emailColumn;
    @FXML private TextField nameField;
    @FXML private TextField emailField;
    @FXML private Button addButton;
    @FXML private Button editButton;
    @FXML private Button deleteButton;

    @FXML
    public void initialize() {
        // Configuration des colonnes
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));

        // Chargement initial des utilisateurs
        fetchUsers();

        // Actions des boutons
        addButton.setOnAction(event -> addUser());
        editButton.setOnAction(event -> editUser());
        deleteButton.setOnAction(event -> deleteUser());
    }

    private void fetchUsers() {
        ApiClient.getUsers(users -> {
            clientTable.getItems().clear();
            if (users != null) {
                clientTable.getItems().addAll(users);
            } else {
                showError("Erreur lors de la récupération des utilisateurs.");
            }
        });
    }

    private void addUser() {
        String name = nameField.getText();
        String email = emailField.getText();

        if (name.isEmpty() || email.isEmpty()) {
            showError("Veuillez remplir tous les champs.");
            return;
        }

        User newUser = new User(0, name, email); // Laissez ID géré par le serveur

        ApiClient.addUser(newUser, result -> {
            if (result) {
                fetchUsers(); // Récupérer et afficher les utilisateurs
                clearFields(); // Nettoyer les champs de texte
            } else {
                showError("Erreur lors de l'ajout de l'utilisateur."); // Afficher une erreur
            }
        });
    }

    private void editUser() {
        User selectedUser = clientTable.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            selectedUser.setName(nameField.getText());
            selectedUser.setEmail(emailField.getText());

            ApiClient.updateUser(selectedUser, result -> {
                if (result) {
                    fetchUsers();
                    clearFields();
                } else {
                    showError("Erreur lors de la mise à jour de l'utilisateur.");
                }
            });
        } else {
            showError("Veuillez sélectionner un utilisateur à modifier.");
        }
    }

    private void deleteUser() {
        User selectedUser = clientTable.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            ApiClient.deleteUser(selectedUser.getId(), result -> {
                if (result) {
                    fetchUsers();
                } else {
                    showError("Erreur lors de la suppression de l'utilisateur.");
                }
            });
        } else {
            showError("Veuillez sélectionner un utilisateur à supprimer.");
        }
    }

    private void clearFields() {
        nameField.clear();
        emailField.clear();
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
