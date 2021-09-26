package ru.ssau;

import ru.ssau.service.Vehicle;
import ru.ssau.utils.VehicleUtils;

import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class Main {

    public static void main(String[] args) {
        /**
         * ru.ssau.service.impl.Car
         * Brand
         * 1
         * Model
         * 1.1
         */
        Vehicle vehicle = VehicleUtils.readVehicle(new InputStreamReader(System.in));
        OutputStreamWriter writer = new OutputStreamWriter(System.out);
        VehicleUtils.writeVehicle(vehicle, writer);
    }
}
