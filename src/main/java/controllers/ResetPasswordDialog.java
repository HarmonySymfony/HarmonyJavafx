package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import services.PersonneServices;

public class ResetPasswordDialog {

    private Stage dialogStage;
    private PersonneServices personneServices = new PersonneServices();

    private boolean resetClicked = false;

    @FXML
    private PasswordField newPasswordField;

    @FXML
    private TextField resetTokenField;

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
}

