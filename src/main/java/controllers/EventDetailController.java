package controllers;

import entities.Evenement;
import entities.Reservation;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.web.WebView;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;
import org.controlsfx.control.Rating;
import services.ServiceEvenement;
import javafx.scene.control.*;
import services.WeatherData;
import services.WeatherService;




import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.ListView;
import javafx.scene.control.ListCell;

import java.util.*;


public class EventDetailController {
    @FXML
    private Label nameLabel, descriptionLabel, priceLabel, placesLabel, addressLabel, messageLabel , messageLabels, weatherLabel;


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

    @FXML
    private WebView mapView = new WebView();
    private double eventLongitude;
    private double eventLatitude;

    public EventDetailController() {
    }


    public void setEvent(Evenement evenement) {
        this.evenement = evenement;
        nameLabel.setText("Name :    " + evenement.getNom());
        descriptionLabel.setText("Description :    " + evenement.getDescription());
        priceLabel.setText("Price :   " + evenement.getPrix() + " DT");
        placesLabel.setText("Available Places :   " + evenement.getPlaceDispo());
        addressLabel.setText("Address :   " + evenement.getAdresse());
        eventLatitude = evenement.getLongitude();
        eventLongitude = evenement.getLatitude();
        displayWeather(evenement.getAdresse());
        updateMapView();
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

        private void updateMapView() {
            String mapUrl = getClass().getResource("/map.html").toExternalForm();
            mapView.getEngine().load(mapUrl);
            mapView.getEngine().setJavaScriptEnabled(true);

            mapView.getEngine().getLoadWorker().stateProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue == Worker.State.SUCCEEDED) {
                    // Define the initMap function
                    mapView.getEngine().executeScript("function initMap(latitude, longitude) {" +
                            "var map = L.map('map').setView([latitude, longitude], 13);" +
                            "L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png').addTo(map);" +
                            "L.marker([latitude, longitude]).addTo(map);" +
                            "}");

                    // Call the initMap function with latitude and longitude
                    mapView.getEngine().executeScript("initMap(" + eventLatitude + ", " + eventLongitude + ")");
                }
            });
        }


    private void displayWeather(String location) {
        Task<WeatherData> fetchWeatherTask = new Task<>() {
            @Override
            protected WeatherData call() throws Exception {
                return WeatherService.getWeather(location);
            }

            @Override
            protected void succeeded() {
                super.succeeded();
                WeatherData weatherData = getValue();
                Platform.runLater(() -> {
                    weatherLabel.setText(weatherData.getMain() + ", " + weatherData.getDescription() + ", " + weatherData.getTemperature() + "°C" + ", " + "Location" + ", " + evenement.getAdresse());
                });
            }

            @Override
            protected void failed() {
                super.failed();
                Platform.runLater(() -> {
                    weatherLabel.setText("Weather data unavailable");
                });
            }
        };

        new Thread(fetchWeatherTask).start();
    }








    @FXML
    public void initialize() {
        eventRating.setRating(0);

        eventRating.ratingProperty().addListener((obs, oldVal, newVal) -> {
            System.out.println("New Rating: " + newVal.doubleValue());
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

        if (evenement != null) {
            loadCommentsFromServer();
        } else {
            System.out.println("Event not initialized");
        }
    }


    public void saveRating(double rating) {
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
        for (String word : comment.split("\\s+")) {
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


















