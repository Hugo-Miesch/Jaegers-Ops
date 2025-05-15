package fr.esgi.pajavafx.controllers;

import fr.esgi.pajavafx.models.Robot;
import fr.esgi.pajavafx.utils.DataGenerator;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class RobotManagerController implements Initializable {
    @FXML
    private TableView<Robot> robotTable;
    @FXML
    private TableColumn<Robot, Integer> idColumn;
    @FXML
    private TableColumn<Robot, String> nameColumn;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        robotTable.getItems().addAll(DataGenerator.generateRobots(10));
    }
}

