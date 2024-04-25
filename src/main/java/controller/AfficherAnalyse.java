package controller;

import entities.Analyse;
import entities.Laboratoire;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import services.AnalyseService;
import services.LaboratoireService;

import java.sql.SQLException;
import java.util.List;
import java.util.Set;

public class AfficherAnalyse {

    @FXML
    private TableView<Analyse> analyseTable;

    @FXML
    private TableColumn<Analyse, Integer> idColumn;

    @FXML
    private TableColumn<Analyse, String> typeColumn;

    @FXML
    private TableColumn<Analyse, Float> prixColumn;

    private AnalyseService analyseService = new AnalyseService();
    private LaboratoireService laboratoireService = new LaboratoireService();

    public void initialize() {
        // Initialize columns
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        prixColumn.setCellValueFactory(new PropertyValueFactory<>("prix"));
     //  afficherAnalayse();
    }
//    @FXML
//    public void getAnalyse() {
//        Analyse analyse = analyseTable.getSelectionModel().getSelectedItem();
//        if (analyse != null) {
//            idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
//            typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
//            prixColumn.setCellValueFactory(new PropertyValueFactory<>("prix"));
//!
//            idColumn.setText(String.valueOf(analyse.getId()));
//            typeColumn.setText(analyse.getType());
//            prixColumn.setText(String.valueOf(analyse.getPrix()));
//        } else {
//            // Gérer le cas où aucune analyse n'est sélectionnée
//            // Vous pouvez par exemple effacer le texte des colonnes ou afficher un message à l'utilisateur
//        }
//    }

//    private void afficherAnalayse() {
//        ObservableList<Analyse> analyses = null;
//        try {
//            analyses = FXCollections.observableList(analyseService.Display());
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//        analyseTable.setItems(analyses);
//    }


    // Méthode pour initialiser les données d'analyse à afficher
//    public void setAnalyses(List<Analyse> analyses) {
//        // Crée une liste observable à partir de la liste d'analyses
//        ObservableList<Analyse> analyseData = FXCollections.observableArrayList(analyses);
//        // Ajoute les données à la table
//        analyseTable.setItems(analyseData);
//    }
    public void setAnalyses(Laboratoire lab) throws SQLException {
        System.out.println((lab));
        Set<Analyse> la = this.laboratoireService.getAnalysesForLaboratoire(lab);
        System.out.println(la);
        ObservableList<Analyse> analyseObservableList = FXCollections.observableArrayList(la);
        analyseTable.setItems(analyseObservableList);
    }
}
