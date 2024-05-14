package services;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import entities.Evenement;
import entities.Personne;
import entities.Reservation;
import interfaces.IServices;
import interfaces.IServicesEvenement;
import utils.MyConnection;

public class ServiceEvenement implements IServicesEvenement<Evenement> {

    Connection connection;

    public ServiceEvenement() { connection= MyConnection.getInstance().getCnx();}



    @Override
    public void Add(Evenement Evenement) throws SQLException {
        String sql = "INSERT INTO evenement(nom, description, prix, place_dispo,adresse,date_event,latitude,longitude) VALUES (?, ? , ? , ?, ?, ?, ?, ?)";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, Evenement.getNom());
        statement.setString(2, Evenement.getDescription());
        statement.setFloat(3, Evenement.getPrix());
        statement.setInt(4, Evenement.getPlace_dispo());
        statement.setString(5, Evenement.getAdresse());
        statement.setDate(6, Evenement.getDate_event());
        statement.setDouble(7,Evenement.getLatitude());
        statement.setDouble(8,Evenement.getLongitude());
        statement.executeUpdate();
        System.out.println("Event Added");
    }

    @Override
    public void modifyEvent(Evenement Evenement) throws SQLException {
        String req = "UPDATE evenement SET nom=?, description=?, adresse=?, place_dispo=?,prix=?, date_event=? WHERE id=?";
        PreparedStatement preparedStatement = connection.prepareStatement(req);
        preparedStatement.setString(1, Evenement.getNom());
        preparedStatement.setString(2, Evenement.getDescription());
        preparedStatement.setString(3, Evenement.getAdresse());
        preparedStatement.setInt(4, Evenement.getPlace_dispo());
        preparedStatement.setFloat(5, Evenement.getPrix());
        preparedStatement.setDate(6, Evenement.getDate_event());

        preparedStatement.setInt(7, Evenement.getId());


        preparedStatement.executeUpdate();
        System.out.println("Event modified");
    }



    @Override
    public void Delete(int id) throws SQLException {
        connection.setAutoCommit(false);

        try {
            String deleteReservations = "DELETE FROM reservation WHERE idevent=?";
            PreparedStatement psReservations = connection.prepareStatement(deleteReservations);
            psReservations.setInt(1, id);
            psReservations.executeUpdate();

            String deleteEvent = "DELETE FROM evenement WHERE id=?";
            PreparedStatement psEvent = connection.prepareStatement(deleteEvent);
            psEvent.setInt(1, id);
            psEvent.executeUpdate();

            connection.commit();
            System.out.println("Event and associated reservations deleted");
        } catch (SQLException e) {
            connection.rollback();
            throw e;
        } finally {
            connection.setAutoCommit(true);
        }
    }


    @Override
    public List<Evenement> afficher() throws SQLException {
        List<Evenement> evenements= new ArrayList<>();
        String query = "SELECT id, nom, description, prix,  place_dispo, adresse, date_event FROM evenement";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Evenement evenement = new Evenement(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("description"),
                        rs.getFloat("prix"),
                        rs.getInt("place_dispo"),
                        rs.getString("adresse"),
                        rs.getDate("date_event")
                );
                evenements.add(evenement);
            }
        }
        return evenements;
    }

    public void addReservation(Reservation reservation) throws SQLException {
        System.out.println("Attempting to add reservation...");
        connection.setAutoCommit(false);
        Savepoint savepoint = connection.setSavepoint("BeforeAddingReservation");

        try {
            if (reservation.getNbrPlace() > reservation.getEvenement().getPlace_dispo()) {
                throw new SQLException("Not enough places available.");
            }

            System.out.println("Places available before reservation: " + reservation.getEvenement().getPlace_dispo());

            Evenement evenement = reservation.getEvenement();
            evenement.setPlace_dispo(evenement.getPlace_dispo() - reservation.getNbrPlace());
            String update_eventEventSQL = "UPDATE evenement SET place_dispo = ? WHERE id = ?";
            try (PreparedStatement update_eventEventStmt = connection.prepareStatement(update_eventEventSQL)) {
                update_eventEventStmt.setInt(1, evenement.getPlace_dispo());
                update_eventEventStmt.setInt(2, evenement.getId());

                update_eventEventStmt.executeUpdate();
            }

            String insertReservationSQL = "INSERT INTO reservation (nbrPlace, idevent, approuve) VALUES (?, ?, ?)";
            try (PreparedStatement insertReservationStmt = connection.prepareStatement(insertReservationSQL)) {
                insertReservationStmt.setInt(1, reservation.getNbrPlace());

                insertReservationStmt.setInt(2, evenement.getId());
                insertReservationStmt.setInt(3, reservation.getApprouve());

                insertReservationStmt.executeUpdate();
            }

            connection.commit();
            System.out.println("Reservation added successfully: " + reservation.getNbrPlace() + " places.");
        } catch (Exception ex) {
            connection.rollback(savepoint);
            System.out.println("Failed to add reservation, transaction rolled back.");
            throw ex;
        } finally {
            connection.setAutoCommit(true);
        }
    }

    public int getReservedPlacesForEvent(int eventId) throws SQLException {
        String query = "SELECT SUM(nbrPlace) AS totalReserved FROM reservation WHERE idevent = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, eventId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("totalReserved");
                }
            }
        }
        return 0;
    }


    public void saveComment(int eventId, String comment) {
    }

    public List<String> getComments(int eventId) {
        return new ArrayList<>();
    }


    public void saveRating(int eventId, double rating) {
    }





}