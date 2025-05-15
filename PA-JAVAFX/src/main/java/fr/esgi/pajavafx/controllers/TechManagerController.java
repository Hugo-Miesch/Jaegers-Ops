package fr.esgi.pajavafx.controllers;

import fr.esgi.pajavafx.models.Tech;
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

public class TechManagerController {
    @FXML
    private TableView<Tech> techTable;

    @FXML
    private TableColumn<Tech, Integer> idColumn;

    @FXML
    private TableColumn<Tech, String> nameColumn;

    @FXML
    private TableColumn<Tech, String> emailColumn;

    private int nextId;

    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));

        ObservableList<Tech> techs = DataGenerator.generateTechs();
        techTable.setItems(techs);

        // Définir la valeur de nextId
        nextId = techs.stream()
                .mapToInt(Tech::getId)
                .max()
                .orElse(0) + 1;
    }

    @FXML
    private void handleCreate() {
        Tech newTech = new Tech(nextId++, "", "");
        Dialog<Tech> dialog = new Dialog<>();
        dialog.setTitle("Créer un nouveau tech");
        dialog.setHeaderText("Saisir les informations du nouveau tech");

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
                newTech.setName(nameField.getText());
                newTech.setEmail(emailField.getText());
                return newTech;
            }
            return null;
        });

        Optional<Tech> result = dialog.showAndWait();
        if (result.isPresent()) {
            techTable.getItems().add(result.get());
        }
    }

    @FXML
    private void handleEdit() {
        Tech selectedTech = techTable.getSelectionModel().getSelectedItem();
        if (selectedTech != null) {
            Dialog<Tech> dialog = new Dialog<>();
            dialog.setTitle("Modifier le tech");
            dialog.setHeaderText("Modifier les informations du tech");

            TextField nameField = new TextField(selectedTech.getName());
            TextField emailField = new TextField(selectedTech.getEmail());

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
                    selectedTech.setName(nameField.getText());
                    selectedTech.setEmail(emailField.getText());
                    return selectedTech;
                }
                return null;
            });

            dialog.showAndWait();
            techTable.refresh();
        }
    }

    @FXML
    private void handleDelete() {
        Tech selectedTech = techTable.getSelectionModel().getSelectedItem();
        if (selectedTech != null) {
            techTable.getItems().remove(selectedTech);
        }
    }
}
