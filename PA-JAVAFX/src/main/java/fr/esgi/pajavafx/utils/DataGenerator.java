package fr.esgi.pajavafx.utils;

import fr.esgi.pajavafx.models.Robot;
import fr.esgi.pajavafx.models.Tech;
import fr.esgi.pajavafx.models.User;
import fr.esgi.pajavafx.models.Warehouse;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class DataGenerator {
    public static ObservableList<User> generateUsers() {
        ObservableList<User> users = FXCollections.observableArrayList();
        users.add(new User(1, "John Doe", "john.doe@example.com"));
        users.add(new User(2, "Jane Doe", "jane.doe@example.com"));
        return users;
    }

    public static ObservableList<Tech> generateTechs() {
        ObservableList<Tech> techs = FXCollections.observableArrayList();
        techs.add(new Tech(1, "John Tech", "john.tech@example.com"));
        techs.add(new Tech(2, "Jane Tech", "jane.tech@example.com"));
        return techs;
    }

    public static ObservableList<Warehouse> generateWarehouses() {
        ObservableList<Warehouse> warehouses = FXCollections.observableArrayList();
        warehouses.add(new Warehouse(1, "Warehouse 1", "123 Main St"));
        warehouses.add(new Warehouse(2, "Warehouse 2", "456 Elm St"));
        return warehouses;
    }

    public static ObservableList<Robot> generateRobots() {
        ObservableList<Robot> robots = FXCollections.observableArrayList();
        robots.add(new Robot(1, "Robot 1", "Type A"));
        robots.add(new Robot(2, "Robot 2", "Type B"));
        return robots;
    }
}

