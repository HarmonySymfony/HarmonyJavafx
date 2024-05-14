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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Screen;
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
    private WebView webView;

    @FXML
    private void initialize() {
        afficherUtilisateurs();
        // Récupérer la taille de l'écran
        double screenWidth = Screen.getPrimary().getVisualBounds().getWidth();
        double screenHeight = Screen.getPrimary().getVisualBounds().getHeight();

        // Lier la taille de la WebView à la taille de l'écran
        webView.setPrefWidth(screenWidth);
        webView.setPrefHeight(screenHeight);

        // Charger le fichier HTML avec le fond animé
        WebEngine webEngine = webView.getEngine();

        // Charger le fichier HTML contenant la carte Google Maps
        webEngine.load(getClass().getResource("/HTML/indexBACK.html").toExternalForm()); }

    private void afficherUtilisateurs() {
        // Récupérer les données des utilisateurs depuis le service
        List<Personne> utilisateurs = us.getAllData();

        // Parcourir la liste des utilisateurs et les ajouter à la GridPane
        int row = 0;
        for (Personne utilisateur : utilisateurs) {
            // Créer des composants d'interface utilisateur pour afficher les détails de l'utilisateur
            Label nomLabel = new Label("Nom: " + utilisateur.getNom());
            Label prenomLabel = new Label("Prénom: " + utilisateur.getPrenom());
            Label emailLabel = new Label("Email: " + utilisateur.getEmail());
            Label ageLabel = new Label("Age: " + utilisateur.getAge()); // Convert age to String
            Label roleLabel = new Label("Role: " + utilisateur.getRole());

            Button supprimerButton = new Button("Supprimer");
            supprimerButton.setOnAction(event -> supprimerUtilisateur(utilisateur));
            Button editButton = new Button("Edit");
            editButton.setOnAction(event -> editUtilisateur(utilisateur));

            // Ajouter les composants à la GridPane
            grid.add(nomLabel, 0, row);
            grid.add(prenomLabel, 1, row);
            grid.add(emailLabel, 2, row);
            grid.add(ageLabel, 3, row); // Add age label to the grid
            grid.add(roleLabel, 4, row); // Add role label to the grid
            grid.add(supprimerButton, 5, row);
            grid.add(editButton, 6, row); // Adjust column index for edit button

            // Incrémenter le numéro de ligne
            row++;
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
                utilisateurs = us.getAllData();
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
            controllers.EditUserController editUserController = loader.getController();
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
            root = FXMLLoader.load(getClass().getResource("/IndexPharmacie.fxml"));
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
    @FXML
    void evenement(ActionEvent event) {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/event.fxml"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();

    }


    @FXML
    void listeCabinets(ActionEvent event) {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/SAHTEK.fxml"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();

    }
    @FXML
    void laboratoires(ActionEvent event) {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/AfiicherLaboratoire.fxml"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();

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


