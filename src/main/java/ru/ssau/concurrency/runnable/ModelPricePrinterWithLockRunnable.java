package ru.ssau.concurrency.runnable;

import lombok.RequiredArgsConstructor;
import ru.ssau.concurrency.ModelPrintHelper;

@RequiredArgsConstructor
public class ModelPricePrinterWithLockRunnable implements Runnable {

    private final ModelPrintHelper modelPrintHelper;

    @Override
    public void run() {
        modelPrintHelper.printModelPrices();
    }
}
