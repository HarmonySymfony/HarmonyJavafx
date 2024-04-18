package services;

public interface IservicesCabinet <T>{
    boolean entityExists(T t);

    void addEntity(T t);




    void addEntity2(T t);

    void updateEntity(T t);


    void deleteEntity(T t);


}
