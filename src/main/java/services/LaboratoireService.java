package services;

import entities.Analyse;
import entities.Laboratoire;
import utils.DB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LaboratoireService implements IService<Laboratoire>{
    private Connection connection ;
    public LaboratoireService(){connection = DB.getInstance().getConnection();}

    @Override
    public void add(Laboratoire laboratoire) throws SQLException {
        String request ="insert into laboratoire ( nom,emplacement)"+
                "values('"+ laboratoire.getNom()+
                "','"+laboratoire.getEmplacement()+"')"
                ;
        Statement statement = connection.createStatement();
        statement.executeUpdate(request);
    }

    @Override
    public void add(Laboratoire laboratoire, int a) throws SQLException {

    }

    @Override
    public void update(Laboratoire laboratoire) throws SQLException {
        String request = "UPDATE laboratoire SET nom=?, emplacement=? WHERE id=?";
        PreparedStatement preparedStatement = connection.prepareStatement(request);
        preparedStatement.setString(1, laboratoire.getNom());
        preparedStatement.setString(2, laboratoire.getEmplacement());

        preparedStatement.setInt(3, laboratoire.getId()); // Set the ID for the WHERE clause
        preparedStatement.executeUpdate();
    }

    @Override
    public void delete(Laboratoire laboratoire) throws SQLException {
        String request = "DELETE FROM laboratoire WHERE id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(request);
        preparedStatement.setInt(1, laboratoire.getId());
        preparedStatement.executeUpdate();
    }

    @Override
    public List<Laboratoire> Display() throws SQLException {
        List<Laboratoire> laboratoireList = new ArrayList<>();
        String request = "SELECT * FROM laboratoire";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(request);
        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String nom = resultSet.getString("nom");
            String emplacement = resultSet.getString("emplacement");

            Laboratoire laboratoire = new Laboratoire(id,nom,emplacement);
            laboratoireList.add(laboratoire);
        }
        return laboratoireList;

    }
    public boolean existeNom(String nom) throws SQLException {
        String request = "SELECT COUNT(*) AS count FROM laboratoire WHERE nom = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(request);
        preparedStatement.setString(1, nom);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            int count = resultSet.getInt("count");
            return count > 0;
        }
        return false;
    }

    public boolean existeEmplacement(String emplacement) throws SQLException {
        String request = "SELECT COUNT(*) AS count FROM laboratoire WHERE emplacement = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(request);
        preparedStatement.setString(1, emplacement);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            int count = resultSet.getInt("count");
            return count > 0;
        }
        return false;
    }
}
