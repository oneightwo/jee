package ru.ssau.concurrency.runnable;

import lombok.RequiredArgsConstructor;
import ru.ssau.service.Vehicle;
import ru.ssau.utils.VehicleUtils;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.concurrent.BlockingQueue;

@RequiredArgsConstructor
public class VehicleCreatorServiceRunnable implements Runnable {

    private final String pathToFile;
    private final BlockingQueue<Vehicle> blockingQueue;

    @Override
    public void run() {
        try {
            Vehicle vehicle = VehicleUtils.readVehicle(new FileReader(pathToFile));
            blockingQueue.put(vehicle);
        } catch (FileNotFoundException | InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }
}
