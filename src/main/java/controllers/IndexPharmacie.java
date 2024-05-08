package controllers;

import entities.pharmacie;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Screen;
import javafx.stage.Stage;
import services.PharmacieServices;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class IndexPharmacie {


    @FXML
    private WebView webView;

    @FXML
    private ListView<pharmacie> listepharmacie;

    @FXML
    private TextField searchField;



    private PharmacieServices pharmacieServices;

    @FXML
    public void initialize() {
        pharmacieServices = new PharmacieServices();
        populateListView();

        // Assurez-vous que searchField est initialisé depuis le fichier FXML
        assert searchField != null : "fx:id searchField not injected!";

        // Ajoutez le listener de changement de texte uniquement si searchField est correctement initialisé
        if (searchField != null) {
            searchField.textProperty().addListener((observable, oldValue, newValue) -> {
                filterPharmacies(newValue);
            });
        } else {
            System.err.println("searchField is null!");
        }
        // Récupérer la taille de l'écran
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
                Button deleteButton = new Button("Delete");
                Button editButton = new Button("Edit");
                Button medicamentButton = new Button("Médicament"); // Nouveau bouton "Médicament"

                deleteButton.setOnAction(event -> {
                    pharmacieServices.deleteEntity(item);
                    populateListView();
                });
                editButton.setOnAction(event -> openModifierPharmacieView(item));
                medicamentButton.setOnAction(event -> openMedicamentView(item)); // Action pour le bouton "Médicament"

                HBox buttonsBox = new HBox(5, deleteButton, editButton, medicamentButton); // Ajoutez le bouton "Médicament"
                Region spacer = new Region();
                HBox.setHgrow(spacer, Priority.ALWAYS);

                HBox cellBox = new HBox(spacer, buttonsBox);
                setGraphic(cellBox);

                setText(item.getNom() + " - " + item.getAdress() + " (" + item.getType() + ")");
            }
        }
    }

    @FXML
    private void openModifierPharmacieView(pharmacie selectedPharmacy) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierPharmacie.fxml"));
            Parent root = loader.load();

            ModifierPharmacie controller = loader.getController();
            controller.setPharmacie(selectedPharmacy);
            controller.setListePharmacies(listepharmacie);

            // Fermer la fenêtre actuelle
            Stage currentStage = (Stage) listepharmacie.getScene().getWindow();
            currentStage.close();

            // Afficher la fenêtre pour modifier la pharmacie sélectionnée
            Stage stage = new Stage();
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

    @FXML
    private void openMedicamentView(pharmacie selectedPharmacy) {
        // Implémentez la logique pour ouvrir une vue pour ajouter des médicaments à la pharmacie sélectionnée
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/IndexMedicament.fxml"));
            Parent root = loader.load();

            IndexMedicament controller = loader.getController(); // Correction ici
            controller.setPharmacie(selectedPharmacy);
            controller.setListePharmacies(listepharmacie);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
