package controllers;

import entities.medicament;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Screen;
import javafx.stage.Stage;
import services.MedicamentServices;

import java.io.IOException;

public class ModifierMedicament {
    @FXML
    private TextField descriptionTextField;

    @FXML
    private TextField disponibiliteTextField;

    @FXML
    private TextField prixTextField;

    @FXML
    private TextField referenceTextField;

    @FXML
    private TextField stockTextField;

    private medicament selectedMedicament;
    private ObservableList<medicament> medicamentsList;

    @FXML
    private WebView webView;
    @FXML
    public void initialize() {
        // Récupérer la taille de l'écran
        double screenWidth = Screen.getPrimary().getVisualBounds().getWidth();
        double screenHeight = Screen.getPrimary().getVisualBounds().getHeight();

        // Lier la taille de la WebView à la taille de l'écran
        webView.setPrefWidth(screenWidth);
        webView.setPrefHeight(screenHeight);

        // Charger le fichier HTML avec le fond animé
        WebEngine webEngine = webView.getEngine();

        // Charger le fichier HTML contenant la carte Google Maps
        webEngine.load(getClass().getResource("/HTML/indexBACK.html").toExternalForm());

    }

    public void initData(medicament selectedMedicament, ObservableList<medicament> medicamentsList) {
        this.selectedMedicament = selectedMedicament;
        this.medicamentsList = medicamentsList;
        // Initialiser les champs de texte avec les valeurs du médicament sélectionné
        referenceTextField.setText(selectedMedicament.getReference());
        stockTextField.setText(String.valueOf(selectedMedicament.getStock()));
        disponibiliteTextField.setText(selectedMedicament.getDisponibilite());
        descriptionTextField.setText(selectedMedicament.getDescription());
        prixTextField.setText(String.valueOf(selectedMedicament.getPrix()));
    }


        @FXML
        void submitbutton(ActionEvent event) {
            // Mettre à jour les valeurs du médicament sélectionné avec les nouvelles valeurs saisies dans les champs de texte
            selectedMedicament.setReference(referenceTextField.getText());
            selectedMedicament.setStock(Integer.parseInt(stockTextField.getText()));
            selectedMedicament.setDisponibilite(disponibiliteTextField.getText());
            selectedMedicament.setDescription(descriptionTextField.getText());
            selectedMedicament.setPrix(Integer.parseInt(prixTextField.getText()));

            // Mettre à jour le médicament dans la base de données
            MedicamentServices medicamentServices = new MedicamentServices();
            medicamentServices.updateEntity(selectedMedicament);

            // Mettre à jour le médicament dans la liste
            medicamentsList.set(medicamentsList.indexOf(selectedMedicament), selectedMedicament);

            // Fermer la fenêtre
            ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
        }


    @FXML
    void backTOlist(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/IndexMedicament.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void AnalyseTable(MouseEvent event) {
        try {
            // Charger le fichier FXML AfficheUser.fxml
            Parent root = FXMLLoader.load(getClass().getResource("/afficherAnalyse.fxml"));

            // Créer une nouvelle scène avec le contenu chargé
            Scene scene = new Scene(root);

            // Obtenir la fenêtre actuelle
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Mettre la nouvelle scène dans la fenêtre
            stage.setScene(scene);

            // Afficher la fenêtre
            stage.show();
        } catch (IOException e) {
            // Gérer les erreurs de chargement du fichier FXML
            throw new RuntimeException(e);
        }

    }

    @FXML
    void CabinetTable(MouseEvent event) {
        try {
            // Charger le fichier FXML AfficheUser.fxml
            Parent root = FXMLLoader.load(getClass().getResource("/SAHTEK.fxml"));

            // Créer une nouvelle scène avec le contenu chargé
            Scene scene = new Scene(root);

            // Obtenir la fenêtre actuelle
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Mettre la nouvelle scène dans la fenêtre
            stage.setScene(scene);

            // Afficher la fenêtre
            stage.show();
        } catch (IOException e) {
            // Gérer les erreurs de chargement du fichier FXML
            throw new RuntimeException(e);
        }

    }

    @FXML
    void EventTable(MouseEvent event) {
        try {
            // Charger le fichier FXML AfficheUser.fxml
            Parent root = FXMLLoader.load(getClass().getResource("/event.fxml"));

            // Créer une nouvelle scène avec le contenu chargé
            Scene scene = new Scene(root);

            // Obtenir la fenêtre actuelle
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Mettre la nouvelle scène dans la fenêtre
            stage.setScene(scene);

            // Afficher la fenêtre
            stage.show();
        } catch (IOException e) {
            // Gérer les erreurs de chargement du fichier FXML
            throw new RuntimeException(e);
        }

    }

    @FXML
    void LaboratoireTable(MouseEvent event) {
        try {
            // Charger le fichier FXML AfficheUser.fxml
            Parent root = FXMLLoader.load(getClass().getResource("/afiicherLaboratoire.fxml"));

            // Créer une nouvelle scène avec le contenu chargé
            Scene scene = new Scene(root);

            // Obtenir la fenêtre actuelle
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Mettre la nouvelle scène dans la fenêtre
            stage.setScene(scene);

            // Afficher la fenêtre
            stage.show();
        } catch (IOException e) {
            // Gérer les erreurs de chargement du fichier FXML
            throw new RuntimeException(e);
        }

    }

    @FXML
    void LoginTable(MouseEvent event) {
        try {
            // Charger le fichier FXML AfficheUser.fxml
            Parent root = FXMLLoader.load(getClass().getResource("/Login.fxml"));

            // Créer une nouvelle scène avec le contenu chargé
            Scene scene = new Scene(root);

            // Obtenir la fenêtre actuelle
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Mettre la nouvelle scène dans la fenêtre
            stage.setScene(scene);

            // Afficher la fenêtre
            stage.show();
        } catch (IOException e) {
            // Gérer les erreurs de chargement du fichier FXML
            throw new RuntimeException(e);
        }

    }

    @FXML
    void MedicTable(MouseEvent event) {
        try {
            // Charger le fichier FXML AfficheUser.fxml
            Parent root = FXMLLoader.load(getClass().getResource("/IndexMedicament.fxml"));

            // Créer une nouvelle scène avec le contenu chargé
            Scene scene = new Scene(root);

            // Obtenir la fenêtre actuelle
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Mettre la nouvelle scène dans la fenêtre
            stage.setScene(scene);

            // Afficher la fenêtre
            stage.show();
        } catch (IOException e) {
            // Gérer les erreurs de chargement du fichier FXML
            throw new RuntimeException(e);
        }

    }

    @FXML
    void PharmacieTable(MouseEvent event) {
        try {
            // Charger le fichier FXML AfficheUser.fxml
            Parent root = FXMLLoader.load(getClass().getResource("/IndexPharmacie.fxml"));

            // Créer une nouvelle scène avec le contenu chargé
            Scene scene = new Scene(root);

            // Obtenir la fenêtre actuelle
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Mettre la nouvelle scène dans la fenêtre
            stage.setScene(scene);

            // Afficher la fenêtre
            stage.show();
        } catch (IOException e) {
            // Gérer les erreurs de chargement du fichier FXML
            throw new RuntimeException(e);
        }

    }

    @FXML
    void PostsTable(MouseEvent event) {
        try {
            // Charger le fichier FXML AfficheUser.fxml
            Parent root = FXMLLoader.load(getClass().getResource("/indexPost.fxml"));

            // Créer une nouvelle scène avec le contenu chargé
            Scene scene = new Scene(root);

            // Obtenir la fenêtre actuelle
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Mettre la nouvelle scène dans la fenêtre
            stage.setScene(scene);

            // Afficher la fenêtre
            stage.show();
        } catch (IOException e) {
            // Gérer les erreurs de chargement du fichier FXML
            throw new RuntimeException(e);
        }

    }

    @FXML
    void UsersTable(MouseEvent event) {
        try {
            // Charger le fichier FXML AfficheUser.fxml
            Parent root = FXMLLoader.load(getClass().getResource("/AfficheUser.fxml"));

            // Créer une nouvelle scène avec le contenu chargé
            Scene scene = new Scene(root);

            // Obtenir la fenêtre actuelle
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Mettre la nouvelle scène dans la fenêtre
            stage.setScene(scene);

            // Afficher la fenêtre
            stage.show();
        } catch (IOException e) {
            // Gérer les erreurs de chargement du fichier FXML
            throw new RuntimeException(e);
        }

    }


}
