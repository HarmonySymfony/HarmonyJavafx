package controllers;

import entities.Evenement;
import entities.Reservation;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import services.ServiceEvenement;
import javafx.scene.control.*;


public class EventDetailController {
    @FXML
    private Label nameLabel, descriptionLabel, priceLabel, placesLabel, addressLabel, messageLabel;

    @FXML private TextField reservationPlacesField;

    private ServiceEvenement serviceEvenement = new ServiceEvenement();

    private Evenement evenement;


    public void setEvent(Evenement evenement) {
        this.evenement = evenement;
        nameLabel.setText("Name :    " + evenement.getNom());
        descriptionLabel.setText("Description :    " + evenement.getDescription());
        priceLabel.setText("Price :   " + evenement.getPrix() + " DT");
        placesLabel.setText("Available Places :   " + evenement.getPlaceDispo());
        addressLabel.setText("Address :   " + evenement.getAdresse());
    }




    @FXML
    private void handleReserveAction() {

        try {
            int requestedPlaces = Integer.parseInt(reservationPlacesField.getText());
            if (requestedPlaces > evenement.getPlaceDispo()) {
                messageLabel.setText("Error: Not enough places available.");
                return;
            }

            Reservation reservation = new Reservation(0, requestedPlaces, evenement);
            serviceEvenement.addReservation(reservation);

            // Updating UI
            placesLabel.setText("Available Places: " + evenement.getPlaceDispo());
            messageLabel.setText("Success: Reservation made.");
            reservationPlacesField.clear();
        } catch (NumberFormatException e) {
            messageLabel.setText("Error: Please enter a valid number.");
        } catch (Exception e) {
            messageLabel.setText("An unexpected error occurred.");
            e.printStackTrace();
        }
    }
}
