package controllers;

import entities.Comments;
import entities.Posts;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import services.CommentsServices;

import java.io.IOException;
import java.sql.SQLException;

public class detailsComment {

    @FXML
    public Button retourButton;
    @FXML
    public Label commentIdLabel;
    @FXML
    public Label commentedAsLabel;

    @FXML
    private Label postIdLabel;

    @FXML
    private Label contenuLabel;

    @FXML
    private Label dateCreationLabel;

    @FXML
    private Label lastModificationLabel;



    // Add a private field to store the stage
    private Stage stage;

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

    private TableView<Comments> commentTableView;

    public void setCommentTableView(TableView<Comments> commentTableView) {
        this.commentTableView = commentTableView;
    }


    private ObservableList<Comments> commentsList;

    public void setCommentsList(ObservableList<Comments> commentsList) {
        this.commentsList = commentsList;
    }

    private Comments comment;

    public void refreshTableView() throws SQLException {
        if (commentsList != null) { // Ensure commentsList is not null before using it
            // Clear the current items in the TableView
            commentTableView.getItems().clear();

            // Retrieve comments from the database
            CommentsServices commentsServices = new CommentsServices();
            commentsList.addAll(commentsServices.getAll());

            // Populate the TableView with comments
            commentTableView.setItems(commentsList);
        }
    }

    public void setComment(Comments comment) {
        this.comment = comment;
        commentIdLabel.setText(String.valueOf(comment.getId()));
        contenuLabel.setText(comment.getContenu() != null ? comment.getContenu() : "");
        dateCreationLabel.setText(comment.getDateCreation() != null ? comment.getDateCreation().toString() : "");
        lastModificationLabel.setText(comment.getLastModification() != null ? comment.getLastModification().toString() : "");
        commentedAsLabel.setText(comment.getCommentedAs() != null ? comment.getCommentedAs() : "");
//        Comments selectedComment = this.comment;
    }




    @FXML
    private void modifierAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/updateComment.fxml"));
            Parent root = loader.load();

            // Get the controller from the FXMLLoader
            updateComment updateController = loader.getController();

            // Set the TableView reference for the details controller
            updateController.setCommentTableView(commentTableView);

            // Set the stage for the details controller
            Stage indexStage = (Stage) commentTableView.getScene().getWindow();
            updateController.setIndexStage(indexStage);

            // Pass the selected post to the update controller
            updateController.setComment(comment);

            // Close the index window
            indexStage.close();

            Stage updateStage = new Stage();
            updateStage.setScene(new Scene(root));
            updateStage.setTitle("Modifier la commentaire");
            updateStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void supprimerAction(ActionEvent event) throws SQLException {
        // Instantiate Alert
        Alert alert = new Alert(Alert.AlertType.INFORMATION);

        // Instantiate CommentsServices to interact with the database
        CommentsServices commentsServices = new CommentsServices();

        int id = comment.getId();

        // Delete the comment using the deleteComment method from CommentsServices
        commentsServices.delete(id);

        // Show alert
        alert.setTitle("Comment Deleted");
        alert.setHeaderText(null);
        alert.setContentText("The comment has been successfully deleted.");
        alert.showAndWait();

        // Refresh the TableView in the index window
        refreshTableView();

        // Close the details window after deleting the comment
        Stage stage = (Stage) commentIdLabel.getScene().getWindow();
        stage.close();

        // Close the index window and show it again
        Stage indexStage = getIndexStage();
        if (indexStage != null) {
            indexStage.show();
        }

        // Remove the deleted comment from the TableView
        ObservableList<Comments> items = commentTableView.getItems();
        items.remove(comment);
    }


    @FXML
    private void retourAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/indexComment.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) retourButton.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}