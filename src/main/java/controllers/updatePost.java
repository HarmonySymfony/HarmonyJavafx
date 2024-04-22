package controllers;

import entities.Posts;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import services.PostsServices;

import java.io.IOException;
import java.util.Optional;

public class updatePost {

    public Button submitButton;
    public Button annulerButton;
    @FXML
    private TextArea post_contenu;

    @FXML
    private RadioButton post_postedAs_username;

    @FXML
    private RadioButton post_postedAs_anonyme;

    @FXML
    private ToggleGroup postedAs;

    private int postId;

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
    private void refreshTableView() {
        // Clear the existing items in the TableView
        postTableView.getItems().clear();

        // Retrieve posts from the database
        PostsServices postsServices = new PostsServices();
        postsList.addAll(postsServices.getAllPosts());

        // Populate the TableView with posts
        postTableView.setItems(postsList);
    }

    public void setPost(Posts post) {
        postId = post.getId();
        post_contenu.setText(post.getContenu());
        if (post.getPostedAs().equals("Anonyme")) {
            post_postedAs_anonyme.setSelected(true);
        } else {
            post_postedAs_username.setSelected(true);
        }
    }

    @FXML
    private void confirmer(ActionEvent event) {
            String contenu = post_contenu.getText();
            String postedAsValue = ((RadioButton) postedAs.getSelectedToggle()).getText();

            Posts updatedPost = new Posts();
            updatedPost.setId(postId);
            updatedPost.setContenu(contenu);
            updatedPost.setPostedAs(postedAsValue);

            PostsServices postsServices = new PostsServices();
            boolean isUpdated = postsServices.updatePost(updatedPost);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            if (isUpdated) {
                alert.setContentText("The post has been successfully updated.");
            } else {
                alert.setContentText("Failed to update the post.");
            }
            alert.showAndWait();

            // Refresh the TableView with the updated data
            refreshTableView();

            // Close the current window
            Stage stage = (Stage) post_contenu.getScene().getWindow();
            stage.close();

            // Close the index window and show it again
            Stage indexStage = getIndexStage();
            if (indexStage != null) {
                indexStage.show();
            }
    }

    @FXML
    private void annuler(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/indexPost.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) annulerButton.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
//
//    private void openIndexPostWindow() {
//        try {
//            Stage indexStage = new Stage();
//            indexStage.setTitle("Index Post");
//            indexStage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/indexPost.fxml"))));
//            indexStage.show();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
}
