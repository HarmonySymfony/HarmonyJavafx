package services;

import entities.Comments;
import entities.Posts;

import java.sql.SQLException;
import java.util.List;

public interface IService<T> {

    void add(T t) throws SQLException;

    void update(T t) throws SQLException;

    void delete(int id) throws SQLException;

    List<T> getAll() throws SQLException;

    T getById(int id) throws  SQLException;

    List<Comments> getCommentsByPost(Posts post) throws SQLException;
}