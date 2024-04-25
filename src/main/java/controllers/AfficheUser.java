package controllers;

import entities.Personne;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import services.PersonneServices;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;


public class AfficheUser {

    @FXML
    private TextField cher;

    PersonneServices us = new PersonneServices();
    @FXML
    private GridPane grid;




    @FXML
    private void initialize() {
        afficherUtilisateurs();
    }

    private void afficherUtilisateurs() {
        // Récupérer les données des utilisateurs depuis le service
        try {
            List<Personne> utilisateurs = us.recuperer();

            // Parcourir la liste des utilisateurs et les ajouter à la GridPane
            int row = 0;
            for (Personne utilisateur : utilisateurs) {
                // Créer des composants d'interface utilisateur pour afficher les détails de l'utilisateur
                Label nomLabel = new Label(utilisateur.getNom());
                Label prenomLabel = new Label(utilisateur.getPrenom());
                Label emailLabel = new Label(utilisateur.getEmail());
                Label ageLabel = new Label(String.valueOf(utilisateur.getAge())); // Convert age to String

                Button supprimerButton = new Button("Supprimer");
                supprimerButton.setOnAction(event -> supprimerUtilisateur(utilisateur));
                Button editButton = new Button("Edit");
                editButton.setOnAction(event -> editUtilisateur(utilisateur));

                // Ajouter les composants à la GridPane
                grid.add(nomLabel, 0, row);
                grid.add(prenomLabel, 1, row);
                grid.add(emailLabel, 2, row);
                grid.add(ageLabel, 3, row); // Add age label to the grid
                grid.add(supprimerButton, 4, row);
                grid.add(editButton, 5, row); // Adjust column index for edit button

                // Incrémenter le numéro de ligne
                row++;
            }
        } catch (SQLException e) {
            // Gérer les exceptions SQL
            e.printStackTrace();
        }
    }



    private void supprimerUtilisateur(Personne utilisateur) {
        try {
            us.supprimer(utilisateur);
            // Mettez à jour l'affichage après la suppression si nécessaire
            grid.getChildren().clear(); // Effacer les éléments actuels
            afficherUtilisateurs(); // Réafficher la liste mise à jour
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void Supprimer(ActionEvent event) {

    }


    @FXML
    void chercherkey(ActionEvent event) {
        String prenom = cher.getText().trim(); // Obtenir le prénom à rechercher depuis le champ de texte

        try {
            List<Personne> utilisateurs;

            // Vérifier si le champ de texte est vide
            if (prenom.isEmpty()) {
                // Si le champ de texte est vide, récupérer tous les utilisateurs
                utilisateurs = us.recuperer();
            } else {
                // Sinon, appeler la méthode de service pour rechercher les utilisateurs par prénom
                utilisateurs = us.recupererParPrenom(prenom);
            }

            // Effacer les éléments actuels de la grille
            grid.getChildren().clear();

            // Parcourir la liste des utilisateurs trouvés et les ajouter à la grille
            int row = 0;
            for (Personne utilisateur : utilisateurs) {
                // Créer des composants d'interface utilisateur pour afficher les détails de l'utilisateur
                Label nomLabel = new Label(utilisateur.getNom());
                Label prenomLabel = new Label(utilisateur.getPrenom());
                Label emailLabel = new Label(utilisateur.getEmail());

                Button supprimerButton = new Button("Supprimer");
                supprimerButton.setOnAction(e -> supprimerUtilisateur(utilisateur));

                // Ajouter les composants à la GridPane
                grid.add(nomLabel, 0, row);
                grid.add(prenomLabel, 1, row);
                grid.add(emailLabel, 2, row);
                grid.add(supprimerButton, 3, row);

                // Incrémenter le numéro de ligne
                row++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Gérer les erreurs de recherche
        }
    }


    @FXML
    void retourHome(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Login.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Get the current stage (window)
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        // Set the new scene content
        Scene scene = new Scene(root);
        stage.setTitle("Harmony");
        stage.setScene(scene);
        stage.show();
    }
    private void editUtilisateur(Personne utilisateur) {
        try {
            // Load the FXML file for editing user details
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/EditUser.fxml"));
            Parent root = loader.load();

            // Pass the user information to the controller of the editing window
            EditUserController editUserController = loader.getController();
            editUserController.initData(utilisateur);

            // Show the editing window
            Stage stage = new Stage();
            stage.setTitle("Edit User");
            stage.setScene(new Scene(root));
            stage.showAndWait();

            // Refresh the user list after editing (if needed)
            grid.getChildren().clear();
            afficherUtilisateurs();
        } catch (IOException e) {
            e.printStackTrace();
        }}
    @FXML
    void ListePharmacie(ActionEvent event) {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/ShowPharmacie.fxml"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();

    }


    public void Forum(ActionEvent event) {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/indexPost.fxml"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
}


