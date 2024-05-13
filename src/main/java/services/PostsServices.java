package services;

import entities.Posts;
import utils.MyConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PostsServices implements IService<Posts> {
    private final Connection connection;

    public PostsServices() {
        this.connection = MyConnection.getInstance().getCnx();
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
        } catch (SQLException e) {
            System.err.println("Error adding post: " + e.getMessage());
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
        } catch (SQLException e) {
            System.err.println("Error updating post: " + e.getMessage());
        }
    }

    @Override
    public void delete(int id) throws SQLException {
        String query = "DELETE FROM posts WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error deleting post: " + e.getMessage());
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


    public int countPosts() throws SQLException {
        String query = "SELECT COUNT(*) AS count FROM posts";
        try (PreparedStatement pstmt1 = connection.prepareStatement(query);
             ResultSet rs1 = pstmt1.executeQuery()) {
            if (rs1.next()) {
                return rs1.getInt("count");
            }
        }
        return 0;
    }


    public List<Posts> getPosts(int limit, int offset, String searchType, String searchText) throws SQLException {
        List<Posts> posts = new ArrayList<>();
        String query = buildSearchQuery(searchType, searchText, limit, offset);
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            int paramIndex = 1;
            if (!searchText.isEmpty() && !searchType.equals("All")) {
                if (searchType.equals("ID")) {
                    pstmt.setString(paramIndex++, searchText);
                } else if (searchType.equals("Content")) {
                    pstmt.setString(paramIndex++, "%" + searchText + "%");
                } else if (searchType.equals("Posted As")) {
                    pstmt.setString(paramIndex++, "%" + searchText + "%"); // Use partial match for "Posted As"
                }
            }
            pstmt.setInt(paramIndex++, limit);
            pstmt.setInt(paramIndex, offset);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Posts post = new Posts();
                post.setId(rs.getInt("id"));
                post.setContenu(rs.getString("contenu"));
                post.setDateCreation(rs.getTimestamp("date_creation"));
                post.setLastModification(rs.getTimestamp("last_modification"));
                post.setPostedAs(rs.getString("posted_as"));
                posts.add(post);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving posts: " + e.getMessage());
            throw e; // Rethrow the exception to notify the caller about the error.
        }
        return posts;
    }


    public int countFilteredPosts(String searchType, String searchText) throws SQLException {
        String query = buildCountFilteredQuery(searchType, searchText);
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            int paramIndex = 1;
            if (!searchText.isEmpty() && !searchType.equals("All")) {
                if (searchType.equals("ID") || searchType.equals("Posted As")) {
                    pstmt.setString(paramIndex++, searchText);
                } else if (searchType.equals("Content")) {
                    pstmt.setString(paramIndex++, "%" + searchText + "%");
                }
            }
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error counting filtered posts: " + e.getMessage());
            throw e; // Rethrow the exception to notify the caller about the error.
        }
        return 0;
    }

    private String buildSearchQuery(String searchType, String searchText, int limit, int offset) {
        String baseQuery = "SELECT * FROM posts";
        switch (searchType) {
            case "ID":
                return baseQuery + " WHERE id = ? LIMIT ? OFFSET ?";
            case "Content":
                return baseQuery + (searchText.isEmpty() ? "" : " WHERE contenu LIKE ?") + " LIMIT ? OFFSET ?";
            case "Posted As":
                return baseQuery + (searchText.isEmpty() ? "" : " WHERE posted_as LIKE ?") + " LIMIT ? OFFSET ?";
            case "All":
                return baseQuery + " ORDER BY date_creation DESC LIMIT ? OFFSET ?";
            default:
                throw new IllegalArgumentException("Unsupported search criterion: " + searchType);
        }
    }




    private String buildCountFilteredQuery(String searchType, String searchText) {
        String baseQuery = "SELECT COUNT(*) FROM posts";
        switch (searchType) {
            case "ID":
                return baseQuery + (searchText.isEmpty() ? "" : " WHERE id = ?");
            case "Content":
                return baseQuery + (searchText.isEmpty() ? "" : " WHERE contenu LIKE ?");
            case "Posted As":
                return baseQuery + (searchText.isEmpty() ? "" : " WHERE posted_as LIKE ?");
            case "All":
                return baseQuery;
            default:
                throw new IllegalArgumentException("Unsupported search criterion: " + searchType);
        }
    }






    public List<Map<String, Object>> getPostsWithCommentCount() throws SQLException {
        List<Map<String, Object>> postsWithComments = new ArrayList<>();
        String query = "SELECT p.id, p.contenu, COUNT(c.id) AS comment_count " +
                "FROM posts p LEFT JOIN comments c ON p.id = c.posts_id " +
                "GROUP BY p.id, p.contenu ORDER BY p.date_creation DESC";

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                Map<String, Object> postWithComments = new HashMap<>();
                postWithComments.put("id", resultSet.getInt("id"));
                postWithComments.put("contenu", resultSet.getString("contenu"));
                postWithComments.put("comment_count", resultSet.getInt("comment_count"));
                postsWithComments.add(postWithComments);
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving posts with comment count: " + e.getMessage());
        }
        return postsWithComments;
    }
}
