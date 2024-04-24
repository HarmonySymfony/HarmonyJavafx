package interfaces;

import java.util.List;

public interface IServices <T>{
    void addEntity (T t);
    void updateEntity(T t);
    void deleteEntity(T t);

    List<T> getAllData() ;




    void Ajouter(entities.Personne personne);

    void Ajouter(Personne personne);

    void addEntity (T t);
    void updateEntity(T t);
    void deleteEntity(T t);
    List<T> getAllData();
}
