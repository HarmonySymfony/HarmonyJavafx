package utils;

import java.sql.*;

public class DB {
    private final String url="jdbc:mysql://localhost:3306/harmony" ;
    private final String user="root";
    private final String passwd="";
    private static DB instance ;
    private Connection connection;
    private DB() {
        try {
            connection= DriverManager.getConnection(url,user,passwd);
            System.out.println("Connected");
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

    }
    public static DB getInstance(){
        if (instance==null)
            instance=new DB();
        return instance ;
    }
    public Connection getConnection(){
        return connection;
    }
}
