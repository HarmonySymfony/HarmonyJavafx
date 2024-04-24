package services;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import entities.Evenement;
import entities.Personne;
import interfaces.IServices;
import interfaces.IServicesEvenement;
import utils.MyConnection;

public class ServiceEvenement implements IServicesEvenement<Evenement> {

     Connection connection;

    public ServiceEvenement() { connection= MyConnection.getInstance().getCnx();}



    @Override
    public void Add(Evenement Evenement) throws SQLException {
        String sql = "INSERT INTO evenement(nom, description, prix, placeDispo,adresse) VALUES (?, ? , ? , ?, ?)";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, Evenement.getNom());
        statement.setString(2, Evenement.getDescription());
        statement.setFloat(3, Evenement.getPrix());
        statement.setInt(4, Evenement.getPlaceDispo());
        statement.setString(5, Evenement.getAdresse());
        statement.executeUpdate();
        System.out.println("Event Added");
    }

    @Override
    public void modifyEvent(Evenement Evenement) throws SQLException {
        String req = "UPDATE evenement SET nom=?, description=?, adresse=?, placeDispo=?,prix=? WHERE id=?";
        PreparedStatement preparedStatement = connection.prepareStatement(req);
        preparedStatement.setString(1, Evenement.getNom());
        preparedStatement.setString(2, Evenement.getDescription());
        preparedStatement.setString(3, Evenement.getAdresse());
        preparedStatement.setInt(4, Evenement.getPlaceDispo());
        preparedStatement.setFloat(5, Evenement.getPrix());
        preparedStatement.setInt(6, Evenement.getId());

        preparedStatement.executeUpdate();
        System.out.println("Event modified");
    }



    @Override
    public void Delete(int id) throws SQLException {
        String req = "DELETE FROM evenement WHERE id=?";
        PreparedStatement preparedStatement = connection.prepareStatement(req);
        preparedStatement.setInt(1, id);
        preparedStatement.executeUpdate();
        System.out.println("Event deleted");
    }

   @Override
   public List<Evenement> afficher() throws SQLException {
        List<Evenement> evenements= new ArrayList<>();
        String query = "SELECT id, nom, description, prix,  placeDispo, adresse FROM evenement";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Evenement evenement = new Evenement(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("description"),
                        rs.getInt("prix"),
                        rs.getInt("placeDispo"),
                        rs.getString("adresse")
                );
                evenements.add(evenement);
            }
        }
        return evenements;
    }

}
