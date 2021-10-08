package ru.ssau.concurrency;

import ru.ssau.service.Vehicle;

public class TransportSynchronizer {
    private final Vehicle vehicle;
    private final Object lock = new Object();
    private volatile int current = 0;
    private boolean isPrintModelLocked = false;

    public TransportSynchronizer(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public void printPrice() throws InterruptedException {
        synchronized (lock) {
            Double[] modelPrices = vehicle.getModelPrices();
            if (!canPrintPrice()) throw new InterruptedException();
            while (!isPrintModelLocked) {
                lock.wait();
            }
            System.out.println("Print price: " + modelPrices[current++]);
            isPrintModelLocked = false;
            lock.notifyAll();
        }
    }

    public void printModel() throws InterruptedException {
        synchronized (lock) {
            String[] modelNames = vehicle.getModelNames();
            if (!canPrintModel()) throw new InterruptedException();
            while (isPrintModelLocked) {
                lock.wait();
            }
            System.out.println("Print model: " + modelNames[current]);
            isPrintModelLocked = true;
            lock.notifyAll();
        }
    }

    public boolean canPrintPrice() {
        return current < vehicle.getModelSize();
    }

    public boolean canPrintModel() {
        return (!isPrintModelLocked && current < vehicle.getModelSize())
                || (isPrintModelLocked && current < vehicle.getModelSize() - 1);
    }
}
