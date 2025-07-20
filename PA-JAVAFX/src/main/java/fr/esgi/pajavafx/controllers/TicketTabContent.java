package fr.esgi.pajavafx.controllers;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class TicketTabContent extends VBox {

    public TicketTabContent() {
        Label label = new Label("Liste des tickets");
        TextField textField = new TextField();

        getChildren().addAll(label, textField);
        setPadding(new Insets(20));
        setSpacing(20);
    }
}