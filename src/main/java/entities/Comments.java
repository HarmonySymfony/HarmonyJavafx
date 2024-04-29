package entities;

import javax.persistence.*;

@Entity
public class Comments {

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
    private String commentedAs = "Anonyme";


    @ManyToOne
    @JoinColumn(name = "posts_id")
    private Posts post;


    // Add a field to store the post ID
    @Column(name = "posts_id")
    private int postId;

    // Constructors, getters, and setters...

    public Comments() {
    }



    public Comments(String contenu, java.sql.Timestamp dateCreation, java.sql.Timestamp lastModification, String commentedAs, Posts post) {
        this.contenu = contenu;
        this.dateCreation = dateCreation;
        this.lastModification = lastModification;
        this.commentedAs = commentedAs;
        this.post = post;
        // Associate this comment with the post by adding it to the post's collection of comments
        if (post != null) {
            post.addComment(this);
        }

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

    public String getCommentedAs() {
        return commentedAs;
    }

    public void setCommentedAs(String postedAs) {
        this.commentedAs = postedAs;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }
}
