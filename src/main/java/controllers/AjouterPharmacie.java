package controllers;

import entities.pharmacie;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import services.PharmacieServices;

import java.io.IOException;

public class AjouterPharmacie {

    @FXML
    private TextField AdressTextField;

    @FXML
    private TextField NomTextField;

    @FXML
    private TextField TypeTextField;

    @FXML
    void BackToListButton(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/IndexPharmacie.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void SubmitButton(ActionEvent event) throws IOException {
        String nom = NomTextField.getText();
        String adresse = AdressTextField.getText();
        String type = TypeTextField.getText();

        if (nom.isEmpty() || adresse.isEmpty() || type.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Veuillez remplir tous les champs.");
            alert.showAndWait();
            return;
        }

        pharmacie pharmacieToAdd = new pharmacie(nom, adresse, type);
        PharmacieServices pharmacieServices = new PharmacieServices();
        pharmacieServices.addEntity(pharmacieToAdd);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("Pharmacie ajoutée avec succès.");
        alert.showAndWait();

        // Recharger la vue ShowPharmacie
        Parent root = FXMLLoader.load(getClass().getResource("/IndexPharmacie.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
}
