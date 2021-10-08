package ru.ssau.concurrency.runnable;

import lombok.RequiredArgsConstructor;
import ru.ssau.concurrency.TransportSynchronizer;
import ru.ssau.service.Vehicle;

import java.util.Arrays;

@RequiredArgsConstructor
public class ModelNamePrinterRunnable implements Runnable {

    private final TransportSynchronizer transportSynchronizer;

    @Override
    public void run() {
        while (transportSynchronizer.canPrintModel()) {
            try {
                transportSynchronizer.printModel();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
