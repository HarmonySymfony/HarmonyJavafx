package controllers;

import entites.Rendezvous;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import services.Rendezvousservices;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;


import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Callback;
import services.Rendezvousservices;


import java.io.IOException;
import java.time.LocalDate;
import java.time.YearMonth;

public class CalendarRDV implements Initializable {
    private Rendezvousservices rendezvousServices = new Rendezvousservices();
    private ObservableList<Rendezvous> rendezvousList = FXCollections.observableArrayList();



    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button AllerVersFormulaire;

    @FXML
    private Button closeclendar;
    @FXML
    private Label anneeLabel;

    @FXML
    private Button anneePrecedenteButton;

    @FXML
    private Button anneeSuivanteButton;

    @FXML
    private GridPane calendrierGridPane;

    @FXML
    private Label moisAnneeLabel;

    @FXML
    private Label moisLabel;

    @FXML
    private Button moisPrecedentButton;
    @FXML
    private Button moisSuivanttButton;
    @FXML
    private ListView<Rendezvous> listcalentRDV;

    private int currentYear;
    private int currentMonth;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        assert AllerVersFormulaire != null : "fx:id=\"AllerVersFormulaire\" was not injected: check your FXML file 'CalendarRDV.fxml'.";
        assert anneeLabel != null : "fx:id=\"anneeLabel\" was not injected: check your FXML file 'CalendarRDV.fxml'.";
        assert anneePrecedenteButton != null : "fx:id=\"anneePrecedenteButton\" was not injected: check your FXML file 'CalendarRDV.fxml'.";
        assert anneeSuivanteButton != null : "fx:id=\"anneeSuivanteButton\" was not injected: check your FXML file 'CalendarRDV.fxml'.";
        assert calendrierGridPane != null : "fx:id=\"calendrierGridPane\" was not injected: check your FXML file 'CalendarRDV.fxml'.";
        assert moisAnneeLabel != null : "fx:id=\"moisAnneeLabel\" was not injected: check your FXML file 'CalendarRDV.fxml'.";
        assert moisLabel != null : "fx:id=\"moisLabel\" was not injected: check your FXML file 'CalendarRDV.fxml'.";
        assert moisPrecedentButton != null : "fx:id=\"moisPrecedentButton\" was not injected: check your FXML file 'CalendarRDV.fxml'.";
        assert moisSuivanttButton != null : "fx:id=\"moisSuivanttButton\" was not injected: check your FXML file 'CalendarRDV.fxml'.";
        assert closeclendar != null : "fx:id=\"closeclendar\" was not injected: check your FXML file 'CalendarRDV.fxml'.";

        assert listcalentRDV != null : "fx:id=\"listcalentRDV\" was not injected: check your FXML file 'CalendarRDV.fxml'.";
        configureListView();
        afficherRendezVous();
        LocalDate currentDate = LocalDate.now();
        currentYear = currentDate.getYear();
        currentMonth = currentDate.getMonthValue();
        afficherCalendrier(currentYear, currentMonth);


        updateMonthLabel(currentMonth);
        updateYearLabel(currentYear);
    }



    @FXML
    public void close() {
        Stage stage = (Stage) closeclendar.getScene().getWindow();
        stage.close();
    }
    // Mettez à jour l'étiquette du mois avec le mois sélectionné
    private void updateMonthLabel(int month) {
        String[] months = {"Janvier", "Février", "Mars", "Avril", "Mai", "Juin", "Juillet", "Août", "Septembre", "Octobre", "Novembre", "Décembre"};
        moisLabel.setText(months[month - 1]);
    }

    // Mettez à jour l'étiquette de l'année avec l'année sélectionnée
    private void updateYearLabel(int year) {
        anneeLabel.setText(Integer.toString(year));
    }


    private void configureListView() {
        // Création d'une cellule personnalisée pour afficher les rendez-vous dans la ListView
        listcalentRDV.setCellFactory(param -> new ListCell<Rendezvous>() {
            @Override
            protected void updateItem(Rendezvous rendezvous, boolean empty) {
                super.updateItem(rendezvous, empty);

                if (empty || rendezvous == null) {
                    setText(null);
                } else {
                    // Créer une chaîne représentant les détails du rendez-vous
                    String rendezvousDetails =
                            " Date: " + rendezvous.getDate() +
                                    "\n prenom: " + rendezvous.getPrenom() +
                                    "\n Nom de l'utilisateur: " + rendezvous.getNom() +
                                    "\n Email de l'utilisateur: " + rendezvous.getEmail() ;

                    setText(rendezvousDetails);
                }
            }
        });

        // Charger les données de rendez-vous dans la ListView
        List<Rendezvous> rendezvousList = rendezvousServices.getAllData();
        ObservableList<Rendezvous> observableRendezvousList = FXCollections.observableArrayList(rendezvousList);
        listcalentRDV.setItems(observableRendezvousList);
    }

    // Factory pour créer les cellules de boutons Modifier
    private Callback<TableColumn<Rendezvous, Void>, TableCell<Rendezvous, Void>> cellFactoryModifier = new Callback<>() {
        @Override
        public TableCell<Rendezvous, Void> call(final TableColumn<Rendezvous, Void> param) {
            final TableCell<Rendezvous, Void> cell = new TableCell<>() {
                private final Button modifierButton = new Button("Modifier");

                {
                    modifierButton.setStyle("-fx-background-color: #69BFA7; -fx-text-fill: #F2F2F2;");
                    modifierButton.setOnAction(event -> {
                        Rendezvous rdv = getTableView().getItems().get(getIndex());

                    });
                }

                @Override
                public void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        setGraphic(modifierButton);
                    }
                }
            };
            return cell;
        }
    };

    // Factory pour créer les cellules de boutons Supprimer
    private Callback<TableColumn<Rendezvous, Void>, TableCell<Rendezvous, Void>> cellFactorySupprimer = new Callback<>() {
        @Override
        public TableCell<Rendezvous, Void> call(final TableColumn<Rendezvous, Void> param) {
            final TableCell<Rendezvous, Void> cell = new TableCell<>() {
                private final Button supprimerButton = new Button("Annuler");

                {
                    supprimerButton.setStyle("-fx-background-color: #69BFA7; -fx-text-fill: #F2F2F2;");
                    supprimerButton.setOnAction(event -> {
                        Rendezvous rdv = getTableView().getItems().get(getIndex());
                       // RendezvousServices.deleteRendezvous(Rendezvous);
                        afficherRendezVous();
                    });
                }

                @Override
                public void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        setGraphic(supprimerButton);
                    }
                }
            };
            return cell;
        }
    };

    private void afficherRendezVous() {


    }


    private void redirectToRdvAdd() {
        try {
            // Charger le fichier FXML de rdvAdd.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/rdvAdd.fxml"));
            Parent root = loader.load();

            // Créer une nouvelle scène
            Scene scene = new Scene(root);

            // Obtenir la fenêtre principale et la modifier pour afficher la nouvelle scène
            Stage primaryStage = (Stage) AllerVersFormulaire.getScene().getWindow();
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void afficherCalendrier(int year, int month) {
        calendrierGridPane.getChildren().clear();
        YearMonth yearMonth = YearMonth.of(year, month);
        int joursDansMois = yearMonth.lengthOfMonth();
        int jourDebutMois = yearMonth.atDay(1).getDayOfWeek().getValue(); // Jour de la semaine du premier jour du mois

        String[] joursSemaine = {"Lun", "Mar", "Mer", "Jeu", "Ven", "Sam", "Dim"};
        for (int i = 0; i < 7; i++) {
            Label labelJourSemaine = new Label(joursSemaine[i]);
            labelJourSemaine.setStyle("-fx-font-weight: bold");
            calendrierGridPane.add(labelJourSemaine, i, 0);
        }

        int row = 1;
        int jourCourant = 1;

        while (jourCourant <= joursDansMois) {
            for (int col = 0; col < 7; col++) {
                Button btnJour = new Button(Integer.toString(jourCourant));
                btnJour.setStyle("-fx-background-color: linear-gradient(#73AA18, #73AA18); -fx-text-fill: #F2F2F2; -fx-font-size: 14px;");
                btnJour.setPrefWidth(40);
                btnJour.setPrefHeight(40);

                if (hasRendezVous(year, month, jourCourant)) {
                    Circle circle = new Circle(4, Color.RED); // Ajustez le rayon ici (par exemple, 2.5)
                    StackPane stack = new StackPane(btnJour, circle);
                    StackPane.setAlignment(circle, Pos.TOP_RIGHT);
                    calendrierGridPane.add(stack, col, row);
                } else {
                    calendrierGridPane.add(btnJour, col, row);
                }

                final int jourSelectionne = jourCourant;
                btnJour.setOnAction(event -> {
                    afficherRendezVousDuJour(year, month, jourSelectionne);
                });

                jourCourant++;

                if (jourCourant > joursDansMois) {
                    break;
                }
            }
            row++;
        }
    }


    private void afficherRendezVousDuJour(int annee, int mois, int jour) {
        // Construire la date sélectionnée
        LocalDate dateSelectionnee = LocalDate.of(annee, mois, jour);

        // Récupérer les rendez-vous pour la date sélectionnée
        List<Rendezvous> rdvs = rendezvousServices.getRdvByDate(dateSelectionnee);

        // Effacer la TableView actuelle
        listcalentRDV.getItems().clear();

        // Afficher un message en fonction de la présence de rendez-vous
        if (rdvs.isEmpty()) {
            // Afficher une boîte de dialogue avec le message approprié
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Aucun rendez-vous");
            alert.setHeaderText(null);
            alert.setContentText("Aucun rendez-vous n'est pris pour cette date.");
            alert.showAndWait();
        } else {
            // Ajouter les rendez-vous à la TableView
            listcalentRDV.getItems().addAll(rdvs);

            // Créer un menu contextuel pour les options de modification
            ContextMenu contextMenu = new ContextMenu();
            MenuItem modifierMenuItem = new MenuItem("Modifier");
            contextMenu.getItems().add(modifierMenuItem);

// Ajouter un événement de souris pour détecter le clic droit et afficher le menu contextuel
            listcalentRDV.setOnMouseClicked(event -> {
                if (event.getButton() == MouseButton.SECONDARY) { // Vérifier si le clic est le clic droit
                    // Afficher le menu contextuel au position du clic
                    contextMenu.show(listcalentRDV, event.getScreenX(), event.getScreenY());
                }
            });

// Ajouter un gestionnaire d'événements pour l'élément de menu "Modifier"
            modifierMenuItem.setOnAction(event -> {
                // Récupérer le rendez-vous sélectionné
                Rendezvous selectedRdv = listcalentRDV.getSelectionModel().getSelectedItem();
                if (selectedRdv != null) {
                    // Afficher une boîte de dialogue ou une nouvelle vue pour modifier la date du rendez-vous
                    modifierDateRendezVous(selectedRdv);
                }
            });


        }
    }

    private void modifierDateRendezVous(Rendezvous rendezvous) {
        // Afficher une boîte de dialogue ou une nouvelle vue pour modifier la date du rendez-vous
        // Vous pouvez utiliser un FXMLLoader pour charger une vue FXML pour la modification
        // Ou créer une boîte de dialogue personnalisée pour la modification de la date
        // Par exemple :
        // FXMLLoader loader = new FXMLLoader(getClass().getResource("ModifyRdvDate.fxml"));
        // Parent root = loader.load();
        // ModifierRdvDateController controller = loader.getController();
        // controller.setRendezVous(rendezvous);
        // Stage stage = new Stage();
        // stage.setScene(new Scene(root));
        // stage.show();
    }


    @FXML
    private void moisPrecedent() {
        currentMonth--;
        if (currentMonth == 0) {
            currentMonth = 12;
            currentYear--;
        }
        afficherCalendrier(currentYear, currentMonth);
        updateMonthLabel(currentMonth);
        afficherRendezVous();
    }

    @FXML
    private void moisSuivant() {
        currentMonth++;
        if (currentMonth == 13) {
            currentMonth = 1;
            currentYear++;
        }
        afficherCalendrier(currentYear, currentMonth);
        updateMonthLabel(currentMonth);
        afficherRendezVous();
    }

    @FXML
    private void anneePrecedente() {
        currentYear--;
        afficherCalendrier(currentYear, currentMonth);
        updateYearLabel(currentYear);
        afficherRendezVous();
    }

    @FXML
    private void anneeSuivante() {
        currentYear++;
        afficherCalendrier(currentYear, currentMonth);
        updateYearLabel(currentYear);
        afficherRendezVous();
    }
    private boolean hasRendezVous(int year, int month, int day) {
        LocalDate date = LocalDate.of(year, month, day);
        Rendezvousservices rendezvousServices = new Rendezvousservices();
        List<Rendezvous> rdvs = rendezvousServices.getRdvByDate(date);
        return !rdvs.isEmpty();
    }



}


