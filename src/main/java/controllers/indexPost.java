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
import javafx.stage.Modality;
import javafx.stage.Stage;
import services.PostsServices;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

public class indexPost {

    public TextField searchField;
    @FXML
    private ComboBox<String> searchTypeComboBox;

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
        // Ensure components are injected correctly
        assert searchField != null : "fx:id 'searchField' was not injected!";
        assert searchTypeComboBox != null : "fx:id 'searchTypeComboBox' was not injected!";

        // Setup dynamic search filtering
        searchField.textProperty().addListener((obs, oldVal, newVal) -> updateTable());
        searchTypeComboBox.valueProperty().addListener((obs, oldVal, newVal) -> updateTable());

    }



    @FXML
    private void updateTable() {
        try {
            PostsServices postsServices = new PostsServices();
            String searchText = searchField.getText().trim();
            String searchType = searchTypeComboBox.getValue() != null ? searchTypeComboBox.getValue() : "All";

            // Calculate the total number of posts based on the search criteria
            int totalFilteredPosts = searchText.isEmpty() ? postsServices.countPosts() : postsServices.countFilteredPosts(searchType, searchText);

            // Calculate the total number of pages
            totalPages = (int) Math.ceil((double) totalFilteredPosts / rowsPerPage);

            // Update the current page if it exceeds the total pages
            if (currentPage > totalPages) {
                currentPage = totalPages;
            }

            // Calculate the offset based on the current page
            int offset = Math.max((currentPage - 1) * rowsPerPage, 0); // Adjusted offset calculation

            // Fetch the posts based on the search criteria and pagination
            List<Posts> fetchedPosts = searchText.isEmpty() ? postsServices.getPosts(rowsPerPage, offset, "All", "") :
                    postsServices.getPosts(rowsPerPage, offset, searchType, searchText);

            // Update the posts list
            postsList.clear();
            postsList.addAll(fetchedPosts);

            // Update the TableView with the fetched posts
            postTableView.setItems(postsList);

            // Update the page label
            pageLabel.setText("Page " + currentPage + " of " + totalPages);

            // Enable/disable pagination buttons based on the current page
            prevButton.setDisable(currentPage <= 1);
            nextButton.setDisable(currentPage >= totalPages);
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to update table: " + e.getMessage());
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/addPost.fxml"));
            Parent root = loader.load();
            addPost addController = loader.getController();
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/detailsPost.fxml"));
            Parent root = loader.load();
            detailsPost detailsController = loader.getController();
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/updatePost.fxml"));
            Parent root = loader.load();
            updatePost updateController = loader.getController();
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
            root = FXMLLoader.load(getClass().getResource("/AfficheUser.fxml"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Scene scene = new Scene(root);
        Stage stage = (Stage) postTableView.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    private void openStatisticsWindow() {
        try {
            // Load the statistics view FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/forumStats.fxml"));
            Parent statisticsView = loader.load();

            // Create a new stage for the statistics window
            Stage statisticsStage = new Stage();
            statisticsStage.setTitle("Post Comment Statistics");
            statisticsStage.setScene(new Scene(statisticsView));
            statisticsStage.initModality(Modality.NONE); // This allows interaction with other windows
            statisticsStage.initOwner(postTableView.getScene().getWindow()); // Optional: set owner
            statisticsStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to open the statistics window.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
