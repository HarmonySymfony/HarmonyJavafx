package controllers;


import entites.Cabinet;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import services.CabinetServices;

public class cabinet implements Initializable {
    @FXML
    private Button ADD_Cabinet;

    @FXML
    private Button Ajouter_Rapport;

    @FXML
    private Button PDF_Cabinet;

    @FXML
    private Button Delete_Cabinet;

    @FXML
    private Button Home_page;

    @FXML
    private TextField ID_cabinet;

    // @FXML
    // private AnchorPane RDV_form;

    @FXML
    private Button Rendezvouspage;

    @FXML
    private TextField Search_RDV;

    @FXML
    private Button Update_Cabinet;

    //  @FXML
    //private AnchorPane User_form;

    @FXML
    private TextField adress_cabinet;

    @FXML
    private TableColumn<Cabinet, String> adresscolone;

    @FXML
    private TableView<Cabinet> cabinetTableView;

    @FXML
    private Button close;

    @FXML
    private TextField email_cabinet;

    @FXML
    private TableColumn<Cabinet, String> emailcolone;

    @FXML
    private TextField heur_cabinet;

    @FXML
    private TableColumn<Cabinet, String> horairescolone;

    @FXML
    private TableColumn<Cabinet, Integer> IDcolone;



    @FXML
    private Button logout;

    // @FXML
    //private AnchorPane main_form;

    @FXML
    private Button minimize;

    @FXML
    private TextField nom_cabinet;

    @FXML
    private TableColumn<Cabinet, String> nomcolone;

    @FXML
    private Button user_page;

    private CabinetServices cabinetServices;



    /////////////////////////////////////////////////crud//////////////////////////////////////////////

    @FXML
    private void addCabinet(ActionEvent event) {

        String adress = adress_cabinet.getText();
        String nom = nom_cabinet.getText();
        String horaires = heur_cabinet.getText();
        String email = email_cabinet.getText();
        if (adress.isEmpty() || nom.isEmpty() || horaires.isEmpty() || email.isEmpty()) {
            // Afficher une alerte indiquant que tous les champs sont obligatoires
            showAlert("Erreur", "Veuillez remplir tous les champs.");
            return;
        }

        // Valider l'adresse email à l'aide d'une expression régulière
        if (!email.matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")) {
            // Afficher une alerte indiquant que l'adresse email est invalide
            showAlert("Erreur", "Veuillez saisir une adresse email valide.");
            return;
        }

        // Valider le format des horaires à l'aide d'une expression régulière
        if (!horaires.matches("\\d{2}:\\d{2}")) {
            // Afficher une alerte indiquant que le format des horaires est invalide
            showAlert("Erreur", "Veuillez saisir les horaires au format HH:MM.");
            return;
        }
        Cabinet cabinet = new Cabinet( adress, nom, horaires, email );
        CabinetServices cabinetServices = new CabinetServices(); // Create an instance
        cabinetServices.addEntity2(cabinet); // Call the method on the instance
        loadCabinetData(); // Refresh the TableView after updating



        // Le reste de votre logique pour ajouter le cabinet va ici...
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }





    @FXML
    private void deleteCabinet(ActionEvent event) {
        String idStr = ID_cabinet.getText();
        if (idStr.isEmpty()) {
            System.out.println("Veuillez saisir l'ID du cabinet à supprimer.");
            return;
        }
        int id = Integer.parseInt(idStr);

        Cabinet cabinet = new Cabinet();
        cabinet.setId(id); // Set the ID of the cabinet to delete

        CabinetServices cabinetServices = new CabinetServices();
        cabinetServices.deleteEntity(cabinet);
        loadCabinetData(); // Refresh the TableView after updating
    }


    @FXML
    void updateCabinet(ActionEvent event) {
        String idStr = ID_cabinet.getText();
        if (idStr.isEmpty()) {
            System.out.println("Veuillez saisir l'ID du cabinet à mettre à jour.");
            return;
        }
        int id = Integer.parseInt(idStr);
        if (id <= 0) {
            System.out.println("L'ID du cabinet est invalide.");
            return;
        }
        String adress = adress_cabinet.getText();
        String nom = nom_cabinet.getText();
        String horaires = heur_cabinet.getText();
        String email = email_cabinet.getText();

        Cabinet cabinet = new Cabinet( adress, nom, horaires, email);
        cabinet.setId(id); // Set the ID of the cabinet to update

        cabinetServices.updateEntity(cabinet); // Call the updateEntity method from CabinetServices
        loadCabinetData(); // Refresh the TableView after updating
    }



    @FXML
    void getcabinet() {
        Cabinet cabinet = cabinetTableView.getSelectionModel().getSelectedItem();
        ID_cabinet.setText(cabinet.getNom());
        nom_cabinet.setText(cabinet.getNom());
        adress_cabinet.setText(cabinet.getAdress());
        heur_cabinet.setText(cabinet.getHoraires());
        email_cabinet.setText(cabinet.getEmail());
    }

    private void loadCabinetData() {
        List<Cabinet> cabinets = cabinetServices.getAllDataCabinet();

        IDcolone.setCellValueFactory(new PropertyValueFactory<>("id"));
        nomcolone.setCellValueFactory(new PropertyValueFactory<>("nom"));
        adresscolone.setCellValueFactory(new PropertyValueFactory<>("adress"));
        horairescolone.setCellValueFactory(new PropertyValueFactory<>("horaires"));
        emailcolone.setCellValueFactory(new PropertyValueFactory<>("email"));


        // Clear the TableView to avoid duplicates if called multiple times
        cabinetTableView.getItems().clear();

        // Add cabinet objects to the TableView
        cabinetTableView.getItems().addAll(cabinets);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cabinetServices = new CabinetServices();
        // Initialize column cell value factories

        loadCabinetData(); // Load data into the TableView on initialization
    }

    @FXML
    void Ajouter_Rapport (ActionEvent event){
        try {
            // Load the new FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/RDV.fxml"));
            Parent root = loader.load();

            // Create a new stage
            Stage stage = new Stage();
            stage.setScene(new Scene(root));

            // Show the stage
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }



    @FXML
    private void search(ActionEvent event) {
        String searchTerm = Search_RDV.getText().trim().toLowerCase();

        // Vérifier si le terme de recherche n'est pas vide
        if (!searchTerm.isEmpty()) {
            List<Cabinet> filteredCabinets = new ArrayList<>();

            // Filtrer les cabinets qui correspondent au terme de recherche
            for (Cabinet cabinet : cabinetTableView.getItems()) {
                if (cabinet.getNom().toLowerCase().contains(searchTerm) ||
                        cabinet.getAdress().toLowerCase().contains(searchTerm) ||
                        cabinet.getHoraires().toLowerCase().contains(searchTerm) ||
                        cabinet.getEmail().toLowerCase().contains(searchTerm)) {
                    filteredCabinets.add(cabinet);
                }
            }

            // Mettre à jour les données affichées dans le TableView avec les résultats de la recherche
            cabinetTableView.getItems().clear();
            cabinetTableView.getItems().addAll(filteredCabinets);
        } else {
            // Si le champ de recherche est vide, afficher toutes les données
            loadCabinetData();
        }
    }

}