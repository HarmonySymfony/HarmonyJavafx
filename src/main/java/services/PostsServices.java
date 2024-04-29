package services;

import entities.Posts;
import utils.MyConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PostsServices implements IService<Posts> {

    private final Connection connection;

    public PostsServices() {
        connection = MyConnection.getInstance().getCnx();
    }

    @Override
    public void add(Posts post) throws SQLException {
        String query = "INSERT INTO posts (contenu, date_creation, last_modification, posted_as) VALUES (?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, post.getContenu());
            preparedStatement.setTimestamp(2, new java.sql.Timestamp(post.getDateCreation().getTime()));
            preparedStatement.setTimestamp(3, post.getLastModification() != null ? new java.sql.Timestamp(post.getLastModification().getTime()) : null);
            preparedStatement.setString(4, post.getPostedAs());
            preparedStatement.executeUpdate();
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                post.setId(generatedKeys.getInt(1));
            }
            System.out.println("Post added successfully.");
        } catch (SQLException e) {
            System.out.println("Error adding post: " + e.getMessage());
        }
    }

    @Override
    public void update(Posts post) throws SQLException {
        String query = "UPDATE posts SET contenu = ?, last_modification = ?, posted_as = ? WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, post.getContenu());
            preparedStatement.setTimestamp(2, new java.sql.Timestamp(post.getLastModification().getTime()));
            preparedStatement.setString(3, post.getPostedAs());
            preparedStatement.setInt(4, post.getId());
            preparedStatement.executeUpdate();
            System.out.println("Post updated successfully.");
        } catch (SQLException e) {
            System.out.println("Error updating post: " + e.getMessage());
        }
    }

    @Override
    public void delete(int id) throws SQLException {
        String query = "DELETE FROM posts WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
            System.out.println("Post deleted successfully.");
        } catch (SQLException e) {
            System.out.println("Error deleting post: " + e.getMessage());
        }
    }

    @Override
    public List<Posts> getAll() throws SQLException {
        List<Posts> postsList = new ArrayList<>();
        String query = "SELECT * FROM posts";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
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

    @Override
    public Posts getById(int id) throws SQLException {
        Posts post = null;
        String query = "SELECT * FROM posts WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                post = new Posts();
                post.setId(resultSet.getInt("id"));
                post.setContenu(resultSet.getString("contenu"));
                post.setDateCreation(resultSet.getTimestamp("date_creation"));
                post.setLastModification(resultSet.getTimestamp("last_modification"));
                post.setPostedAs(resultSet.getString("posted_as"));
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving post by ID: " + e.getMessage());
        }
        return post;
    }
}
