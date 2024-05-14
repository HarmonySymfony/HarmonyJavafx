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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Screen;
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
    private entities.pharmacie pharmacie;
    @FXML
    private WebView webView;

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
        // Récupérer la taille de l'écran
        double screenWidth = Screen.getPrimary().getVisualBounds().getWidth();
        double screenHeight = Screen.getPrimary().getVisualBounds().getHeight();

        // Lier la taille de la WebView à la taille de l'écran
        webView.setPrefWidth(screenWidth);
        webView.setPrefHeight(screenHeight);

        // Charger le fichier HTML avec le fond animé
        WebEngine webEngine = webView.getEngine();

        // Charger le fichier HTML contenant la carte Google Maps
        webEngine.load(getClass().getResource("/HTML/indexBACK.html").toExternalForm());
    }

    @FXML
    void ajoutmedicament(ActionEvent event) {
        loadFXML("/AjouterMedicament.fxml", event);
    }

    @FXML
    void retourBack(ActionEvent event) {
        loadFXML("/IndexPharmacie.fxml", event);
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

    public void setListePharmacies(ListView<entities.pharmacie> listepharmacie) {
    }
    @FXML
    void AnalyseTable(MouseEvent event) {
        try {
            // Charger le fichier FXML AfficheUser.fxml
            Parent root = FXMLLoader.load(getClass().getResource("/afficherAnalyse.fxml"));

            // Créer une nouvelle scène avec le contenu chargé
            Scene scene = new Scene(root);

            // Obtenir la fenêtre actuelle
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Mettre la nouvelle scène dans la fenêtre
            stage.setScene(scene);

            // Afficher la fenêtre
            stage.show();
        } catch (IOException e) {
            // Gérer les erreurs de chargement du fichier FXML
            throw new RuntimeException(e);
        }

    }

    @FXML
    void CabinetTable(MouseEvent event) {
        try {
            // Charger le fichier FXML AfficheUser.fxml
            Parent root = FXMLLoader.load(getClass().getResource("/SAHTEK.fxml"));

            // Créer une nouvelle scène avec le contenu chargé
            Scene scene = new Scene(root);

            // Obtenir la fenêtre actuelle
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Mettre la nouvelle scène dans la fenêtre
            stage.setScene(scene);

            // Afficher la fenêtre
            stage.show();
        } catch (IOException e) {
            // Gérer les erreurs de chargement du fichier FXML
            throw new RuntimeException(e);
        }

    }

    @FXML
    void EventTable(MouseEvent event) {
        try {
            // Charger le fichier FXML AfficheUser.fxml
            Parent root = FXMLLoader.load(getClass().getResource("/event.fxml"));

            // Créer une nouvelle scène avec le contenu chargé
            Scene scene = new Scene(root);

            // Obtenir la fenêtre actuelle
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Mettre la nouvelle scène dans la fenêtre
            stage.setScene(scene);

            // Afficher la fenêtre
            stage.show();
        } catch (IOException e) {
            // Gérer les erreurs de chargement du fichier FXML
            throw new RuntimeException(e);
        }

    }

    @FXML
    void LaboratoireTable(MouseEvent event) {
        try {
            // Charger le fichier FXML AfficheUser.fxml
            Parent root = FXMLLoader.load(getClass().getResource("/AfiicherLaboratoire.fxml"));

            // Créer une nouvelle scène avec le contenu chargé
            Scene scene = new Scene(root);

            // Obtenir la fenêtre actuelle
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Mettre la nouvelle scène dans la fenêtre
            stage.setScene(scene);

            // Afficher la fenêtre
            stage.show();
        } catch (IOException e) {
            // Gérer les erreurs de chargement du fichier FXML
            throw new RuntimeException(e);
        }

    }

    @FXML
    void LoginTable(MouseEvent event) {
        try {
            // Charger le fichier FXML AfficheUser.fxml
            Parent root = FXMLLoader.load(getClass().getResource("/Login.fxml"));

            // Créer une nouvelle scène avec le contenu chargé
            Scene scene = new Scene(root);

            // Obtenir la fenêtre actuelle
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Mettre la nouvelle scène dans la fenêtre
            stage.setScene(scene);

            // Afficher la fenêtre
            stage.show();
        } catch (IOException e) {
            // Gérer les erreurs de chargement du fichier FXML
            throw new RuntimeException(e);
        }

    }

    @FXML
    void MedicTable(MouseEvent event) {
        try {
            // Charger le fichier FXML AfficheUser.fxml
            Parent root = FXMLLoader.load(getClass().getResource("/IndexMedicament.fxml"));

            // Créer une nouvelle scène avec le contenu chargé
            Scene scene = new Scene(root);

            // Obtenir la fenêtre actuelle
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Mettre la nouvelle scène dans la fenêtre
            stage.setScene(scene);

            // Afficher la fenêtre
            stage.show();
        } catch (IOException e) {
            // Gérer les erreurs de chargement du fichier FXML
            throw new RuntimeException(e);
        }

    }

    @FXML
    void PharmacieTable(MouseEvent event) {
        try {
            // Charger le fichier FXML AfficheUser.fxml
            Parent root = FXMLLoader.load(getClass().getResource("/IndexPharmacie.fxml"));

            // Créer une nouvelle scène avec le contenu chargé
            Scene scene = new Scene(root);

            // Obtenir la fenêtre actuelle
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Mettre la nouvelle scène dans la fenêtre
            stage.setScene(scene);

            // Afficher la fenêtre
            stage.show();
        } catch (IOException e) {
            // Gérer les erreurs de chargement du fichier FXML
            throw new RuntimeException(e);
        }

    }

    @FXML
    void PostsTable(MouseEvent event) {
        try {
            // Charger le fichier FXML AfficheUser.fxml
            Parent root = FXMLLoader.load(getClass().getResource("/indexPost.fxml"));

            // Créer une nouvelle scène avec le contenu chargé
            Scene scene = new Scene(root);

            // Obtenir la fenêtre actuelle
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Mettre la nouvelle scène dans la fenêtre
            stage.setScene(scene);

            // Afficher la fenêtre
            stage.show();
        } catch (IOException e) {
            // Gérer les erreurs de chargement du fichier FXML
            throw new RuntimeException(e);
        }

    }

    @FXML
    void UsersTable(MouseEvent event) {
        try {
            // Charger le fichier FXML AfficheUser.fxml
            Parent root = FXMLLoader.load(getClass().getResource("/AfficheUser.fxml"));

            // Créer une nouvelle scène avec le contenu chargé
            Scene scene = new Scene(root);

            // Obtenir la fenêtre actuelle
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Mettre la nouvelle scène dans la fenêtre
            stage.setScene(scene);

            // Afficher la fenêtre
            stage.show();
        } catch (IOException e) {
            // Gérer les erreurs de chargement du fichier FXML
            throw new RuntimeException(e);
        }

    }

}
