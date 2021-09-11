package ru.ssau.service.impl;

import ru.ssau.exception.DuplicateModelNameException;
import ru.ssau.exception.ModelPriceOutOfBoundsException;
import ru.ssau.exception.NoSuchModelNameException;
import ru.ssau.service.Vehicle;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

public class Car implements Vehicle {

    private String brand;
    private Model[] models;
    private final int initArraySize;

    public Car(String brand, int arraySize) {
        this.brand = brand;
        this.initArraySize = arraySize;
        this.models = new Model[arraySize];
    }

    @Override
    public String getBrand() {
        return brand;
    }

    @Override
    public void setBrand(String brand) {
        this.brand = brand;
    }

    @Override
    public String[] getModelNames() {
        return Arrays.stream(models)
                .filter(Objects::nonNull)
                .map(Model::getName)
                .filter(Objects::nonNull)
                .toArray(String[]::new);
    }

    @Override
    public Double getPriceByName(String name) throws NoSuchModelNameException {
        return getModelByName(name).price;
    }

    @Override
    public void updatePriceByName(String name, Double price) throws NoSuchModelNameException {
        checkName(name);
        checkPrice(price);
        for (Model model : models) {
            if (Objects.nonNull(model) && model.name.equals(name)) {
                model.price = price;
                return;
            }
        }
        throw new NoSuchModelNameException();
    }

    @Override
    public void updateName(String oldName, String newName) throws DuplicateModelNameException, NoSuchModelNameException {
        checkName(oldName);
        checkName(newName);
        Model model = getModelByName(oldName);
        if (getModelByNameOrEmpty(newName).isPresent() && !oldName.equals(newName)) {
            throw new DuplicateModelNameException();
        }
        model.name = newName;
    }

    @Override
    public int getModelSize() {
        return models.length;
    }

    @Override
    public Double[] getModelPrices() {
        return Arrays.stream(models)
                .filter(Objects::nonNull)
                .map(Model::getPrice)
                .filter(Objects::nonNull)
                .toArray(Double[]::new);
    }

    @Override
    public void addModel(String name, Double price) throws DuplicateModelNameException, NoSuchModelNameException {
        checkName(name);
        if (getModelByNameOrEmpty(name).isPresent()) {
            throw new DuplicateModelNameException();
        }
        checkPrice(price);
        Optional<Integer> indexOfFreeSlot = getIndexOfFreeCell();
        if (indexOfFreeSlot.isPresent()) {
            models[indexOfFreeSlot.get()] = new Model(name, price);
        } else {
            Model[] tempModels = Arrays.copyOf(models, models.length + 1);
            tempModels[models.length] = new Model(name, price);
            models = tempModels;
        }
    }

    @Override
    public void deleteModel(String name, Double price) throws NoSuchModelNameException {
        if (getModelByNameOrEmpty(name).isEmpty()) {
            throw new NoSuchModelNameException();
        }
        checkPrice(price);
        int arraySize = initArraySize;
        if (models.length - 1 >= initArraySize) {
            arraySize = models.length - 1;
        }
        Model[] resultArray = new Model[arraySize];
        Integer elementIndex = null;
        for (int i = 0; i < models.length; i++) {
            Model model = models[i];
            if (Objects.nonNull(model) && model.name.equals(name) && model.price.equals(price)) {
                elementIndex = i;
                break;
            }
        }
        if (Objects.nonNull(elementIndex)) {
            if (elementIndex.equals(0) && !elementIndex.equals(models.length - 1)) {
                System.arraycopy(models, elementIndex + 1, resultArray, 0, models.length - 1);
            } else if (elementIndex.equals(models.length - 1)) {
                System.arraycopy(models, 0, resultArray, 0, models.length - 1);
            } else {
                System.arraycopy(models, 0, resultArray, 0, elementIndex);
                System.arraycopy(models, elementIndex + 1, resultArray, elementIndex, models.length - elementIndex - 1);
            }
            models = resultArray;
        }
    }

    private Optional<Model> getModelByNameOrEmpty(String name) {
        if (Objects.isNull(name)) {
            return Optional.empty();
        }
        return Arrays.stream(models)
                .filter(Objects::nonNull)
                .filter(model -> model.name.equals(name))
                .findFirst();
    }

    private Model getModelByName(String name) throws NoSuchModelNameException {
        checkName(name);
        return Arrays.stream(models)
                .filter(model -> model.name.equals(name))
                .findFirst()
                .orElseThrow(NoSuchModelNameException::new);
    }

    private Optional<Integer> getIndexOfFreeCell() {
        for (int i = 0; i < models.length; i++) {
            if (Objects.isNull(models[i])) {
                return Optional.of(i);
            }
        }
        return Optional.empty();
    }

    private void checkName(String name) throws NoSuchModelNameException {
        if (Objects.isNull(name)) {
            throw new NoSuchModelNameException();
        }
    }

    private void checkPrice(Double price) {
        if (Objects.isNull(price) || price <= 0.0) {
            throw new ModelPriceOutOfBoundsException();
        }
    }

    private class Model {
        private String name;
        private Double price;

        public Model(String name, Double price) {
            this.name = name;
            this.price = price;
        }

        public String getName() {
            return name;
        }

        public Double getPrice() {
            return price;
        }

        @Override
        public String toString() {
            return "Model{" +
                    "name='" + name + '\'' +
                    ", price=" + price +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "Car{" +
                "brand='" + brand + '\'' +
                ", models=" + Arrays.toString(models) +
                '}';
    }
}
