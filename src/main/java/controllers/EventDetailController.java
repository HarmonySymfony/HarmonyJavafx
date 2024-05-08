package controllers;

import entities.Evenement;
import entities.Reservation;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;
import org.controlsfx.control.Rating;
import services.ServiceEvenement;
import javafx.scene.control.*;




import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.ListView;
import javafx.scene.control.ListCell;

import java.util.*;


public class EventDetailController {
    @FXML
    private Label nameLabel, descriptionLabel, priceLabel, placesLabel, addressLabel, messageLabel , messageLabels;



    @FXML private TextField reservationPlacesField;

    @FXML
    private TextArea commentTextArea;
    @FXML
    private ListView<String> commentListView;
    @FXML
    private Rating eventRating;


    private Set<String> bannedWords = new HashSet<>(Arrays.asList("pute", "fuck", "tiiiitt"));


    private ObservableList<String> commentList = FXCollections.observableArrayList();


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


    private void showConfirmation(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Operation Successful");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
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

            placesLabel.setText("Available Places: " + evenement.getPlaceDispo());
            messageLabel.setText("Success: Reservation made.");
            reservationPlacesField.clear();
            showConfirmation("Event reserved successfully.");
            Notifications notifications = Notifications.create();
            notifications.text("Event reserved successfully !");
            notifications.title("Successful");
            notifications.hideAfter(Duration.seconds(6));
            notifications.show();
        } catch (NumberFormatException e) {
            messageLabel.setText("Error: Please enter a valid number.");
        } catch (Exception e) {
            messageLabel.setText("An unexpected error occurred.");
            e.printStackTrace();
        }
    }





    @FXML
    public void initialize() {
        eventRating.setRating(0);

        eventRating.ratingProperty().addListener((obs, oldVal, newVal) -> {
            // Replace with your logic to handle the rating change
            System.out.println("New Rating: " + newVal.doubleValue());
            // Call your method to handle the new rating
            saveRating(newVal.doubleValue());
        });

        // Setting up the comment list view
        commentListView.setItems(commentList);
        commentListView.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item);
                }
            }
        });

        // Load comments if the event is already initialized
        if (evenement != null) {
            loadCommentsFromServer();
        } else {
            System.out.println("Event not initialized");
        }
    }


    public void saveRating(double rating) {
        // Your code to save the rating, e.g., update a database or a model
        System.out.println("Rating saved: " + rating);
    }


    @FXML
    private void handleCommentSubmit() {
        String comment = commentTextArea.getText().trim();
        if (!comment.isEmpty()) {
            if (containsBannedWords(comment)) {
                messageLabel.setText("Comment contains inappropriate content !!!");
                showConfirmation("Comment contains inappropriate content !!!");
                Notifications notifications = Notifications.create();
                notifications.text("Comment contains inappropriate content !!! !");
                notifications.title("Warning !");
                notifications.hideAfter(Duration.seconds(6));
                notifications.show();
                commentTextArea.setText("");
            } else {
                serviceEvenement.saveComment(evenement.getId(), comment);
                commentList.add(comment);
                commentTextArea.clear();
                showConfirmation("Comment added successfully !");
                Notifications notifications = Notifications.create();
                notifications.text("Comment added successfully !");
                notifications.title("Successful");
                notifications.hideAfter(Duration.seconds(6));
                notifications.show();
                messageLabel.setText("Comment added successfully !");
            }
        } else {
            messageLabel.setText("Comment is empty.");

        }
    }

    private boolean containsBannedWords(String comment) {
        for (String word : comment.split("\\s+")) { // Split by whitespace
            if (bannedWords.contains(word.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    private void loadCommentsFromServer() {
        List<String> comments = serviceEvenement.getComments(evenement.getId());
        commentList.addAll(comments);
    }





}


















