package fr.esgi.pajavafx.controllers;

import fr.esgi.pajavafx.models.Warehouse;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import fr.esgi.pajavafx.utils.DataGenerator;
import javafx.collections.ObservableList;
import javafx.scene.control.Dialog;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import java.util.Optional;

public class WarehouseManagerController {
    @FXML
    private TableView<Warehouse> warehouseTable;

    @FXML
    private TableColumn<Warehouse, Integer> idColumn;

    @FXML
    private TableColumn<Warehouse, String> nameColumn;

    @FXML
    private TableColumn<Warehouse, String> addressColumn;

    private int nextId;

    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));

        ObservableList<Warehouse> warehouses = DataGenerator.generateWarehouses();
        warehouseTable.setItems(warehouses);

        // Définir la valeur de nextId
        nextId = warehouses.stream()
                .mapToInt(Warehouse::getId)
                .max()
                .orElse(0) + 1;
    }

    @FXML
    private void handleCreate() {
        Warehouse newWarehouse = new Warehouse(nextId++, "", "");
        Dialog<Warehouse> dialog = new Dialog<>();
        dialog.setTitle("Créer un nouvel entrepôt");
        dialog.setHeaderText("Saisir les informations de l'entrepôt");

        TextField nameField = new TextField();
        TextField addressField = new TextField();

        GridPane grid = new GridPane();
        grid.add(new Label("Nom :"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Adresse :"), 0, 1);
        grid.add(addressField, 1, 1);
        dialog.getDialogPane().setContent(grid);

        ButtonType okButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == okButtonType) {
                newWarehouse.setName(nameField.getText());
                newWarehouse.setAddress(addressField.getText());
                return newWarehouse;
            }
            return null;
        });

        Optional<Warehouse> result = dialog.showAndWait();
        if (result.isPresent()) {
            warehouseTable.getItems().add(result.get());
        }
    }

    @FXML
    private void handleEdit() {
        Warehouse selectedWarehouse = warehouseTable.getSelectionModel().getSelectedItem();
        if (selectedWarehouse != null) {
            Dialog<Warehouse> dialog = new Dialog<>();
            dialog.setTitle("Modifier l'entrepôt");
            dialog.setHeaderText("Modifier les informations de l'entrepôt");

            TextField nameField = new TextField(selectedWarehouse.getName());
            TextField addressField = new TextField(selectedWarehouse.getAddress());

            GridPane grid = new GridPane();
            grid.add(new Label("Nom :"), 0, 0);
            grid.add(nameField, 1, 0);
            grid.add(new Label("Adresse :"), 0, 1);
            grid.add(addressField, 1, 1);
            dialog.getDialogPane().setContent(grid);

            ButtonType okButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);

            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == okButtonType) {
                    selectedWarehouse.setName(nameField.getText());
                    selectedWarehouse.setAddress(addressField.getText());
                    return selectedWarehouse;
                }
                return null;
            });

            dialog.showAndWait();
            warehouseTable.refresh();
        }
    }

    @FXML
    private void handleDelete() {
        Warehouse selectedWarehouse = warehouseTable.getSelectionModel().getSelectedItem();
        if (selectedWarehouse != null) {
            warehouseTable.getItems().remove(selectedWarehouse);
        }
    }
}
