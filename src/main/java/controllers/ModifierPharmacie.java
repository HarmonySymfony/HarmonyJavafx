package controllers;

import entities.pharmacie;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import services.PharmacieServices;

import java.io.IOException;

public class ModifierPharmacie {

    @FXML
    private TextField AdressPharmacie;

    @FXML
    private TextField NomPharmacie;

    @FXML
    private TextField TypePharmacie;

    private ListView<pharmacie> listePharmacies;


    public void setListePharmacies(ListView<pharmacie> listePharmacies) {
        this.listePharmacies = listePharmacies;
    }


    @FXML
    void BackToListeUpdate(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/IndexPharmacie.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void SubmitButtonUpdate(ActionEvent event) throws IOException {
        String nom = NomPharmacie.getText();
        String adress = AdressPharmacie.getText();
        String type = TypePharmacie.getText();

        if (nom.isEmpty() || adress.isEmpty() || type.isEmpty()) {
            showAlert("Error", "Please fill in all fields.");
            return;
        }

        pharmacie selectedPharmacy = listePharmacies.getSelectionModel().getSelectedItem();
        if (selectedPharmacy != null) {
            selectedPharmacy.setNom(nom);
            selectedPharmacy.setAdress(adress);
            selectedPharmacy.setType(type);

            PharmacieServices pharmacieServices = new PharmacieServices();
            pharmacieServices.updateEntity(selectedPharmacy);

            showAlert("Success", "Pharmacy updated successfully.");
            BackToListeUpdate(event);
        } else {
            showAlert("Error", "No pharmacy selected.");
        }
    }


    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public void setPharmacie(pharmacie pharmacy) {
        // Préremplir les champs avec les informations de la pharmacie sélectionnée
        NomPharmacie.setText(pharmacy.getNom());
        AdressPharmacie.setText(pharmacy.getAdress());
        TypePharmacie.setText(pharmacy.getType());
    }
}
