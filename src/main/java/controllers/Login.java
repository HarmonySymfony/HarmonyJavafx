package controllers;

import java.util.Properties;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.mindrot.jbcrypt.BCrypt;
import services.PersonneServices;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class Login {

    public static int loggedInUserId;
    public static entities.Personne UserConnected = new entities.Personne();
    private PersonneServices us = new PersonneServices();
    @FXML
    private TextField emailtextfield2;

    @FXML
    private PasswordField passwordtextfield2;
    private String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt(13));
    }

    @FXML
    void login(ActionEvent event) throws SQLException, IOException {
        if (emailtextfield2.getText().isEmpty() || passwordtextfield2.getText().isEmpty()) {

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Champ vide,Remplir les champs vides!");
            alert.show();
        } else if (!emailtextfield2.getText().matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")) {

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Email non valide Format email non valide!");
            alert.show();
        } else {
            Boolean verif = false;
            List<entities.Personne> users = us.getAllData();

            for (entities.Personne user : users) {
                if (user.getEmail().equals(emailtextfield2.getText()) && BCrypt.checkpw(passwordtextfield2.getText(), user.getPassword())) {
                    UserConnected = user;
                    verif = true;
                    break;
                }
            }


            if (verif) {
                if (emailtextfield2.getText().equals("admin@admin.fr") && passwordtextfield2.getText().equals("admin123")) {
                    // Chargement de la fenêtre AfficheUser.fxml
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficheUser.fxml"));
                    Parent root = loader.load();
                    Scene scene = new Scene(root);

                    Stage stage = new Stage();
                    stage.setTitle("Sahtik");
                    stage.setScene(scene);
                    stage.show();

                    // Fermeture de la fenêtre de connexion
                    Stage currentStage = (Stage) emailtextfield2.getScene().getWindow();
                    currentStage.close();
                } else {


                    if (UserConnected != null) {
                        loggedInUserId = UserConnected.getId();
                        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                        currentStage.close();
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Homepage.fxml"));
                        Parent root = loader.load();
                        Scene scene = new Scene(root);
                        Homepage homepageController = loader.getController();
                        homepageController.setUser(UserConnected.getId());

                        Stage stage = new Stage();
                        stage.setTitle("Sahtik");
                        stage.setScene(scene);
                        stage.show();
                        showAlert("Bienvenue", "Welcome " + UserConnected.getPrenom());
                    } else {
                        showAlert("Utilisateur inexistant", "Utilisateur inexistant!");
                    }

                    // Affichage de la page d'accueil



                }
            } else {
                showAlert("Utilisateur inexistant", "Utilisateur inexistant!");
            }
        }


    }

    @FXML
    void motdepasseoubliee(ActionEvent event) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Mot de passe oublié");
        dialog.setHeaderText(null);
        dialog.setContentText("Veuillez entrer votre adresse e-mail pour réinitialiser votre mot de passe:");
        Optional<String> result = dialog.showAndWait();

        result.ifPresent(email -> {
            try {
                String resetToken = generateResetToken(); // Générer un jeton de réinitialisation
                sendForgotPasswordRequest(email); // Envoyer l'e-mail avec le jeton de réinitialisation
            } catch (MessagingException e) {
                e.printStackTrace();
                showAlert("Erreur", "Une erreur s'est produite lors de la demande de réinitialisation du mot de passe.");
            }
        });
    }
    private String generateResetToken() {
        // Utilisation de UUID pour générer un identifiant unique
        return UUID.randomUUID().toString();
    }
    private void sendForgotPasswordRequest(String email) throws MessagingException {
        // Generate a new temporary password
        String tempPassword = generateTempPassword();

        // Update the user's password in the database with the plain text temporary password
        PersonneServices.resetPassword(email, tempPassword);

        // Send the plain text temporary password to the user's email
        sendEmail(email, tempPassword);
    }

    private String generateTempPassword() {
        // Generate a random temporary password
        // This is just a simple example, you should use a more secure way to generate a temporary password
        return UUID.randomUUID().toString().substring(0, 8);
    }

    private void sendEmail(String email, String tempPassword) throws MessagingException {
        // Configuration des propriétés pour l'envoi d'e-mails via Outlook SMTP
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.office365.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        // Création de la session
        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("alaeddine.aouf@esprit.tn", "7984651320Aa");
            }
        });

        // Création du message
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress("alaeddine.aouf@esprit.tn"));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
        message.setSubject("Réinitialisation de mot de passe");
        message.setText("Bonjour,\n\nVous avez demandé la réinitialisation de votre mot de passe. Veuillez utiliser ce jeton pour réinitialiser votre mot de passe : " + tempPassword + "\n\nCordialement,\nVotre application");

        // Envoi du message
        Transport.send(message);
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ResetPassword.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setTitle("Reset Password");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "An error occurred while displaying the reset password page.");
        }
    }
    
    @FXML
    void signup(ActionEvent event) throws IOException {
        // Load the FXML file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Personne.fxml"));
        Parent root = loader.load();

        // Access the current scene and root element
        Scene currentScene = emailtextfield2.getScene();
        Pane rootPane = (Pane) currentScene.getRoot();

        // Replace the content of the root with the content of the new FXML file
        rootPane.getChildren().clear(); // Remove existing content
        rootPane.getChildren().add(root); // Add new content

        // Optionally, you can also set the scene back to the stage if needed
        Stage stage = (Stage) currentScene.getWindow();
        stage.setScene(currentScene);
    }


    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.show();
    }

}