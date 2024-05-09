package controllers;

import entites.Rendezvous;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Screen;
import javafx.stage.Stage;
import services.Rendezvousservices;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class frontRDV implements Initializable {

    @FXML
    private TableColumn<Rendezvous, Integer> IDcol;

    @FXML
    private TableColumn<Rendezvous, String> datecol;

    @FXML
    private TableColumn<Rendezvous, String> emailcol;

    @FXML
    private TableColumn<Rendezvous, String> nomcol;

    @FXML
    private TableColumn<Rendezvous, String> prenomcol;

    @FXML
    private TableView<Rendezvous> rendezvousTableViewfront;
    @FXML
    private WebView webView;

    private void loadrendezvousData() {
        List<Rendezvous> rendezvous = Rendezvousservices.getAllData();

        IDcol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nomcol.setCellValueFactory(new PropertyValueFactory<>("nom"));
        prenomcol.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        datecol.setCellValueFactory(new PropertyValueFactory<>("date"));
        emailcol.setCellValueFactory(new PropertyValueFactory<>("email"));


        // Clear the TableView to avoid duplicates if called multiple times
        rendezvousTableViewfront.getItems().clear();

        // Add rendezvous objects to the TableView
        rendezvousTableViewfront.getItems().addAll(rendezvous);
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Rendezvousservices Rendezvousservices = new Rendezvousservices();
        // Initialize column cell value factories

        loadrendezvousData(); // Load data into the TableView on initialization

        // Récupérer la taille de l'écran
        double screenWidth = Screen.getPrimary().getVisualBounds().getWidth();
        double screenHeight = Screen.getPrimary().getVisualBounds().getHeight();

        // Lier la taille de la WebView à la taille de l'écran
        webView.setPrefWidth(screenWidth);
        webView.setPrefHeight(screenHeight);

        // Charger le fichier HTML avec le fond animé
        WebEngine webEngine = webView.getEngine();

        // Charger le fichier HTML contenant la carte Google Maps
        webEngine.load(getClass().getResource("/HTML/index.html").toExternalForm());

    }
    @FXML
    void retour(ActionEvent event) {

        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/frontcab.fxml"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }


}
