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
        String query = "INSERT INTO analyse (prix, type,laboratoireId) VALUES (?,?,?)";
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

    @Override
    public List<Analyse> Display() throws SQLException {
        List<Analyse> analyseList = new ArrayList<>();
        String request = "SELECT * FROM analyse";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(request);
        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            float prix = resultSet.getFloat("prix");
            String type = resultSet.getString("type");

            Analyse analyse = new Analyse(id,prix,type);
            analyseList.add(analyse);
        }
        return analyseList;
    }
}
