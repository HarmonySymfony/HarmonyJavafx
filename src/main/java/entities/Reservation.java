package entities;

public class Reservation {

    private int id;
    private int nbrPlace;
    private Evenement evenement;

    public Reservation() {
    }

    public Reservation(int id, int nbrPlace, Evenement evenement) {
        this.id = id;
        this.nbrPlace = nbrPlace;
        this.evenement = evenement;
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
