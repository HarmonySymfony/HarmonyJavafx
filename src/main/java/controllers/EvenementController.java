package controllers;
import entities.TwilioSMS;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.util.Duration;
import kong.unirest.Unirest;
import services.ServiceEvenement;
import entities.Evenement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

import org.apache.http.util.EntityUtils;
import java.nio.charset.StandardCharsets;

import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import java.io.IOException;








import java.awt.*;
import java.io.File;
//import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import org.controlsfx.control.Notifications;

import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;

import javax.swing.text.html.ImageView;
import java.io.IOException;
import java.time.LocalDate;
import java.sql.Date;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class EvenementController {
    @FXML
    private TextField idField;
    @FXML
    private TextField NomField;
    @FXML
    private TextField DescriptionField;
    @FXML
    private TextField PrixField;
    @FXML
    private TextField PlaceDispoField;
    @FXML
    private TextField AdresseField;
    @FXML
    private TextField DateField;
    @FXML
    private DatePicker dateproPicker;



    @FXML
    private Button addButton;
    @FXML
    private Button updateButton;
    @FXML
    private Button deleteButton;
    @FXML
    private Button clearButton;


    @FXML
    private TableView<Evenement> evenementTableView;
    @FXML
    private TableColumn<Evenement, Integer> columnId;
    @FXML
    private TableColumn<Evenement, String> columnNom;
    @FXML
    private TableColumn<Evenement, String> columnDescription;
    @FXML
    private TableColumn<Evenement, Integer> columnPrix;
    @FXML
    private TableColumn<Evenement, Integer> columnPlaceDispo;
    @FXML
    private TableColumn<Evenement, String> columnAdresse;
    @FXML
    private TableColumn<Evenement, Date> columnDate;

    @FXML
    private BarChart<String, Number> statsBarChart;

    @FXML
    private WebView mapView;
    @FXML
    private TextField latTextField;
    @FXML
    private TextField lonTextField;
    @FXML
    private Button updateLocationButton;

    private ServiceEvenement serviceEvenement;

    @FXML
    private BarChart<String, Integer> reservationBarChart;


    @FXML
    private ImageView imagePreview;


    private Evenement currentSelectedEvenement;
    private int selectedEvenementId;


    @FXML
    public void initialize() {
        serviceEvenement = new ServiceEvenement();
        columnNom.setCellValueFactory(new PropertyValueFactory<>("Nom"));
        columnDescription.setCellValueFactory(new PropertyValueFactory<>("Description"));
        columnPrix.setCellValueFactory(new PropertyValueFactory<>("Prix"));
        columnPlaceDispo.setCellValueFactory(new PropertyValueFactory<>("PlaceDispo"));
        columnAdresse.setCellValueFactory(new PropertyValueFactory<>("Adresse"));
        columnDate.setCellValueFactory(new PropertyValueFactory<>("Date"));
        serviceEvenement = new ServiceEvenement();
        initializeStatsChart();
        loadEvenementData();
        loadMap();
    }

    private void loadMap() {
        WebEngine webEngine = mapView.getEngine();
        webEngine.load(getClass().getResource("/maptest.html").toExternalForm());

        String javascriptCode = "function getSelectedLatitude() {" +
                "    return selectedLatitude;" +
                "}" +
                "function getSelectedLongitude() {" +
                "    return selectedLongitude;" +
                "}" +
                "function getSelectedLocationName() {" +
                "    return selectedLocationName;" +
                "}";

        webEngine.executeScript(javascriptCode);
    }

    @FXML
    public void updateLocationButtonClicked() {
        WebEngine webEngine = mapView.getEngine();

        Object latitudeObj = webEngine.executeScript("getSelectedLocation().latitude");
        Object longitudeObj = webEngine.executeScript("getSelectedLocation().longitude");
        String locationName = (String) webEngine.executeScript("getSelectedLocation().locationName");

        if (latitudeObj instanceof Double && longitudeObj instanceof Double) {
            Double latitude = (Double) latitudeObj;
            Double longitude = (Double) longitudeObj;
            String cleanLocationName = locationName.replaceAll("(?i)Tunisia", "").trim();

            AdresseField.setText(cleanLocationName);
            System.out.println("Latitude: " + latitude + ", Longitude: " + longitude + ", Location: " + locationName);
            lonTextField.setText(longitude.toString());
            latTextField.setText(latitude.toString());
//            AdresseField.textProperty().addListener((observable, oldValue, newValue) -> {
//                updateCityComboBox();
//            });
        } else {
            showAlert(Alert.AlertType.ERROR, "Unable to retrieve location from the map.", "error");
        }
    }


    private void initializeStatsChart() {
        statsBarChart.setTitle("Participants per Event");
    }

    private void loadEvenementData() {
        try {
            ObservableList<Evenement> programmeData = FXCollections.observableArrayList(serviceEvenement.afficher());
            evenementTableView.setItems(programmeData);
        } catch (SQLException e) {
            showError("Error loading programmes: " + e.getMessage());
        }
    }


    @FXML
    private void clearForm() {
        NomField.clear();
        DescriptionField.clear();
        PrixField.clear();
        PlaceDispoField.clear();
        AdresseField.clear();
        dateproPicker.setValue(null);
        evenementTableView.getSelectionModel().clearSelection();
        statsBarChart.getData().clear();

    }


    @FXML
    public void handleEvenementSelection() {
        Evenement selectedEvenement = evenementTableView.getSelectionModel().getSelectedItem();
        if (selectedEvenement != null) {
            selectedEvenementId = selectedEvenement.getId();
            NomField.setText(selectedEvenement.getNom());
            DescriptionField.setText(selectedEvenement.getDescription());
            PrixField.setText(String.valueOf(selectedEvenement.getPrix()));
            PlaceDispoField.setText((String.valueOf(selectedEvenement.getPlaceDispo())));
            AdresseField.setText(selectedEvenement.getAdresse());
            dateproPicker.setValue(selectedEvenement.getDate().toLocalDate());
            try {
                int reservedPlaces = serviceEvenement.getReservedPlacesForEvent(selectedEvenement.getId());
                updateChartWithSelectedEvent(selectedEvenement.getPlaceDispo(), reservedPlaces);
            } catch (SQLException e) {
                showError("Error retrieving reservation data: " + e.getMessage());
            }

        }
    }


    @FXML
    public void addEvenement() {
        if (!validateNom() || !validateDescription() || !validatePrix() || !validatePlaceDispo() || !validateAdresse() || !validateDate()) {
            return;
        }
        try {
            Evenement evenement = new Evenement(
                    0,
                    NomField.getText(),
                    DescriptionField.getText(),
                    Float.parseFloat(PrixField.getText()),


                    Integer.parseInt(PlaceDispoField.getText()),

                    AdresseField.getText(),
                    Date.valueOf(dateproPicker.getValue()),
                    Double.parseDouble(latTextField.getText()),
                    Double.parseDouble(lonTextField.getText())

            );

            serviceEvenement.Add(evenement);
            loadEvenementData();
//            TwilioSMS.sendCustomMessage("21620515171", "Event added successfully !");

            clearForm();
            showConfirmation("Event added successfully.");
            Notifications notifications = Notifications.create();
            notifications.text("Event added successfully !");
            notifications.title("Successful");
            notifications.hideAfter(Duration.seconds(6));
            notifications.show();
        } catch (Exception e) {
            showError(e.getMessage());
        }
    }


    @FXML
    public void updateEvent() {
        if (!validateNom() || !validateDescription() || !validatePrix() || !validatePlaceDispo() || !validateAdresse() || !validateDate()) {
            return;
        }
        try {
            Evenement evenement = new Evenement(
                    selectedEvenementId,
                    NomField.getText(),
                    DescriptionField.getText(),
                    Float.parseFloat(PrixField.getText()),
                    Integer.parseInt(PlaceDispoField.getText()),
                    AdresseField.getText(),
                    java.sql.Date.valueOf(dateproPicker.getValue())


            );
            serviceEvenement.modifyEvent(evenement);
            loadEvenementData();
            TwilioSMS.sendCustomMessage("21620515171", "Event updated successfully !");
            clearForm();
            showConfirmation("Event updated successfully.");
            Notifications notifications = Notifications.create();
            notifications.text("Event updated successfully !");
            notifications.title("Successful");
            notifications.hideAfter(Duration.seconds(6));
            notifications.show();
        } catch (Exception e) {
            showError(e.getMessage());
        }
    }


    @FXML
    public void deleteEvenement() {
        try {
            serviceEvenement.Delete(selectedEvenementId);
            loadEvenementData();
//            TwilioSMS.sendCustomMessage("21620515171", "Event deleted successfully !");
            clearForm();
            showConfirmation("Event deleted successfully.");
            Notifications notifications = Notifications.create();
            notifications.text("Event deleted successfully !");
            notifications.title("Successful");
            notifications.hideAfter(Duration.seconds(6));
            notifications.show();

        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    private void showConfirmation(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Operation Successful");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Operation Failed");
        alert.setContentText(message);
        alert.showAndWait();
    }


    private boolean validateNom() {
        String title = NomField.getText().trim();
        if (title.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Nom cannot be empty.");
            return false;
        }
        if (title.length() > 15) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Nom is too long.");
            return false;
        }
        return true;
    }

    private boolean validateDescription() {
        String Description = DescriptionField.getText().trim();
        if (Description.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Description cannot be empty.");
            return false;
        }
        return true;
    }

    private boolean validatePlaceDispo() {
        String availablePlaces = PlaceDispoField.getText().trim();
        try {
            int places = Integer.parseInt(availablePlaces);
            if (places <= 0) {
                showAlert(Alert.AlertType.ERROR, "Validation Error", "Available places must be a positive number.");
                return false;
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Available places must be a valid integer.");
            return false;
        }
        return true;
    }

    private boolean validatePrix() {
        String prix = PrixField.getText().trim();
        try {
            double price = Double.parseDouble(prix);
            if (price <= 0) {
                showAlert(Alert.AlertType.ERROR, "Validation Error", "Price must be a positive number.");
                return false;
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Price must be a valid number.");
            return false;
        }
        return true;
    }

    private boolean validateAdresse() {
        String adresse = AdresseField.getText().trim();
        if (adresse.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Nom cannot be empty.");
            return false;
        }
        return true;
    }

    private boolean validateDate() {
        if (dateproPicker.getValue() == null) {
            showAlert(Alert.AlertType.ERROR, "validation error", "please select a date !");
            return false;
        }
        if (dateproPicker.getValue().isBefore(LocalDate.now())) {
            showAlert(Alert.AlertType.ERROR, "validation error", "the date cannopt be in the past !");
            return false;
        }
        return true;

    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    void Back(ActionEvent event) {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/AfficheUser.fxml"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }


    private void updateChartWithSelectedEvent(int availablePlaces, int reservedPlaces) {
        XYChart.Series<String, Number> seriesAvailable = new XYChart.Series<>();
        seriesAvailable.setName("Available Places");
        seriesAvailable.getData().add(new XYChart.Data<>("Available", availablePlaces));

        XYChart.Series<String, Number> seriesReserved = new XYChart.Series<>();
        seriesReserved.setName("Reserved Places");
        seriesReserved.getData().add(new XYChart.Data<>("Reserved", reservedPlaces));

        statsBarChart.getData().clear();
        statsBarChart.getData().addAll(seriesAvailable, seriesReserved);
    }


    private String EvenementListToHtml(List<Evenement> evenementList) {
        StringBuilder htmlBuilder = new StringBuilder();

        // Begin HTML
        htmlBuilder.append("<html><body>");
        htmlBuilder.append("<h1>Liste events :</h1>");
        htmlBuilder.append("<table border='1'><tr><th>Nom:</th><th>Adresse:</th><th>Available Places:</th><th>Description:</th></tr>");

        // Add rows for each Programme
        for (Evenement event : evenementList) {
            htmlBuilder.append("<tr>");
            htmlBuilder.append("<td>").append(event.getNom()).append("</td>");
            htmlBuilder.append("<td>").append(event.getAdresse()).append("</td>");
            htmlBuilder.append("<td>").append(event.getPlaceDispo()).append("</td>");
            htmlBuilder.append("<td>").append(event.getDescription().toString()).append("</td>");
            htmlBuilder.append("</tr>");
        }

        // End HTML
        htmlBuilder.append("</table></body></html>");

        return htmlBuilder.toString();
    }

    @FXML
    public void generatePDFReport() {
        ServiceEvenement serviceEvenement = new ServiceEvenement();
        try {
            List<Evenement> evenementsList = serviceEvenement.afficher();
            String htmlContent = EvenementListToHtml(evenementsList);
            String apiEndpoint = "https://pdf-api.co/pdf";
            String apiKey = "952DB3D1DA80ED588277A06827311D0A627F";
            String requestBody = String.format("{\"apiKey\": \"%s\", \"html\": \"%s\"}", apiKey, htmlContent);
            HttpResponse<byte[]> response = Unirest.post(apiEndpoint)
                    .header("Content-Type", "application/json")
                    .body(requestBody)
                    .asBytes();

            if (response.getStatus() == 200 && "application/pdf".equals(response.getHeaders().getFirst("Content-Type"))) {
                Path path = Paths.get("EventReport.pdf");
                Files.write(path, response.getBody());
                System.out.println("PDF Generated at: " + path.toAbsolutePath());

                if (Desktop.isDesktopSupported()) {
                    try {
                        File pdfFile = new File(path.toString());
                        Desktop.getDesktop().open(pdfFile);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                        System.err.println("Unable to open the PDF file. Error: " + ex.getMessage());
                    }
                } else {
                    System.err.println("Desktop operations not supported on the current platform. Cannot open the PDF file automatically.");
                }
            } else {
                System.err.println("Failed to generate PDF: " + response.getStatusText());
                System.err.println("Status Code: " + response.getStatus());
                System.err.println("Response Headers: " + response.getHeaders());
                String responseBody = new String(response.getBody(), StandardCharsets.UTF_8);
                System.err.println("Response Body: " + responseBody);
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }






}