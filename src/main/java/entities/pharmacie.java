package entities;

public class pharmacie {
    private int id ;
    private String nom;
    private String adress;
    private String type;

    public pharmacie() {
    }

    public pharmacie(int id, String nom, String adress, String type) {
        this.id = id;
        this.nom = nom;
        this.adress = adress;
        this.type = type ;    }

    public pharmacie (String nom, String adress, String type) {
        this.nom = nom;
        this.adress = adress;
        this.type = type;
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

    public String getAdress() {
        return adress;
    }
    public void setAdress(String adress) {
        this.adress = adress;
    }

    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "pharmacie{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", adress='" + adress + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
