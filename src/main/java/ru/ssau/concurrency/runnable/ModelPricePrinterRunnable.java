package ru.ssau.concurrency.runnable;

import lombok.RequiredArgsConstructor;
import ru.ssau.concurrency.TransportSynchronizer;
import ru.ssau.service.Vehicle;

import java.util.Arrays;

@RequiredArgsConstructor
public class ModelPricePrinterRunnable implements Runnable {

    private final TransportSynchronizer transportSynchronizer;

    @Override
    public void run() {
        while (transportSynchronizer.canPrintPrice()) {
            try {
                transportSynchronizer.printPrice();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
