package entities;

import java.sql.Blob;
import java.util.Objects;

public class Personne {
    private int id;
    private String Nom;
    private String Prenom;
    private String Email;
    private String Password;
    private String Role;
    private int age;
    private Blob ProfilePicture;


    public Personne(int id, String Nom, String Prenom, String Email, String Password, int age, String Role, Blob ProfilePicture) {
        this.id = id;
        this.Nom = Nom;
        this.Prenom = Prenom;
        this.Email = Email;
        this.Password = Password;
        this.age = age;
        this.Role = Role;
        this.ProfilePicture = ProfilePicture;
    }

    public Personne(String Nom, String Prenom, String Email, String Password, int age, String Role, Blob ProfilePicture) {
        this.Nom = Nom;
        this.Prenom = Prenom;
        this.Email = Email;
        this.Password = Password;
        this.age = age;
        this.Role = Role;
        this.ProfilePicture = ProfilePicture;
    }

    public Personne() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRole() {
        return Role;
    }

    public void setRole(String Role) {
        this.Role = Role;
    }

    public String getNom() {
        return Nom;
    }

    public void setNom(String nom) {
        this.Nom = Nom;
    }

    public String getPrenom() {
        return Prenom;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        age = age;
    }

    @Override
    public String toString() {
        return "Personne{" +
                "id=" + id +
                ", Nom='" + Nom + '\'' +
                ", prenom='" + Prenom + '\'' +
                ", Email='" + Email + '\'' +
                ", age='" + age + '\'' +
                ", Role='" + Role + '\'' +
                '}';
    }

    @Override
    public int hashCode() {
        int hash = 3;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Personne other = (Personne) obj;
        if (this.id != other.id) {
            return false;
        }
        if (!Objects.equals(this.Nom, other.Nom)) {
            return false;
        }
        if (!Objects.equals(this.Prenom, other.Prenom)) {
            return false;
        }
        if (!Objects.equals(this.Email, other.Email)) {
            return false;
        }
        return Objects.equals(this.Password, other.Password);
    }

    // Méthode pour afficher les informations de la personne
    public void afficherInformations() {
        System.out.println("Nom : " + Nom);
        System.out.println("Prénom : " + Prenom);
        System.out.println("Email : " + Email);
        System.out.println("age : " + age);
        System.out.println("Role : " + Role);
    }

    public void setPrenom(String Prenom) {
        this.Prenom = Prenom;
    }



    public Blob getProfilePicture() {
        return ProfilePicture;
    }

    public void setProfilePicture(Blob ProfilePicture) {
        this.ProfilePicture = ProfilePicture;
    }

}