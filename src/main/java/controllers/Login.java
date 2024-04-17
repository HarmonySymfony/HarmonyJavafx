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
import java.sql.SQLException;
import java.util.List;

public class Login {

    public static int loggedInUserId;
    public static entities.Personne UserConnected = new entities.Personne();
    private PersonneServices us = new PersonneServices();
    @FXML
    private TextField emailtextfield2;

    @FXML
    private PasswordField passwordtextfield2;

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
            List<entities.Personne> users = us.recuperer();

            for (int i = 0; i < users.size(); i++) {
                if (users.get(i).getEmail().equals(emailtextfield2.getText()) && users.get(i).getPassword().equals(passwordtextfield2.getText())) {
                    UserConnected = users.get(i);
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
                    stage.setTitle("Viragecom");
                    stage.setScene(scene);
                    stage.show();

                    // Fermeture de la fenêtre de connexion
                    Stage currentStage = (Stage) emailtextfield2.getScene().getWindow();
                    currentStage.close();
                } else {
                    showAlert("Bienvenue", "Welcome " + UserConnected.getPrenom());

                    loggedInUserId = UserConnected.getId();



                    // Affichage de la page d'accueil



                }
            } else {
                showAlert("Utilisateur inexistant", "Utilisateur inexistant!");
            }
        }


    }

    @FXML
    void motdepasseoubliee(ActionEvent event) {

    }

    @FXML
    void signup(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Personne.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);

        Stage stage = new Stage();
        stage.setTitle("Viragecom");
        stage.setScene(scene);
        stage.show();

    }
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.show();
    }

}
