package controllers;

import entities.Personne;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import services.PersonneServices;

public class EditUserController {
    @FXML
    private TextField nomField;

    @FXML
    private TextField prenomField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField passwordField;

    @FXML
    private TextField ageField;

    private Personne user; // The user being edited

    // Method to initialize the user data in the fields
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
    public void initData(Personne user) {
        this.user = user;
        if (user != null) {
            nomField.setText(user.getNom());
            prenomField.setText(user.getPrenom());
            emailField.setText(user.getEmail());
            passwordField.setText(user.getPassword());
            ageField.setText(String.valueOf(user.getAge()));
        }
    }

    // Method to handle saving the edited user details
    @FXML
    void saveUser(ActionEvent event) {
        if (user != null) {
            // Check if required fields are not empty
            if (!nomField.getText().isEmpty() && !prenomField.getText().isEmpty() && !emailField.getText().isEmpty() && !passwordField.getText().isEmpty() && !ageField.getText().isEmpty()) {
                user.setNom(nomField.getText());
                user.setPrenom(prenomField.getText());
                user.setEmail(emailField.getText());
                user.setPassword(passwordField.getText());

                // Parse age field to int, handle exceptions as needed
                try {
                    int age = Integer.parseInt(ageField.getText());
                    user.setAge(age);
                } catch (NumberFormatException e) {
                    // Handle invalid age input
                }

                // Update the user in the database
                PersonneServices service = new PersonneServices();
                service.updateEntity(user);

//                // Close the editing window
//                nomField.getScene().getWindow().hide();
            } else {
                // Display an error message if any required field is empty
                showAlert("Error", "Please fill in all fields.");
            }
        }
    }


}
