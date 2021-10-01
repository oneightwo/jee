package ru.ssau.utils;

import ru.ssau.exception.DuplicateModelNameException;
import ru.ssau.exception.NoSuchModelNameException;
import ru.ssau.service.Vehicle;
import ru.ssau.service.impl.Car;
import ru.ssau.service.impl.Motorcycle;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

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

    public static void outputVehicle(Vehicle vehicle, OutputStream outputStream) {
        try (DataOutputStream dataOutputStream = new DataOutputStream(outputStream)) {
            writeStringToByteOutput(vehicle.getClass().getName(), dataOutputStream);
            writeStringToByteOutput(vehicle.getBrand(), dataOutputStream);
            dataOutputStream.writeInt(vehicle.getModelSize());
            String[] modelNames = vehicle.getModelNames();
            Double[] modelPrices = vehicle.getModelPrices();
            for (int i = 0; i < modelNames.length; i++) {
                writeStringToByteOutput(modelNames[i], dataOutputStream);
                dataOutputStream.writeDouble(modelPrices[i]);
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void writeStringToByteOutput(String str, DataOutputStream dataOutputStream) {
        try {
            dataOutputStream.writeInt(str.length());
            dataOutputStream.write(str.getBytes());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static Vehicle inputVehicle(InputStream inputStream) {
        Vehicle vehicle = null;
        try (DataInputStream dataInputStream = new DataInputStream(inputStream)) {
            Optional<Vehicle> optionalVehicle = vehicleFactory(readBytesFromInput(dataInputStream));
            if (optionalVehicle.isPresent()) {
                vehicle = optionalVehicle.get();
                vehicle.setBrand(readBytesFromInput(dataInputStream));
                int modelSize = dataInputStream.readInt();
                for (int i = 0; i < modelSize; i++) {
                    try {
                        vehicle.addModel(readBytesFromInput(dataInputStream), dataInputStream.readDouble());
                    } catch (DuplicateModelNameException | NoSuchModelNameException e) {
                        System.out.println(e.getMessage());
                    }
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return vehicle;
    }

    private static String readBytesFromInput(DataInputStream dataInputStream) {
        String result = null;
        try {
            int stringSize = dataInputStream.readInt();
            result = new String(dataInputStream.readNBytes(stringSize));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return result;
    }

    public static void writeVehicle(Vehicle vehicle, Writer writer) {
        try (PrintWriter printWriter = new PrintWriter(writer)) {
            printWriter.println(vehicle.getClass().getName());
            printWriter.println(vehicle.getBrand());
            printWriter.println(vehicle.getModelSize());
            String[] modelNames = vehicle.getModelNames();
            Double[] modelPrices = vehicle.getModelPrices();
            for (int i = 0; i < modelNames.length; i++) {
                printWriter.println(modelNames[i]);
                printWriter.println(modelPrices[i]);
            }
        }
    }

    public static Vehicle readVehicle(Reader reader) {
        Vehicle vehicle = null;
        try (BufferedReader bufferedReader = new BufferedReader(reader)) {
            Optional<Vehicle> optionalVehicle = vehicleFactory(bufferedReader.readLine());
            if (optionalVehicle.isPresent()) {
                vehicle = optionalVehicle.get();
                vehicle.setBrand(bufferedReader.readLine());
                String modelSize = bufferedReader.readLine();
                if (Objects.nonNull(modelSize)) {
                    for (int i = 0; i < Integer.parseInt(modelSize); i++) {
                        try {
                            vehicle.addModel(bufferedReader.readLine(), Double.parseDouble(bufferedReader.readLine()));
                        } catch (DuplicateModelNameException | NoSuchModelNameException e) {
                            System.out.println(e.getMessage());
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return vehicle;
    }

    private static Optional<Vehicle> vehicleFactory(String className) {
        if (Objects.nonNull(className)) {
            if (className.equals(Car.class.getName())) {
                return Optional.of(new Car(null, 0));
            }
            if (className.equals(Motorcycle.class.getName())) {
                return Optional.of(new Motorcycle(null, 0));
            }
        }
        return Optional.empty();
    }
}
