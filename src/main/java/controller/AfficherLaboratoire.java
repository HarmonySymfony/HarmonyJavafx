package controller;

import entities.Laboratoire;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Pair;
import services.LaboratoireService;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class AfficherLaboratoire {
    public Button ajouterLaboratoire;
    public TableColumn<Laboratoire,Void> actionsColumn;
    @FXML
    private ResourceBundle resources;
    @FXML
    private URL location;
    @FXML
    TableView<Laboratoire> laboratoireTable;
    @FXML
    TableColumn<Laboratoire,Integer> idColumn;
    @FXML
    TableColumn<Laboratoire,String> nomColumn;
    @FXML
    TableColumn<Laboratoire,String> emplacementColumn;
   
    LaboratoireService laboratoireService = new LaboratoireService();
    @FXML
    void initialize(){
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nomColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));
        emplacementColumn.setCellValueFactory(new PropertyValueFactory<>("emplacement"));
        initializeActionsColumn();
        afficherLaboratoire();

    }
    private void afficherLaboratoire(){
        ObservableList<Laboratoire> laboratoires = null;
        try {
            laboratoires = FXCollections.observableList(laboratoireService.Display());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        laboratoireTable.setItems(laboratoires);

    }



    public void addLaboratoire(javafx.event.ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ajouterLaboratoire.fxml"));
            Parent root = fxmlLoader.load();
            AjouterLaboratoire controller = fxmlLoader.getController();
            controller.initLaboratoireService(laboratoireService); // Pass the AdminService to the controller
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
            private final HBox container = new HBox(editButton, deleteButton);

            {
                editButton.setStyle("-fx-background-color: #1554b3; -fx-text-fill: white; -fx-min-width: 40px; -fx-font-size: 12px;");
                deleteButton.setStyle("-fx-background-color: #1554b3; -fx-text-fill: white; -fx-min-width: 40px; -fx-font-size: 12px;");

                editButton.setOnAction(event -> {
                    // Handle edit action
                    handleEditButton();
                });

                deleteButton.setOnAction(event -> {
                    handleDeleteButton();

                });
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
        // Get the selected student from the table
        Laboratoire selectedLab = laboratoireTable.getSelectionModel().getSelectedItem();

        if (selectedLab != null) {
            // Create an instance of the EditStudentPopup dialog
            EditLaboratoirepopup editStudentPopup = new EditLaboratoirepopup(selectedLab);

            // Show the dialog and wait for the user response
            Optional<Pair<Laboratoire, String>> result = editStudentPopup.showAndWait();

            // If the user clicked the Save button, update the student information
            result.ifPresent(pair -> {
                Laboratoire updatedLab = pair.getKey();
                String newNom = pair.getValue();

                // Update the selected student with the new information
                selectedLab.setNom(newNom);
                selectedLab.setEmplacement(updatedLab.getEmplacement());

                try {
                    // Update the changes in the database using the StudentService
                    laboratoireService.update(selectedLab);
                } catch (SQLException e) {
                    e.printStackTrace();
                    // Handle the exception
                }

                // Refresh the TableView to reflect the changes
                laboratoireTable.refresh();
            });
        } else {
            // No student selected, display an error message or handle accordingly
            System.out.println("No laboratoire selected for editing.");
        }
    }
    @FXML
    private void handleDeleteButton() {
        // Get the selected student from the table
        Laboratoire selectedLab = laboratoireTable.getSelectionModel().getSelectedItem();

        if (selectedLab != null) {
            // Create a confirmation dialog
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Deletion");
            alert.setHeaderText("Delete Lab");
            alert.setContentText("Are you sure you want to delete " + selectedLab.getNom());
            // Add OK and Cancel buttons to the dialog
            ButtonType okButton = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
            ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
            alert.getButtonTypes().setAll(okButton, cancelButton);

            // Show the confirmation dialog and wait for user response
            Optional<ButtonType> result = alert.showAndWait();

            // If the user confirms deletion, proceed with deletion
            if (result.isPresent() && result.get() == okButton) {
                try {
                    // Delete the selected student from the database using the StudentService
                    laboratoireService.delete(selectedLab);

                    // Remove the selected student from the TableView
                    laboratoireTable.getItems().remove(selectedLab);
                } catch (SQLException e) {
                    e.printStackTrace();
                    // Handle the exception
                }
            }
        } else {
            // No student selected, display an error message or handle accordingly
            System.out.println("No lab selected for deletion.");
        }
    }
}
