package services;

import entities.Personne;
import interfaces.IServicesUser;
import utils.MyConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PersonneServices implements IServicesUser<Personne> {
    static Connection cnx;

    public PersonneServices() {
        cnx = MyConnection.getInstance().getCnx();
    }

    @Override
    public void Ajouter(Personne personne) {
        String sql = "INSERT INTO utilisateur (Nom,Prenom,Email,Password,age,role)" + " VALUES ('" + personne.getNom() + "','" + personne.getPrenom() + "','" + personne.getEmail() + "','" + personne.getPassword() + "','" + personne.getAge() + "','" + personne.getRole() + "')";
        try {
            Statement st = MyConnection.getInstance().getCnx().createStatement();
            st.executeUpdate(sql);
            System.out.println("Personne ajoutée");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }



    @Override
    public void addEntity(Personne personne) {

    }

    public void updateEntity(Personne personne) {
        try {
            // Préparer la requête SQL pour la mise à jour
            String sql = "UPDATE utilisateur SET Nom=?, Prenom=?, Email=?, Password=?, Age=?,Role=? WHERE id=?";
            PreparedStatement statement = cnx.prepareStatement(sql);

            // Définir les valeurs des paramètres de la requête
            statement.setString(1, personne.getNom());
            statement.setString(2, personne.getPrenom());
            statement.setString(3, personne.getEmail());
            statement.setString(4, personne.getPassword());
            statement.setInt(5, personne.getAge());
            statement.setString(6, personne.getRole());
            statement.setInt(7, personne.getId());

            // Exécuter la mise à jour
            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("La personne a été mise à jour avec succès !");
            } else {
                System.out.println("Échec de la mise à jour de la personne.");
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la mise à jour de la personne : " + e.getMessage());
        }
    }

    @Override
    public void deleteEntity(Personne personne) {

    }


    public List<Personne> getAllData() {
        List<Personne> data = new ArrayList<>();
        String requete = "SELECT * FROM utilisateur ";
        try {
            Statement st = MyConnection.getInstance().getCnx().createStatement();
            ResultSet rs = st.executeQuery(requete);
            while (rs.next()) {
                Personne p = new Personne("Rebai", "Saber", "saberweldzakeya@gmail.com", "aloulou123", 20,"PATIENT");
                p.setId(rs.getInt(1));
                p.setNom(rs.getString("Nom"));
                p.setPrenom(rs.getString("Prenom"));
                p.setEmail(rs.getString("Email"));
                p.setPassword(rs.getString("Password"));
                p.setRole(rs.getString("Role"));

                data.add(p);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            // Handle the exception here
        }
        return data;
    }
    public void generateAndStoreResetToken(String email, String resetToken) {
        try {
            String sql = "UPDATE utilisateur SET ResetToken = ? WHERE Email = ?";
            PreparedStatement statement = cnx.prepareStatement(sql);
            statement.setString(1, resetToken);
            statement.setString(2, email);
            statement.executeUpdate();
            System.out.println("Jeton de réinitialisation généré et stocké avec succès !");
        } catch (SQLException e) {
            System.out.println("Erreur lors de la génération et du stockage du jeton de réinitialisation : " + e.getMessage());
        }
    }

    public boolean updatePasswordWithToken(String newPassword, String resetToken) {
        try {
            // Vérifiez d'abord si le service est null
            if (this == null || cnx == null) {
                System.out.println("PersonneServices is not initialized!");
                return false;
            }

            // Vérifiez ensuite si le jeton de réinitialisation est valide
            if (!isResetTokenValid(resetToken)) {
                System.out.println("Le jeton de réinitialisation n'est pas valide.");
                return false;
            }

            // Mettez à jour le mot de passe correspondant à ce jeton
            String sql = "UPDATE user SET Password = ? WHERE ResetToken = ?";
            PreparedStatement statement = cnx.prepareStatement(sql);
            statement.setString(1, newPassword);
            statement.setString(2, resetToken);

            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Mot de passe mis à jour avec succès !");
                return true;
            } else {
                System.out.println("Échec de la mise à jour du mot de passe.");
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la mise à jour du mot de passe : " + e.getMessage());
            return false;
        }
    }


    private boolean isResetTokenValid(String resetToken) {
        // Implémentez ici la logique pour vérifier si le jeton de réinitialisation est valide
        // Vous pouvez rechercher le jeton dans la base de données et vérifier s'il est expiré ou non
        // Retournez true si le jeton est valide, sinon false
        // Cette méthode doit être adaptée à votre modèle de données et à votre logique de gestion des jetons de réinitialisation
        return true; // Placeholder pour la validation réussie
    }


    public void supprimer(Personne t) throws SQLException {
        String req = "Delete from utilisateur where id=?";
        PreparedStatement stmt = cnx.prepareStatement(req);
        stmt.setInt(1, t.getId());
        stmt.executeUpdate();
        System.out.println(" suppression établie!");
    }



    public List<Personne> rechercherParNom(String nom) throws SQLException {
        List<Personne> users = new ArrayList<>();
        String req = "SELECT * FROM utilisateur WHERE (Nom LIKE '%" + nom + "%' OR Email LIKE '%" + nom + "%' OR Prenom LIKE '%" + nom + "%')";
        Statement stm = cnx.createStatement();
        ResultSet rs = stm.executeQuery(req);

        while (rs.next()) {
            Personne p = new Personne();
            p.setId(rs.getInt("id"));
            p.setEmail(rs.getString("Email"));
            p.setNom(rs.getString("Nom"));
            p.setPrenom(rs.getString("Prenom"));
            p.setPassword(rs.getString("Password"));
            p.setRole(rs.getString("Role"));

            users.add(p);
        }
        return users;
    }

    public List<Personne> recupererParPrenom(String prenom) throws SQLException {
        List<Personne> personnes = new ArrayList<>();
        String query = "SELECT id, Nom, Prenom, Email, Password,Role FROM utilisateur WHERE Prenom = ?";
        try (PreparedStatement statement = cnx.prepareStatement(query)) {
            statement.setString(1, prenom);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Personne personne = new Personne();
                    personne.setId(resultSet.getInt("id"));
                    personne.setNom(resultSet.getString("Nom"));
                    personne.setPrenom(resultSet.getString("Prenom"));
                    personne.setEmail(resultSet.getString("Email"));
                    personne.setPassword(resultSet.getString("Password"));
                    personne.setRole(resultSet.getString("Role"));
                    personnes.add(personne);
                }
            }
        }
        return personnes;
    }
    public static void resetPassword(String email, String newPassword) {
        try {
            String sql = "UPDATE utilisateur SET Password=? WHERE Email=?";
            PreparedStatement statement = cnx.prepareStatement(sql);

            // Set the new password and the email of the user
            statement.setString(1, newPassword);
            statement.setString(2, email);

            // Execute the update
            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("The password was updated successfully!");
            } else {
                System.out.println("The password update failed. No user with this email found.");
            }
        } catch (SQLException e) {
            System.out.println("Error updating password: " + e.getMessage());
        }
    }
    public boolean checkTempPassword(String tempPassword) {
        try {
            String sql = "SELECT * FROM utilisateur WHERE Password=?";
            PreparedStatement statement = cnx.prepareStatement(sql);

            // Set the temporary password
            statement.setString(1, tempPassword);

            // Execute the query
            ResultSet resultSet = statement.executeQuery();

            // If the query returns a result, the temporary password is valid
            if (resultSet.next()) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Error checking temporary password: " + e.getMessage());
            return false;
        }
    }
    public Personne getUserById(int userId) {
        // Replace with your actual SQL query
        String query = "SELECT * FROM users WHERE id = ?";

        try (PreparedStatement statement = cnx.prepareStatement(query)) {
            statement.setInt(1, userId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    // Replace with your actual column names
                    int id = resultSet.getInt("id");
                    String nom = resultSet.getString("nom");
                    String prenom = resultSet.getString("prenom");
                    String email = resultSet.getString("email");
                    String password = resultSet.getString("password");
                    int age = resultSet.getInt("age");
                    String role = resultSet.getString("role");

                    return new Personne(id, nom, prenom, email, password, age, role);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}
