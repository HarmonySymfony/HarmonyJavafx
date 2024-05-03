package controllers;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
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
import java.sql.SQLException;
import java.util.Map;

import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;

import java.io.IOException;
import java.time.LocalDate;
import java.sql.Date;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class EvenementController {
    @FXML private TextField idField;
    @FXML private TextField NomField;
    @FXML private TextField DescriptionField;
    @FXML private TextField PrixField;
    @FXML private TextField PlaceDispoField;
    @FXML private TextField AdresseField;
    @FXML private TextField DateField;
    @FXML private DatePicker dateproPicker;


    @FXML private Button addButton;
    @FXML private Button updateButton;
    @FXML private Button deleteButton;
    @FXML private Button clearButton;



    @FXML private TableView<Evenement> evenementTableView;
    @FXML private TableColumn<Evenement, Integer> columnId;
    @FXML private TableColumn<Evenement, String> columnNom;
    @FXML private TableColumn<Evenement, String> columnDescription;
    @FXML private TableColumn<Evenement, Integer> columnPrix;
    @FXML private TableColumn<Evenement, Integer> columnPlaceDispo;
    @FXML private TableColumn<Evenement, String> columnAdresse;
    @FXML private TableColumn<Evenement, Date> columnDate;

    @FXML
    private BarChart<String, Number> statsBarChart;

    private ServiceEvenement serviceEvenement;

    @FXML
    private BarChart<String, Integer> reservationBarChart;






    private Evenement currentSelectedEvenement;
    private int selectedEvenementId;


    @FXML
    public void initialize() {
        serviceEvenement = new ServiceEvenement();
//        columnId.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnNom.setCellValueFactory(new PropertyValueFactory<>("Nom"));
        columnDescription.setCellValueFactory(new PropertyValueFactory<>("Description"));
        columnPrix.setCellValueFactory(new PropertyValueFactory<>("Prix"));
        columnPlaceDispo.setCellValueFactory(new PropertyValueFactory<>("PlaceDispo"));
        columnAdresse.setCellValueFactory(new PropertyValueFactory<>("Adresse"));
        columnDate.setCellValueFactory(new PropertyValueFactory<>("Date"));
        serviceEvenement = new ServiceEvenement();
        initializeStatsChart();
        loadEvenementData();
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
            updateChartWithSelectedEvent(selectedEvenement.getPlaceDispo());

        }
    }



    @FXML
    public void addEvenement() {
        if (!validateNom() || !validateDescription() || !validatePrix()|| !validatePlaceDispo()||!validateAdresse() ||!validateDate() ) {
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
                    Date.valueOf(dateproPicker.getValue())


            );
            serviceEvenement.Add(evenement);
            loadEvenementData();
            clearForm();
            showConfirmation("Event added successfully.");
        } catch (Exception e) {
            showError(e.getMessage());
        }
    }


    @FXML
    public void updateEvent() {
        if (!validateNom() || !validateDescription() || !validatePrix()|| !validatePlaceDispo()||!validateAdresse() ||!validateDate() ) {
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
            clearForm();
            showConfirmation("Event updated successfully.");
        } catch (Exception e) {
            showError(e.getMessage());
        }
    }



    @FXML
    public void deleteEvenement() {
        try {
            serviceEvenement.Delete(selectedEvenementId);
            loadEvenementData();
            clearForm();
            showConfirmation("Event deleted successfully.");

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
        String prix = PrixField.getText().trim();  //
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

    private boolean validateDate(){
        if(dateproPicker.getValue()== null){
            showAlert(Alert.AlertType.ERROR, "validation error", "please select a date !");
            return false;
        }
        if(dateproPicker.getValue().isBefore(LocalDate.now())){
            showAlert(Alert.AlertType.ERROR, "validation error" , "the date cannopt be in the past !");
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






    private void updateChartWithSelectedEvent(int availablePlaces) {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Available Places");

        // Add available places data
        series.getData().add(new XYChart.Data<>("Available Places", availablePlaces));

        // Clear old data and add new data
        statsBarChart.getData().clear();
        statsBarChart.getData().add(series);
    }




}