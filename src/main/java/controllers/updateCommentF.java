package controllers;

import entities.Comments;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import services.CommentsServices;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

public class updateCommentF {

    @FXML
    public Button submitButton;
    @FXML
    public Button annulerButton;
    @FXML
    public TextArea comment_contenu;
    @FXML
    public RadioButton comment_commentedAs_username;
    @FXML
    public ToggleGroup commentedAs;
    @FXML
    public RadioButton comment_commentedAs_anonyme;
    
    private int commentId;

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

    // Initialize commentsList
    private ObservableList<Comments> commentsList = FXCollections.observableArrayList();
    public void setCommentTableView(TableView<Comments> commentTableView) {
        this.commentTableView = commentTableView;
    }

    // Add a method to initialize commentsList
    public void setCommentsList(ObservableList<Comments> commentsList) {
        this.commentsList = commentsList;
    }

    // Add a method to clear and repopulate the TableView with comments
    private void refreshTableView() throws SQLException {
        // Clear the existing items in the TableView
        commentTableView.getItems().clear();

        // Retrieve comments from the database
        CommentsServices commentsServices = new CommentsServices();
        commentsList.addAll(commentsServices.getAll());

        // Populate the TableView with comments
        commentTableView.setItems(commentsList);
    }

    public void setComment(Comments comment) {
        commentId = comment.getId();
        comment_contenu.setText(comment.getContenu());
        if (comment.getCommentedAs().equals("Anonyme")) {
            comment_commentedAs_anonyme.setSelected(true);
        } else {
            comment_commentedAs_username.setSelected(true);
        }
    }

    @FXML
    private void confirmer(ActionEvent event) throws SQLException {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        String contenu = comment_contenu.getText();
        String commentedAsValue = ((RadioButton) commentedAs.getSelectedToggle()).getText();
        if (!contenu.isBlank()) {

            Comments updatedComment = new Comments();
            updatedComment.setId(commentId);
            updatedComment.setContenu(contenu);
            updatedComment.setCommentedAs(commentedAsValue);
            java.sql.Timestamp now = new java.sql.Timestamp(System.currentTimeMillis());
            updatedComment.setLastModification(now);



            CommentsServices commentsServices = new CommentsServices();
            commentsServices.update(updatedComment);

            alert.setContentText("The comment has been successfully updated.");
        } else {
            alert.setContentText("Failed to update the comment.");
        }
        alert.showAndWait();

        // Refresh the TableView with the updated data
        refreshTableView();

        // Close the current window
        Stage stage = (Stage) comment_contenu.getScene().getWindow();
        stage.close();

        // Close the index window and show it again
        Stage indexStage = getIndexStage();
        if (indexStage != null) {
            indexStage.show();
        }
    }

    @FXML
    private void annuler(ActionEvent event) throws SQLException {
        // Refresh the TableView with the updated data
        refreshTableView();

        // Close the current window
        Stage stage = (Stage) comment_contenu.getScene().getWindow();
        stage.close();

        // Close the index window and show it again
        Stage indexStage = getIndexStage();
        if (indexStage != null) {
            indexStage.show();
        }
    }
//
//    private void openIndexCommentWindow() {
//        try {
//            Stage indexStage = new Stage();
//            indexStage.setTitle("Index Comment");
//            indexStage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/indexCommentF.fxml"))));
//            indexStage.show();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
}
