package controllers;

import entities.Personne;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import services.PersonneServices;

import java.io.IOException;

public class Homepage {
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

    private PersonneServices personneServices = new PersonneServices();

    public void setUser(int userId) {
        Personne user = personneServices.getUserById(userId);
        welcomeText.setText("Welcome to our app " + user.getNom());
        nameLabel.setText("Name: " + user.getNom());
        surnameLabel.setText("Prenom: " + user.getPrenom());
        emailLabel.setText("Email: " + user.getEmail());
        roleLabel.setText("Role: " + user.getRole());
        ageLabel.setText("Age: " + user.getAge());
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


}
