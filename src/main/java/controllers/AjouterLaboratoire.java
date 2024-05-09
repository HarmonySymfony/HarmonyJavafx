package controllers;

import entities.Laboratoire;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import services.LaboratoireService;

import java.sql.SQLException;
import java.util.regex.Pattern;

public class AjouterLaboratoire {
    @FXML
    private TextField nomtextfield, emplacementtextfield;
    LaboratoireService laboratoireService = new LaboratoireService();

    public void initLaboratoireService(LaboratoireService L) {
        this.laboratoireService = L;
    }

    public void addLab(ActionEvent actionEvent) {
        String nom = nomtextfield.getText();
        String emplacement = emplacementtextfield.getText();

        // Vérification si les champs ne sont pas vides
        if (nom.isEmpty() || emplacement.isEmpty()) {
            afficherAlerteErreur("Veuillez remplir tous les champs.");
            return;
        }

        // Vérification si le nom et l'emplacement ne contiennent que des lettres alphabétiques
        if (!estAlphabetique(nom) || !estAlphabetique(emplacement)) {
            afficherAlerteErreur("Le nom et l'emplacement doivent contenir uniquement des lettres alphabétiques.");
            return;
        }

        try {
            // Vérification si le nom et l'emplacement sont uniques en interrogeant la base de données
            if (laboratoireService.existeNom(nom)) {
                afficherAlerteErreur("Ce nom de laboratoire existe déjà.");
                return;
            }
            if (laboratoireService.existeEmplacement(emplacement)) {
                afficherAlerteErreur("Cet emplacement de laboratoire est déjà utilisé.");
                return;
            }

            // Si toutes les vérifications passent, ajouter le laboratoire
            Laboratoire L = new Laboratoire(nom, emplacement);
            laboratoireService.add(L);

            // Afficher un message de succès ou prendre une autre action appropriée
            afficherAlerteInformation("Laboratoire ajouté avec succès.");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean estAlphabetique(String chaine) {
        // Utiliser une expression régulière pour vérifier si la chaîne ne contient que des lettres alphabétiques
        return Pattern.matches("^[a-zA-Z\\s]+$", chaine);
    }

    private void afficherAlerteErreur(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void afficherAlerteInformation(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}