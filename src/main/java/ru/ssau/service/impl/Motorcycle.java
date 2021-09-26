package ru.ssau.service.impl;


import ru.ssau.exception.DuplicateModelNameException;
import ru.ssau.exception.ModelPriceOutOfBoundsException;
import ru.ssau.exception.NoSuchModelNameException;
import ru.ssau.service.Vehicle;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;

public class Motorcycle implements Vehicle, Serializable {

    private String brand;
    private int size;
    private final Model head = new Model();

    {
        head.prev = head;
        head.next = head;
    }

    public Motorcycle(String brand, int size) {
        this.brand = brand;
        for (int i = 0; i < size; i++) {
            try {
                addModel("name" + i, (double) (i + 1));
            } catch (NoSuchModelNameException | DuplicateModelNameException e) {
                System.out.println(e.getMessage());
            }
        }
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
        String[] names = new String[size];
        Model model = head.next;
        for (int i = 0; i < size; i++) {
            names[i] = model.name;
            model = model.next;
        }
        return names;
    }

    @Override
    public Double getPriceByName(String name) throws NoSuchModelNameException {
        checkName(name);
        Model model = head.next;
        for (int i = 0; i < size; i++) {
            if (model.name.equals(name)) {
                return model.price;
            }
            model = model.next;
        }
        throw new NoSuchModelNameException();
    }

    @Override
    public void updatePriceByName(String name, Double price) throws NoSuchModelNameException {
        checkName(name);
        checkPrice(price);
        Model model = head.next;
        for (int i = 0; i < size; i++) {
            if (Objects.nonNull(model) && model.name.equals(name)) {
                model.price = price;
                return;
            }
            model = model.next;
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
        return size;
    }

    @Override
    public Double[] getModelPrices() {
        Double[] prices = new Double[size];
        Model model = head.next;
        for (int i = 0; i < size; i++) {
            prices[i] = model.price;
            model = model.next;
        }
        return prices;
    }

    @Override
    public void addModel(String name, Double price) throws NoSuchModelNameException, DuplicateModelNameException {
        checkName(name);
        if (getModelByNameOrEmpty(name).isPresent()) {
            throw new DuplicateModelNameException();
        }
        checkPrice(price);
        Model model = new Model(name, price);
        if (Objects.isNull(head.next) && Objects.isNull(head.prev)) {
            model.next = head;
            model.prev = head;
            head.next = model;
        } else {
            model.next = head;
            model.prev = head.prev;
            head.prev.next = model;
        }
        head.prev = model;
        size++;
    }

    @Override
    public void deleteModel(String name, Double price) throws NoSuchModelNameException {
        if (getModelByNameOrEmpty(name).isEmpty()) {
            throw new NoSuchModelNameException();
        }
        checkPrice(price);
        if (size == 1) {
            head.next = null;
            head.prev = null;
            size--;
            return;
        }
        Model model = head.next;
        for (int i = 0; i < size; i++) {
            if (model.name.equals(name) && model.price.equals(price)) {
                model.prev.next = model.next;
                model.next.prev = model.prev;
                size--;
                break;
            }
            model = model.next;
        }
    }

    private Optional<Model> getModelByNameOrEmpty(String name) {
        if (Objects.isNull(name)) {
            return Optional.empty();
        }
        Model model = head.next;
        for (int i = 0; i < size; i++) {
            if (Objects.nonNull(model.name) && model.name.equals(name)) {
                return Optional.of(model);
            }
            model = model.next;
        }
        return Optional.empty();
    }

    private Model getModelByName(String name) throws NoSuchModelNameException {
        checkName(name);
        Model model = head.next;
        for (int i = 0; i < size; i++) {
            if (model.name.equals(name)) {
                return model;
            }
            model = model.next;
        }
        throw new NoSuchModelNameException();
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

    private class Model implements Serializable {
        private String name;
        private Double price;

        private Model prev = null;
        private Model next = null;

        public Model() {
            this.name = null;
            this.price = null;
        }

        public Model(String name, Double price) {
            this.name = name;
            this.price = price;
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
        StringBuilder result = new StringBuilder("Motorcycle{brand='" + brand + "', models=");
        if (Objects.isNull(head.next) && Objects.isNull(head.prev)) {
            result.append("[]");
        } else {
            result.append("[" + "Model{" + "name='").append(head.next.name).append('\'')
                    .append(", price=").append(head.next.price)
                    .append('}');
            Model current = head.next.next;
            while (current != head) {
                result.append(", ")
                        .append("Model{" + "name='").append(current.name).append('\'')
                        .append(", price=").append(current.price)
                        .append('}');
                current = current.next;
            }
            result.append("]}");
            return result.toString();
        }
        return result.toString();
    }
}
