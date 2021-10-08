package ru.ssau.concurrency.runnable;

import lombok.RequiredArgsConstructor;
import ru.ssau.service.Vehicle;

@RequiredArgsConstructor
public class BrandPrinterRunnable implements Runnable {

    private final Vehicle vehicle;

    @Override
    public void run() {
        System.out.println(vehicle.getBrand());
    }
}
