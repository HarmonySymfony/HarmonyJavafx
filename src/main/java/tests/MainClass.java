package tests;

import entities.Posts;
import services.PostsServices;
import utils.MyConnection;

import java.sql.SQLException;

public class MainClass {

    public static void main(String[] args) throws SQLException {
        //MyConnection mc = new MyConnection();
        Posts p =new Posts();
        PostsServices ps = new PostsServices();
      //  ps.addEntity2(p);
        System.out.println(ps.getAll());
    }
}
