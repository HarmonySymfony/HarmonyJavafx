package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import services.ServiceEvenement;
import entities.Evenement;
import javafx.scene.layout.TilePane;

import java.io.IOException;
import java.sql.SQLException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class EventFrontController {
    @FXML
    private TilePane cardContainer;

    private ServiceEvenement serviceEvenement = new ServiceEvenement();

    @FXML
    public void initialize() {
        loadEventCards();
    }

    private void loadEventCards() {
        try {
            for (Evenement event : serviceEvenement.afficher()) {
                VBox card = new VBox(10);
                card.setPadding(new Insets(15));
                card.setStyle("-fx-border-color: navy; -fx-border-width: 2;");

                Label nameLabel = new Label("Name : " + event.getNom());
                Label descLabel = new Label("Description : " + event.getDescription());
                Button detailsButton = new Button("Show more Details");
                detailsButton.setOnAction(e -> showEventDetails(event));

                card.getChildren().addAll(nameLabel, descLabel, detailsButton);
                cardContainer.getChildren().add(card);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
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


}
