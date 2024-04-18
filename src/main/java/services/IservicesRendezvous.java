package services;

import entites.Rendezvous;

import java.util.List;

public interface IservicesRendezvous <T> {


    boolean entityExists(T t);

    void addEntity(T t);


    void addEntity2(T t);

    void updateEntity(T t);


    void deleteEntity(T t);


    List<T> getAllData();




}
