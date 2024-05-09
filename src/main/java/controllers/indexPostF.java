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
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Screen;
import javafx.stage.Stage;
import services.PostsServices;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;

public class indexPostF {

    @FXML
    private Button addPostButton, nextButton, prevButton;

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


    @FXML
    private Label pageLabel;


    private ObservableList<Posts> postsList = FXCollections.observableArrayList();
    private int currentPage = 1;
    private final int rowsPerPage = 10;
    private int totalPosts;  // Total number of posts in the database
    private int totalPages;  // Total number of pages

    @FXML
    private WebView webView;



    @FXML
    public void initialize() {
        // Bind Table Columns to Posts properties
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        postedAsCol.setCellValueFactory(new PropertyValueFactory<>("postedAs"));
        contenuCol.setCellValueFactory(new PropertyValueFactory<>("contenu"));
        dateCreationCol.setCellValueFactory(new PropertyValueFactory<>("dateCreation"));
        lastModificationCol.setCellValueFactory(new PropertyValueFactory<>("lastModification"));

        actionsCol.setCellValueFactory(param -> new SimpleObjectProperty<>(createButtonBox(param.getValue())));
        try {
            PostsServices postsServices = new PostsServices();
            totalPosts = postsServices.countPosts();  // Assume this method returns the total number of posts
            totalPages = (int) Math.ceil((double) totalPosts / rowsPerPage);
            updateTable();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        double screenWidth = Screen.getPrimary().getVisualBounds().getWidth();
        double screenHeight = Screen.getPrimary().getVisualBounds().getHeight();

        // Lier la taille de la WebView à la taille de l'écran
        webView.setPrefWidth(screenWidth);
        webView.setPrefHeight(screenHeight);

        // Charger le fichier HTML avec le fond animé
        WebEngine webEngine = webView.getEngine();

        // Charger le fichier HTML contenant la carte Google Maps
        webEngine.load(getClass().getResource("/HTML/index.html").toExternalForm());}




    @FXML
    private void updateTable() {
        try {
            int offset = (currentPage - 1) * rowsPerPage;
            PostsServices postsServices = new PostsServices();
            postsList.clear();
            postsList.addAll(postsServices.getPosts(rowsPerPage, offset));
            postTableView.setItems(postsList);
            pageLabel.setText("Page " + currentPage + " of " + totalPages);

            // Button controls
            prevButton.setDisable(currentPage <= 1);
            nextButton.setDisable(currentPage >= totalPages);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void handleNextPage(ActionEvent event) {
        if (currentPage < totalPages) {
            currentPage++;
            updateTable();
        }
    }

    @FXML
    private void handlePrevPage(ActionEvent event) {
        if (currentPage > 1) {
            currentPage--;
            updateTable();
        }
    }


    @FXML
    private void handleAddPost(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/addPostF.fxml"));
            Parent root = loader.load();
            addPostF addController = loader.getController();
            addController.setPostTableView(postTableView);

            Stage addStage = new Stage();
            addStage.setScene(new Scene(root));
            addStage.setTitle("Créer nouvelle publication");
            addStage.showAndWait(); // Wait for the "Add Post" window to close

            // After the "Add Post" window is closed, check if a new post was successfully added
            if (addController.isPostAdded()) {
                // If a new post was added, update the total posts count and recalculate the total pages
                totalPosts++;
                totalPages = (int) Math.ceil((double) totalPosts / rowsPerPage);

                // If the current page exceeds the total pages after adding a new post, reset the current page to the last page
                if (currentPage > totalPages) {
                    currentPage = totalPages;
                }

                // Then call updateTable() to refresh the TableView with the updated data
                updateTable();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private HBox createButtonBox(Posts post) {
        Button detailsButton = new Button("Détails");
        Button editButton = new Button("Modifier");
        Button deleteButton = new Button("Supprimer");

        detailsButton.setOnAction(event -> handleDetails(post));
        editButton.setOnAction(event -> handleEdit(post));
        deleteButton.setOnAction(event -> handleDelete(post));

        return new HBox(detailsButton, editButton, deleteButton);
    }

    private void handleDetails(Posts post) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/detailsPostF.fxml"));
            Parent root = loader.load();
            detailsPostF detailsController = loader.getController();
            detailsController.setPostTableView(postTableView);
            detailsController.setIndexStage((Stage) postTableView.getScene().getWindow());
            detailsController.setPost(post);

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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/updatePostF.fxml"));
            Parent root = loader.load();
            updatePostF updateController = loader.getController();
            updateController.setPostTableView(postTableView);
            updateController.setIndexStage((Stage) postTableView.getScene().getWindow());
            updateController.setPost(post);

            Stage updateStage = new Stage();
            updateStage.setScene(new Scene(root));
            updateStage.setTitle("Modifier la publication");
            updateStage.showAndWait(); // Wait for the "Update Post" window to close

            // After the "Update Post" window is closed, check if a post was successfully updated
            if (updateController.isPostUpdated()) {
                // If a post was updated, refresh the TableView with the updated data
                updateTable();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void handleDelete(Posts postToDelete) {
        try {
            PostsServices postsServices = new PostsServices();
            postsServices.delete(postToDelete.getId());

            // Assuming the post was successfully deleted, remove it from the TableView
            postsList.remove(postToDelete);

            // Update the total number of posts and recalculate the total pages
            totalPosts--;
            totalPages = (int) Math.ceil((double) totalPosts / rowsPerPage);

            // If the current page exceeds the total pages after deleting a post, reset the current page to the last page
            if (currentPage > totalPages) {
                currentPage = totalPages;
            }

            // Then call updateTable() to refresh the TableView with the updated data
            updateTable();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to delete post.");
        }
    }


    @FXML
    public void RetourBack(ActionEvent event) {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/Login.fxml"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Scene scene = new Scene(root);
        Stage stage = (Stage) postTableView.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
