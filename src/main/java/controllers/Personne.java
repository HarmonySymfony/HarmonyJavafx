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

    private PersonneServices personneServices = new PersonneServices();

    // Constructor not needed if using FXML
    // You can remove the constructor

    @FXML
    void ajouterPersonne(ActionEvent event) throws IOException {


        // Create a new Personne object with the entered data
        entities.Personne p = new entities.Personne(nomTextField.getText(), prenomTextField.getText(), emailTextField.getText(), passwordTextField.getText(), Integer.parseInt(ageTextField.getText()));
        PersonneServices personneServices = new PersonneServices();
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
            }

            else if (!ageTextField.getText().matches("[1-99]")) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Age non valide");
                alert.setHeaderText(null);
                alert.setContentText("format AGE non valide!");
                alert.show();
            }
            // Call the Ajouter method of PersonneServices to add the Personne to the database
            else {
                personneServices.Ajouter(p);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("l'utilisateur a été ajouté avec succès");
                alert.show();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Login.fxml"));
                Parent root = null;
                try {
                    root = loader.load();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                Scene scene = new Scene(root);

                Stage stage = new Stage();
                stage.setTitle("Harmony");
                stage.setScene(scene);
                stage.show();
            }

            // Optionally, you can clear the text fields after adding the Personne
            nomTextField.clear();
            prenomTextField.clear();
            emailTextField.clear();
            passwordTextField.clear();
            ageTextField.clear();
        } catch (Exception e) { // Catch more general exception or handle specific exceptions thrown by Ajouter method
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(e.getMessage());
            alert.show();
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
}
