package controllers;
import com.twilio.rest.api.v2010.account.Message;
import io.github.cdimascio.dotenv.Dotenv;

import com.twilio.Twilio;
import com.twilio.type.PhoneNumber;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;

import javafx.scene.control.TableView;

import java.io.IOException;
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
import javafx.stage.Stage;
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
        loadrendezvousData();// Refresh the TableView after updating

        // Appeler la fonction pour envoyer un SMS
        sendSMS();

        // Le reste de votre logique pour ajouter le rendezvous va ici...
    }

        private void sendSMS() {
            // Initialize Twilio with your Account SID and Auth Token
            Twilio.init("ACf1bd89101882eec6968eb79dcd988463", "d140e45289fd47b390a906fc4fc4f91a");

            // Remplacer les valeurs suivantes par votre numéro Twilio et le numéro de téléphone de destination
            String twilioNumber = "+13342923501"; // Votre numéro Twilio
            String recipientNumber = "+21692338746"; // Numéro de téléphone du destinataire

            // Message à envoyer
            String messageBody = "Rendez-vous chez sahtekk confirmé !";

            // Envoyer le message SMS
            Message message = Message.creator(
                    new PhoneNumber(recipientNumber),
                    new PhoneNumber(twilioNumber),
                    messageBody
            ).create();

            System.out.println("Message SID: " + message.getSid());
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
    @FXML
    void calendar(ActionEvent event) {
        try {
            // Load the new FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/CalendarRDV.fxml"));
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


}