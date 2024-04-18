package entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Posts {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(columnDefinition = "text")
    private String contenu;

    @Column(columnDefinition = "TIMESTAMP")
    private java.sql.Timestamp dateCreation;

    @Column(columnDefinition = "TIMESTAMP")
    private java.sql.Timestamp lastModification;

    @Column(length = 255)
    private String postedAs = "Anonyme";

    @ElementCollection
    private List<String> likedBy = new ArrayList<>();

    @ElementCollection
    private List<String> dislikedBy = new ArrayList<>();

    // Constructors
    public Posts() {
    }

    public Posts(String contenu, java.sql.Timestamp dateCreation, java.sql.Timestamp lastModification, String postedAs, List<String> likedBy, List<String> dislikedBy) {
        this.contenu = contenu;
        this.dateCreation = dateCreation;
        this.lastModification = lastModification;
        this.postedAs = postedAs;
        this.likedBy = likedBy;
        this.dislikedBy = dislikedBy;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContenu() {
        return contenu;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    public java.sql.Timestamp getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(java.sql.Timestamp dateCreation) {
        this.dateCreation = dateCreation;
    }

    public java.sql.Timestamp getLastModification() {
        return lastModification;
    }

    public void setLastModification(java.sql.Timestamp lastModification) {
        this.lastModification = lastModification;
    }

    public String getPostedAs() {
        return postedAs;
    }

    public void setPostedAs(String postedAs) {
        this.postedAs = postedAs;
    }

    public List<String> getLikedBy() {
        return likedBy;
    }

    public void setLikedBy(List<String> likedBy) {
        this.likedBy = likedBy;
    }

    public List<String> getDislikedBy() {
        return dislikedBy;
    }

    public void setDislikedBy(List<String> dislikedBy) {
        this.dislikedBy = dislikedBy;
    }
}
