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
import org.mindrot.jbcrypt.BCrypt;

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

    private String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt(13));
    }

    @FXML
    void ajouterPersonne(ActionEvent event) throws IOException {
        String hashedPassword = hashPassword(passwordTextField.getText());

        entities.Personne p = new entities.Personne(nomTextField.getText(), prenomTextField.getText(), emailTextField.getText(), hashedPassword, Integer.parseInt(ageTextField.getText()),roleTextField.getText());
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
            } else if (!ageTextField.getText().matches("^([1-9]|[1-9][0-9])$")) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Age non valide");
                alert.setHeaderText(null);
                alert.setContentText("Format AGE non valide! L'âge doit être compris entre 1 et 99.");
                alert.show();
            } else {
                personneServices.Ajouter(p);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("l'utilisateur a été ajouté avec succès");
                alert.show();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Login.fxml"));
                Parent root = loader.load();
                Scene scene = new Scene(root);

                Stage stage = new Stage();
                stage.setTitle("Harmony");
                stage.setScene(scene);
                stage.show();
            }
        } catch (Exception e) {
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

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Harmony");
        stage.setScene(scene);
        stage.show();
    }
}