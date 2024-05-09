package controllers;

import entities.Posts;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Screen;
import javafx.stage.Stage;
import services.PostsServices;
import java.io.IOException;
import java.sql.SQLException;

public class detailsPostF {

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
    @FXML
    private VBox commentsSection;

    private boolean commentsVisible = false;


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
//        Posts selectedPost = this.post;
    }




    @FXML
    private void modifierAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/updatePostF.fxml"));
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
        }
    }

    @FXML
    private void supprimerAction(ActionEvent event) throws SQLException {
        // Instantiate Alert
        Alert alert = new Alert(Alert.AlertType.INFORMATION);

        // Instantiate PostsServices to interact with the database
        PostsServices postsServices = new PostsServices();

        int id = post.getId();

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
    private ToggleButton toggleButton;


//    @FXML
//    private void toggleComments(ActionEvent event) {
//        try {
//            FXMLLoader loader = new FXMLLoader(getClass().getResource("/indexCommentF.fxml"));
//            Parent root = loader.load();
//    //      Get the controller from the FXMLLoader
//            indexComment commentsController = loader.getController();
//    //
//            System.out.println("Setting post in indexComment controller: " + post);
//            // Pass the selected post to the indexComment controller
//                commentsController.setSelectedPostAndRefreshTableView(post);
//            if (toggleButton.isSelected()) {
//                // Show indexComment section
//                indexCommentSection.setVisible(true);
//            } else {
//                // Hide indexComment section
//                indexCommentSection.setVisible(false);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//      }
//    }

    @FXML
    private void afficherCommentairesAction(ActionEvent event) {
        // Toggle the visibility of the comments section
        commentsVisible = !commentsVisible;
        commentsSection.setVisible(commentsVisible);

        if (commentsVisible) {
            // If comments are now visible, initialize the indexComment section
            initializeIndexCommentSection();
        } else {
            // If comments are hidden, you might want to perform some cleanup
            // or reset any state related to the comments.
        }
    }

    private void initializeIndexCommentSection() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/indexCommentF.fxml"));
            Parent root = loader.load();

            // Get the controller from the FXMLLoader
            indexCommentF commentsController = loader.getController();

            // Pass the necessary data to the indexComment controller
            commentsController.setSelectedPostAndRefreshTableView(post);
            commentsController.setIndexStage(stage); // Assuming you have set the stage earlier

            // Add the loaded FXML to the comments section VBox
            commentsSection.getChildren().add(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


//    @FXML
//    private void afficherCommentairesAction(ActionEvent event) {
//        try {
//            FXMLLoader loader = new FXMLLoader(getClass().getResource("/indexCommentF.fxml"));
//            Parent root = loader.load();
//
//            // Get the controller from the FXMLLoader
//            indexComment commentsController = loader.getController();
//
//            System.out.println("Setting post in indexComment controller: " + post);
//
//            // Pass the selected post to the indexComment controller
//            commentsController.setSelectedPostAndRefreshTableView(post);
//
//            // Set the stage for the comments controller
//            Stage indexStage = (Stage) postIdLabel.getScene().getWindow();
//            commentsController.setIndexStage(indexStage);
//
////            // Close the details window
////            Stage thisStage = (Stage) postIdLabel.getScene().getWindow();
////            thisStage.close();
//
//            // Show the comments window
//            Stage stage = new Stage();
//            stage.setScene(new Scene(root));
//            stage.setTitle("Commentaires");
//            stage.show();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    @FXML
    private void retourAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/indexPostF.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) retourButton.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}