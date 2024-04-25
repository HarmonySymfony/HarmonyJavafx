package controllers;

import entities.Comments;
import entities.Posts;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import services.CommentsServices;

import java.sql.SQLException;
import java.util.Date;

public class addComment {

    @FXML
    private TextArea commentTextArea;

    private Posts post; // Store the selected post

    // Method to set the selected post
    public void setPost(Posts post) {
        this.post = post;
    }

    // Method to handle adding a new comment
    @FXML
    private void handleAddComment(ActionEvent event) {
        String contenu = commentTextArea.getText().trim();
        if (contenu.isEmpty()) {
            showAlert("Veuillez saisir un commentaire.");
            return;
        }

        // Create a new comment object
        Comments comment = new Comments();
        comment.setContenu(contenu);
        comment.setDateCreation(new java.sql.Timestamp(new Date().getTime()));
        comment.setPost(post);

        // Save the comment using the CommentsServices
        CommentsServices commentsServices = new CommentsServices();
        try {
            commentsServices.add(comment);
            showAlert("Commentaire ajouté avec succès.");
        } catch (SQLException e) {
            showAlert("Erreur lors de l'ajout du commentaire: " + e.getMessage());
        }
    }

    // Method to show an alert dialog
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
