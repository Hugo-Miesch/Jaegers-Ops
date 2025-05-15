package fr.esgi.pajavafx.controllers;

import fr.esgi.pajavafx.models.Tech;
import fr.esgi.pajavafx.utils.DataGenerator;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class TechManagerController implements Initializable {
    @FXML
    private TableView<Tech> techTable;
    @FXML
    private TableColumn<Tech, Integer> idColumn;
    @FXML
    private TableColumn<Tech, String> nameColumn;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        techTable.getItems().addAll(DataGenerator.generateTechs(10));
    }
}
