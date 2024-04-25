package controller;

import entities.Analyse;
import entities.Laboratoire;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Pair;
import services.AnalyseService;
import services.LaboratoireService;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class AfficherLaboratoire {
    @FXML
    private ResourceBundle resources;
    @FXML
    private URL location;
    @FXML
    private TableView<Laboratoire> laboratoireTable;
    @FXML
    private TableColumn<Laboratoire, Integer> idColumn;
    @FXML
    private TableColumn<Laboratoire, String> nomColumn;
    @FXML
    private TableColumn<Laboratoire, String> emplacementColumn;
    @FXML
    private TableColumn<Laboratoire, Void> actionsColumn;

    private LaboratoireService laboratoireService = new LaboratoireService();
    private AnalyseService analyseService = new AnalyseService();

    @FXML
    void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nomColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));
        emplacementColumn.setCellValueFactory(new PropertyValueFactory<>("emplacement"));
        initializeActionsColumn();
        afficherLaboratoire();
    }

    private void afficherLaboratoire() {
        ObservableList<Laboratoire> laboratoires = null;
        try {
            laboratoires = FXCollections.observableList(laboratoireService.Display());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        laboratoireTable.setItems(laboratoires);
    }

    public void addLaboratoire(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ajouterLaboratoire.fxml"));
            Parent root = fxmlLoader.load();
            AjouterLaboratoire controller = fxmlLoader.getController();
            controller.initLaboratoireService(laboratoireService);
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception
        }
    }

    private void initializeActionsColumn() {
        actionsColumn.setCellFactory(param -> new TableCell<>() {
            private final Button editButton = new Button("Edit");
            private final Button deleteButton = new Button("Delete");
            private final Button viewAnalysesButton = new Button("Voir Analyses");

            private final HBox container = new HBox(editButton, deleteButton, viewAnalysesButton);

            {
                editButton.setStyle("-fx-background-color: #1554b3; -fx-text-fill: white; -fx-min-width: 40px; -fx-font-size: 12px;");
                deleteButton.setStyle("-fx-background-color: #1554b3; -fx-text-fill: white; -fx-min-width: 40px; -fx-font-size: 12px;");

                editButton.setOnAction(event -> handleEditButton());
                deleteButton.setOnAction(event -> handleDeleteButton());
                viewAnalysesButton.setOnAction(event -> handleViewAnalysesButton());
            }

            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(container);
                }
            }
        });
    }

    @FXML
    private void handleEditButton() {
        Laboratoire selectedLab = laboratoireTable.getSelectionModel().getSelectedItem();
        if (selectedLab != null) {
            EditLaboratoirepopup editStudentPopup = new EditLaboratoirepopup(selectedLab);
            Optional<Pair<Laboratoire, String>> result = editStudentPopup.showAndWait();
            result.ifPresent(pair -> {
                Laboratoire updatedLab = pair.getKey();
                String newNom = pair.getValue();
                selectedLab.setNom(newNom);
                selectedLab.setEmplacement(updatedLab.getEmplacement());
                try {
                    laboratoireService.update(selectedLab);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                laboratoireTable.refresh();
            });
        } else {
            System.out.println("No laboratoire selected for editing.");
        }
    }

    @FXML
    private void handleDeleteButton() {
        Laboratoire selectedLab = laboratoireTable.getSelectionModel().getSelectedItem();
        if (selectedLab != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Deletion");
            alert.setHeaderText("Delete Lab");
            alert.setContentText("Are you sure you want to delete " + selectedLab.getNom());
            ButtonType okButton = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
            ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
            alert.getButtonTypes().setAll(okButton, cancelButton);
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == okButton) {
                try {
                    laboratoireService.delete(selectedLab);
                    laboratoireTable.getItems().remove(selectedLab);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } else {
            System.out.println("No lab selected for deletion.");
        }
    }

//    @FXML
//    private void handleViewAnalysesButton() {
//        Laboratoire selectedLab = laboratoireTable.getSelectionModel().getSelectedItem();
//        if (selectedLab != null) {
//            List<Analyse> analyses = null;
//            try {
//                analyses = laboratoireService.getAnalysesForLaboratoire(selectedLab.getId());
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//            if (analyses != null) {
//                try {
//                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/afficherAnalyse.fxml"));
//                    Parent root = fxmlLoader.load();
//                    AfficherAnalyse controller = fxmlLoader.getController();
//                    controller.setAnalyses(analyses);
//                    Stage stage = new Stage();
//                    stage.setScene(new Scene(root));
//                    stage.show();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            } else {
//                System.out.println("Erreur lors de la récupération des analyses pour le laboratoire sélectionné.");
//            }
//        } else {
//            System.out.println("Aucun laboratoire sélectionné pour afficher les analyses.");
//        }
//    }

    @FXML
    private void handleViewAnalysesButton() {
        Laboratoire selectedLab = laboratoireTable.getSelectionModel().getSelectedItem();
        if (selectedLab != null) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/afficherAnalyse.fxml"));
                Parent root = fxmlLoader.load();
                AfficherAnalyse controller = fxmlLoader.getController();
                controller.setAnalyses(selectedLab);
                this.laboratoireTable.getScene().setRoot(root);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        } else {
            System.out.println("Aucun laboratoire sélectionné pour afficher les analyses.");
        }
    }


    public void ajouteranalyse(ActionEvent actionEvent) {
        Laboratoire selectedLab = laboratoireTable.getSelectionModel().getSelectedItem();
        if (selectedLab != null) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ajouterAnalyse.fxml"));
                Parent root = fxmlLoader.load();
                AjouterAnalyse controller = fxmlLoader.getController();
                controller.initLaboratoire(selectedLab.getId());
                Stage stage = new Stage();
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setScene(new Scene(root));
                stage.showAndWait();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("No laboratory selected to add analysis.");
        }
    }
}
