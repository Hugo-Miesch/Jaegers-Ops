package fr.esgi.pajavafx.controllers;

import fr.esgi.pajavafx.models.User;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.ObservableList;
import fr.esgi.pajavafx.utils.DataGenerator;
import javafx.scene.control.Dialog;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import java.util.Optional;

public class UserListController {
    @FXML
    private TableView<User> userTable;

    @FXML
    private TableColumn<User, Integer> idColumn;

    @FXML
    private TableColumn<User, String> nameColumn;

    @FXML
    private TableColumn<User, String> emailColumn;

    private int nextId;

    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));

        ObservableList<User> users = DataGenerator.generateUsers();
        userTable.setItems(users);

        // Définir la valeur de nextId
        nextId = users.stream()
                .mapToInt(User::getId)
                .max()
                .orElse(0) + 1;
    }

    @FXML
    private void handleCreate() {
        User newUser = new User(nextId++, "", "");
        Dialog<User> dialog = new Dialog<>();
        dialog.setTitle("Créer un nouvel utilisateur");
        dialog.setHeaderText("Saisir les informations de l'utilisateur");

        TextField nameField = new TextField();
        TextField emailField = new TextField();

        GridPane grid = new GridPane();
        grid.add(new Label("Nom :"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Email :"), 0, 1);
        grid.add(emailField, 1, 1);
        dialog.getDialogPane().setContent(grid);

        ButtonType okButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == okButtonType) {
                newUser.setName(nameField.getText());
                newUser.setEmail(emailField.getText());
                return newUser;
            }
            return null;
        });

        Optional<User> result = dialog.showAndWait();
        if (result.isPresent()) {
            userTable.getItems().add(result.get());
        }
    }

    @FXML
    private void handleEdit() {
        User selectedUser = userTable.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            Dialog<User> dialog = new Dialog<>();
            dialog.setTitle("Modifier l'utilisateur");
            dialog.setHeaderText("Modifier les informations de l'utilisateur");

            TextField nameField = new TextField(selectedUser.getName());
            TextField emailField = new TextField(selectedUser.getEmail());

            GridPane grid = new GridPane();
            grid.add(new Label("Nom :"), 0, 0);
            grid.add(nameField, 1, 0);
            grid.add(new Label("Email :"), 0, 1);
            grid.add(emailField, 1, 1);
            dialog.getDialogPane().setContent(grid);

            ButtonType okButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);

            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == okButtonType) {
                    selectedUser.setName(nameField.getText());
                    selectedUser.setEmail(emailField.getText());
                    return selectedUser;
                }
                return null;
            });

            dialog.showAndWait();
            userTable.refresh();
        }
    }

    @FXML
    private void handleDelete() {
        User selectedUser = userTable.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            userTable.getItems().remove(selectedUser);
        }
    }
}
