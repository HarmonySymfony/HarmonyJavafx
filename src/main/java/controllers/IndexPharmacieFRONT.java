package controllers;

import entities.pharmacie;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.List;
import services.PharmacieServices;

public class IndexPharmacieFRONT {

    @FXML
    private ListView<pharmacie> listepharmacie;

    @FXML
    private TextField searchField;

    private PharmacieServices pharmacieServices;

    @FXML
    public void initialize() {
        pharmacieServices = new PharmacieServices();
        populateListView();

        assert searchField != null : "fx:id searchField not injected!";

        if (searchField != null) {
            searchField.textProperty().addListener((observable, oldValue, newValue) -> {
                filterPharmacies(newValue);
            });
        } else {
            System.err.println("searchField is null!");
        }
    }

    @FXML
    private void populateListView() {
        List<pharmacie> pharmacies = pharmacieServices.getAllData();
        listepharmacie.getItems().clear();

        for (pharmacie pharmacy : pharmacies) {
            listepharmacie.getItems().add(pharmacy);
        }

        listepharmacie.setCellFactory(param -> new PharmacieListCell());
    }

    private class PharmacieListCell extends ListCell<pharmacie> {
        @Override
        protected void updateItem(pharmacie item, boolean empty) {
            super.updateItem(item, empty);

            if (empty || item == null) {
                setText(null);
                setGraphic(null);
            } else {
                Button showButton = new Button("Show");
                Button medicamentButton = new Button("Médicament");

                showButton.setOnAction(event -> {
                    Alert alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Détails de la pharmacie");
                    alert.setHeaderText(null);
                    alert.setContentText("Nom: " + item.getNom() + "\nAdresse: " + item.getAdress() + "\nType: " + item.getType());
                    alert.showAndWait();
                });

                medicamentButton.setOnAction(event -> openMedicamentView(event, item));

                HBox buttonsBox = new HBox(5, showButton, medicamentButton);
                buttonsBox.setSpacing(10); // Espacement entre les boutons
                buttonsBox.setPadding(new Insets(5)); // Marge intérieure des boutons

                Region spacer = new Region();
                HBox.setHgrow(spacer, Priority.ALWAYS);

                HBox cellBox = new HBox(spacer, buttonsBox);
                setGraphic(cellBox);

                setText(item.getNom() + " - " + item.getAdress() + " (" + item.getType() + ")");
            }
        }
    }

    private void filterPharmacies(String keyword) {
        List<pharmacie> pharmacies = pharmacieServices.getAllData();

        listepharmacie.getItems().clear();

        for (pharmacie pharmacy : pharmacies) {
            if (pharmacy.getNom().toLowerCase().contains(keyword.toLowerCase())) {
                listepharmacie.getItems().add(pharmacy);
            }
        }
    }

    @FXML
    private void openMedicamentView(ActionEvent event, pharmacie selectedPharmacy) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/IndexMedicamentFRONT.fxml"));
            Parent root = loader.load();

            IndexMedicamentFRONT controller = loader.getController();
            controller.setPharmacie(selectedPharmacy);
            controller.setListePharmacies(listepharmacie);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();

            // Fermer la fenêtre IndexPharmacieFRONT
            ((Node) event.getSource()).getScene().getWindow().hide();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void buttonRetour(ActionEvent event) {
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
    @FXML
    void MapClick(ActionEvent event) {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/Carte.fxml"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();

    }

}
