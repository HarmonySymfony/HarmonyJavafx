package controllers;

import entities.Laboratoire;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;

public class EditLaboratoirepopup extends javafx.scene.control.Dialog<Pair<Laboratoire,String>> {
    private TextField nomField;
    private TextField emplacementField;

    public EditLaboratoirepopup(Laboratoire initlaboratoire) {
        setTitle("Edit Laboratoire");

        // Set the button types (Save and Cancel)
        getDialogPane().getButtonTypes().addAll(ButtonType.APPLY, ButtonType.CANCEL);

        // Create the grid pane layout for the dialog content
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        // Add text fields for editing laboratory information
        nomField = new TextField(initlaboratoire.getNom());
        emplacementField = new TextField(initlaboratoire.getEmplacement());

        // Add labels and text fields to the grid
        grid.add(new javafx.scene.control.Label("Nom:"), 0, 0);
        grid.add(nomField, 1, 0);
        grid.add(new javafx.scene.control.Label("Emplacement:"), 0, 1);
        grid.add(emplacementField, 1, 1);

        // Set the dialog content
        getDialogPane().setContent(grid);

        // Convert the result to a pair when the Save button is clicked
        setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.APPLY) {
                // Retrieve values from fields
                String nom = nomField.getText();
                String emplacement = emplacementField.getText();

                // Validate inputs
                if (!isValidInput(nom, emplacement)) {
                    showAlert(Alert.AlertType.ERROR, "Erreur de saisie", "Veuillez entrer des valeurs valides pour le nom et l'emplacement.");
                    return null;
                }

                // Create updated laboratory object
                Laboratoire updatedLab = new Laboratoire(nom, emplacement);

                // Return the updated laboratory and nom pair
                return new Pair<>(updatedLab, nom);
            }
            return null;
        });
    }

    private boolean isValidInput(String nom, String emplacement) {
        // Check if nom and emplacement are not empty
        if (nom.isEmpty() || emplacement.isEmpty()) {
            return false;
        }

        // Check if nom and emplacement contain only alphabetic characters
        if (!isAlphabetic(nom) || !isAlphabetic(emplacement)) {
            return false;
        }

        return true;
    }

    private boolean isAlphabetic(String input) {
        return input.matches("^[a-zA-Z\\s]+$");
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}