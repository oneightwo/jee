package ru.ssau.utils;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.ssau.exception.DuplicateModelNameException;
import ru.ssau.exception.NoSuchModelNameException;
import ru.ssau.service.Vehicle;
import ru.ssau.service.impl.Car;
import ru.ssau.service.impl.Motorcycle;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class VehicleUtilsTest {

    private static final String BRAND = "brand";
    private static final String MODEL_ONE = "model_one";
    private static final String MODEL_TWO = "model_two";
    private static final String MODEL_THREE = "model_three";
    private static final String TEMP_FILE_TXT = "tempFile.txt";

    @TempDir
    File parentFile;

    private File file;
    private static Car car;
    private static Motorcycle motorcycle;

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    @BeforeAll
    public void init() throws DuplicateModelNameException, NoSuchModelNameException {
        System.setOut(new PrintStream(outContent));

        car = new Car(BRAND, 0);
        car.addModel(MODEL_ONE, 1.0);
        car.addModel(MODEL_TWO, 2.0);
        car.addModel(MODEL_THREE, 3.0);

        motorcycle = new Motorcycle(BRAND, 0);
        motorcycle.addModel(MODEL_ONE, 1.0);
        motorcycle.addModel(MODEL_TWO, 2.0);
        motorcycle.addModel(MODEL_THREE, 3.0);
    }

    @BeforeEach
    public void tearDown() {
        outContent.reset();
        System.setOut(new PrintStream(outContent));
        file = new File(parentFile, TEMP_FILE_TXT);
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

    @ParameterizedTest
    @MethodSource("vehicleDataProvider")
    public void testOutputVehicle(Vehicle expectedResult) throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        VehicleUtils.outputVehicle(expectedResult, fileOutputStream);

        try (DataInputStream dataInputStream = new DataInputStream(new FileInputStream(file))) {
            assertBytesToStringFromInputStream(expectedResult.getClass().getName(), dataInputStream);
            assertBytesToStringFromInputStream(expectedResult.getBrand(), dataInputStream);
            int modelSize = dataInputStream.readInt();
            assertEquals(expectedResult.getModelSize(), modelSize);
            String[] modelNames = expectedResult.getModelNames();
            Double[] modelPrices = expectedResult.getModelPrices();
            for (int i = 0; i < modelSize; i++) {
                assertBytesToStringFromInputStream(modelNames[i], dataInputStream);
                assertEquals(modelPrices[i], dataInputStream.readDouble());
            }
        }
    }

    private void assertBytesToStringFromInputStream(String expectedResult, DataInputStream dataInputStream) throws IOException {
        int stringSize = dataInputStream.readInt();
        assertEquals(expectedResult.length(), stringSize);
        assertEquals(expectedResult, new String(dataInputStream.readNBytes(stringSize)));
    }

    @ParameterizedTest
    @MethodSource("vehicleDataProvider")
    public void testInputVehicle(Vehicle expectedResult) throws IOException {
        try (DataOutputStream dataOutputStream = new DataOutputStream(new FileOutputStream(file))) {
            writeStringToByteOutput(expectedResult.getClass().getName(), dataOutputStream);
            writeStringToByteOutput(expectedResult.getBrand(), dataOutputStream);
            dataOutputStream.writeInt(expectedResult.getModelSize());
            String[] modelNames = expectedResult.getModelNames();
            Double[] modelPrices = expectedResult.getModelPrices();
            for (int i = 0; i < modelNames.length; i++) {
                writeStringToByteOutput(modelNames[i], dataOutputStream);
                dataOutputStream.writeDouble(modelPrices[i]);
            }
        }

        Vehicle vehicle = VehicleUtils.inputVehicle(new FileInputStream(file));
        assertEquals(expectedResult.getClass().getName(), vehicle.getClass().getName());
        assertEquals(expectedResult.getBrand(), vehicle.getBrand());
        assertEquals(expectedResult.getModelSize(), vehicle.getModelSize());
        assertArrayEquals(expectedResult.getModelNames(), vehicle.getModelNames());
        assertArrayEquals(expectedResult.getModelPrices(), vehicle.getModelPrices());
    }

    private void writeStringToByteOutput(String str, DataOutputStream dataOutputStream) {
        try {
            dataOutputStream.writeInt(str.length());
            dataOutputStream.write(str.getBytes());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @ParameterizedTest
    @MethodSource("vehicleDataProvider")
    public void testWriteVehicle(Vehicle expectedResult) throws IOException {
        VehicleUtils.writeVehicle(expectedResult, new FileWriter(file));

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
            assertEquals(expectedResult.getClass().getName(), bufferedReader.readLine());
            assertEquals(expectedResult.getBrand(), bufferedReader.readLine());
            int modelSize = Integer.parseInt(bufferedReader.readLine());
            assertEquals(expectedResult.getModelSize(), modelSize);
            String[] modelNames = expectedResult.getModelNames();
            Double[] modelPrices = expectedResult.getModelPrices();
            for (int i = 0; i < modelSize; i++) {
                assertEquals(modelNames[i], bufferedReader.readLine());
                assertEquals(modelPrices[i], Double.parseDouble(bufferedReader.readLine()));
            }
        }
    }

    @ParameterizedTest
    @MethodSource("vehicleDataProvider")
    public void testReadVehicle(Vehicle expectedResult) throws IOException {
        try (PrintWriter printWriter = new PrintWriter(file)) {
            printWriter.println(expectedResult.getClass().getName());
            printWriter.println(expectedResult.getBrand());
            printWriter.println(expectedResult.getModelSize());
            String[] modelNames = expectedResult.getModelNames();
            Double[] modelPrices = expectedResult.getModelPrices();
            for (int i = 0; i < modelNames.length; i++) {
                printWriter.println(modelNames[i]);
                printWriter.println(modelPrices[i]);
            }
        }

        Vehicle vehicle = VehicleUtils.readVehicle(new FileReader(file));
        assertEquals(expectedResult.getClass().getName(), vehicle.getClass().getName());
        assertEquals(expectedResult.getBrand(), vehicle.getBrand());
        assertEquals(expectedResult.getModelSize(), vehicle.getModelSize());
        assertArrayEquals(expectedResult.getModelNames(), vehicle.getModelNames());
        assertArrayEquals(expectedResult.getModelPrices(), vehicle.getModelPrices());
    }

    @ParameterizedTest
    @MethodSource("vehicleDataProvider")
    public void testOutputVehicleSystemOut(Vehicle expectedResult) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        VehicleUtils.outputVehicle(expectedResult, System.out);

        try (DataInputStream dataInputStream = new DataInputStream(new ByteArrayInputStream(out.toByteArray()))) {
            assertBytesToStringFromInputStream(expectedResult.getClass().getName(), dataInputStream);
            assertBytesToStringFromInputStream(expectedResult.getBrand(), dataInputStream);
            int modelSize = dataInputStream.readInt();
            assertEquals(modelSize, expectedResult.getModelSize());
            String[] modelNames = expectedResult.getModelNames();
            Double[] modelPrices = expectedResult.getModelPrices();
            for (int i = 0; i < modelSize; i++) {
                assertBytesToStringFromInputStream(modelNames[i], dataInputStream);
                assertEquals(modelPrices[i], dataInputStream.readDouble());
            }
        }
    }

    @ParameterizedTest
    @MethodSource("vehicleDataProvider")
    public void testInputVehicleCarSystemIn(Vehicle expectedResult) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));

        try (DataOutputStream dataOutputStream = new DataOutputStream(System.out)) {
            writeStringToByteOutput(expectedResult.getClass().getName(), dataOutputStream);
            writeStringToByteOutput(expectedResult.getBrand(), dataOutputStream);
            dataOutputStream.writeInt(expectedResult.getModelSize());
            String[] modelNames = expectedResult.getModelNames();
            Double[] modelPrices = expectedResult.getModelPrices();
            for (int i = 0; i < modelNames.length; i++) {
                writeStringToByteOutput(modelNames[i], dataOutputStream);
                dataOutputStream.writeDouble(modelPrices[i]);
            }
        }
        System.setIn(new ByteArrayInputStream(out.toByteArray()));

        Vehicle vehicle = VehicleUtils.inputVehicle(System.in);
        assertEquals(expectedResult.getClass().getName(), vehicle.getClass().getName());
        assertEquals(expectedResult.getBrand(), vehicle.getBrand());
        assertEquals(expectedResult.getModelSize(), vehicle.getModelSize());
        assertArrayEquals(expectedResult.getModelNames(), vehicle.getModelNames());
        assertArrayEquals(expectedResult.getModelPrices(), vehicle.getModelPrices());
    }

    private static Stream<Arguments> vehicleDataProvider() {
        return Stream.of(
                Arguments.of(car),
                Arguments.of(motorcycle)
        );
    }
}
