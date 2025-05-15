package fr.esgi.pajavafx.utils;

import fr.esgi.pajavafx.models.Robot;
import fr.esgi.pajavafx.models.Tech;
import fr.esgi.pajavafx.models.User;
import fr.esgi.pajavafx.models.Warehouse;

import java.util.ArrayList;
import java.util.List;

public class DataGenerator {
    public static List<User> generateUsers(int count) {
        List<User> users = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            users.add(new User(i, "User " + i, "user" + i + "@example.com"));
        }
        return users;
    }

    public static List<Tech> generateTechs(int count) {
        List<Tech> techs = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            techs.add(new Tech(i, "Tech " + i));
        }
        return techs;
    }

    public static List<Warehouse> generateWarehouses(int count) {
        List<Warehouse> warehouses = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            warehouses.add(new Warehouse(i, "Warehouse " + i));
        }
        return warehouses;
    }

    public static List<Robot> generateRobots(int count) {
        List<Robot> robots = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            robots.add(new Robot(i, "Robot " + i));
        }
        return robots;
    }
}
