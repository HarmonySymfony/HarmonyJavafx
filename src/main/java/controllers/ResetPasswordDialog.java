package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
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

    @FXML
    void OK_DONE(ActionEvent event) {
        String email = "email@example.com"; // Récupérez l'email de l'utilisateur depuis votre interface utilisateur
        String resetTokenGenerated = "votre-jeton-de-reinitialisation"; // Générez un jeton unique
        personneServices.generateAndStoreResetToken(email, resetTokenGenerated); // Stockez le jeton dans la base de données

        String newPassword = newPasswordField.getText();
        String resetTokenEntered = resetTokenField.getText(); // Renommez cette variable pour éviter les conflits

        if (personneServices.updatePasswordWithToken(newPassword, resetTokenEntered)) {
            resetClicked = true;
            showAlert("Mot de passe réinitialisé", "Votre mot de passe a été réinitialisé avec succès !");
            dialogStage.close();
        } else {
            showAlert("Jeton de réinitialisation invalide", "Le jeton de réinitialisation saisi est invalide. Veuillez réessayer.");
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public void resetPassword(ActionEvent actionEvent) {
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

        // Check if the temporary password matches the one in the database
        if (!personneServices.checkTempPassword(tempPassword)) {
            showAlert("Error", "Temporary password does not match");
            return;
        }

        // Call the resetPassword method from the PersonneServices class
        try {
            personneServices.resetPassword(email, newPassword);
            showAlert("Success", "Password reset successful");
            dialogStage.close();
        } catch (Exception e) {
            showAlert("Error", "Error resetting password: " + e.getMessage());
        }
    }
}

