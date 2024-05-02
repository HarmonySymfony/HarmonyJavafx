package services;

import entities.Personne;
import interfaces.IServicesUser;
import utils.MyConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PersonneServices implements IServicesUser<Personne> {
    Connection cnx;

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

    public boolean existemail(String Email) {
        boolean exist = false;
        String sql = "SELECT * FROM utilisateur WHERE Email = ?";
        try {
            PreparedStatement ps = cnx.prepareStatement(sql);
            ps.setString(1, Email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                exist = true;
            }
        } catch (SQLException ex) {
            // Handle or log the exception appropriately
            System.err.println("An error occurred while executing SQL query: " + ex.getMessage());
        }
        return exist;
    }

    public void addEntity2(Personne personne) {
        String requete = "INSERT INTO utilisateur(Nom,Prenom,Email,Password,age,role) VALUES (?,?,?,?,?,?)";
        try {
            PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(requete);
            pst.setString(1, personne.getNom());
            pst.setString(2, personne.getPrenom());
            pst.setString(3, personne.getEmail());
            pst.setString(4, personne.getPassword());
            pst.setInt(5, personne.getAge());
            pst.setString(6, personne.getRole());

            pst.executeUpdate();
            System.out.println("Personne added");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void Ajouter(controllers.Personne personne) {

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
        // Ajouter la logique de suppression ici si nécessaire
    }

    public Personne getPersonneById(int id) throws SQLException {

        Personne personne = null;
        String query = "SELECT * FROM utilisateur WHERE id = ?";
        try (PreparedStatement statement = cnx.prepareStatement(query)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    personne = new Personne();
                    personne.setId(resultSet.getInt("id"));
                    personne.setNom(resultSet.getString("Nom"));
                    personne.setPrenom(resultSet.getString("Prenom"));
                    personne.setEmail(resultSet.getString("Email"));
                    personne.setPassword(resultSet.getString("Password"));
                    personne.setAge(resultSet.getInt("Age"));
                    personne.setRole(resultSet.getString("Role"));
                }
            }
        }
        return personne;
    }

    public Personne recuperer2(int id) throws SQLException {
        String query = "SELECT * FROM utilisateur WHERE id = ?";
        try (PreparedStatement statement = cnx.prepareStatement(query)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Personne personne = new Personne();
                    personne.setId(resultSet.getInt("id"));
                    personne.setNom(resultSet.getString("Nom"));
                    personne.setPrenom(resultSet.getString("Prenom"));
                    personne.setEmail(resultSet.getString("Email"));
                    personne.setPassword(resultSet.getString("Password"));
                    personne.setAge(resultSet.getInt("Age"));
                    personne.setRole(resultSet.getString("Role"));
                    return personne;
                }
            }
        }
        return null; // Retourne null si aucune personne n'est trouvée avec cet ID
    }

    public List<Personne> recuperer() throws SQLException {
        List<Personne> personnes = new ArrayList<>();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = MyConnection.getInstance().getCnx(); // Utilisation de MyConnection.getConnection()
            String query = "SELECT id, Nom, Prenom, Email, Password,Role FROM utilisateur"; // Assurez-vous de spécifier le nom de votre table
            statement = connection.prepareStatement(query);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                // Création d'un objet Personne à partir des données récupérées de la base de données
                Personne personne = new Personne();
                personne.setId(resultSet.getInt("id"));
                personne.setNom(resultSet.getString("Nom"));
                personne.setPrenom(resultSet.getString("Prenom"));
                personne.setEmail(resultSet.getString("Email"));
                personne.setPassword(resultSet.getString("Password"));
                personne.setRole(resultSet.getString("Role"));

                // Ajout de la personne à la liste
                personnes.add(personne);
            }
        } finally {
            // Fermeture des ressources
            if (resultSet != null) {
                resultSet.close();
            }
            if (statement != null) {
                statement.close();
            }
            // Notez que nous n'avons pas besoin de fermer explicitement la connexion ici car elle est obtenue à partir de MyConnection.getInstance().getCnx(),
            // et sa gestion de la connexion est gérée ailleurs (probablement dans MyConnection)
        }

        return personnes;
    }

    public void modifier(Personne t) throws SQLException {
        String req = "Update utilisateur set Nom=?, Prenom=?, Email=?, Password=?,Age=?,Role where id=?";
        PreparedStatement stmt = cnx.prepareStatement(req);
        stmt.setString(1, t.getNom());
        stmt.setString(2, t.getPrenom());
        stmt.setString(3, t.getEmail());
        stmt.setString(4, t.getRole());
        stmt.setInt(5, t.getId());

        stmt.executeUpdate();

        System.out.println(" modification établie!");
    }

    public void supprimer(Personne t) throws SQLException {
        String req = "Delete from utilisateur where id=?";
        PreparedStatement stmt = cnx.prepareStatement(req);
        stmt.setInt(1, t.getId());
        stmt.executeUpdate();
        System.out.println(" suppression établie!");
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
            String sql = "UPDATE utilisateur SET Password = ? WHERE ResetToken = ?";
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

    @Override
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
        }
        return data;
    }
}