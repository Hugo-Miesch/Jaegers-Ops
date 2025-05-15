package fr.esgi.pajavafx.controllers;

import fr.esgi.pajavafx.models.Robot;
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

public class RobotManagerController {
    @FXML
    private TableView<Robot> robotTable;

    @FXML
    private TableColumn<Robot, Integer> idColumn;

    @FXML
    private TableColumn<Robot, String> nameColumn;

    @FXML
    private TableColumn<Robot, String> typeColumn;

    private int nextId;

    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));

        ObservableList<Robot> robots = DataGenerator.generateRobots();
        robotTable.setItems(robots);

        // Définir la valeur de nextId
        nextId = robots.stream()
                .mapToInt(Robot::getId)
                .max()
                .orElse(0) + 1;
    }

    @FXML
    private void handleCreate() {
        Robot newRobot = new Robot(nextId++, "", "");
        Dialog<Robot> dialog = new Dialog<>();
        dialog.setTitle("Créer un nouveau robot");
        dialog.setHeaderText("Saisir les informations du robot");

        TextField nameField = new TextField();
        TextField typeField = new TextField();

        GridPane grid = new GridPane();
        grid.add(new Label("Nom :"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Type :"), 0, 1);
        grid.add(typeField, 1, 1);
        dialog.getDialogPane().setContent(grid);

        ButtonType okButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == okButtonType) {
                newRobot.setName(nameField.getText());
                newRobot.setType(typeField.getText());
                return newRobot;
            }
            return null;
        });

        Optional<Robot> result = dialog.showAndWait();
        if (result.isPresent()) {
            robotTable.getItems().add(result.get());
        }
    }

    @FXML
    private void handleEdit() {
        Robot selectedRobot = robotTable.getSelectionModel().getSelectedItem();
        if (selectedRobot != null) {
            Dialog<Robot> dialog = new Dialog<>();
            dialog.setTitle("Modifier le robot");
            dialog.setHeaderText("Modifier les informations du robot");

            TextField nameField = new TextField(selectedRobot.getName());
            TextField typeField = new TextField(selectedRobot.getType());

            GridPane grid = new GridPane();
            grid.add(new Label("Nom :"), 0, 0);
            grid.add(nameField, 1, 0);
            grid.add(new Label("Type :"), 0, 1);
            grid.add(typeField, 1, 1);
            dialog.getDialogPane().setContent(grid);

            ButtonType okButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);

            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == okButtonType) {
                    selectedRobot.setName(nameField.getText());
                    selectedRobot.setType(typeField.getText());
                    return selectedRobot;
                }
                return null;
            });

            dialog.showAndWait();
            robotTable.refresh();
        }
    }

    @FXML
    private void handleDelete() {
        Robot selectedRobot = robotTable.getSelectionModel().getSelectedItem();
        if (selectedRobot != null) {
            robotTable.getItems().remove(selectedRobot);
        }
    }
}
