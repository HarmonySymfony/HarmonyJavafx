package entities;

public class medicament {
    private int id ;

    private String reference;

    private String description;
    private int stock;

    private String disponibilite;

    private int prix;

    public medicament() {
    }

    public medicament(int id, String reference, int stock, String description, String disponibilite, int prix) {
        this.id = id;
        this.reference = reference;
        this.stock = stock;
        this.description = description ;
        this.prix = prix;
        this.disponibilite=disponibilite;
    }

    public medicament (String reference, int stock, String description, String disponibilite, int prix) {
        this.reference = reference;
        this.stock = stock;
        this.description = description ;
        this.prix = prix;
        this.disponibilite=disponibilite;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public int getStock() {
        return stock;
    }
    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getDisponibilite() { return disponibilite;
    }
    public void setDisponibilite(String disponibilite) {
        this.disponibilite = disponibilite;
    }

    public String getDescription() { return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public int getPrix() {
        return prix;
    }
    public void setPrix(int prix) {
        this.prix = prix;
    }

    @Override
    public String toString() {
        return "medicament{" +
                "id=" + id +
                ", reference='" + reference + '\'' +
                ", stock='" + stock + '\'' +
                ", disponibilite='" + disponibilite + '\'' +

                ", description='" + description + '\''+

                ", prix='" + prix + '\''+
        '}';
    }
}
