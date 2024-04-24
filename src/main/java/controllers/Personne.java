package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import services.PersonneServices;

import java.io.IOException;

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
    private TextField roleTextField;

    private PersonneServices personneServices = new PersonneServices();

    @FXML
    void ajouterPersonne(ActionEvent event) {
        try {
            String nom = nomTextField.getText();
            String prenom = prenomTextField.getText();
            String email = emailTextField.getText();
            String password = passwordTextField.getText();
            int age = Integer.parseInt(ageTextField.getText());
            String role = roleTextField.getText(); // Default role

            // Create a new Personne object with the entered data
            entities.Personne p = new entities.Personne(nom, prenom, email, password, age, role);

            // Call the Ajouter method of PersonneServices to add the Personne to the database
            personneServices.Ajouter(p);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("L'utilisateur a été ajouté avec succès");
            alert.show();

            // Clear the text fields after adding the Personne
            nomTextField.clear();
            prenomTextField.clear();
            emailTextField.clear();
            passwordTextField.clear();
            ageTextField.clear();
            roleTextField.clear();
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Format d'âge invalide");
            alert.show();
        } catch (Exception e) { // Catch more general exception or handle specific exceptions thrown by Ajouter method
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(e.getMessage());
            alert.show();
        }
    }

    @FXML
    void retourHome(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Login.fxml"));
        Parent root = loader.load();

        // Get the current stage (window)
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        // Set the new scene content
        Scene scene = new Scene(root);
        stage.setTitle("Harmony");
        stage.setScene(scene);
        stage.show();
    }
}
