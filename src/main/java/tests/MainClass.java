package tests;

import entities.pharmacie;
import services.PharmacieServices;
import utils.MyConnection;

public class MainClass {

    public static void main(String[] args) {
        //MyConnection mc = new MyConnection();
        pharmacie p =new pharmacie("azerty","azerty","nuit");
        PharmacieServices ps = new PharmacieServices();
      //  ps.addEntity2(p);
        System.out.println(ps.getAllData());
    }
}
