package controllers;

import entities.Comments;
import entities.Posts;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.apache.pdfbox.io.IOUtils;
import services.CommentsServices;
import services.PostsServices;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

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
    @FXML
    private VBox commentsSection;

    private boolean commentsVisible = false;


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
//        Posts selectedPost = this.post;
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
    private void genererPDFAction(ActionEvent event) throws SQLException {
        genererPDF();
    }
    @FXML
    private ToggleButton toggleButton;


//    @FXML
//    private void toggleComments(ActionEvent event) {
//        try {
//            FXMLLoader loader = new FXMLLoader(getClass().getResource("/indexComment.fxml"));
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/indexComment.fxml"));
            Parent root = loader.load();

            // Get the controller from the FXMLLoader
            indexComment commentsController = loader.getController();

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
//            FXMLLoader loader = new FXMLLoader(getClass().getResource("/indexComment.fxml"));
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/indexPost.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) retourButton.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void genererPDF() {
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);

            PDPageContentStream contentStream = new PDPageContentStream(document, page);

            PDType0Font font = PDType0Font.load(document, getClass().getResourceAsStream("/fonts/CairoPlay-VariableFont_slnt,wght.ttf"));

            float margin = 50; // Adjusted margin for better spacing
            float fontSize = 12; // Adjust the font size if needed
            float lineHeight = 1.5f * fontSize; // Adjust the line height for spacing

            // Start writing post content
            float yPosition = page.getMediaBox().getHeight() - 2 * margin - 100;

            // Write the content of the currently opened post
            contentStream.beginText();
            contentStream.setFont(font, fontSize);
            contentStream.newLineAtOffset(margin, yPosition);
            contentStream.showText("Post content: " + post.getContenu()+"          Timestamp: " + post.getDateCreation());
            contentStream.newLine();
            yPosition -= lineHeight; // Adjust the y position for the next line
            contentStream.endText();

            // Retrieve comments associated with the currently opened post
            CommentsServices commentsService = new CommentsServices();
            List<Comments> comments;
            try {
                comments = commentsService.getCommentsByPost(post);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            // Write the comments
            for (Comments comment : comments) {
                contentStream.beginText();
                contentStream.setFont(font, fontSize);
                contentStream.newLineAtOffset(margin, yPosition);
                contentStream.showText("Comment: " + comment.getContenu()+"      Date de Commentaire: " + comment.getDateCreation());
                contentStream.newLine();
                yPosition -= lineHeight; // Adjust the y position for the next line
                contentStream.endText();
            }

            contentStream.close();

            // Save the PDF file
            File file = new File("ListOfPosts.pdf");
            document.save(file);

            // Open the PDF file
            Desktop.getDesktop().open(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}