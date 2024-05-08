package services;

import java.sql.SQLException;
import java.util.List;

public interface IServiceLabo<T>{
    void add(T t) throws SQLException;
    void add(T t, int a) throws SQLException;
    void update(T t) throws SQLException ;
    void delete(T t) throws SQLException;
    List<T> Display() throws SQLException;
}
