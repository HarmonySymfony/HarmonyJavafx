package controllers;

import entities.medicament;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import services.MedicamentServices;

import java.io.IOException;

public class IndexMedicament {
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

    @FXML
    private Button ajoutButton;

    private ObservableList<medicament> medicaments = FXCollections.observableArrayList();

    @FXML
    void initialize() {
        // Utiliser le service MedicamentServices pour récupérer la liste de tous les médicaments
        MedicamentServices medicamentServices = new MedicamentServices();
        medicaments.addAll(medicamentServices.getAllData());

        // Liaison de la liste observable à la ListView
        listemedicament.setItems(medicaments);

        // Définir une cellule personnalisée pour la ListView
        listemedicament.setCellFactory(param -> new ListCell<medicament>() {
            @Override
            protected void updateItem(medicament item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    // Création des labels pour afficher les détails du médicament
                    Label referenceLabel = new Label("" + item.getReference());
                    Label stockLabel = new Label("" + item.getStock());
                    Label disponibiliteLabel = new Label(" " + item.getDisponibilite());
                    Label descriptionLabel = new Label(" " + item.getDescription());
                    Label prixLabel = new Label(" " + item.getPrix());

                    // Création du bouton "Edit"
                    Button editButton = new Button("Edit");

                    // Action pour le bouton "Edit"
                    editButton.setOnAction(event -> {
                        medicament selectedMedicament = getItem();
                        if (selectedMedicament != null) {
                            try {
                                FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierMedicament.fxml"));
                                Parent root = loader.load();
                                ModifierMedicament controller = loader.getController();
                                controller.initData(selectedMedicament, medicaments);
                                Stage stage = new Stage();
                                stage.setScene(new Scene(root));
                                stage.show();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });

                    // Création du bouton "Delete"
                    Button deleteButton = new Button("Delete");

                    // Action pour le bouton "Delete"
                    deleteButton.setOnAction(event -> {
                        medicament selectedMedicament = getItem();
                        if (selectedMedicament != null) {
                            // Supprimer le médicament de la base de données
                            MedicamentServices medicamentServices = new MedicamentServices();
                            medicamentServices.deleteEntity(selectedMedicament);
                            // Supprimer le médicament de la liste observable
                            medicaments.remove(selectedMedicament);
                        }
                    });

                    // Ajout des éléments dans un VBox
                    VBox detailsVBox = new VBox(referenceLabel, stockLabel, disponibiliteLabel, descriptionLabel, prixLabel);
                    HBox buttonsHBox = new HBox(editButton, deleteButton);

                    // Ajouter un espacement horizontal flexible entre les boutons
                    Region spacer = new Region();
                    HBox.setHgrow(spacer, Priority.ALWAYS);

                    // Ajouter les éléments dans un HBox
                    HBox cellBox = new HBox(detailsVBox, spacer, buttonsHBox);
                    setGraphic(cellBox);

                    // Définition du texte pour afficher les détails du médicament
                    setText(null);
                }
            }
        });
    }

    @FXML
    void ajoutmedicament(ActionEvent event) {
        loadFXML("/AjouterMedicament.fxml", event);
    }

    @FXML
    void retourBack(ActionEvent event) {
        loadFXML("/AfficheUser.fxml", event);
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
}
