package Controllers;


import entites.Cabinet;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import services.CabinetServices;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class PleaseProvideControllerClassName implements Initializable {

    @FXML
    private Button Ajouter_Rapport;

    @FXML
    private TableColumn<Cabinet, String> IDcolone;

    @FXML
    private TextField Search_RDV;

    @FXML
    private TableColumn<Cabinet, String> adresscolone;

    @FXML
    private TableView<Cabinet> cabinetTableView;

    @FXML
    private TableColumn<Cabinet, String> emailcolone;

    @FXML
    private TableColumn<Cabinet, String> horairescolone;

    @FXML
    private TableColumn<Cabinet, String> nomcolone;

    private CabinetServices cabinetServices;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cabinetServices = new CabinetServices();
        loadCabinetData(); // Charge les données dans le TableView au démarrage de l'application
    }

    private void loadCabinetData() {
        List<Cabinet> cabinets = cabinetServices.getAllDataCabinet();

        IDcolone.setCellValueFactory(new PropertyValueFactory<>("id"));
        nomcolone.setCellValueFactory(new PropertyValueFactory<>("nom"));
        adresscolone.setCellValueFactory(new PropertyValueFactory<>("adress"));
        horairescolone.setCellValueFactory(new PropertyValueFactory<>("horaires"));
        emailcolone.setCellValueFactory(new PropertyValueFactory<>("email"));

        cabinetTableView.getItems().clear();
        cabinetTableView.getItems().addAll(cabinets); // Ajoute les cabinets au TableView
    }

    @FXML
    void Ajouter_Rapport(ActionEvent event) {
        try {
            // Charge le nouveau fichier FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/frontrendezvous.fxml"));
            Parent root = loader.load();

            // Crée une nouvelle fenêtre
            Stage stage = new Stage();
            stage.setScene(new Scene(root));

            // Affiche la fenêtre
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void search(ActionEvent event) {
        String searchTerm = Search_RDV.getText().trim().toLowerCase();

        if (!searchTerm.isEmpty()) {
            List<Cabinet> filteredCabinets = new ArrayList<>();

            for (Cabinet cabinet : cabinetTableView.getItems()) {
                if (cabinet.getNom().toLowerCase().contains(searchTerm)
                        || cabinet.getAdress().toLowerCase().contains(searchTerm)
                        || cabinet.getHoraires().toLowerCase().contains(searchTerm)
                        || cabinet.getEmail().toLowerCase().contains(searchTerm)) {
                    filteredCabinets.add(cabinet);
                }
            }

            cabinetTableView.getItems().clear();
            cabinetTableView.getItems().addAll(filteredCabinets);
        } else {
            loadCabinetData();
        }
    }
}
