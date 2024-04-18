package services;

import entites.Rendezvous;
import outils.MyDB;
import java.sql.ResultSet;



import java.sql.PreparedStatement;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.text.SimpleDateFormat;
import java.text.ParseException;

public  class Rendezvousservices implements IservicesRendezvous<Rendezvous> {




    public boolean isValidEmail(String email) {
        String regex = "^(.+)@(.+)$";
        return email.matches(regex);
    }
    public boolean isValidDateTime(String dateTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setLenient(false);
        try {
            sdf.parse(dateTime);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }


    //verf ex///
    public boolean entityExists(Rendezvous rendezvous) {
        try {
            // Vérifier si le rendez-vous existe dans la base de données en utilisant une requête SELECT
            String requete = "SELECT * FROM rendez_vous WHERE nom = ? AND prenom = ? AND date = ? AND email = ?";
            PreparedStatement pst = MyDB.getInstance().getConnection().prepareStatement(requete);
            pst.setString(1, rendezvous.getNom());
            pst.setString(2, rendezvous.getPrenom());
            pst.setString(3, rendezvous.getDate());
            pst.setString(4, rendezvous.getEmail());
            ResultSet rs = pst.executeQuery();
            return rs.next(); // Renvoie true si le résultat de la requête n'est pas vide (le rendez-vous existe déjà), sinon false
        } catch (SQLException e) {
            // Gérer les exceptions SQLException
            System.out.println("Erreur lors de la vérification de l'existence du rendez-vous : " + e.getMessage());
            return false; // En cas d'erreur, retourner false par défaut
        }
    }
    public void addEntity(Rendezvous Rendezvous) {


    }


    public void addEntity2(Rendezvous rendezvous) {
        try {
            // Vérifier si le rendez-vous existe déjà dans la base de données
            if (entityExists(rendezvous)) {
                System.out.println("Le rendez-vous existe déjà dans la base de données.");
            } else {
                // Vérifier si l'email est valide
                if (!isValidEmail(rendezvous.getEmail())) {
                    System.out.println("Email invalide.");
                    return;
                }
                // Vérifier si la date/heure est valide
                if (!isValidDateTime(rendezvous.getDate())) {
                    System.out.println("Date/heure invalide.");
                    return;
                }
                // Ajouter le rendez-vous à la base de données s'il n'existe pas déjà
                String requete = "INSERT INTO rendez_vous (nom,prenom,date, email) VALUES (?,?,?, ?)";
                PreparedStatement pst = MyDB.getInstance().getConnection().prepareStatement(requete);
                pst.setString(1, rendezvous.getNom());
                pst.setString(2, rendezvous.getPrenom());
                pst.setString(3, rendezvous.getDate());
                pst.setString(4, rendezvous.getEmail());
                pst.executeUpdate();
                System.out.println("Rendezvous ajouté avec succès !");
            }
        } catch (SQLException e) {
            // Gérer les exceptions SQLException
            System.out.println("Erreur lors de l'ajout du rendezvous : " + e.getMessage());
        }
    }
     public void updateEntity(Rendezvous rendezvous) {
         String requete = "UPDATE rendez_vous SET nom = ?, prenom = ?, date = ?, email = ? WHERE id=?";

         try {
             if (!isValidEmail(rendezvous.getEmail())) {
                 System.out.println("Email invalide.");
                 return;
             }
             // Vérifier si la date/heure est valide
             if (!isValidDateTime(rendezvous.getDate())) {
                 System.out.println("Date/heure invalide.");
                 return;
             }
            PreparedStatement pst = MyDB.getInstance().getConnection().prepareStatement(requete);

            pst.setString(1, rendezvous.getNom());
            pst.setString(2, rendezvous.getPrenom());
            pst.setString(3, rendezvous.getDate());
            pst.setString(4, rendezvous.getEmail());
            pst.setInt(5, rendezvous.getId());

            int rowsAffected = pst.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Rendezvous Updated");
            } else {
                System.out.println("Rendezvous not found");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }




    public void deleteEntity(Rendezvous rendezvous) {
        String requete = "DELETE FROM rendez_vous WHERE id=?";
        try {
            PreparedStatement pst = MyDB.getInstance().getConnection().prepareStatement(requete);
            pst.setInt(1, rendezvous.getId());

            int rowsAffected = pst.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Rendez-vous Deleted");
            } else {
                System.out.println("Rendez-vous not found");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }



    public List<Rendezvous> getAllData() {
        List<Rendezvous> data = new ArrayList<>();
        String requete = "SELECT * FROM rendez_vous";
        try {
            Statement st = MyDB.getInstance().getConnection().createStatement();
            ResultSet rs = st.executeQuery(requete);
            while (rs.next()) {
                Rendezvous r = new Rendezvous();

                r.setId(rs.getInt("id"));
                r.setNom(rs.getString("nom"));
                r.setPrenom(rs.getString("prenom"));

                r.setDate(rs.getString("date"));
                r.setEmail(rs.getString("email"));
                data.add(r);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return data;
    }

}
