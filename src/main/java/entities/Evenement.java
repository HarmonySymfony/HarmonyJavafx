
package entities;

import java.util.Date;

public class Evenement {

    private int id;

    private String nom;

    private String description;

    private float prix;

    private int placeDispo;

    private String adresse;
    private Date date;

    private double latitude ;
    private double longitude ;




    public Evenement (){

    }

    public Evenement (int id , String nom , String description , float prix , int placeDispo , String adresse, Date date){
        this.id = id;
        this.nom = nom;
        this.description = description;
        this.prix = prix;
        this.placeDispo = placeDispo;
        this.adresse = adresse;
        this.date = date;
    }

    public Evenement(int id, String nom, String description, float prix, int placeDispo, String adresse, Date date, double latitude, double longitude) {
        this.id = id;
        this.nom = nom;
        this.description = description;
        this.prix = prix;
        this.placeDispo = placeDispo;
        this.adresse = adresse;
        this.date = date;
        this.latitude = latitude;
        this.longitude = longitude;
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

    public java.sql.Date getDate() {
        return (java.sql.Date) date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
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
                ", date=" + date +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}