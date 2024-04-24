package controllers;

import entities.Evenement;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class EventDetailController {
    @FXML
    private Label nameLabel, descriptionLabel, priceLabel, placesLabel, addressLabel;

    public void setEvent(Evenement event) {
        nameLabel.setText("Name :   " + event.getNom());
        descriptionLabel.setText("Description :   " + event.getDescription());
        priceLabel.setText("Price :   " + event.getPrix() + " DT");
        placesLabel.setText("Available Places :   " + event.getPlaceDispo());
        addressLabel.setText("Address :   " + event.getAdresse());


    }
}
