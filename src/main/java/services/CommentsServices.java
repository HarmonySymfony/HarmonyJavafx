package services;

import entities.Comments;
import entities.Posts;
import utils.MyConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CommentsServices implements IService<Comments> {

    private final Connection connection;

    public CommentsServices() {
        connection = MyConnection.getInstance().getCnx();
    }

    @Override
    public void add(Comments comment) throws SQLException {
        String query = "INSERT INTO comments (contenu, date_creation, last_modification, posts_id, commented_as) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, comment.getContenu());
            preparedStatement.setTimestamp(2, new java.sql.Timestamp(comment.getDateCreation().getTime()));
            preparedStatement.setTimestamp(3, comment.getLastModification() != null ? new java.sql.Timestamp(comment.getLastModification().getTime()) : null);
            preparedStatement.setInt(4, comment.getPost().getId());
            preparedStatement.setString(5, comment.getCommentedAs());
            preparedStatement.executeUpdate();
            System.out.println("Comment added successfully.");
        } catch (SQLException e) {
            System.out.println("Error adding comment: " + e.getMessage());
        }
    }

    @Override
    public void update(Comments comment) throws SQLException {
        String query = "UPDATE comments SET contenu = ?, last_modification = ?, commented_as = ? WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, comment.getContenu());
            preparedStatement.setTimestamp(2, new java.sql.Timestamp(System.currentTimeMillis()));
            preparedStatement.setString(3, comment.getCommentedAs());
            preparedStatement.setInt(4, comment.getId());
            preparedStatement.executeUpdate();
            System.out.println("Comment updated successfully.");
        } catch (SQLException e) {
            System.out.println("Error updating comment: " + e.getMessage());
        }
    }

    @Override
    public void delete(int id) throws SQLException {
        String query = "DELETE FROM comments WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
            System.out.println("Comment deleted successfully.");
        } catch (SQLException e) {
            System.out.println("Error deleting comment: " + e.getMessage());
        }
    }

    @Override
    public List<Comments> getAll() throws SQLException {
        List<Comments> commentsList = new ArrayList<>();
        String query = "SELECT * FROM comments";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                Comments comment = new Comments();
                comment.setId(resultSet.getInt("id"));
                comment.setContenu(resultSet.getString("contenu"));
                comment.setDateCreation(resultSet.getTimestamp("date_creation"));
                comment.setLastModification(resultSet.getTimestamp("last_modification"));
                comment.setCommentedAs(resultSet.getString("commented_as"));
                commentsList.add(comment);
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving comments: " + e.getMessage());
        }
        return commentsList;
    }

    @Override
    public Comments getById(int id) throws SQLException {
        Comments comment = null;
        String query = "SELECT * FROM comments WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                comment = new Comments();
                comment.setId(resultSet.getInt("id"));
                comment.setContenu(resultSet.getString("contenu"));
                comment.setDateCreation(resultSet.getTimestamp("date_creation"));
                comment.setLastModification(resultSet.getTimestamp("last_modification"));
                comment.setCommentedAs(resultSet.getString("commented_as"));
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving comment by ID: " + e.getMessage());
        }
        return comment;
    }

    // Method to fetch comments by post
    public List<Comments> getCommentsByPost(Posts post) throws SQLException {
        List<Comments> commentsList = new ArrayList<>();
        String query = "SELECT * FROM comments WHERE posts_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, post.getId());
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Comments comment = new Comments();
                    comment.setId(resultSet.getInt("id"));
                    comment.setContenu(resultSet.getString("contenu"));
                    comment.setDateCreation(resultSet.getTimestamp("date_creation"));
                    comment.setLastModification(resultSet.getTimestamp("last_modification"));
                    comment.setCommentedAs(resultSet.getString("commented_as"));
                    // Set the post association
                    comment.setPost(post);
                    commentsList.add(comment);
                }
            }
        }
        return commentsList;
    }

    // Method to manage bidirectional association
    public void manageBidirectionalAssociation(Comments comment, Posts post) throws SQLException {
        // Add the comment to the post's collection of comments
        post.addComment(comment);
        // Update the comment to set the post association
        update(comment);
    }
}
