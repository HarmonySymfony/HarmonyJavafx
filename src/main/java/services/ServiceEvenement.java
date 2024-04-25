package services;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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
        String sql = "INSERT INTO evenement(nom, description, prix, placeDispo,adresse,date) VALUES (?, ? , ? , ?, ?, ?)";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, Evenement.getNom());
        statement.setString(2, Evenement.getDescription());
        statement.setFloat(3, Evenement.getPrix());
        statement.setInt(4, Evenement.getPlaceDispo());
        statement.setString(5, Evenement.getAdresse());
        statement.setDate(6, Evenement.getDate());
        statement.executeUpdate();
        System.out.println("Event Added");
    }

    @Override
    public void modifyEvent(Evenement Evenement) throws SQLException {
        String req = "UPDATE evenement SET nom=?, description=?, adresse=?, placeDispo=?,prix=?, date=? WHERE id=?";
        PreparedStatement preparedStatement = connection.prepareStatement(req);
        preparedStatement.setString(1, Evenement.getNom());
        preparedStatement.setString(2, Evenement.getDescription());
        preparedStatement.setString(3, Evenement.getAdresse());
        preparedStatement.setInt(4, Evenement.getPlaceDispo());
        preparedStatement.setFloat(5, Evenement.getPrix());
        preparedStatement.setDate(6, Evenement.getDate());

        preparedStatement.setInt(7, Evenement.getId());


        preparedStatement.executeUpdate();
        System.out.println("Event modified");
    }



    @Override
    public void Delete(int id) throws SQLException {
        // Start transaction
        connection.setAutoCommit(false);

        try {
            // Delete dependent reservations
            String deleteReservations = "DELETE FROM reservation WHERE event_id=?";
            PreparedStatement psReservations = connection.prepareStatement(deleteReservations);
            psReservations.setInt(1, id);
            psReservations.executeUpdate();

            // Delete the event
            String deleteEvent = "DELETE FROM evenement WHERE id=?";
            PreparedStatement psEvent = connection.prepareStatement(deleteEvent);
            psEvent.setInt(1, id);
            psEvent.executeUpdate();

            // Commit transaction
            connection.commit();
            System.out.println("Event and associated reservations deleted");
        } catch (SQLException e) {
            // If there is an error, rollback any changes
            connection.rollback();
            throw e; // Re-throw the exception to handle it further (e.g., logging or user feedback)
        } finally {
            // Reset auto-commit to default
            connection.setAutoCommit(true);
        }
    }


    @Override
    public List<Evenement> afficher() throws SQLException {
        List<Evenement> evenements= new ArrayList<>();
        String query = "SELECT id, nom, description, prix,  placeDispo, adresse, date FROM evenement";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Evenement evenement = new Evenement(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("description"),
                        rs.getFloat("prix"),
                        rs.getInt("placeDispo"),
                        rs.getString("adresse"),
                        rs.getDate("Date")
                );
                evenements.add(evenement);
            }
        }
        return evenements;
    }


//    public void addReservation(Reservation reservation) throws SQLException {
//        // Start a transaction
//        connection.setAutoCommit(false);
//        Savepoint savepoint = connection.setSavepoint("BeforeAddingReservation");
//
//        if (reservation.getEvenement() == null) {
//            throw new SQLException("The event associated with this reservation is null.");
//        }
//
//        try {
//            Evenement evenement = reservation.getEvenement();
//            if (reservation.getNbrPlace() > evenement.getPlaceDispo()) {
//                throw new SQLException("Not enough places available for reservation.");
//            }
//
//            // Update the Evenement with the new number of available places
//            evenement.setPlaceDispo(evenement.getPlaceDispo() - reservation.getNbrPlace());
//            String updateEventSQL = "UPDATE evenement SET placeDispo = ? WHERE id = ?";
//            try (PreparedStatement updateEventStmt = connection.prepareStatement(updateEventSQL)) {
//                updateEventStmt.setInt(1, evenement.getPlaceDispo());
//                updateEventStmt.setInt(2, evenement.getId());
//                int affectedRows = updateEventStmt.executeUpdate();
//                if (affectedRows == 0) {
//                    throw new SQLException("Updating event failed, no rows affected.");
//                }
//            }
//
//            // Insert the new Reservation into the database
//            String insertReservationSQL = "INSERT INTO reservation (nbrPlace, event_id) VALUES (?, ?)";
//            try (PreparedStatement insertReservationStmt = connection.prepareStatement(insertReservationSQL)) {
//                insertReservationStmt.setInt(1, reservation.getNbrPlace());
//                insertReservationStmt.setInt(2, evenement.getId());
//                int affectedRows = insertReservationStmt.executeUpdate();
//                if (affectedRows == 0) {
//                    throw new SQLException("Creating reservation failed, no rows affected.");
//                }
//            }
//
//            // Commit the transaction
//            connection.commit();
//        } catch (Exception ex) {
//            // Rollback the transaction if anything goes wrong
//            connection.rollback(savepoint);
//            throw ex;
//        } finally {
//            // Reset default commit behavior
//            connection.setAutoCommit(true);
//        }
//    }



    public void addReservation(Reservation reservation) throws SQLException {
        System.out.println("Attempting to add reservation...");
        connection.setAutoCommit(false);
        Savepoint savepoint = connection.setSavepoint("BeforeAddingReservation");

        try {
            if (reservation.getNbrPlace() > reservation.getEvenement().getPlaceDispo()) {
                throw new SQLException("Not enough places available.");
            }

            System.out.println("Places available before reservation: " + reservation.getEvenement().getPlaceDispo());

            // Update the Evenement with the new number of available places
            Evenement evenement = reservation.getEvenement();
            evenement.setPlaceDispo(evenement.getPlaceDispo() - reservation.getNbrPlace());
            String updateEventSQL = "UPDATE evenement SET placeDispo = ? WHERE id = ?";
            try (PreparedStatement updateEventStmt = connection.prepareStatement(updateEventSQL)) {
                updateEventStmt.setInt(1, evenement.getPlaceDispo());
                updateEventStmt.setInt(2, evenement.getId());
                updateEventStmt.executeUpdate();
            }

            String insertReservationSQL = "INSERT INTO reservation (nbrPlace, event_id) VALUES (?, ?)";
            try (PreparedStatement insertReservationStmt = connection.prepareStatement(insertReservationSQL)) {
                insertReservationStmt.setInt(1, reservation.getNbrPlace());
                insertReservationStmt.setInt(2, evenement.getId());
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


}
