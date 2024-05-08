package controllers;

import entities.medicament;
import entities.pharmacie;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import services.MedicamentServices;

import java.io.IOException;

public class IndexMedicamentFRONT {

    @FXML
    private TextField descriptionTextField;

    @FXML
    private TextField disponibiliteTextField;

    @FXML
    private TextField prixTextField;

    @FXML
    private TextField referenceTextField;

    @FXML
    private TextField stockTextField;

    @FXML
    private ListView<medicament> listemedicament;

    private ObservableList<medicament> medicaments = FXCollections.observableArrayList();
    private pharmacie pharmacie;

    @FXML
    void initialize() {
        MedicamentServices medicamentServices = new MedicamentServices();
        medicaments.addAll(medicamentServices.getAllData());

        listemedicament.setItems(medicaments);

        listemedicament.setCellFactory(param -> new ListCell<medicament>() {
            @Override
            protected void updateItem(medicament item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    Label referenceLabel = new Label("Référence: " + item.getReference());
                    Label stockLabel = new Label("Stock: " + item.getStock());
                    Label disponibiliteLabel = new Label("Disponibilité: " + item.getDisponibilite());
                    Label descriptionLabel = new Label("Description: " + item.getDescription());
                    Label prixLabel = new Label("Prix: " + item.getPrix());

                    Button showButton = new Button("Show");
                    showButton.setOnAction(event -> {
                        Alert alert = new Alert(AlertType.INFORMATION);
                        alert.setTitle("Détails du médicament");
                        alert.setHeaderText(null);
                        alert.setContentText("Référence: " + item.getReference() +
                                "\nStock: " + item.getStock() +
                                "\nDisponibilité: " + item.getDisponibilite() +
                                "\nDescription: " + item.getDescription() +
                                "\nPrix: " + item.getPrix());
                        alert.showAndWait();
                    });

                    VBox detailsVBox = new VBox(referenceLabel, stockLabel, disponibiliteLabel, descriptionLabel, prixLabel);
                    HBox buttonsBox = new HBox(5, showButton);

                    Region spacer = new Region();
                    HBox.setHgrow(spacer, Priority.ALWAYS);

                    HBox cellBox = new HBox(detailsVBox, spacer, buttonsBox);
                    setGraphic(cellBox);
                }
            }
        });
    }

    @FXML
    void retourBack(ActionEvent event) {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/IndexPharmacieFRONT.fxml"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    private void loadFXML(String fxmlPath, ActionEvent event) {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource(fxmlPath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public void setPharmacie(pharmacie pharmacie) {
        this.pharmacie = pharmacie;
    }

    public pharmacie getPharmacie() {
        return pharmacie;
    }

    public void setListePharmacies(ListView<pharmacie> listepharmacie) {
    }
}
