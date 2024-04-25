package controllers;

import entities.pharmacie;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.List;
import services.PharmacieServices;

public class ShowPharmacie {

    @FXML
    private ListView<pharmacie> listepharmacie; // ListView de type pharmacie

    private PharmacieServices pharmacieServices;

    @FXML
    public void initialize() {
        pharmacieServices = new PharmacieServices();
        // Retrieve the list of pharmacies from the database and populate the ListView
        populateListView();
    }

    @FXML
    private void populateListView() {
        List<pharmacie> pharmacies = pharmacieServices.getAllData();
        listepharmacie.getItems().clear();

        for (pharmacie pharmacy : pharmacies) {
            listepharmacie.getItems().add(pharmacy);
        }

        // Set the custom cell factory for the ListView
        listepharmacie.setCellFactory(param -> new PharmacieListCell());
    }

    // Custom ListCell for displaying pharmacies with delete and edit buttons
    private class PharmacieListCell extends ListCell<pharmacie> {
        @Override
        protected void updateItem(pharmacie item, boolean empty) {
            super.updateItem(item, empty);

            if (empty || item == null) {
                setText(null);
                setGraphic(null);
            } else {
                // Create buttons for delete and edit
                Button deleteButton = new Button("Delete");
                Button editButton = new Button("Edit");

                // Set actions for buttons
                deleteButton.setOnAction(event -> {
                    pharmacieServices.deleteEntity(item);
                    populateListView(); // Refresh list view after deletion
                });
                editButton.setOnAction(event -> openModifierPharmacieView(item));

                // Create HBox to hold buttons
                HBox buttonsBox = new HBox(5, deleteButton, editButton);

                // Create a region to fill the space
                Region spacer = new Region();
                HBox.setHgrow(spacer, Priority.ALWAYS);

                // Add buttons and spacer to the cell
                HBox cellBox = new HBox(spacer, buttonsBox);
                setGraphic(cellBox);

                // Set text to display pharmacy details
                setText(item.getNom() + " - " + item.getAdress() + " (" + item.getType() + ")");
            }
        }
    }

    @FXML
    private void openModifierPharmacieView(pharmacie selectedPharmacy) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierPharmacie.fxml"));
            Parent root = loader.load();

            // Obtenir le contrôleur de la vue de modification
            controllers.ModifierPharmacie controller = loader.getController();

            // Passer les informations de la pharmacie sélectionnée
            controller.setPharmacie(selectedPharmacy);

            // Passer la liste des pharmacies à modifier
            controller.setListePharmacies(listepharmacie);

            // Afficher la scène
            Stage stage = new Stage(); // Créez une nouvelle fenêtre pour afficher la modification
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
    @FXML
    void ajouterpharmacie(ActionEvent event) {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/AjouterPharmacie.fxml"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();

    }
    @FXML
    void RetourBack(ActionEvent event) {
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

}
