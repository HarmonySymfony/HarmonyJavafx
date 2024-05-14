package entities;

public class Reservation {

    private int id;
    private int nbrPlace;
    private Evenement evenement;

    private int approuve;

    public int getApprouve() {
        return approuve;
    }

    public void setApprouve(int approuve) {
        this.approuve = approuve;
    }

    public Reservation() {
    }

    public Reservation(int id, int nbrPlace, Evenement evenement) {
        this.id = id;
        this.nbrPlace = nbrPlace;
        this.evenement = evenement;
    }
    public Reservation(int id, int nbrPlace, Evenement evenement, int approuve) {
        this.id = id;
        this.nbrPlace = nbrPlace;
        this.evenement = evenement;
        this.approuve = approuve;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNbrPlace() {
        return nbrPlace;
    }

    public void setNbrPlace(int nbrPlace) {
        this.nbrPlace = nbrPlace;
    }

    public Evenement getEvenement() {
        return evenement;
    }

    public void setEvenement(Evenement evenement) {
        this.evenement = evenement;
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "id=" + id +
                ", nbrPlace=" + nbrPlace +
                ", evenement=" + evenement +
                '}';
    }
}