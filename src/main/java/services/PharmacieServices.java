package services;

import entities.pharmacie;
import interfaces.IServices;
import utils.MyConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class PharmacieServices implements IServices <pharmacie> {
    @Override
    public void addEntity(pharmacie pharmacie) {
        String requete = "INSERT INTO pharmacie(nom,adress,type)" + " VALUES ('"+pharmacie.getNom()+"','"+pharmacie.getAdress()+"','"+pharmacie.getType()+"')";
        try {
            Statement st = MyConnection.getInstance().getCnx().createStatement();
            st.executeUpdate(requete);
            System.out.println("Pharmacie ajoutée");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public void ajoutPharmacie(pharmacie pharmacie) {
        String requete = "INSERT INTO pharmacie (nom,adress,type) VALUES (?,?)";
        try {
            PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(requete);
            pst.setString(1, pharmacie.getNom());
            pst.setString(2, pharmacie.getAdress());
            pst.setString(2, pharmacie.getType());
            pst.executeUpdate();
            System.out.println("Pharmacie added");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void updateEntity(pharmacie pharmacie) {
        String requete = "UPDATE pharmacie SET nom=?, adress=?, type=? WHERE id=?";
        try {
            PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(requete);
            pst.setString(1, pharmacie.getNom());
            pst.setString(2, pharmacie.getAdress());
            pst.setString(3, pharmacie.getType());
            pst.setInt(4, pharmacie.getId());
            pst.executeUpdate();
            System.out.println("Pharmacie mise à jour");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    @Override
    public void deleteEntity(pharmacie pharmacie) {
        String requete = "DELETE FROM pharmacie WHERE id = ?";
        try {
            PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(requete);
            pst.setInt(1, pharmacie.getId());
            pst.executeUpdate();
            System.out.println("Pharmacie supprimée");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }


    public List<pharmacie> getAllData() {
        List<pharmacie> data=new ArrayList<>();
        String requete = "SELECT * FROM  pharmacie ";
        try {
            Statement st =  MyConnection.getInstance().getCnx().createStatement();
           ResultSet rs= st.executeQuery(requete);
           while (rs.next()){
               pharmacie p = new pharmacie();
               p.setId(rs.getInt(1));
               p.setNom(rs.getString("nom"));
               p.setAdress(rs.getString("adress"));
               p.setType(rs.getString("type"));
               data.add(p);
           }
        } catch (SQLException e) {
            System.out.println(e.getMessage());

        }
        return data;
    }
}
