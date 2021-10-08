package ru.ssau.concurrency;

import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.ssau.concurrency.runnable.BrandPrinterRunnable;
import ru.ssau.concurrency.runnable.ModelNamePrinterRunnable;
import ru.ssau.concurrency.runnable.ModelNamePrinterWithLockRunnable;
import ru.ssau.concurrency.runnable.ModelPricePrinterRunnable;
import ru.ssau.concurrency.runnable.ModelPricePrinterWithLockRunnable;
import ru.ssau.concurrency.runnable.VehicleCreatorServiceRunnable;
import ru.ssau.concurrency.thread.ModelNamePrinterThread;
import ru.ssau.concurrency.thread.ModelPricePrinterThread;
import ru.ssau.service.Vehicle;
import ru.ssau.service.impl.Car;
import ru.ssau.service.impl.Motorcycle;
import ru.ssau.utils.VehicleUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static ru.ssau.constant.TestConstant.BRAND;
import static ru.ssau.constant.TestConstant.MODELS;
import static ru.ssau.constant.TestConstant.MODEL_FOUR;
import static ru.ssau.constant.TestConstant.MODEL_ONE;
import static ru.ssau.constant.TestConstant.MODEL_THREE;
import static ru.ssau.constant.TestConstant.MODEL_TWO;
import static ru.ssau.constant.TestConstant.PRICE_ONE;
import static ru.ssau.constant.TestConstant.PRICE_THREE;
import static ru.ssau.constant.TestConstant.PRICE_TWO;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ConcurrencyTest {

    private final List<File> files = new ArrayList<>();
    private final List<String> vehicleAsString = new ArrayList<>();

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    private static Car car;
    private static Motorcycle motorcycle;

    @TempDir
    File parentFile;

    @SneakyThrows
    @BeforeAll
    public void init() {
        System.setOut(new PrintStream(outContent));

        car = new Car(BRAND, 0);
        car.addModel(MODEL_ONE, PRICE_ONE);
        car.addModel(MODEL_TWO, PRICE_TWO);
        car.addModel(MODEL_THREE, PRICE_THREE);

        motorcycle = new Motorcycle(BRAND, 0);
        motorcycle.addModel(MODEL_ONE, PRICE_ONE);
        motorcycle.addModel(MODEL_TWO, PRICE_TWO);
        motorcycle.addModel(MODEL_THREE, PRICE_THREE);

        for (int i = 1; i <= 5; i++) {
            File file = new File(parentFile, BRAND + i + ".txt");
            Vehicle vehicle;
            if (i % 2 == 0) {
                vehicle = new Car(BRAND + i, 0);
            } else {
                vehicle = new Motorcycle(BRAND + i, 0);
            }
            vehicleAsString.add(vehicle.toString());
            VehicleUtils.writeVehicle(vehicle, new FileWriter(file));
            files.add(file);
        }
    }

    @AfterAll
    public void clear() {
        files.forEach(File::delete);
    }

    @BeforeEach
    public void tearDown() {
        outContent.reset();
        System.setOut(new PrintStream(outContent));
    }

    @SneakyThrows
    @ParameterizedTest
    @MethodSource("vehicleDataProvider")
    public void testPrintNameAndPriceModelsWithSynchronizedBlock(Vehicle vehicle) {
        Thread namePrinter = new ModelNamePrinterThread(vehicle);
        Thread pricePrinter = new ModelPricePrinterThread(vehicle);
        namePrinter.start();
        pricePrinter.start();
        namePrinter.join();
        pricePrinter.join();

        String output = outContent.toString();
        assertContainsString(output, MODEL_ONE);
        assertContainsString(output, MODEL_TWO);
        assertContainsString(output, MODEL_THREE);
        assertContainsString(output, PRICE_ONE.toString());
        assertContainsString(output, PRICE_TWO.toString());
        assertContainsString(output, PRICE_THREE.toString());
    }

    @SneakyThrows
    @ParameterizedTest
    @MethodSource("vehicleDataProvider")
    public void testTransportSynchronizer(Vehicle vehicle) {
        TransportSynchronizer transportSynchronizer = new TransportSynchronizer(vehicle);
        Thread printNameThread = new Thread(new ModelNamePrinterRunnable(transportSynchronizer));
        Thread printPriceThread = new Thread(new ModelPricePrinterRunnable(transportSynchronizer));
        printNameThread.start();
        printPriceThread.start();
        printNameThread.join();
        printPriceThread.join();

        String output = outContent.toString();
        assertEquals("Print model: model_one\r\n" +
                "Print price: 1.0\r\n" +
                "Print model: model_two\r\n" +
                "Print price: 2.0\r\n" +
                "Print model: model_three\r\n" +
                "Print price: 3.0\r\n", output);
    }

    @SneakyThrows
    @ParameterizedTest
    @MethodSource("vehicleDataProvider")
    public void testPrintNameAndPriceModelsWithLock(Vehicle vehicle) {
        ModelPrintHelper modelPrintHelper = new ModelPrintHelper(vehicle);
        Thread threadPrintName = new Thread(new ModelNamePrinterWithLockRunnable(modelPrintHelper));
        Thread threadPrintPrice = new Thread(new ModelPricePrinterWithLockRunnable(modelPrintHelper));
        threadPrintName.start();
        threadPrintPrice.start();
        threadPrintName.join();
        threadPrintPrice.join();

        String output = outContent.toString();
        assertEquals("model_one\r\n" +
                "model_two\r\n" +
                "model_three\r\n" +
                "1.0\r\n" +
                "2.0\r\n" +
                "3.0\r\n", output);
    }

    @SneakyThrows
    @Test
    public void testPrintBrand() {
        List<Vehicle> vehicles = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            vehicles.add(new Car(MODELS.get(i), 0));
        }

        ExecutorService executorService = Executors.newFixedThreadPool(1);
        vehicles.forEach(v -> executorService.execute(new BrandPrinterRunnable(v)));
        executorService.shutdown();
        executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);

        String output = outContent.toString();
        assertContainsString(output, MODEL_ONE);
        assertContainsString(output, MODEL_TWO);
        assertContainsString(output, MODEL_THREE);
        assertContainsString(output, MODEL_FOUR);
    }

    @SneakyThrows
    @Test
    public void testVehicleCreatorServiceWithBlockingQueue() {
        BlockingQueue<Vehicle> blockingQueue = new ArrayBlockingQueue<>(3);

        for (File file : files) {
            new Thread(new VehicleCreatorServiceRunnable(file.getAbsolutePath(), blockingQueue)).start();
        }

        StringBuilder result = new StringBuilder();
        int count = 0;
        while (count < 5) {
            result.append(blockingQueue.take());
            count++;
        }
        vehicleAsString.forEach(str -> assertTrue(result.toString().contains(str)));
    }

    private static Stream<Arguments> vehicleDataProvider() {
        return Stream.of(
                Arguments.of(car),
                Arguments.of(motorcycle)
        );
    }

    private void assertContainsString(String string, String actual) {
        assertTrue(string.contains(actual));
    }

}
