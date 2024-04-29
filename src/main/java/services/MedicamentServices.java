package services;

import entities.medicament;
import interfaces.IServices;
import utils.MyConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MedicamentServices implements IServices <medicament> {
    @Override
    public void addEntity(medicament medicament) {
        String requete = "INSERT INTO medicament(reference,stock,disponibilite,description,prix)" + " VALUES ('"+medicament.getReference()+"','"+medicament.getStock()+"','"+medicament.getDisponibilite()+"','"+medicament.getDescription()+"','"+medicament.getPrix()+"')";
        try {
            Statement st = MyConnection.getInstance().getCnx().createStatement();
            st.executeUpdate(requete);
            System.out.println("medicament ajoutée");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public void ajoutMedicament(medicament medicament) {
        String requete = "INSERT INTO medicament (reference,stock,disponibilite,description,prix) VALUES (?,?)";
        try {
            PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(requete);
            pst.setString(1, medicament.getReference());
            pst.setInt(2, medicament.getStock());
            pst.setString(2, medicament.getDisponibilite());
            pst.setString(2, medicament.getDescription());
            pst.setInt(2, medicament.getPrix());
            pst.executeUpdate();
            System.out.println("medicament added");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void updateEntity(medicament medicament) {
        String requete = "UPDATE medicament SET reference=?, stock=?, disponibilite=?, description=?, prix=? WHERE id=?";
        try {
            PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(requete);
            pst.setString(1, medicament.getReference());
            pst.setInt(2, medicament.getStock());
            pst.setString(3, medicament.getDisponibilite());
            pst.setString(4, medicament.getDescription());
            pst.setInt(5, medicament.getPrix());
            pst.setInt(6, medicament.getId()); // Assurez-vous d'ajouter l'ID à la fin
            pst.executeUpdate();
            System.out.println("Médicament mis à jour");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void deleteEntity(medicament medicament) {
        int id = medicament.getId(); // Extraire l'ID du médicament
        String requete = "DELETE FROM medicament WHERE id = ?";
        try {
            PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(requete);
            pst.setInt(1, id);
            pst.executeUpdate();
            System.out.println("Médicament supprimé avec succès");
        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression du médicament: " + e.getMessage());
        }
    }



    public List<medicament> getAllData() {
        List<medicament> data=new ArrayList<>();
        String requete = "SELECT * FROM  medicament ";
        try {
            Statement st =  MyConnection.getInstance().getCnx().createStatement();
            ResultSet rs= st.executeQuery(requete);
            while (rs.next()){
                medicament m = new medicament();
                m.setId(rs.getInt(1));
                m.setReference(rs.getString("reference"));
                m.setStock(rs.getInt("stock"));
                m.setDisponibilite(rs.getString("disponibilite"));
                m.setDescription(rs.getString("description"));
                m.setPrix(rs.getInt("prix"));

                data.add(m);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());

        }
        return data;
    }
}
