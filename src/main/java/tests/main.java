package tests;

import entites.Rendezvous;
import services.Rendezvousservices;

import outils.MyDB;

import java.sql.SQLException;
import java.util.List; // Importez la classe List depuis java.util

public class main {
    public static void main(String[] args) {
        // Obtention d'une instance de MyDB
        MyDB db = MyDB.getInstance();

        // Création d'une instance de RendezvousServices
        Rendezvousservices rendezvousservices = new Rendezvousservices();

        // Appel de la méthode d'ajout d'entité avec l'objet Rendezvous
        rendezvousservices.addEntity2(new Rendezvous("nour","hnena","2024-02-02 09:53:00", "olfa1@gmail.com"));

        Rendezvous r = new Rendezvous("ghofrane","hne","2024-02-02 09:53:00", "olfa@gmail.com");
        r.setId(7);

        // Appel de la méthode pour mettre à jour l'entité Rendezvous
        rendezvousservices.updateEntity(r);



        // Création d'un objet Rendezvous pour la suppression
        Rendezvous rdv = new Rendezvous();
        rdv.setId(4);

        // Appel de la méthode pour supprimer l'entité Rendezvous
        rendezvousservices.deleteEntity(rdv);




// Appel de la méthode pour récupérer toutes les données des rendez-vous
        List<Rendezvous> rendezvousList = rendezvousservices.getAllData();

        // Affichage des rendez-vous récupérés
        System.out.println("************************** Afficheeeeeeeeeeeeeeeeeeeeeeeeeeeeeeer ***********************************");
        for (Rendezvous rendezvous : rendezvousList) {
            System.out.println(rendezvous.toString());
        }





    }}