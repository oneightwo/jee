package ru.ssau.utils;

import ru.ssau.service.Vehicle;

import java.util.Arrays;

public class VehicleUtils {

    public static Double getAveragePrice(Vehicle vehicle) {
        return Arrays.stream(vehicle.getModelPrices())
                .mapToDouble(i -> i)
                .average()
                .orElse(Double.NaN);
    }

    public static void printModelNames(Vehicle vehicle) {
        System.out.println(Arrays.toString(vehicle.getModelNames()));
    }

    public static void printModelPrices(Vehicle vehicle) {
        System.out.println(Arrays.toString(vehicle.getModelPrices()));
    }

    public static void printModels(Vehicle vehicle) {
        String[] modelNames = vehicle.getModelNames();
        Double[] prices = vehicle.getModelPrices();
        for (int i = 0; i < modelNames.length; i++) {
            System.out.println("Model: " + modelNames[i] + "; Price: " + prices[i]);
        }
    }
}
