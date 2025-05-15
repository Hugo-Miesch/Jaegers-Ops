package fr.esgi.pajavafx.controllers;

import fr.esgi.pajavafx.models.Warehouse;
import fr.esgi.pajavafx.utils.DataGenerator;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class WarehouseManagerController implements Initializable {
    @FXML
    private TableView<Warehouse> warehouseTable;
    @FXML
    private TableColumn<Warehouse, Integer> idColumn;
    @FXML
    private TableColumn<Warehouse, String> nameColumn;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        warehouseTable.getItems().addAll(DataGenerator.generateWarehouses(10));
    }
}
