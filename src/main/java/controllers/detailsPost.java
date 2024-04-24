package controllers;

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
import services.PostsServices;

import java.io.IOException;
import java.sql.SQLException;

public class detailsPost {

    @FXML
    public Button retourButton;

    @FXML
    private Label postIdLabel;

    @FXML
    private Label contenuLabel;

    @FXML
    private Label dateCreationLabel;

    @FXML
    private Label lastModificationLabel;

    @FXML
    private Label postedAsLabel;

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

    private TableView<Posts> postTableView;

    public void setPostTableView(TableView<Posts> postTableView) {
        this.postTableView = postTableView;
    }

    private ObservableList<Posts> postsList;

    public void setPostsList(ObservableList<Posts> postsList) {
        this.postsList = postsList;
    }

    private Posts post;

    public void refreshTableView() throws SQLException {
        if (postsList != null) { // Ensure postsList is not null before using it
            // Clear the current items in the TableView
            postTableView.getItems().clear();

            // Retrieve posts from the database
            PostsServices postsServices = new PostsServices();
            postsList.addAll(postsServices.getAll());

            // Populate the TableView with posts
            postTableView.setItems(postsList);
        }
    }

    public void setPost(Posts post) {
        this.post = post;
        postIdLabel.setText(String.valueOf(post.getId()));
        contenuLabel.setText(post.getContenu() != null ? post.getContenu() : "");
        dateCreationLabel.setText(post.getDateCreation() != null ? post.getDateCreation().toString() : "");
        lastModificationLabel.setText(post.getLastModification() != null ? post.getLastModification().toString() : "");
        postedAsLabel.setText(post.getPostedAs() != null ? post.getPostedAs() : "");
    }


    @FXML
    private void modifierAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/updatePost.fxml"));
            Parent root = loader.load();

            // Get the controller from the FXMLLoader
            updatePost updateController = loader.getController();

            // Set the TableView reference for the details controller
            updateController.setPostTableView(postTableView);

            // Set the stage for the details controller
            Stage indexStage = (Stage) postTableView.getScene().getWindow();
            updateController.setIndexStage(indexStage);

            // Pass the selected post to the update controller
            updateController.setPost(post);

            // Close the details window
            Stage stage = (Stage) postIdLabel.getScene().getWindow();
            stage.close();

            Stage updateStage = new Stage();
            updateStage.setScene(new Scene(root));
            updateStage.setTitle("Modifier la publication");
            updateStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }    }

    @FXML
    private void supprimerAction(ActionEvent event) throws SQLException {
        // Instantiate Alert
        Alert alert = new Alert(Alert.AlertType.INFORMATION);

        // Instantiate PostsServices to interact with the database
        PostsServices postsServices = new PostsServices();

        int id=post.getId();

        // Delete the post using the deletePost method from PostsServices
        postsServices.delete(id);

        // Show alert
        alert.setTitle("Post Deleted");
        alert.setHeaderText(null);
        alert.setContentText("The post has been successfully deleted.");
        alert.showAndWait();

        // Refresh the TableView in the index window
        refreshTableView();

        // Close the details window after deleting the post
        Stage stage = (Stage) postIdLabel.getScene().getWindow();
        stage.close();

        // Close the index window and show it again
        Stage indexStage = getIndexStage();
        if (indexStage != null) {
            indexStage.show();
        }

        // Remove the deleted post from the TableView
        ObservableList<Posts> items = postTableView.getItems();
        items.remove(post);
    }

    @FXML
    private void afficherCommentairesAction(ActionEvent event) {
        // Add logic to handle the "Afficher Commentaires" button action
    }

    @FXML
    private void retourAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/indexPost.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) retourButton.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
