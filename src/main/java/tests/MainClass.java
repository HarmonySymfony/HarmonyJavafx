package tests;

import entities.Personne;
import services.PersonneServices;
import utils.MyConnection;

public class MainClass {

    public static void main(String[] args) {
        //MyConnection mc = new MyConnection();
      //  Personne p =new Personne("Rebai","Saber",22,"saber@gaml.com");
        PersonneServices ps = new PersonneServices();
      //  ps.addEntity2(p);
        System.out.println(ps.getAllData());
    }
}
