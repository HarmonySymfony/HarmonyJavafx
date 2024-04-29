package controllers;

import entities.Comments;
import entities.Posts;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import services.CommentsServices;
import services.PostsServices;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;

public class indexComment {

        @FXML
        private Button addCommentButton;

        @FXML
        private TableView<Comments> commentTableView;

        @FXML
        private TableColumn<Comments, Integer> idCol;

        @FXML
        private TableColumn<Comments, String> commentedAsCol;

        @FXML
        private TableColumn<Comments, String> contenuCol;

        @FXML
        private TableColumn<Comments, Timestamp> dateCreationCol;

        @FXML
        private TableColumn<Comments, Timestamp> lastModificationCol;

        @FXML
        private TableColumn<Comments, HBox> actionsCol;

        private ObservableList<Comments> commentList = FXCollections.observableArrayList();

        private Posts post; // Store the selected post

        // Method to set the selected post from detailsPost controller

        private Stage indexStage;

        private detailsPost detailsController; // Reference to detailsPost controller

        // Setter method for detailsController
        public void setDetailsController(detailsPost controller) {
                this.detailsController = controller;
        }

        public void setIndexStage(Stage indexStage) {
                this.indexStage = indexStage;
        }

        public Stage getIndexStage() {
                return indexStage;
        }



//        // Method to set the selected post from detailsPost controller
//        public void setPost(Posts post) {
//                this.post = post;
//        }




        // Method to populate the TableView with comments associated with the selected post
        public void setSelectedPostAndRefreshTableView(Posts post) {
                this.post = post;
                try {
                        initialize(); // Refresh the TableView with comments associated with the selected post
                } catch (SQLException e) {
                        e.printStackTrace();
                }
        }

        // Initialize commentsList

        public void initialize() throws SQLException {
                if (post != null) {

                        // Initialize an observable list of Comments
                        ObservableList<Comments> commentList = FXCollections.observableArrayList();
                        // Retrieve comments associated with the selected post from the database
                        CommentsServices commentsServices = new CommentsServices();
                        commentList.clear(); // Clear existing comments
                        commentList.addAll(commentsServices.getCommentsByPost(post));

                        // Populate the TableView with comments
                        commentTableView.setItems(commentList);


                // Configure the TableView columns
                idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
                commentedAsCol.setCellValueFactory(new PropertyValueFactory<>("commentedAs"));
                contenuCol.setCellValueFactory(new PropertyValueFactory<>("contenu"));
                dateCreationCol.setCellValueFactory(new PropertyValueFactory<>("dateCreation"));
                lastModificationCol.setCellValueFactory(new PropertyValueFactory<>("lastModification"));

                actionsCol.setCellValueFactory(param -> new SimpleObjectProperty<>(createButtonBox(param.getValue())));
                } else {
                        System.out.println("Selected post is null.");
                }
        }


//        public void setSelectedPostAndRefreshTableView(Posts post) {
//                this.selectedPost = post;
//                try {
//                        initialize(); // Refresh the TableView with comments associated with the selected post
//                } catch (SQLException e) {
//                        e.printStackTrace();
//                }
//        }

        public void setCommentTableView(TableView<Posts> postTableView) {
                this.commentTableView = commentTableView;
        }

        // Method to create an HBox containing buttons for each Comment
        private HBox createButtonBox(Comments comment) {
                Button detailsButton = new Button("Détails");
                Button editButton = new Button("Modifier");
                Button deleteButton = new Button("Supprimer");

                // Set action handlers for each button
                detailsButton.setOnAction(event -> handleDetails(comment));
                editButton.setOnAction(event -> handleEdit(comment));
                deleteButton.setOnAction(event -> {
                        try {
                                handleDelete(comment);
                        } catch (SQLException e) {
                                throw new RuntimeException(e);
                        }
                });

                return new HBox(detailsButton, editButton, deleteButton);
        }

        private void handleDelete(Comments comment) throws SQLException {
                // Instantiate Alert
                Alert alert = new Alert(Alert.AlertType.INFORMATION);

                // Instantiate PostsServices to interact with the database
                CommentsServices commentsServices = new CommentsServices();


                int id=comment.getId();
                // Delete the post using the deletePost method from PostsServices
                commentsServices.delete(id);

                // Show alert
                alert.setTitle("Comment Deleted");
                alert.setHeaderText(null);
                alert.setContentText("The Comment has been successfully deleted.");
                alert.showAndWait();

                // Remove the deleted post from the TableView
                ObservableList<Comments> items = commentTableView.getItems();
                items.remove(comment);
        }


        private void handleEdit(Comments comment) {
        }

        private void handleDetails(Comments comment) {
        }


        // Handle button actions

        // Handle adding a new comment
        @FXML
        private void handleAddComment(ActionEvent event) {
                try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/addComment.fxml"));
                        Parent root = loader.load();

                        // Pass the selected post to the addComment controller
                        addComment addController = loader.getController();
                        addController.setPost(post);


                        // Set the TableView reference for the add controller
                        addController.setCommentTableView(commentTableView);


                        // Close the index window
                        Stage indexStage = (Stage) commentTableView.getScene().getWindow();
                        indexStage.close();

                        Stage addStage = new Stage();
                        addStage.setScene(new Scene(root));
                        addStage.setTitle("Créer un nouveau commentaire");
                        addStage.show();
                } catch (IOException e) {
                        e.printStackTrace();
                }
        }

        public void RetourBack(ActionEvent event) {
                Parent root = null;
                try {
                        root = FXMLLoader.load(getClass().getResource("/indexPost.fxml"));
                } catch (IOException e) {
                        throw new RuntimeException(e);
                }
                Scene scene = new Scene(root);
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(scene);
                stage.show();
        }

        // Other action handler methods...
}
