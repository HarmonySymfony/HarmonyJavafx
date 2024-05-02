package entites;

import java.util.List;

public class Cabinet {
    private int id;
    private String adress;
    private String nom;
 private String horaires;
 private String email;


    public Cabinet() {
    }

    public Cabinet(String adress, String nom, String horaires, String email) {
        this.adress = adress;
        this.nom = nom;
        this.horaires = horaires;
        this.email = email;
    }

    public Cabinet(int id, String adress, String nom, String horaires, String email) {
        this.id = id;
        this.adress = adress;
        this.nom = nom;
        this.horaires = horaires;
        this.email = email;
    }

    public static void savePDF(String fileName) {
    }

    public static void generatePDF(List<Cabinet> cabinets, String fileName) {
    }


    public int getId() {
        return 0;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getHoraires() {
        return horaires;
    }

    public void setHoraires(String horaires) {
        this.horaires = horaires;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Cabinet{" +
                "id=" + id +
                ", adress='" + adress + '\'' +
                ", nom='" + nom + '\'' +
                ", horaires='" + horaires + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
