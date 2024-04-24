
package entities;

public class Evenement {

    private int id;

    private String nom;

    private String description;

    private float prix;

    private int placeDispo;

    private String adresse;


    public Evenement (){

    }

    public Evenement (int id , String nom , String description , float prix , int placeDispo , String adresse){
        this.id = id;
        this.nom = nom;
        this.description = description;
        this.prix = prix;
        this.placeDispo = placeDispo;
        this.adresse = adresse;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getPrix() {
        return prix;
    }

    public void setPrix(float prix) {
        this.prix = prix;
    }

    public int getPlaceDispo() {
        return placeDispo;
    }

    public void setPlaceDispo(int placeDispo) {
        this.placeDispo = placeDispo;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }



    @Override
    public String toString() {
        return "Evenement{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", description='" + description + '\'' +
                ", prix=" + prix +
                ", placeDispo=" + placeDispo +
                ", adresse='" + adresse + '\'' +
                '}';
    }
}



