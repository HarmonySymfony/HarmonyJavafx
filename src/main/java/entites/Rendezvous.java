package entites;

import java.util.List;

public class Rendezvous {
   private int id;
   private String nom;
    private String prenom;
    private String date;
    private String email;



    public Rendezvous(int id, String nom, String prenom, String date, String email) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.date = date;
        this.email = email;
    }

    public Rendezvous(String nom, String prenom, String date, String email) {
        this.nom = nom;
        this.prenom = prenom;
        this.date = date;
        this.email = email;
    }


    public Rendezvous() {

    }

    public static void savePDF(List<Rendezvous> rendezvousList, String fileName) {
    }

    public int getId() {
        return id;

    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Rendezvous{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", date='" + date + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
