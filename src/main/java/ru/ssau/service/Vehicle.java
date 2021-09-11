package ru.ssau.service;

import ru.ssau.exception.DuplicateModelNameException;
import ru.ssau.exception.NoSuchModelNameException;

public interface Vehicle {

    String getBrand();
    
    void setBrand(String brand);

    String[] getModelNames();

    Double getPriceByName(String name) throws NoSuchModelNameException;

    void updatePriceByName(String name, Double price) throws NoSuchModelNameException;

    void updateName(String oldName, String newName) throws DuplicateModelNameException, NoSuchModelNameException;

    int getModelSize();

    Double[] getModelPrices();

    void addModel(String name, Double price) throws DuplicateModelNameException, NoSuchModelNameException;

    void deleteModel(String name, Double price) throws NoSuchModelNameException;
}
