
package controllers;

import entities.Analyse;
import entities.Laboratoire;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import services.AnalyseService;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AjouterAnalyse implements Initializable {

    @FXML
    private TextField prixTextField;

    @FXML
    private TextField typeTextField;

    private AnalyseService analyseService = new AnalyseService();
    private int laboratoireId; // Variable pour stocker l'ID du laboratoire sélectionné

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Vous pouvez ajouter des initialisations ici si nécessaire
    }

    // Méthode pour recevoir l'ID du laboratoire sélectionné
    public void initLaboratoire(int laboratoireId) {
        this.laboratoireId = laboratoireId;
    }

    @FXML
    public void ajouterAnalyse(ActionEvent event) {
        // Récupérer les valeurs saisies dans les champs de texte
        float prix = Float.parseFloat(prixTextField.getText());
        String type = typeTextField.getText();

        // Créer une nouvelle instance d'Analyse
        Analyse nouvelleAnalyse = new Analyse(prix, type, laboratoireId); // Passer l'ID du laboratoire

        // Appeler la méthode add de votre service AnalyseService pour ajouter l'analyse à la base de données
        try {
            analyseService.add(nouvelleAnalyse,laboratoireId);
            // Afficher une confirmation ou effectuer d'autres actions en cas de succès
            System.out.println("Analyse ajoutée avec succès !");
        } catch (SQLException e) {
            e.printStackTrace();
            // Gérer les erreurs d'ajout de l'analyse à la base de données
        }
    }
}
