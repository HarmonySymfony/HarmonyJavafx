package services;

import entities.Posts;
import utils.MyConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class PostsServices {

    public void addPost(Posts post) {
        String query = "INSERT INTO posts (contenu, date_creation, posted_as) VALUES (?, ?, ?)";
        try {
            Connection connection = MyConnection.getInstance().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, post.getContenu());
            preparedStatement.setTimestamp(2, new java.sql.Timestamp(post.getDateCreation().getTime()));
            preparedStatement.setString(3, post.getPostedAs());
            preparedStatement.executeUpdate();
            System.out.println("Post added successfully.");
        } catch (SQLException e) {
            System.out.println("Error adding post: " + e.getMessage());
        }
    }


    public boolean updatePost(Posts post) {
        String query = "UPDATE posts SET contenu = ?, last_modification = ?, posted_as = ? WHERE id = ?";
        try {
            Connection connection = MyConnection.getInstance().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, post.getContenu());
            preparedStatement.setTimestamp(2, new java.sql.Timestamp(System.currentTimeMillis()));
            preparedStatement.setString(3, post.getPostedAs());
            preparedStatement.setInt(4, post.getId());
            int rowsUpdated = preparedStatement.executeUpdate();
            System.out.println("Post updated successfully.");
            return rowsUpdated > 0; // Return true if rows were updated
        } catch (SQLException e) {
            System.out.println("Error updating post: " + e.getMessage());
            return false; // Return false if an exception occurred
        }
    }


    public void deletePost(Posts post) {
        String query = "DELETE FROM posts WHERE id = ?";
        try {
            Connection connection = MyConnection.getInstance().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, post.getId());
            preparedStatement.executeUpdate();
            System.out.println("Post deleted successfully.");
        } catch (SQLException e) {
            System.out.println("Error deleting post: " + e.getMessage());
        }
    }

    public List<Posts> getAllPosts() {
        List<Posts> postsList = new ArrayList<>();
        String query = "SELECT * FROM posts";
        try {
            Connection connection = MyConnection.getInstance().getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                Posts post = new Posts();
                post.setId(resultSet.getInt("id"));
                post.setContenu(resultSet.getString("contenu"));
                post.setDateCreation(resultSet.getTimestamp("date_creation"));
                post.setLastModification(resultSet.getTimestamp("last_modification"));
                post.setPostedAs(resultSet.getString("posted_as"));
                postsList.add(post);
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving posts: " + e.getMessage());
        }
        return postsList;
    }
}
