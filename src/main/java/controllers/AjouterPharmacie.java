package controllers;

import entities.pharmacie;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Screen;
import javafx.stage.Stage;
import services.PharmacieServices;

import java.io.IOException;

public class AjouterPharmacie {


    @FXML
    private TextField AdressTextField;

    @FXML
    private TextField NomTextField;

    @FXML
    private TextField TypeTextField;
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

    @FXML
    void BackToListButton(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/IndexPharmacie.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void SubmitButton(ActionEvent event) throws IOException {
        String nom = NomTextField.getText();
        String adresse = AdressTextField.getText();
        String type = TypeTextField.getText();

        if (nom.isEmpty() || adresse.isEmpty() || type.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Veuillez remplir tous les champs.");
            alert.showAndWait();
            return;
        }

        pharmacie pharmacieToAdd = new pharmacie(nom, adresse, type);
        PharmacieServices pharmacieServices = new PharmacieServices();
        pharmacieServices.addEntity(pharmacieToAdd);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("Pharmacie ajoutée avec succès.");
        alert.showAndWait();
        Stage stage = (Stage) ((Node)    event.getSource()).getScene().getWindow();
        stage.close();
        // Ouvrir la liste des pharmacies
        openPharmacieList();
    }

    private void openPharmacieList() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/IndexPharmacie.fxml"));
        Scene scene = new Scene(root);
        Stage stage = new Stage();
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
            Parent root = FXMLLoader.load(getClass().getResource("/AfiicherLaboratoire.fxml"));

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
