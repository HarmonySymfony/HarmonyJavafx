package services;
import entities.Analyse;
import entities.Laboratoire;
import javafx.util.Pair;
import utils.DB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
public class AnalyseService implements IServiceLabo<Analyse>{
    private Connection connection ;
    public AnalyseService(){connection = (Connection) DB.getInstance().getConnection();}



    @Override
    public void add(Analyse analyse) throws SQLException {

    }

    @Override
    public void add(Analyse analyse, int laboratoireId) throws SQLException {
        String query = "INSERT INTO Analyse (prix, type,laboratoire_id) VALUES (?,?,?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setFloat(1, analyse.getPrix());
            statement.setString(2, analyse.getType());
            statement.setInt(3, laboratoireId);
            statement.executeUpdate();
        }
    }

    @Override
    public void update(Analyse analyse) throws SQLException {
        String request = "UPDATE analyse SET prix=?, type=? WHERE id=?";
        PreparedStatement preparedStatement = connection.prepareStatement(request);
        preparedStatement.setFloat(1, analyse.getPrix());
        preparedStatement.setString(2, analyse.getType());

        preparedStatement.setInt(3, analyse.getId()); // Set the ID for the WHERE clause
        preparedStatement.executeUpdate();
    }

    @Override
    public void delete(Analyse analyse) throws SQLException {
        String request = "DELETE FROM analyse WHERE id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(request);
        preparedStatement.setInt(1, analyse.getId());
        preparedStatement.executeUpdate();
    }


    public List<Analyse> Display() throws SQLException{
        List<Analyse> analyseList = new ArrayList<>();
        String request = "SELECT * FROM analyse";
        Statement statement = connection.createStatement();
        ResultSet resultSet = null;
        try {
            resultSet = statement.executeQuery(request);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            float prix = resultSet.getFloat("prix");
            String type = resultSet.getString("type");

            Analyse analyse = new Analyse(prix,type,id);
            analyseList.add(analyse);
        }
        return analyseList;
    }
    public List<Pair<Laboratoire, Integer>> getStatistiquesNombreAnalysesParLaboratoire() throws SQLException {
        List<Pair<Laboratoire, Integer>> statistiques = new ArrayList<>();
        LaboratoireService laboratoireService = new LaboratoireService(); // Créez une instance de LaboratoireService
        String query = "SELECT laboratoire_id, COUNT(*) AS nbAnalyses FROM Analyse GROUP BY laboratoireId";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int laboratoireId = resultSet.getInt("laboratoireId");
                int nbAnalyses = resultSet.getInt("nbAnalyses");
                // Récupérez le laboratoire à partir de son ID en utilisant la méthode getById()
                Laboratoire laboratoire = laboratoireService.getById(laboratoireId); // Appelez la méthode depuis l'instance de LaboratoireService
                if (laboratoire != null) {
                    statistiques.add(new Pair<>(laboratoire, nbAnalyses));
                }
            }
        }
        return statistiques;
    }





}
