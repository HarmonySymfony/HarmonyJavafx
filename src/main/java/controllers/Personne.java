package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Screen;
import javafx.stage.Stage;
import services.PersonneServices;
import at.favre.lib.crypto.bcrypt.BCrypt;
import javafx.stage.FileChooser;
import java.io.File;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;

public class Personne {
    @FXML
    private TextField ageTextField;

    @FXML
    private TextField emailTextField;

    @FXML
    private TextField nomTextField;

    @FXML
    private PasswordField passwordTextField;

    @FXML
    private TextField prenomTextField;
    @FXML
    private ChoiceBox<String> roleChoiceBox;
    @FXML
    private ImageView profilePictureImageView;
    private Blob ProfilePicture;

    @FXML
    private WebView webView;


    private PersonneServices personneServices = new PersonneServices();

    private String hashPassword(String password) {
        return BCrypt.withDefaults().hashToString(12, password.toCharArray());
    }

    @FXML
    public void initialize() {// Récupérer la taille de l'écran
        double screenWidth = Screen.getPrimary().getVisualBounds().getWidth();
        double screenHeight = Screen.getPrimary().getVisualBounds().getHeight();

        // Lier la taille de la WebView à la taille de l'écran
        webView.setPrefWidth(screenWidth);
        webView.setPrefHeight(screenHeight);

        // Charger le fichier HTML avec le fond animé
        WebEngine webEngine = webView.getEngine();

        // Charger le fichier HTML contenant la carte Google Maps
        webEngine.load(getClass().getResource("/HTML/index.html").toExternalForm());
        roleChoiceBox.getItems().addAll("PATIENT", "DOCTOR", "LABORATOIRE", "PHARMACIEN");

    }
    @FXML
    void chooseFile(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Profile Picture");
        File file = fileChooser.showOpenDialog(null);

        if (file != null) {
            try {
                // Convert the selected file to a Blob
                FileInputStream fis = new FileInputStream(file);
                ProfilePicture = new javax.sql.rowset.serial.SerialBlob(fis.readAllBytes());

                // Load the image and display it in the ImageView
                Image image = new Image(file.toURI().toString());
                profilePictureImageView.setImage(image);
            } catch (IOException | SQLException e) {
                e.printStackTrace();
            }
        } else {
            showAlert("Error", "No file selected");
        }
    }
    @FXML
    void ajouterPersonne(ActionEvent event) throws IOException {
        // Hash the password
        String hashedPassword = hashPassword(passwordTextField.getText());

        // Check if a file has been selected
        if (ProfilePicture != null) {
            // Create a new Personne object with the entered data and hashed password
            entities.Personne p = new entities.Personne(nomTextField.getText(), prenomTextField.getText(), emailTextField.getText(), hashedPassword, Integer.parseInt(ageTextField.getText()),roleChoiceBox.getValue(), ProfilePicture);

            // Save the Personne object to the database
            personneServices.Ajouter(p);

            try {
                if (emailTextField.getText().isEmpty() || passwordTextField.getText().isEmpty()) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("champ vide");
                    alert.setHeaderText(null);
                    alert.setContentText("remplir les champs vides!");
                    alert.show();
                } else if (!emailTextField.getText().matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Email non valide");
                    alert.setHeaderText(null);
                    alert.setContentText("format email non valide!");
                    alert.show();
                } else if (!ageTextField.getText().matches("^([1-9]|[1-9][0-9])$")) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Age non valide");
                    alert.setHeaderText(null);
                    alert.setContentText("Format AGE non valide! L'âge doit être compris entre 1 et 99.");
                    alert.show();
                } else {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setContentText("l'utilisateur a été ajouté avec succès");
                    alert.show();
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/Login.fxml"));
                    Parent root = loader.load();
                    Scene scene = new Scene(root);

                    Stage stage = new Stage();
                    stage.setTitle("Harmony");
                    stage.setScene(scene);
                    stage.show();
                }
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText(e.getMessage());
                alert.show();
            }
        } else {
            showAlert("Error", "No profile picture selected");
        }
    }

    private void showAlert(String error, String no_file_selected) {

    }

    @FXML
    void retourHome(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Login.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Harmony");
        stage.setScene(scene);
        stage.show();
    }


}