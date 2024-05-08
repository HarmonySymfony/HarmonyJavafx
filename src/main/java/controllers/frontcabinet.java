package controllers;

import entites.Cabinet;
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
import javafx.stage.Stage;
import services.CabinetServices;
import entites.Cabinet;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class frontcabinet implements Initializable {

    @FXML
    private TableColumn<Cabinet, Integer> ID_cab;

    @FXML
    private TableColumn<Cabinet, String> adresscab;

    @FXML
    private TableView<Cabinet> cabinetTableViewfront;

    @FXML
    private TableColumn<Cabinet, String> emailcab;

    @FXML
    private TableColumn<Cabinet, String> heurcab;

    @FXML
    private TableColumn<Cabinet, String> nomcab;


    private void loadCabinetData() {
        CabinetServices cabinetServices= new CabinetServices();
        List<Cabinet> cabinets = cabinetServices.getAllDataCabinet();
        for (Cabinet cabinet : cabinets) {
            System.out.println("Cabinet ID: " + cabinet.getId());
        }

        ID_cab.setCellValueFactory(new PropertyValueFactory<>("id"));
        nomcab.setCellValueFactory(new PropertyValueFactory<>("nom"));
        adresscab.setCellValueFactory(new PropertyValueFactory<>("adress"));
        heurcab.setCellValueFactory(new PropertyValueFactory<>("horaires"));
        emailcab.setCellValueFactory(new PropertyValueFactory<>("email"));


        // Clear the TableView to avoid duplicates if called multiple times
        cabinetTableViewfront.getItems().clear();

        // Add rendezvous objects to the TableView
        cabinetTableViewfront.getItems().addAll(cabinets);
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        CabinetServices cabinetServices= new CabinetServices(); // Initialisez l'instance de CabinetServices
        // Initialisez les autres composants et chargez les données dans le TableView
        loadCabinetData(); // Chargez les données dans le TableView
    }

    @FXML
    void rendezvous(ActionEvent event) {

        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/frontRDV.fxml"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

}
