package tests;

import entities.Posts;
import services.PostsServices;
import entities.pharmacie;
import services.PharmacieServices;
import entities.Personne;
import services.PersonneServices;
import utils.MyConnection;

import java.sql.SQLException;

public class MainClass {

    public static void main(String[] args) throws SQLException {
        //MyConnection mc = new MyConnection();
      //  ps.addEntity2(p);
        //MyConnection mc = new MyConnection();
        pharmacie p =new pharmacie("azerty","azerty","nuit");
        PharmacieServices ps = new PharmacieServices();
      //  Personne p =new Personne("Rebai","Saber",22,"saber@gaml.com");
        PersonneServices p_s = new PersonneServices();
      //  Posts po =new Posts();
        PostsServices po_s = new PostsServices();
      //  ps.addEntity2(p);
        System.out.println(ps.getAllData());
        System.out.println(p_s.getAllData());
        System.out.println(po_s.getAll());


    }
}
