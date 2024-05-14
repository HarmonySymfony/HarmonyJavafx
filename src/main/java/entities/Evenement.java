
package entities;

import java.util.Date;

public class Evenement {

    private int id;

    private String nom;

    private String description;

    private float prix;

    private int place_dispo;

    private String adresse;

    private Date date_event;

    private double latitude ;
    private double longitude ;




    public Evenement (){

    }

    public Evenement (int id , String nom , String description , float prix , int place_dispo , String adresse,Date date_event){
        this.id = id;
        this.nom = nom;
        this.description = description;
        this.prix = prix;
        this.place_dispo = place_dispo;
        this.adresse = adresse;
        this.date_event = date_event;
    }

    public Evenement(int id, String nom, String description, float prix, int place_dispo, String adresse, Date date_event, double latitude, double longitude) {
        this.id = id;
        this.nom = nom;
        this.description = description;
        this.prix = prix;
        this.place_dispo = place_dispo;
        this.adresse = adresse;
        this.date_event = date_event;
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


    public int getPlace_dispo() {
        return place_dispo;
    }

    public void setPlace_dispo(int place_dispo) {
        this.place_dispo = place_dispo;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }


    public java.sql.Date getDate_event() {
        return (java.sql.Date) date_event;
    }

    public void setDate_event(Date date_event) {
        this.date_event = date_event;
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
                ", place_dispo=" + place_dispo +
                ", adresse='" + adresse + '\'' +
                ", date_event=" + date_event +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}