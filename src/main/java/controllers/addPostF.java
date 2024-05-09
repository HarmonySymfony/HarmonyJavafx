package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Screen;
import javafx.stage.Stage;
import entities.Posts;
import services.PostsServices;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class addPostF {

    private final PostsServices ps = new PostsServices();

    public Button annulerButton;
    @FXML
    private TextArea post_contenu; // TextArea for entering post content

    @FXML
    private RadioButton post_postedAs_anonyme; // RadioButton for posting anonymously

    @FXML
    private RadioButton post_postedAs_username; // RadioButton for posting with a username

    @FXML
    private ToggleGroup postedAs; // ToggleGroup to group the RadioButtons

    // Add a private field to store the stage
    private Stage stage;

    // Add a boolean variable to track whether a post was successfully added
    private boolean postAdded = false;

    @FXML
    private WebView webView;

    @FXML
    public void initialize() {// Récupérer la taille de l'écran
        double screenWidth = Screen.getPrimary().getVisualBounds().getWidth();
        double screenHeight = Screen.getPrimary().getVisualBounds().getHeight();

        // Lier la taille de la WebView à la taille de l'écran
        webView.setPrefWidth(screenWidth);
        webView.setPrefHeight(screenHeight);

        // Charger le fichier HTML avec le fond animé
        WebEngine webEngine = webView.getEngine();

        // Charger le fichier HTML contenant la carte Google Maps
        webEngine.load(getClass().getResource("/HTML/index.html").toExternalForm());}

    // Getter method for postAdded
    public boolean isPostAdded() {
        return postAdded;
    }

    // Setter method for stage
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    private Stage indexStage;

    public void setIndexStage(Stage indexStage) {
        this.indexStage = indexStage;
    }

    public Stage getIndexStage() {
        return indexStage;
    }

    private TableView<Posts> postTableView;

    // Initialize postsList
    private ObservableList<Posts> postsList = FXCollections.observableArrayList();
    public void setPostTableView(TableView<Posts> postTableView) {
        this.postTableView = postTableView;
    }

    // Add a method to initialize postsList
    public void setPostsList(ObservableList<Posts> postsList) {
        this.postsList = postsList;
    }

    // Add a method to clear and repopulate the TableView with posts
    private void refreshTableView() throws SQLException {
        // Clear the existing items in the TableView
        postTableView.getItems().clear();

        // Retrieve posts from the database
        postsList.addAll(ps.getAll());

        // Populate the TableView with posts
        postTableView.setItems(postsList);
    }
    // Define a list of forbidden words
    // Define the list of forbidden words in English
    private final List<String> forbiddenWordsEnglish = Arrays.asList(
            "fuck", "shit", "asshole", "bitch", "cunt",
            "dick", "bastard", "motherfucker", "ass", "cock"
    );

    // Define the list of forbidden words in French
    private final List<String> forbiddenWordsFrench = Arrays.asList(
            "putain", "merde", "connard", "salope", "enculé",
            "bite", "enculée", "con", "bordel", "chier"
    );



    @FXML
    void publier(ActionEvent event) throws SQLException {
        // Get the content of the post from the TextArea
        String contenu = post_contenu.getText().trim(); // Trim to remove leading and trailing whitespace

        // Validate the content
        if (contenu.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Le contenu ne peut pas être vide.");
            return;
        }

        // Ensure the content matches the allowed pattern
        if (!contenu.matches("^[a-zA-Z0-9_\\s!?.,;:]*$")) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Seules les lettres, les chiffres, les traits de soulignement et les espaces sont autorisés.");
            return;
        }

        // Check if the content contains any forbidden words
        if (containsForbiddenWord(contenu)) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Le contenu contient des mots interdits.");
            return;
        }

        // Determine the postedAs value based on the selected RadioButton
        String postedAs = post_postedAs_anonyme.isSelected() ? "Anonyme" : "Username";

        // Create a new instance of Posts with the provided data
        java.sql.Timestamp now = new java.sql.Timestamp(System.currentTimeMillis());
        entities.Posts posts = new entities.Posts(contenu, now, null, postedAs, new ArrayList<>());

        // Add the new post
        ps.add(posts);

        showAlert(Alert.AlertType.INFORMATION, "Succès", "Post ajouté avec succès.");

        // Refresh the TableView with the updated data
        refreshTableView();

        // Close the add window after deleting the post
        Stage stage = (Stage) post_contenu.getScene().getWindow();
        stage.close();
        // Set postAdded to true if a new post was successfully added
        postAdded = true;
    }

    // Method to check if the content contains any forbidden words
    private boolean containsForbiddenWord(String content) {
        // Convert the content to lowercase for case-insensitive comparison
        String lowercaseContent = content.toLowerCase();

        // Check if any forbidden word is present in the content
        for (String word : forbiddenWordsEnglish) {
            if (lowercaseContent.contains(word)) {
                return true;
            }
        }

        for (String word : forbiddenWordsFrench) {
            if (lowercaseContent.contains(word)) {
                return true;
            }
        }

        return false;
    }

    @FXML
    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    private void annuler(ActionEvent event) {
        Stage stage = (Stage) annulerButton.getScene().getWindow();
        stage.close();
    }


}
