package controllers;

import entities.Personne;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Screen;
import javafx.stage.Stage;
import services.PersonneServices;
import javafx.scene.text.Font;



import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.SQLException;

public class Homepage {

    @FXML
    private WebView webView;
    @FXML
    private Text welcomeText;

    @FXML
    private Label nameLabel;

    @FXML
    private Label surnameLabel;

    @FXML
    private Label emailLabel;

    @FXML
    private Label roleLabel;

    @FXML
    private Label ageLabel;

    @FXML
    private ImageView userProfilePicture;

    private PersonneServices personneServices = new PersonneServices();

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
        webEngine.load(getClass().getResource("/HTML/index.html").toExternalForm());}

    public void setUser(int userId) {
        welcomeText.setFont(new Font(40)); // Set font size to 20
        nameLabel.setFont(new Font(20)); // Set font size to 20
        surnameLabel.setFont(new Font(20)); // Set font size to 20
        emailLabel.setFont(new Font(20)); // Set font size to 20
        roleLabel.setFont(new Font(20)); // Set font size to 20
        ageLabel.setFont(new Font(20));
        Personne user = personneServices.getUserById(userId);
        welcomeText.setText("Welcome to our app " + user.getPrenom());
        nameLabel.setText("Name: " + user.getPrenom());
        surnameLabel.setText("Prenom: " + user.getNom());
        emailLabel.setText("Email: " + user.getEmail());
        roleLabel.setText("Role: " + user.getRole());
        ageLabel.setText("Age: " + user.getAge());

        Blob blob = user.getProfilePicture();
        try {
            InputStream is = blob.getBinaryStream();
            Image image = new Image(is);
            userProfilePicture.setImage(image);
        } catch (SQLException e) {
            e.printStackTrace();
        }
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

        // Get the current stage (window)
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        // Set the new scene content
        Scene scene = new Scene(root);
        stage.setTitle("Harmony");

        stage.setScene(scene);
        stage.show();
    }
    @FXML
    public void openEditProfile() {
        try {
            // Load the FXML file for the edit profile window
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/EditProfile.fxml"));
            Parent root = loader.load();

            // Get the controller for the new window
            EditProfileController editProfileController = loader.getController();

            // Pass the currently logged-in user to the controller
            editProfileController.initData(personneServices.getUserById(Home.userID));

            // Show the new window
            Stage stage = new Stage();
            stage.setTitle("Edit Profile");
            stage.setScene(new Scene(root));
            stage.showAndWait();

            // Refresh the user information after editing
            setUser(Home.userID);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    void pharmacie_front(ActionEvent event) {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/IndexPharmacieFRONT.fxml"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();

    }


    public void forum_front(ActionEvent event) {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/indexPostF.fxml"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    void event_front(ActionEvent event) {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/EventFront.fxml"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();

    }
    @FXML
    void labo_front(ActionEvent event) {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/afficherlaborfront.fxml"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();

    }
    @FXML
    void cabinet_front(ActionEvent event) {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/frontcab.fxml"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();

    }


}
