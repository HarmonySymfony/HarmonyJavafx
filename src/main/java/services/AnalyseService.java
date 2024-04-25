package services;
import entities.Analyse;
import utils.DB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
public class AnalyseService implements IService<Analyse>{
    private Connection connection ;
    public AnalyseService(){connection = (Connection) DB.getInstance().getConnection();}



    @Override
    public void add(Analyse analyse) throws SQLException {

    }

    @Override
    public void add(Analyse analyse, int laboratoireId) throws SQLException {
        String query = "INSERT INTO Analyses (prix, type,laboratoireId) VALUES (?,?,?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setFloat(1, analyse.getPrix());
            statement.setString(2, analyse.getType());
            statement.setInt(3, laboratoireId);
            statement.executeUpdate();
        }
    }

    @Override
    public void update(Analyse analyse) throws SQLException {
        String request = "UPDATE analyses SET prix=?, type=? WHERE id=?";
        PreparedStatement preparedStatement = connection.prepareStatement(request);
        preparedStatement.setFloat(1, analyse.getPrix());
        preparedStatement.setString(2, analyse.getType());

        preparedStatement.setInt(3, analyse.getId()); // Set the ID for the WHERE clause
        preparedStatement.executeUpdate();
    }

    @Override
    public void delete(Analyse analyse) throws SQLException {
        String request = "DELETE FROM analyses WHERE id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(request);
        preparedStatement.setInt(1, analyse.getId());
        preparedStatement.executeUpdate();
    }


    public List<Analyse> Display() throws SQLException{
        List<Analyse> analyseList = new ArrayList<>();
        String request = "SELECT * FROM analyses";
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
}
