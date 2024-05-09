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
    @FXML
    private TextField roleField;


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
            roleField.setText(user.getRole());
        }
    }

    // Method to handle saving the edited user details
    @FXML
    void saveUser(ActionEvent event) {
        if (user != null) {
            // Check if required fields are not empty
            if (nomField != null && !nomField.getText().isEmpty() &&
                    prenomField != null && !prenomField.getText().isEmpty() &&
                    emailField != null && !emailField.getText().isEmpty() &&
                    passwordField != null && !passwordField.getText().isEmpty() &&
                    ageField != null && !ageField.getText().isEmpty()) {

                try {
                    // Parse age as an integer
                    int age = Integer.parseInt(ageField.getText());

                    // Update user details
                    user.setNom(nomField.getText());
                    user.setPrenom(prenomField.getText());
                    user.setEmail(emailField.getText());
                    user.setPassword(passwordField.getText());
                    user.setAge(age);
                    user.setRole(roleField.getText());

                    // Update the user details in the database
                    PersonneServices service = new PersonneServices();
                    service.updateEntity(user);
                    showAlert("Success", "User details updated successfully.");
                } catch (NumberFormatException e) {
                    // Handle the case where age is not a valid integer
                    showAlert("Error", "Please enter a valid age.");
                }
            } else {
                // Display an error message if any required field is empty or null
                showAlert("Error", "Please fill in all fields.");
            }
        }
    }



}
