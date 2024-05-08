package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import services.ServiceEvenement;
import entities.Evenement;
import javafx.scene.layout.TilePane;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class EventFrontController {
    @FXML
    private TilePane cardContainer;
    @FXML
    private TextField searchField;


    private ServiceEvenement serviceEvenement = new ServiceEvenement();

    @FXML
    public void initialize() {
        loadEventCards();

        // Add a listener to searchField to handle real-time search updates
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.isEmpty()) {
                loadEventCards(); // Reload all events if search field is cleared
            } else {
                loadEventCards(newValue); // Load filtered events based on the current input
            }
        });
    }





    private void showEventDetails(Evenement evenement) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/EventDetail.fxml"));
            Parent root = loader.load();

            EventDetailController controller = loader.getController();
            controller.setEvent(evenement);

            Stage stage = new Stage();
            stage.setTitle("Event Details");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadEventCards() {
        cardContainer.getChildren().clear(); // Clear previous cards
        try {
            for (Evenement event : serviceEvenement.afficher()) {
                VBox card = createEventCard(event);
                cardContainer.getChildren().add(card);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void loadEventCards(String keyword) {
        cardContainer.getChildren().clear(); // Clear previous cards
        try {
            for (Evenement event : serviceEvenement.afficher()) {
                if (eventMatches(event, keyword)) {
                    VBox card = createEventCard(event);
                    cardContainer.getChildren().add(card);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    private boolean eventMatches(Evenement event, String keyword) {
        return event.getNom().toLowerCase().contains(keyword.toLowerCase())
                || event.getDescription().toLowerCase().contains(keyword.toLowerCase());
    }

    private VBox createEventCard(Evenement event) {
        VBox card = new VBox(10);
        card.setPadding(new Insets(15));
        card.setStyle("-fx-border-color: navy; -fx-border-width: 2;");
        Label nameLabel = new Label("Name : " + event.getNom());
        Label descLabel = new Label("Description : " + event.getDescription());
        Button detailsButton = new Button("Show more Details");
        detailsButton.setOnAction(e -> showEventDetails(event));
        card.getChildren().addAll(nameLabel, descLabel, detailsButton);
        return card;
    }




}