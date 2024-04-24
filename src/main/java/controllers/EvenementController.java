package controllers;
import services.ServiceEvenement;
import entities.Evenement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.SQLException;

public class EvenementController {
    @FXML private TextField idField;
    @FXML private TextField NomField;
    @FXML private TextField DescriptionField;
    @FXML private TextField PrixField;
    @FXML private TextField PlaceDispoField;
    @FXML private TextField AdresseField;

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





    private ServiceEvenement serviceEvenement;
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
        loadEvenementData();
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

        evenementTableView.getSelectionModel().clearSelection();
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

        }
    }



    @FXML
    public void addEvenement() {
        if (!validateNom() || !validateDescription() || !validatePrix()|| !validatePlaceDispo()||!validateAdresse() ) {
            return;
        }
        try {
            Evenement evenement = new Evenement(
                    0,
                    NomField.getText(),
                    DescriptionField.getText(),
                    Float.parseFloat(PrixField.getText()),
                    Integer.parseInt(PlaceDispoField.getText()),
                    AdresseField.getText()

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
        if (!validateNom() || !validateDescription() || !validatePrix()|| !validatePlaceDispo()||!validateAdresse() ) {
            return;
        }
        try {
            Evenement evenement = new Evenement(
                                selectedEvenementId,
                                NomField.getText(),
                                DescriptionField.getText(),
                    Float.parseFloat(PrixField.getText()),
                                Integer.parseInt(PlaceDispoField.getText()),
                    AdresseField.getText()



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

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
