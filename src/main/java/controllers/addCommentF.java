package controllers;

import entities.Comments;
import entities.Posts;
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
import services.CommentsServices;
import services.PostsServices;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;

public class addCommentF {

    @FXML
    private TextArea comment_contenu;

    @FXML
    private RadioButton comment_commentedAs_username;

    @FXML
    private ToggleGroup commentedAs;

    @FXML
    private RadioButton comment_commentedAs_anonyme;

    @FXML
    private Button annulerButton;

    private final CommentsServices cs = new CommentsServices();


    private Posts post; // Store the selected post

    // Add a private field to store the stage
    private Stage stage;

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

    private ObservableList<Comments> commentList = FXCollections.observableArrayList();

    private TableView<Comments> commentTableView;


    // Initialize postsList
    private ObservableList<Comments> CommentList = FXCollections.observableArrayList();
    public void setCommentTableView(TableView<Comments> commentTableView) {
        this.commentTableView = commentTableView;
    }

    // Add a method to initialize postsList
    public void setCommentList(ObservableList<Comments> commentList) {
        this.commentList = commentList;
    }

    // Add a method to clear and repopulate the TableView with posts
    private void refreshTableView() throws SQLException {
        // Clear the existing items in the TableView
        commentTableView.getItems().clear();

        // Retrieve posts from the database
        commentList.addAll(cs.getCommentsByPost(post));

        // Populate the TableView with posts
        commentTableView.setItems(commentList);
    }

    // Method to set the selected post
    public void setPost(Posts post) {
        this.post = post;
    }

    // Method to handle adding a new comment
    @FXML
    private void publier(ActionEvent event) throws SQLException {
        String contenu = comment_contenu.getText().trim();
        if (contenu.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Le contenu du commentaire ne peut pas être vide.");
            return;
        }

        // Determine the commentedAs value based on the selected RadioButton
        String commentedAs = comment_commentedAs_anonyme.isSelected() ? "Anonyme" : "Username";

        // Create a new instance of Comments with the provided data
        java.sql.Timestamp now = new java.sql.Timestamp(System.currentTimeMillis());
        Comments comment = new Comments(contenu, now, now, commentedAs, post);

        // Add the new comment
        CommentsServices commentsServices = new CommentsServices();
        try {
            commentsServices.add(comment);
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Commentaire ajouté avec succès.");
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de l'ajout du commentaire: " + e.getMessage());
        }

        // Refresh the TableView with the updated data
        refreshTableView();

        // Close the add comment window
        Stage stage = (Stage) comment_contenu.getScene().getWindow();
        stage.close();
    }


    // Method to show an alert dialog
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
