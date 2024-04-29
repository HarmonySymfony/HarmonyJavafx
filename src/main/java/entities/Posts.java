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

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<Comments> comments = new ArrayList<>();

    // Constructors
    public Posts() {
    }
    // Advanced constructor
    public Posts(String contenu, java.sql.Timestamp dateCreation, java.sql.Timestamp lastModification, String postedAs, List<Comments> comments) {
        this.contenu = contenu;
        this.dateCreation = dateCreation;
        this.lastModification = lastModification;
        this.postedAs = postedAs;
        this.comments = comments;
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

    public List<Comments> getComments() {
        return comments;
    }

    public void setComments(List<Comments> comments) {
        this.comments = comments;
    }

    // Method to add a comment to the post's collection of comments
    public void addComment(Comments comment) {
        this.comments.add(comment); // Simply add the comment to the list
    }
}
