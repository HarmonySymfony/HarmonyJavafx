package tests;

import entities.pharmacie;
import services.PharmacieServices;
import entities.Personne;
import services.PersonneServices;
import utils.MyConnection;

public class MainClass {

    public static void main(String[] args) {
        //MyConnection mc = new MyConnection();
        pharmacie p =new pharmacie("azerty","azerty","nuit");
        PharmacieServices ps = new PharmacieServices();
      //  Personne p =new Personne("Rebai","Saber",22,"saber@gaml.com");
        PersonneServices ps = new PersonneServices();
      //  ps.addEntity2(p);
        System.out.println(ps.getAllData());
    }
}
