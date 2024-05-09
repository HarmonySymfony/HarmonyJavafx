package interfaces;

import java.util.List;

public interface IServicesUser<T> {
    void Ajouter(T t);
    void addEntity(T t);
    void updateEntity(T t);
    void deleteEntity(T t);
    List<T> getAllData();
}
