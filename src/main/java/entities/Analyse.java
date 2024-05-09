package entities;

import java.util.Objects;

public class Analyse {
    private  int id;
    private  float prix;
    private  String type;
    private int laboratoireId;
    private Laboratoire laboratoire;

    public Analyse() {

    }

    public Analyse(float prix, String type, int laboratoireId) {
        this.prix = prix;
        this.type = type;
        this.laboratoireId = laboratoireId;
    }

    public Analyse(int id, float prix, String type, int laboratoireId) {
        this.id = id;
        this.prix = prix;
        this.type = type;
        this.laboratoireId = laboratoireId;
    }

    public Analyse(int id, float prix, String type) {
        this.id = id;
        this.prix = prix;
        this.type = type;
    }

    public  int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public  float getPrix() {
        return prix;
    }

    public void setPrix(float prix) {
        this.prix = prix;
    }

    public  String getType() {
        return type;
    }

    public int getLaboratoireId() {
        return laboratoireId;
    }

    public void setLaboratoireId(int laboratoireId) {
        this.laboratoireId = laboratoireId;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Analyse analyse)) return false;
        return getId() == analyse.getId() && Float.compare(getPrix(), analyse.getPrix()) == 0 && Objects.equals(getType(), analyse.getType());
    }

    @Override
    public String toString() {
        return "Analyse{" +
                "id=" + id +
                ", prix=" + prix +
                ", type='" + type + '\'' +
                '}';
    }


}
