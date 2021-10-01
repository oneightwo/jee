package ru.ssau.concurrency.runnable;

import lombok.RequiredArgsConstructor;
import ru.ssau.concurrency.ModelPrintHelper;

@RequiredArgsConstructor
public class ModelNamePrinterWithLockRunnable implements Runnable {

    private final ModelPrintHelper modelPrintHelper;

    @Override
    public void run() {
        modelPrintHelper.printModelNames();
    }
}
