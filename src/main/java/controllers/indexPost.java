package controllers;

import entities.Posts;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import services.PostsServices;

import java.io.IOException;
import java.sql.Timestamp;

public class indexPost {

    @FXML
    private Button addPostButton;

    @FXML
    private TableView<Posts> postTableView;

    @FXML
    private TableColumn<Posts, Integer> idCol;

    @FXML
    private TableColumn<Posts, String> postedAsCol;

    @FXML
    private TableColumn<Posts, String> contenuCol;

    @FXML
    private TableColumn<Posts, Timestamp> dateCreationCol;

    @FXML
    private TableColumn<Posts, Timestamp> lastModificationCol;

    @FXML
    private TableColumn<Posts, HBox> actionsCol;

    private ObservableList<Posts> postsList = FXCollections.observableArrayList();

    @FXML
    private void handleKeyPress(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            Posts selectedPost = postTableView.getSelectionModel().getSelectedItem();
            if (selectedPost != null) {
                handleDetails(selectedPost);
            }
        }
    }

    @FXML
    public void initialize() {
        // Retrieve posts from the database
        PostsServices postsServices = new PostsServices();
        postsList.addAll(postsServices.getAllPosts());

        // Populate the TableView with posts
        postTableView.setItems(postsList);

        // Optionally, you can configure the TableColumn cell value factories here
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        postedAsCol.setCellValueFactory(new PropertyValueFactory<>("postedAs"));
        contenuCol.setCellValueFactory(new PropertyValueFactory<>("contenu"));
        dateCreationCol.setCellValueFactory(new PropertyValueFactory<>("dateCreation"));
        lastModificationCol.setCellValueFactory(new PropertyValueFactory<>("lastModification"));

        actionsCol.setCellValueFactory(param -> new SimpleObjectProperty<>(createButtonBox(param.getValue())));
    }

    // Method to create an HBox containing buttons for each post
    private HBox createButtonBox(Posts post) {
        Button detailsButton = new Button("Détails");
        Button editButton = new Button("Modifier");
        Button deleteButton = new Button("Supprimer");

        // Set action handlers for each button
        detailsButton.setOnAction(event -> handleDetails(post));
        editButton.setOnAction(event -> handleEdit(post));
        deleteButton.setOnAction(event -> handleDelete(post));

        return new HBox(detailsButton, editButton, deleteButton);
    }

    @FXML
    private void handleAddPost(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/addPost.fxml"));
            Parent root = loader.load();

            // Get the controller from the FXMLLoader
            addPost addController = loader.getController();

            // Set the TableView reference for the add controller
            addController.setPostTableView(postTableView);

            // Set the stage for the add controller
            Stage indexStage = (Stage) postTableView.getScene().getWindow();
            addController.setIndexStage(indexStage);


            // Close the index window
            indexStage.close();

            Stage addStage = new Stage();
            addStage.setScene(new Scene(root));
            addStage.setTitle("Créer nouvelle publication");
            addStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Action handler methods for buttons
    private void handleDetails(Posts post) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/detailsPost.fxml"));
            Parent root = loader.load();

            // Get the controller from the FXMLLoader
            detailsPost detailsController = loader.getController();

            // Set the TableView reference for the details controller
            detailsController.setPostTableView(postTableView);

            // Set the stage for the details controller
            Stage indexStage = (Stage) postTableView.getScene().getWindow();
            detailsController.setIndexStage(indexStage);

            // Pass the selected post to the details controller
            detailsController.setPost(post);

            // Close the index window
            indexStage.close();

            Stage detailsStage = new Stage();
            detailsStage.setScene(new Scene(root));
            detailsStage.setTitle("Details");
            detailsStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void handleEdit(Posts post) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/updatePost.fxml"));
            Parent root = loader.load();

            // Get the controller from the FXMLLoader
            updatePost updateController = loader.getController();

            // Pass the selected post to the update controller
            updateController.setPost(post);

            // Close the index window
            Stage indexStage = (Stage) postTableView.getScene().getWindow();
            indexStage.close();

            Stage updateStage = new Stage();
            updateStage.setScene(new Scene(root));
            updateStage.setTitle("Modifier la publication");
            updateStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void handleDelete(Posts post) {
        // Instantiate Alert
        Alert alert = new Alert(Alert.AlertType.INFORMATION);

        // Instantiate PostsServices to interact with the database
        PostsServices postsServices = new PostsServices();

        // Delete the post using the deletePost method from PostsServices
        postsServices.deletePost(post);

        // Show alert
        alert.setTitle("Post Deleted");
        alert.setHeaderText(null);
        alert.setContentText("The post has been successfully deleted.");
        alert.showAndWait();

        // Remove the deleted post from the TableView
        ObservableList<Posts> items = postTableView.getItems();
        items.remove(post);
    }
}
