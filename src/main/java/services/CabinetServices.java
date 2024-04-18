package services;
import entites.Cabinet;

import outils.MyDB;
import java.sql.ResultSet;



import java.sql.PreparedStatement;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.text.SimpleDateFormat;
import java.text.ParseException;

public class CabinetServices implements IservicesCabinet<Cabinet> {


    public boolean entityExists(Cabinet cabinet) {
        try {
            // Vérifier si le rendez-vous existe dans la base de données en utilisant une requête SELECT
            String requete = "SELECT * FROM cabinet WHERE adress = ? AND nom = ? AND horaires = ? AND email = ?";
            PreparedStatement pst = MyDB.getInstance().getConnection().prepareStatement(requete);
            pst.setString(1, cabinet.getAdress());
            pst.setString(2, cabinet.getNom());
            pst.setString(3, cabinet.getHoraires());
            pst.setString(4, cabinet.getEmail());
            ResultSet rs = pst.executeQuery();
            return rs.next(); // Renvoie true si le résultat de la requête n'est pas vide (le rendez-vous existe déjà), sinon false
        } catch (SQLException e) {
            // Gérer les exceptions SQLException
            System.out.println("Erreur lors de la vérification de l'existence du cabinet : " + e.getMessage());
            return false; // En cas d'erreur, retourner false par défaut
        }
    }


    public  boolean isValidEmail(String email) {
        // Expression régulière pour valider un email
        String regex = "^(.+)@(.+)$";
        return email.matches(regex);
    }

    public  boolean isValidDateTime(String dateTime) {
        // Format de date/heure attendu
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        sdf.setLenient(false);
        try {
            // Tentative de parsing de la chaîne en date/heure
            sdf.parse(dateTime);
            return true;
        } catch (ParseException e) {
            return false; // Si le parsing échoue, la date/heure est invalide
        }
    }
    public void addEntity(Cabinet cabinet) {


    }



    public  void addEntity2(Cabinet cabinet) {
        try {
            // Vérifier si le rendez-vous existe déjà dans la base de données
            if (entityExists(cabinet)) {
                System.out.println("Le Cabinet existe déjà dans la base de données.");
            } else {
                // Vérifier si l'email est valide
                if (!isValidEmail(cabinet.getEmail())) {
                    System.out.println("Email invalide.");
                    return;
                }
                // Vérifier si les horaires sont valides
                if (!isValidDateTime(cabinet.getHoraires())) {
                    System.out.println("Horaires invalides.");
                    return;
                }

                // Ajouter le rendez-vous à la base de données s'il n'existe pas déjà
                String requete = "INSERT INTO cabinet (adress,nom,horaires,email) VALUES (?,?,?,?)";
                PreparedStatement pst = MyDB.getInstance().getConnection().prepareStatement(requete);
                pst.setString(1, cabinet.getAdress());
                pst.setString(2, cabinet.getNom());
                pst.setString(3, cabinet.getHoraires());
                pst.setString(4, cabinet.getEmail());
                pst.executeUpdate();
                System.out.println("Cabinet ajouté avec succès !");
            }
        } catch (SQLException e) {
            // Gérer les exceptions SQLException
            System.out.println("Erreur lors de l'ajout du Cabinet : " + e.getMessage());
        }
    }





    public void updateEntity(Cabinet cabinet) {
        String requete = "UPDATE cabinet SET adress = ?, nom = ?, horaires = ?, email = ? WHERE id=?";

        try {


                // Vérifier si l'email est valide
                if (!isValidEmail(cabinet.getEmail())) {
                    System.out.println("Email invalide.");
                    return;
                }
                // Vérifier si les horaires sont valides
                if (!isValidDateTime(cabinet.getHoraires())) {
                    System.out.println("Horaires invalides.");
                    return;
                }

            PreparedStatement pst = MyDB.getInstance().getConnection().prepareStatement(requete);

            pst.setString(1, cabinet.getAdress());
            pst.setString(2, cabinet.getNom());
            pst.setString(3, cabinet.getHoraires());
            pst.setString(4, cabinet.getEmail());
            pst.setInt(5, cabinet.getId());

            int rowsAffected = pst.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Cabinet Updated");
            } else {
                System.out.println("Cabinet not found");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    public void deleteEntity(Cabinet cabinet) {
        String requete = "DELETE FROM cabinet WHERE id=?";
        try {
            PreparedStatement pst = MyDB.getInstance().getConnection().prepareStatement(requete);
            pst.setInt(1, cabinet.getId());

            int rowsAffected = pst.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Cabinet Deleted");
            } else {
                System.out.println("Cabinet not found");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }




     public static List<Cabinet> getAllDataCabinet() {
        List<Cabinet> data = new ArrayList<>();
        String requete = "SELECT * FROM cabinet";
        try {
            Statement st = MyDB.getInstance().getConnection().createStatement();
            ResultSet rs = st.executeQuery(requete);
            while (rs.next()) {
                Cabinet c = new Cabinet();

                c.setId(rs.getInt("id"));
                c.setAdress(rs.getString("Adress"));
                c.setNom(rs.getString("nom"));

                c.setHoraires(rs.getString("horaires"));
                c.setEmail(rs.getString("email"));
                data.add(c);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return data;
    }



















}
