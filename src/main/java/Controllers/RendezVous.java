package Controllers;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import java.awt.Desktop;
import java.io.File;
import javafx.scene.layout.AnchorPane;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.web.WebEngine;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.File;
import java.io.IOException;

import javafx.scene.control.TableView;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;

import javafx.scene.control.ProgressBar;

import java.util.HashMap;
import java.util.Map;
import javafx.event.ActionEvent;
import entites.Rendezvous;
import javafx.scene.control.*;

import java.util.List;
import java.util.stream.Collectors;

import javafx.scene.control.cell.PropertyValueFactory;
import services.Rendezvousservices;
import javafx.scene.web.WebView;




public class RendezVous implements Initializable {

    @FXML
    private Button ADD_rendezvous;

    @FXML
    private Button Delete_rendezvous;

    @FXML
    private Button Home_page;

    @FXML
    private TextField ID_rendezvous;

    @FXML
    private TableColumn<Rendezvous,Integer> IDcolone;

       /* @FXML
        private AnchorPane RDV_form;*/

    @FXML
    private Button Rendezvouspage;

    @FXML
    private TextField searchField;

    @FXML
    private Button Update_rendezvous;

       /* @FXML
        private AnchorPane User_form;*/

    @FXML
    private Button close;

    @FXML
    private TextField date_rendezvous;

    @FXML
    private TableColumn<Rendezvous, String> datecolone;

    @FXML
    private TextField email_rendezvous;

    @FXML
    private TableColumn<Rendezvous, String> emailcolone;

    @FXML
    private Button logout;

       /* @FXML
        private AnchorPane main_form;*/

    @FXML
    private Button minimize;

    @FXML
    private TextField nom_rendezvous;

    @FXML
    private TableColumn<Rendezvous, String> nomcolone;

    @FXML
    private TextField prenom_rendezvous;

    @FXML
    private TableColumn<Rendezvous, String> prenomcolone;

    @FXML
    private TableView<Rendezvous> rendezvousTableView;

    @FXML
    private Button user_page;

    @FXML
    private Button generatePDF;

    @FXML
    private ProgressBar progressBar;

    @FXML
    private BarChart<String, Integer> appointmentBarChart;
    @FXML
    private Button showStatisticsButton;
    @FXML
    private WebView webView;

    /////////////////////////////////////////////////crud//////////////////////////////////////////////

    @FXML
    void addrendezvous(ActionEvent event) {

        String nom = nom_rendezvous.getText();
        String prenom = prenom_rendezvous.getText();
        String date = date_rendezvous.getText();
        String email = email_rendezvous.getText();
        if (nom.isEmpty() || prenom.isEmpty() || date.isEmpty() || email.isEmpty()) {
            // Afficher une alerte si tous les champs ne sont pas remplis
            showAlert("Erreur", "Veuillez remplir tous les champs.");
            return;
        }

        // Valider le format de la date à l'aide d'une expression régulière
        if (!date.matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}")) {
            // Afficher une alerte si le format de la date est invalide
            showAlert("Erreur", "Veuillez saisir la date au format YYYY-MM-DD HH:MM:SS.");
            return;
        }

        // Valider l'email à l'aide d'une expression régulière
        if (!email.matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")) {
            // Afficher une alerte si l'email est invalide
            showAlert("Erreur", "Veuillez saisir une adresse email valide.");
            return;
        }
        Rendezvous rendezvous = new Rendezvous(nom, prenom, date, email);
        Rendezvousservices Rendezvousservices = new Rendezvousservices(); // Create an instance
        Rendezvousservices.addEntity2(rendezvous); // Call the method on the instance
        loadrendezvousData(); // Refresh the TableView after updating


        // Le reste de votre logique pour ajouter le rendezvous va ici...
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    @FXML
    void updaterendezvous(ActionEvent event) {
        String idStr = ID_rendezvous.getText();
        if (idStr.isEmpty()) {
            System.out.println("Veuillez saisir l'ID du cabinet à mettre à jour.");
            return;
        }
        int id = Integer.parseInt(idStr);
        if (id <= 0) {
            System.out.println("L'ID du cabinet est invalide.");
            return;
        }
        String nom = nom_rendezvous.getText();
        String prenom = prenom_rendezvous.getText();
        String date = date_rendezvous.getText();
        String email = email_rendezvous.getText();
        Rendezvous rendezvous = new Rendezvous( nom, prenom, date, email);
        rendezvous.setId(id); // Set the ID of the cabinet to update

        Rendezvousservices rendezvousservices = new Rendezvousservices(); // Create an instance
        rendezvousservices.updateEntity(rendezvous);
        loadrendezvousData(); // Refresh the TableView after updating
    }


    @FXML
    private void deleterendezvous(ActionEvent event) {
        String idStr = ID_rendezvous.getText();
        if (idStr.isEmpty()) {
            System.out.println("Veuillez saisir l'ID du cabinet à supprimer.");
            return;
        }
        int id = Integer.parseInt(idStr);

        Rendezvous rendezvous = new Rendezvous();
        rendezvous.setId(id); // Set the ID of the cabinet to delete

        Rendezvousservices rendezvousservices = new Rendezvousservices(); // Create an instance
        rendezvousservices.deleteEntity(rendezvous);
        loadrendezvousData();// Refresh the TableView after updating
    }

    @FXML
    void getrendezvous() {
        Rendezvous rendezvous = rendezvousTableView.getSelectionModel().getSelectedItem();
        nom_rendezvous.setText(rendezvous.getNom());
        prenom_rendezvous.setText(rendezvous.getPrenom());
        date_rendezvous.setText(rendezvous.getDate());
        email_rendezvous.setText(rendezvous.getEmail());
    }

    private void loadrendezvousData() {
        List<Rendezvous> rendezvous = Rendezvousservices.getAllData();

        IDcolone.setCellValueFactory(new PropertyValueFactory<>("id"));
        nomcolone.setCellValueFactory(new PropertyValueFactory<>("nom"));
        prenomcolone.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        datecolone.setCellValueFactory(new PropertyValueFactory<>("date"));
        emailcolone.setCellValueFactory(new PropertyValueFactory<>("email"));


        // Clear the TableView to avoid duplicates if called multiple times
        rendezvousTableView.getItems().clear();

        // Add rendezvous objects to the TableView
        rendezvousTableView.getItems().addAll(rendezvous);
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Rendezvousservices Rendezvousservices = new Rendezvousservices();
        // Initialize column cell value factories

        loadrendezvousData(); // Load data into the TableView on initialization

    }

    @FXML
    void showAppointmentStatistics(ActionEvent event) {
        if (appointmentBarChart == null) {
            System.out.println("Le BarChart n'a pas été correctement initialisé dans le fichier FXML.");
            return;
        }

        displayAppointmentStatistics();
    }

    private void displayAppointmentStatistics() {
        if (appointmentBarChart == null) {
            System.out.println("Le BarChart est null. Impossible d'afficher les statistiques.");
            return;
        }

        Map<String, Integer> appointmentCounts = new HashMap<>();

        // Parcourir tous les rendez-vous et compter le nombre de rendez-vous par jour
        for (Rendezvous rendezvous : rendezvousTableView.getItems()) {
            String date = rendezvous.getDate().split(" ")[0]; // Obtenir la date sans l'heure
            appointmentCounts.put(date, appointmentCounts.getOrDefault(date, 0) + 1);
        }

        // Configurer les données du graphique à barres
        XYChart.Series<String, Integer> series = new XYChart.Series<>();
        for (Map.Entry<String, Integer> entry : appointmentCounts.entrySet()) {
            series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }

        if (appointmentBarChart.getData() != null) {
            appointmentBarChart.getData().clear();
        }

        appointmentBarChart.getData().add(series);
    }


    @FXML
    void searchAction(ActionEvent event) {
        String searchText = searchField.getText().toLowerCase(); // Récupérer le texte de recherche
        List<Rendezvous> filteredList = rendezvousTableView.getItems().stream()
                .filter(rendezvous -> rendezvous.getNom().toLowerCase().contains(searchText)
                        || rendezvous.getPrenom().toLowerCase().contains(searchText)
                        || rendezvous.getDate().toLowerCase().contains(searchText)
                        || rendezvous.getEmail().toLowerCase().contains(searchText))
                .collect(Collectors.toList());

        // Mettre à jour la TableView avec la liste filtrée
        rendezvousTableView.getItems().setAll(filteredList);
    }



}