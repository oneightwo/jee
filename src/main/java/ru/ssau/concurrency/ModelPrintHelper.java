package ru.ssau.concurrency;

import lombok.RequiredArgsConstructor;
import ru.ssau.service.Vehicle;

import java.util.Arrays;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@RequiredArgsConstructor
public class ModelPrintHelper {

    private final Vehicle vehicle;
    private final Lock locker = new ReentrantLock();
    private final Condition condition = locker.newCondition();

    private boolean isModelNamesPrinted = false;

    public void printModelNames() {
        locker.lock();
        Arrays.stream(vehicle.getModelNames())
                .forEach(System.out::println);
        isModelNamesPrinted = true;
        condition.signalAll();
        locker.unlock();
    }

    public void printModelPrices() {
        locker.lock();
        while (!isModelNamesPrinted) {
            try {
                condition.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Arrays.stream(vehicle.getModelPrices())
                .forEach(System.out::println);
        locker.unlock();
    }

}
