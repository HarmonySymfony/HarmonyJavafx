package services;

import entities.Analyse;
import entities.Laboratoire;
import utils.DB;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    public Set<Analyse> getAnalysesForLaboratoire(Laboratoire laboratoire) throws SQLException {
        Set<Analyse> analyses = new HashSet<>();

        String request = "SELECT * FROM analyses WHERE laboratoireId = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(request);
        preparedStatement.setInt(1, laboratoire.getId());
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            float prix = resultSet.getFloat("prix");
            String type = resultSet.getString("type");
            Analyse analyse = new Analyse();
            analyse.setId(id);
            analyse.setPrix(prix);
            analyse.setType(type);
            System.out.println(analyse);
            analyses.add(analyse);
            System.out.println(analyses);

        }
        System.out.println("in service");
        System.out.println(analyses);
        return analyses;
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
    public List<Laboratoire> rechercherParNom(String nom) throws SQLException {
        List<Laboratoire> laboratoireList = new ArrayList<>();
        String request = "SELECT * FROM laboratoire WHERE nom LIKE ?";
        PreparedStatement preparedStatement = connection.prepareStatement(request);
        preparedStatement.setString(1, "%" + nom + "%");
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String nomLab = resultSet.getString("nom");
            String emplacement = resultSet.getString("emplacement");

            Laboratoire laboratoire = new Laboratoire(id, nomLab, emplacement);
            laboratoireList.add(laboratoire);
        }
        return laboratoireList;
    }

    public List<Laboratoire> rechercherParEmplacement(String emplacement) throws SQLException {
        List<Laboratoire> laboratoireList = new ArrayList<>();
        String request = "SELECT * FROM laboratoire WHERE emplacement LIKE ?";
        PreparedStatement preparedStatement = connection.prepareStatement(request);
        preparedStatement.setString(1, "%" + emplacement + "%");
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String nom = resultSet.getString("nom");
            String emplacementLab = resultSet.getString("emplacement");

            Laboratoire laboratoire = new Laboratoire(id, nom, emplacementLab);
            laboratoireList.add(laboratoire);
        }
        return laboratoireList;
    }

}