package entities;

import java.util.Objects;

public class Laboratoire {
    private int id;
    private String nom;
    private String emplacement;

    public Laboratoire(int id, String nom, String emplacement) {
        this.id = id;
        this.nom = nom;
        this.emplacement = emplacement;
    }
    public Laboratoire(String nom, String emplacement) {
        this.nom = nom;
        this.emplacement = emplacement;
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

    public String getEmplacement() {
        return emplacement;
    }

    public void setEmplacement(String emplacement) {
        this.emplacement = emplacement;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Laboratoire that)) return false;
        return getId() == that.getId() && Objects.equals(getNom(), that.getNom()) && Objects.equals(getEmplacement(), that.getEmplacement());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getNom(), getEmplacement());
    }

    @Override
    public String toString() {
        return "Laboratoire{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", emplacement='" + emplacement + '\'' +
                '}';
    }
}
