package tests;


import entites.Cabinet;

import services.CabinetServices;

import outils.MyDB;
import services.Rendezvousservices;

import java.sql.SQLException;
import java.util.List; // Importez la classe List depuis java.util

public class maincabinet {
    public static void main(String[] args) {

        // Obtention d'une instance de MyDB
        MyDB db = MyDB.getInstance();

        // Création d'une instance de RendezvousServices
        CabinetServices cabinetServices = new CabinetServices();

        // Appel de la méthode d'ajout d'entité avec l'objet Rendezvous
        cabinetServices.addEntity2(new Cabinet("manar","hnena","10:57", "nour@gmail.com"));

        Cabinet c = new Cabinet("jardine","hne","15:00", "olfa@gmail.com");
        c.setId(4);

        // Appel de la méthode pour mettre à jour l'entité Rendezvous
        cabinetServices.updateEntity(c);


// Création d'un objet Rendezvous pour la suppression
        Cabinet cab = new Cabinet();
        cab.setId(2);

        // Appel de la méthode pour supprimer l'entité Rendezvous
        cabinetServices.deleteEntity(cab);



// Appel de la méthode pour récupérer toutes les données des rendez-vous
        List<Cabinet> cabinetList = CabinetServices.getAllDataCabinet();

        // Affichage des rendez-vous récupérés
        System.out.println("************************** Afficheeeeeeeeeeeeeeeeeeeeeeeeeeeeeeer ***********************************");
        for (Cabinet cabinet : cabinetList) {
            System.out.println(cabinet.toString());
        }






    }
}
