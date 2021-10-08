package ru.ssau.concurrency.thread;

import lombok.RequiredArgsConstructor;
import ru.ssau.service.Vehicle;

import java.util.Arrays;

@RequiredArgsConstructor
public class ModelNamePrinterThread extends Thread {

    private final Vehicle vehicle;

    @Override
    public void run() {
        Arrays.stream(vehicle.getModelNames()).forEach(System.out::println);
    }
}
