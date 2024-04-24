package entities;

import java.util.Objects;

public class Personne {
    private int id ;
    private String Nom;
    private String Prenom;
    private String Email;
    private String Password;
    private int age;




    public Personne(int id, String Nom, String Prenom,String Email,String Password,int age) {
        this.id = id;
        this.Nom = Nom;
        this.Prenom = Prenom;
        this.Email = Email;
        this.Password= Password;
        this.age= age;
    }

    public Personne( String Nom, String Prenom,String Email,String Password,int age) {
        this.Nom = Nom;
        this.Prenom = Prenom;
        this.Email=Email;
        this.Password=Password;
        this.age=age;
    }
    public Personne() {}

    public Personne(String rebai, String saber, String email, String aloulou123, String s) {
    }




    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
    }


    public void setPrenom(String Prenom) {
        this.Prenom = Prenom;
    }
}
