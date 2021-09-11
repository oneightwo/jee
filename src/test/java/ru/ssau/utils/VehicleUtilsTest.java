package ru.ssau.utils;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import ru.ssau.exception.DuplicateModelNameException;
import ru.ssau.exception.NoSuchModelNameException;
import ru.ssau.service.impl.Car;
import ru.ssau.service.impl.Motorcycle;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class VehicleUtilsTest {

    private static final String BRAND = "brand";
    private static final String MODEL_ONE = "model_one";
    private static final String MODEL_TWO = "model_two";
    private static final String MODEL_THREE = "model_three";

    private Car car;
    private Motorcycle motorcycle;

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    @BeforeAll
    public void init() throws DuplicateModelNameException, NoSuchModelNameException {
        System.setOut(new PrintStream(outContent));

        car = new Car(BRAND, 3);
        car.addModel(MODEL_ONE, 1.0);
        car.addModel(MODEL_TWO, 2.0);
        car.addModel(MODEL_THREE, 3.0);

        motorcycle = new Motorcycle(BRAND);
        motorcycle.addModel(MODEL_ONE, 1.0);
        motorcycle.addModel(MODEL_TWO, 2.0);
        motorcycle.addModel(MODEL_THREE, 3.0);
    }

    @BeforeEach
    public void tearDown() {
        outContent.reset();
    }

    @Test
    public void testGetAveragePrice() {
        assertEquals(2.0, VehicleUtils.getAveragePrice(car));
        assertEquals(2.0, VehicleUtils.getAveragePrice(motorcycle));
    }

    @Test
    public void testCarPrintModelNames() {
        VehicleUtils.printModelNames(car);
        assertEquals("[model_one, model_two, model_three]\r\n", outContent.toString());
    }

    @Test
    public void testMotorcyclePrintModelNames() {
        VehicleUtils.printModelNames(motorcycle);
        assertEquals("[model_one, model_two, model_three]\r\n", outContent.toString());
    }

    @Test
    public void testCarPrintModelPrices() {
        VehicleUtils.printModelPrices(car);
        assertEquals("[1.0, 2.0, 3.0]\r\n", outContent.toString());
    }

    @Test
    public void testMotorcyclePrintModelPrices() {
        VehicleUtils.printModelPrices(motorcycle);
        assertEquals("[1.0, 2.0, 3.0]\r\n", outContent.toString());
    }

    @Test
    public void testCarPrintModels() {
        VehicleUtils.printModels(car);
        assertEquals("Model: model_one; Price: 1.0\r\n" +
                "Model: model_two; Price: 2.0\r\n" +
                "Model: model_three; Price: 3.0\r\n", outContent.toString());
    }

    @Test
    public void testMotorcyclePrintModels() {
        VehicleUtils.printModels(motorcycle);
        assertEquals("Model: model_one; Price: 1.0\r\n" +
                "Model: model_two; Price: 2.0\r\n" +
                "Model: model_three; Price: 3.0\r\n", outContent.toString());
    }
}
