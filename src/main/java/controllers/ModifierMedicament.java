package controllers;

import entities.medicament;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import services.MedicamentServices;

public class ModifierMedicament {
    @FXML
    private TextField descriptionTextField;

    @FXML
    private TextField disponibiliteTextField;

    @FXML
    private TextField prixTextField;

    @FXML
    private TextField referenceTextField;

    @FXML
    private TextField stockTextField;

    private medicament selectedMedicament;
    private ObservableList<medicament> medicamentsList;

    public void initData(medicament selectedMedicament, ObservableList<medicament> medicamentsList) {
        this.selectedMedicament = selectedMedicament;
        this.medicamentsList = medicamentsList;
        // Initialiser les champs de texte avec les valeurs du médicament sélectionné
        referenceTextField.setText(selectedMedicament.getReference());
        stockTextField.setText(String.valueOf(selectedMedicament.getStock()));
        disponibiliteTextField.setText(selectedMedicament.getDisponibilite());
        descriptionTextField.setText(selectedMedicament.getDescription());
        prixTextField.setText(String.valueOf(selectedMedicament.getPrix()));
    }


        @FXML
        void submitbutton(ActionEvent event) {
            // Mettre à jour les valeurs du médicament sélectionné avec les nouvelles valeurs saisies dans les champs de texte
            selectedMedicament.setReference(referenceTextField.getText());
            selectedMedicament.setStock(Integer.parseInt(stockTextField.getText()));
            selectedMedicament.setDisponibilite(disponibiliteTextField.getText());
            selectedMedicament.setDescription(descriptionTextField.getText());
            selectedMedicament.setPrix(Integer.parseInt(prixTextField.getText()));

            // Mettre à jour le médicament dans la base de données
            MedicamentServices medicamentServices = new MedicamentServices();
            medicamentServices.updateEntity(selectedMedicament);

            // Mettre à jour le médicament dans la liste
            medicamentsList.set(medicamentsList.indexOf(selectedMedicament), selectedMedicament);

            // Fermer la fenêtre
            ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
        }


    public void backTOlist(ActionEvent actionEvent) {
    }



}
