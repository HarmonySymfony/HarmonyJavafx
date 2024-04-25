package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import entities.medicament;
import services.MedicamentServices;

import java.io.IOException;

public class AjouterMedicament {

    @FXML
    private TextField descriptiontextfield;

    @FXML
    private TextField disponibilitetextfield;

    @FXML
    private TextField prixtextfield;

    @FXML
    private TextField refrencetextfield;

    @FXML
    private TextField stocktextfield;

    @FXML
    private Label erreurLabel;

    @FXML
    void Backtolist(ActionEvent event) {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/IndexMedicament.fxml"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void SubmitButton(ActionEvent event) {
        // Récupérer les valeurs saisies dans les champs de texte
        String reference = refrencetextfield.getText();
        String description = descriptiontextfield.getText();
        String disponibilite = disponibilitetextfield.getText();
        int stock = 0;
        int prix = 0;

        // Validation des champs saisis
        try {
            stock = Integer.parseInt(stocktextfield.getText());
            prix = Integer.parseInt(prixtextfield.getText());

            if (reference.isEmpty() || description.isEmpty() || disponibilite.isEmpty()) {
                // Afficher une boîte de dialogue d'alerte avec un message d'erreur
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur de saisie");
                alert.setHeaderText(null);
                alert.setContentText("Veuillez remplir tous les champs obligatoires.");
                alert.showAndWait();
            } else {
                // Créer un nouvel objet medicament avec les valeurs saisies
                medicament newMedicament = new medicament(reference, stock, description, disponibilite, prix);

                // Utiliser le service MedicamentServices pour ajouter le nouvel objet medicament à la base de données
                MedicamentServices medicamentServices = new MedicamentServices();
                medicamentServices.addEntity(newMedicament);

                // Revenir à la vue IndexMedicament.fxml
                Parent root = FXMLLoader.load(getClass().getResource("/IndexMedicament.fxml"));
                Scene scene = new Scene(root);
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(scene);
                stage.show();
            }
        } catch (NumberFormatException e) {
            // Gérer les exceptions si les champs stock ou prix ne sont pas des nombres valides
            System.out.println("Le stock et le prix doivent être des nombres valides.");
        } catch (IOException e) {
            // Gérer les exceptions liées au chargement de la vue IndexMedicament.fxml
            throw new RuntimeException(e);
        }
    }
}
