package ru.ssau.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import ru.ssau.exception.DuplicateModelNameException;
import ru.ssau.exception.ModelPriceOutOfBoundsException;
import ru.ssau.exception.NoSuchModelNameException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CarTest {

    private static final String BRAND_ONE = "brand_one";
    private static final String BRAND_TWO = "brand_two";
    private static final String MODEL_ONE = "model_one";
    private static final String MODEL_TWO = "model_two";
    private static final String MODEL_THREE = "model_three";
    private static final String MODEL_FOUR = "model_four";
    private static final String TEMP_FILE_TXT = "tempFile.txt";

    @TempDir
    File parentFile;

    private File file;

    @BeforeEach
    public void tearDown() {
        file = new File(parentFile, TEMP_FILE_TXT);
    }

    @Test
    public void testCreateModelsWhenCreateMotorcycle() {
        Car car = new Car(BRAND_ONE, 3);
        assertEquals(3, car.getModelSize());
        assertEquals(3, car.getModelNames().length);
        assertEquals(3, car.getModelPrices().length);
    }
    
    @Test
    public void testSetAndGetName() {
        Car car = new Car(BRAND_ONE, 0);
        assertEquals(BRAND_ONE, car.getBrand());

        car.setBrand(BRAND_TWO);
        assertEquals(BRAND_TWO, car.getBrand());
    }

    @Test
    public void testCreateModelsWhenCreateMotorcycle() {
        Car car = new Car(BRAND_ONE, 3);
        assertEquals(3, car.getModelSize());
        assertEquals(3, car.getModelNames().length);
        assertEquals(3, car.getModelPrices().length);
    }

    @Test
    public void testAddModels() throws DuplicateModelNameException, NoSuchModelNameException {
        Car car = new Car(BRAND_ONE, 0);
        car.addModel(MODEL_ONE, 1.0);
        car.addModel(MODEL_TWO, 2.0);
        car.addModel(MODEL_THREE, 3.0);
    }

    @Test
    public void testAddModelsWhenDuplicateModelName() throws DuplicateModelNameException, NoSuchModelNameException {
        Car car = new Car(BRAND_ONE, 0);
        car.addModel(MODEL_ONE, 1.0);
        assertThrows(DuplicateModelNameException.class, () -> car.addModel(MODEL_ONE, 2.0));
    }

    @Test
    public void testAddModelsWhenNoSuchModelName() throws DuplicateModelNameException, NoSuchModelNameException {
        Car car = new Car(BRAND_ONE, 0);
        car.addModel(MODEL_ONE, 1.0);
        assertThrows(NoSuchModelNameException.class, () -> car.addModel(null, 2.0));
    }

    @Test
    public void testAddModelsWhenModelPriceOutOfBounds() throws DuplicateModelNameException, NoSuchModelNameException {
        Car car = new Car(BRAND_ONE, 0);
        car.addModel(MODEL_ONE, 1.0);
        assertThrows(ModelPriceOutOfBoundsException.class, () -> car.addModel(MODEL_TWO, 0.0));
        assertThrows(ModelPriceOutOfBoundsException.class, () -> car.addModel(MODEL_TWO, null));
    }

    @Test
    public void testGetModelNames() throws DuplicateModelNameException, NoSuchModelNameException {
        Car car = new Car(BRAND_ONE, 0);
        car.addModel(MODEL_ONE, 1.0);
        car.addModel(MODEL_TWO, 2.0);
        car.addModel(MODEL_THREE, 3.0);

        assertEquals("[model_one, model_two, model_three]", Arrays.toString(car.getModelNames()));
    }

    @Test
    public void testGetModelPrices() throws DuplicateModelNameException, NoSuchModelNameException {
        Car car = new Car(BRAND_ONE, 0);
        car.addModel(MODEL_ONE, 1.0);
        car.addModel(MODEL_TWO, 2.0);
        car.addModel(MODEL_THREE, 3.0);

        assertEquals("[1.0, 2.0, 3.0]", Arrays.toString(car.getModelPrices()));
    }

    @Test
    public void testGetModelSize() throws DuplicateModelNameException, NoSuchModelNameException {
        Car car = new Car(BRAND_ONE, 0);
        car.addModel(MODEL_ONE, 1.0);
        car.addModel(MODEL_TWO, 2.0);
        car.addModel(MODEL_THREE, 3.0);

        assertEquals(3, car.getModelSize());
    }

    @Test
    public void testGetPriceByName() throws DuplicateModelNameException, NoSuchModelNameException {
        Car car = new Car(BRAND_ONE, 0);
        car.addModel(MODEL_ONE, 1.0);
        car.addModel(MODEL_TWO, 2.0);
        car.addModel(MODEL_THREE, 3.0);

        assertEquals(2.0, car.getPriceByName(MODEL_TWO));
    }

    @Test
    public void testGetPriceByNameWhenNoSuchModelName() throws DuplicateModelNameException, NoSuchModelNameException {
        Car car = new Car(BRAND_ONE, 0);
        car.addModel(MODEL_ONE, 1.0);
        car.addModel(MODEL_TWO, 2.0);
        car.addModel(MODEL_THREE, 3.0);

        assertThrows(NoSuchModelNameException.class, () -> car.getPriceByName(MODEL_FOUR));
        assertThrows(NoSuchModelNameException.class, () -> car.getPriceByName(null));
    }

    @Test
    public void testUpdatePriceByName() throws DuplicateModelNameException, NoSuchModelNameException {
        Car car = new Car(BRAND_ONE, 0);
        car.addModel(MODEL_ONE, 1.0);
        car.addModel(MODEL_TWO, 2.0);

        car.updatePriceByName(MODEL_ONE, 3.0);
        assertEquals(car.getPriceByName(MODEL_ONE), 3.0);
    }

    @Test
    public void testUpdatePriceByNameWhenNoSuchModelName() throws DuplicateModelNameException, NoSuchModelNameException {
        Car car = new Car(BRAND_ONE, 0);
        car.addModel(MODEL_ONE, 1.0);
        car.addModel(MODEL_TWO, 2.0);

        assertThrows(NoSuchModelNameException.class, () -> car.updatePriceByName(MODEL_THREE, 3.0));
        assertThrows(NoSuchModelNameException.class, () -> car.updatePriceByName(null, 3.0));
    }

    @Test
    public void testUpdatePriceByNameWhenModelPriceOutOfBounds() throws DuplicateModelNameException, NoSuchModelNameException {
        Car car = new Car(BRAND_ONE, 0);
        car.addModel(MODEL_ONE, 1.0);
        car.addModel(MODEL_TWO, 2.0);

        assertThrows(ModelPriceOutOfBoundsException.class, () -> car.updatePriceByName(MODEL_ONE, 0.0));
        assertThrows(ModelPriceOutOfBoundsException.class, () -> car.updatePriceByName(MODEL_ONE, null));
    }

    @Test
    public void testUpdateName() throws DuplicateModelNameException, NoSuchModelNameException {
        Car car = new Car(BRAND_ONE, 0);
        car.addModel(MODEL_ONE, 1.0);
        car.addModel(MODEL_TWO, 2.0);

        car.updateName(MODEL_ONE, MODEL_THREE);
        assertEquals("[model_three, model_two]", Arrays.toString(car.getModelNames()));
        car.updateName(MODEL_THREE, MODEL_THREE);
        assertEquals("[model_three, model_two]", Arrays.toString(car.getModelNames()));
    }

    @Test
    public void testUpdateNameWhenNoSuchModelName() throws DuplicateModelNameException, NoSuchModelNameException {
        Car car = new Car(BRAND_ONE, 0);
        car.addModel(MODEL_ONE, 1.0);
        car.addModel(MODEL_TWO, 2.0);

        assertThrows(NoSuchModelNameException.class, () -> car.updateName(MODEL_ONE, null));
        assertThrows(NoSuchModelNameException.class, () -> car.updateName(null, MODEL_ONE));
        assertThrows(NoSuchModelNameException.class, () -> car.updateName(null, null));
    }

    @Test
    public void testUpdateNameWhenDuplicateModelName() throws DuplicateModelNameException, NoSuchModelNameException {
        Car car = new Car(BRAND_ONE, 0);
        car.addModel(MODEL_ONE, 1.0);
        car.addModel(MODEL_TWO, 2.0);

        assertThrows(DuplicateModelNameException.class, () -> car.updateName(MODEL_TWO, MODEL_ONE));
    }

    @Test
    public void testDeleteModelFromStart() throws NoSuchModelNameException, DuplicateModelNameException {
        Car car = new Car(BRAND_ONE, 0);
        car.addModel(MODEL_ONE, 1.0);
        car.addModel(MODEL_TWO, 2.0);
        car.addModel(MODEL_THREE, 3.0);
        car.addModel(MODEL_FOUR, 4.0);

        car.deleteModel(MODEL_ONE, 1.0);
        assertEquals("[model_two, model_three, model_four]", Arrays.toString(car.getModelNames()));
        assertEquals("[2.0, 3.0, 4.0]", Arrays.toString(car.getModelPrices()));
    }

    @Test
    public void testDeleteModelFromCenter() throws NoSuchModelNameException, DuplicateModelNameException {
        Car car = new Car(BRAND_ONE, 0);
        car.addModel(MODEL_ONE, 1.0);
        car.addModel(MODEL_TWO, 2.0);
        car.addModel(MODEL_THREE, 3.0);
        car.addModel(MODEL_FOUR, 4.0);

        car.deleteModel(MODEL_TWO, 2.0);
        assertEquals("[model_one, model_three, model_four]", Arrays.toString(car.getModelNames()));
        assertEquals("[1.0, 3.0, 4.0]", Arrays.toString(car.getModelPrices()));
    }

    @Test
    public void testDeleteModelFromEnd() throws NoSuchModelNameException, DuplicateModelNameException {
        Car car = new Car(BRAND_ONE, 0);
        car.addModel(MODEL_ONE, 1.0);
        car.addModel(MODEL_TWO, 2.0);
        car.addModel(MODEL_THREE, 3.0);
        car.addModel(MODEL_FOUR, 4.0);

        car.deleteModel(MODEL_FOUR, 4.0);
        assertEquals("[model_one, model_two, model_three]", Arrays.toString(car.getModelNames()));
        assertEquals("[1.0, 2.0, 3.0]", Arrays.toString(car.getModelPrices()));
    }

    @Test
    public void testDeleteModelWhenNoSuchModelName() throws NoSuchModelNameException, DuplicateModelNameException {
        Car car = new Car(BRAND_ONE, 0);
        car.addModel(MODEL_ONE, 1.0);
        car.addModel(MODEL_TWO, 2.0);
        car.addModel(MODEL_THREE, 3.0);

        assertThrows(NoSuchModelNameException.class, () -> car.deleteModel(MODEL_FOUR, 4.0));
        assertThrows(NoSuchModelNameException.class, () -> car.deleteModel(null, 4.0));
    }

    @Test
    public void testDeleteModelWhenModelPriceOutOfBounds() throws NoSuchModelNameException, DuplicateModelNameException {
        Car car = new Car(BRAND_ONE, 0);
        car.addModel(MODEL_ONE, 1.0);
        car.addModel(MODEL_TWO, 2.0);
        car.addModel(MODEL_THREE, 3.0);

        assertThrows(ModelPriceOutOfBoundsException.class, () -> car.deleteModel(MODEL_ONE, 0.0));
        assertThrows(ModelPriceOutOfBoundsException.class, () -> car.deleteModel(MODEL_ONE, null));
    }

    @Test
    public void testToString() throws NoSuchModelNameException, DuplicateModelNameException {
        Car car = new Car(BRAND_ONE, 0);
        car.addModel(MODEL_ONE, 1.0);
        car.addModel(MODEL_TWO, 2.0);
        car.addModel(MODEL_THREE, 3.0);

        assertEquals("Car{brand='brand_one', models=[Model{name='model_one', price=1.0}, " +
                "Model{name='model_two', price=2.0}, " +
                "Model{name='model_three', price=3.0}]}", car.toString());
    }

    @Test
    public void testSerialization() throws IOException, DuplicateModelNameException, NoSuchModelNameException, ClassNotFoundException {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(file));
             ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(file))) {
            Car expected = new Car(BRAND_ONE, 0);
            expected.addModel(MODEL_ONE, 1.0);
            expected.addModel(MODEL_TWO, 2.0);
            expected.addModel(MODEL_THREE, 3.0);
            outputStream.writeObject(expected);

            Car actual = (Car) inputStream.readObject();
            assertEquals(expected.getBrand(), actual.getBrand());
            assertEquals(expected.getModelSize(), actual.getModelSize());
            assertArrayEquals(expected.getModelNames(), actual.getModelNames());
            assertArrayEquals(expected.getModelPrices(), actual.getModelPrices());
        }
    }
}
