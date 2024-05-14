package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Screen;
import javafx.stage.Stage;
import entities.medicament;
import services.MedicamentServices;

import java.io.IOException;

public class AjouterMedicament {

    @FXML
    private TextField descriptiontextfield;

    @FXML
    private TextField disponibilitetextfield;

    @FXML
    private TextField prixtextfield;

    @FXML
    private TextField refrencetextfield;

    @FXML
    private TextField stocktextfield;

    @FXML
    private Label erreurLabel;

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
    void Backtolist(ActionEvent event) {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/IndexMedicament.fxml"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    @FXML

    void SubmitButton(ActionEvent event) {
        // Récupérer les valeurs saisies dans les champs de texte
        String reference = refrencetextfield.getText();
        String description = descriptiontextfield.getText();
        String disponibilite = disponibilitetextfield.getText();
        String stockText = stocktextfield.getText();
        String prixText = prixtextfield.getText();
        int stock = 0;
        int prix = 0;

        // Validation des champs saisis
        if (reference.isEmpty() || description.isEmpty() || disponibilite.isEmpty() || stockText.isEmpty() || prixText.isEmpty()) {
            erreurLabel.setText("Veuillez remplir tous les champs obligatoires.");
        } else {
            try {
                stock = Integer.parseInt(stockText);
                prix = Integer.parseInt(prixText);

                if (stock <= 0 || prix <= 0) {
                    erreurLabel.setText("Le stock et le prix doivent être des nombres positifs.");
                } else {
                    // Créer un nouvel objet medicament avec les valeurs saisies
                    medicament newMedicament = new medicament(reference, stock, description, disponibilite, prix);

                    // Utiliser le service MedicamentServices pour ajouter le nouvel objet medicament à la base de données
                    MedicamentServices medicamentServices = new MedicamentServices();
                    medicamentServices.addEntity(newMedicament);

                    // Revenir à la vue IndexMedicament.fxml
                    Parent root = FXMLLoader.load(getClass().getResource("/IndexMedicament.fxml"));
                    Scene scene = new Scene(root);
                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    stage.setScene(scene);
                    stage.show();
                    stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    stage.close();
                }
            } catch (NumberFormatException e) {
                erreurLabel.setText("Le stock et le prix doivent être des nombres valides.");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
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
