package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.mindrot.jbcrypt.BCrypt;
import services.PersonneServices;

import static controllers.Home.email;

public class ResetPasswordDialog {

    public PasswordField confirmPasswordField;
    private Stage dialogStage;
    private PersonneServices personneServices = new PersonneServices();

    private boolean resetClicked = false;

    @FXML
    private PasswordField newPasswordField;

    @FXML
    private TextField resetTokenField;
    @FXML
    private PasswordField tempPasswordField;
    @FXML
    private TextField emailField;
    @FXML
    private WebView webView;

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
        webEngine.load(getClass().getResource("/HTML/index.html").toExternalForm());}

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public boolean isResetClicked() {
        return resetClicked;
    }

    @FXML
    void CANCEL_CLOSE(ActionEvent event) {
        dialogStage.close();
    }
    private String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt(13));
    }



    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }


    @FXML
    void submitResetPassword(ActionEvent event) {
        String email = emailField.getText();
        String tempPassword = tempPasswordField.getText();
        String newPassword = newPasswordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        // Check if the fields are empty
        if (email.isEmpty() || tempPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
            showAlert("Error", "All fields must be filled");
            return;
        }

        // Check if the new password and confirmation password match
        if (!newPassword.equals(confirmPassword)) {
            showAlert("Error", "Passwords do not match");
            return;
        }

        // Check if the plain text temporary password matches the one in the database
        if (!personneServices.checkTempPassword(email, tempPassword)) {
            showAlert("Error", "Temporary password does not match");
            return;
        }

        // Hash the new password
        String hashedPassword = hashPassword(newPassword);

        // Call the resetPassword method from the PersonneServices class
        try {
            personneServices.resetPassword(email, hashedPassword);
            showAlert("Success", "Password reset successful");
            dialogStage.close();
        } catch (Exception e) {
            showAlert("Error", "Error resetting password: " + e.getMessage());
        }
    }
}

