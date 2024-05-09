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
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Screen;
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
    private WebView webView;



    @FXML
    public void initialize() {
        loadEventCards();

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.isEmpty()) {
                loadEventCards();
            } else {
                loadEventCards(newValue);
            }
        });
        double screenWidth = Screen.getPrimary().getVisualBounds().getWidth();
        double screenHeight = Screen.getPrimary().getVisualBounds().getHeight();

        // Lier la taille de la WebView à la taille de l'écran
        webView.setPrefWidth(screenWidth);
        webView.setPrefHeight(screenHeight);

        // Charger le fichier HTML avec le fond animé
        WebEngine webEngine = webView.getEngine();

        // Charger le fichier HTML contenant la carte Google Maps
        webEngine.load(getClass().getResource("/HTML/index.html").toExternalForm());
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
        cardContainer.getChildren().clear();
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
        cardContainer.getChildren().clear();
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

    @FXML
    void retourfront(ActionEvent event) {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/Homepage.fxml"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();

    }
    }


