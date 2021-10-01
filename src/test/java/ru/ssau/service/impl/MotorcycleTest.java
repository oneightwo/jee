package ru.ssau.service.impl;

import org.junit.jupiter.api.Test;
import ru.ssau.exception.DuplicateModelNameException;
import ru.ssau.exception.ModelPriceOutOfBoundsException;
import ru.ssau.exception.NoSuchModelNameException;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MotorcycleTest {

    private static final String BRAND_ONE = "brand_one";
    private static final String BRAND_TWO = "brand_two";
    private static final String MODEL_ONE = "model_one";
    private static final String MODEL_TWO = "model_two";
    private static final String MODEL_THREE = "model_three";
    private static final String MODEL_FOUR = "model_four";

    @Test
    public void testCreateModelsWhenCreateMotorcycle() {
        Motorcycle motorcycle = new Motorcycle(BRAND_ONE, 3);
        assertEquals(3, motorcycle.getModelSize());
        assertEquals(3, motorcycle.getModelNames().length);
        assertEquals(3, motorcycle.getModelPrices().length);
    }
    
    @Test
    public void testSetAndGetName() {
        Motorcycle motorcycle = new Motorcycle(BRAND_ONE, 0);
        assertEquals(BRAND_ONE, motorcycle.getBrand());

        motorcycle.setBrand(BRAND_TWO);
        assertEquals(BRAND_TWO, motorcycle.getBrand());
    }

    @Test
    public void testAddModels() throws DuplicateModelNameException, NoSuchModelNameException {
        Motorcycle motorcycle = new Motorcycle(BRAND_ONE, 0);
        motorcycle.addModel(MODEL_ONE, 1.0);
        motorcycle.addModel(MODEL_TWO, 2.0);
        motorcycle.addModel(MODEL_THREE, 3.0);
    }

    @Test
    public void testAddModelsWhenDuplicateModelName() throws DuplicateModelNameException, NoSuchModelNameException {
        Motorcycle motorcycle = new Motorcycle(BRAND_ONE, 0);
        motorcycle.addModel(MODEL_ONE, 1.0);
        assertThrows(DuplicateModelNameException.class, () -> motorcycle.addModel(MODEL_ONE, 2.0));
    }

    @Test
    public void testAddModelsWhenNoSuchModelName() throws DuplicateModelNameException, NoSuchModelNameException {
        Motorcycle motorcycle = new Motorcycle(BRAND_ONE, 0);
        motorcycle.addModel(MODEL_ONE, 1.0);
        assertThrows(NoSuchModelNameException.class, () -> motorcycle.addModel(null, 2.0));
    }

    @Test
    public void testAddModelsWhenModelPriceOutOfBounds() throws DuplicateModelNameException, NoSuchModelNameException {
        Motorcycle motorcycle = new Motorcycle(BRAND_ONE, 0);
        motorcycle.addModel(MODEL_ONE, 1.0);
        assertThrows(ModelPriceOutOfBoundsException.class, () -> motorcycle.addModel(MODEL_TWO, 0.0));
        assertThrows(ModelPriceOutOfBoundsException.class, () -> motorcycle.addModel(MODEL_TWO, null));
    }

    @Test
    public void testGetModelNames() throws DuplicateModelNameException, NoSuchModelNameException {
        Motorcycle motorcycle = new Motorcycle(BRAND_ONE, 0);
        motorcycle.addModel(MODEL_ONE, 1.0);
        motorcycle.addModel(MODEL_TWO, 2.0);
        motorcycle.addModel(MODEL_THREE, 3.0);

        assertEquals("[model_one, model_two, model_three]", Arrays.toString(motorcycle.getModelNames()));
    }

    @Test
    public void testGetModelPrices() throws DuplicateModelNameException, NoSuchModelNameException {
        Motorcycle motorcycle = new Motorcycle(BRAND_ONE, 0);
        motorcycle.addModel(MODEL_ONE, 1.0);
        motorcycle.addModel(MODEL_TWO, 2.0);
        motorcycle.addModel(MODEL_THREE, 3.0);

        assertEquals("[1.0, 2.0, 3.0]", Arrays.toString(motorcycle.getModelPrices()));
    }

    @Test
    public void testGetModelSize() throws DuplicateModelNameException, NoSuchModelNameException {
        Motorcycle motorcycle = new Motorcycle(BRAND_ONE, 0);
        motorcycle.addModel(MODEL_ONE, 1.0);
        motorcycle.addModel(MODEL_TWO, 2.0);
        motorcycle.addModel(MODEL_THREE, 3.0);

        assertEquals(3, motorcycle.getModelSize());
    }

    @Test
    public void testGetPriceByName() throws DuplicateModelNameException, NoSuchModelNameException {
        Motorcycle motorcycle = new Motorcycle(BRAND_ONE, 0);
        motorcycle.addModel(MODEL_ONE, 1.0);
        motorcycle.addModel(MODEL_TWO, 2.0);
        motorcycle.addModel(MODEL_THREE, 3.0);

        assertEquals(2.0, motorcycle.getPriceByName(MODEL_TWO));
    }

    @Test
    public void testGetPriceByNameWhenNoSuchModelName() throws DuplicateModelNameException, NoSuchModelNameException {
        Motorcycle motorcycle = new Motorcycle(BRAND_ONE, 0);
        motorcycle.addModel(MODEL_ONE, 1.0);
        motorcycle.addModel(MODEL_TWO, 2.0);
        motorcycle.addModel(MODEL_THREE, 3.0);

        assertThrows(NoSuchModelNameException.class, () -> motorcycle.getPriceByName(MODEL_FOUR));
        assertThrows(NoSuchModelNameException.class, () -> motorcycle.getPriceByName(null));
    }

    @Test
    public void testUpdatePriceByName() throws DuplicateModelNameException, NoSuchModelNameException {
        Motorcycle motorcycle = new Motorcycle(BRAND_ONE, 0);
        motorcycle.addModel(MODEL_ONE, 1.0);
        motorcycle.addModel(MODEL_TWO, 2.0);

        motorcycle.updatePriceByName(MODEL_ONE, 3.0);
        assertEquals(motorcycle.getPriceByName(MODEL_ONE), 3.0);
    }

    @Test
    public void testUpdatePriceByNameWhenNoSuchModelName() throws DuplicateModelNameException, NoSuchModelNameException {
        Motorcycle motorcycle = new Motorcycle(BRAND_ONE, 0);
        motorcycle.addModel(MODEL_ONE, 1.0);
        motorcycle.addModel(MODEL_TWO, 2.0);

        assertThrows(NoSuchModelNameException.class, () -> motorcycle.updatePriceByName(MODEL_THREE, 3.0));
        assertThrows(NoSuchModelNameException.class, () -> motorcycle.updatePriceByName(null, 3.0));
    }

    @Test
    public void testUpdatePriceByNameWhenModelPriceOutOfBounds() throws DuplicateModelNameException, NoSuchModelNameException {
        Motorcycle motorcycle = new Motorcycle(BRAND_ONE, 0);
        motorcycle.addModel(MODEL_ONE, 1.0);
        motorcycle.addModel(MODEL_TWO, 2.0);

        assertThrows(ModelPriceOutOfBoundsException.class, () -> motorcycle.updatePriceByName(MODEL_ONE, 0.0));
        assertThrows(ModelPriceOutOfBoundsException.class, () -> motorcycle.updatePriceByName(MODEL_ONE, null));
    }

    @Test
    public void testUpdateName() throws DuplicateModelNameException, NoSuchModelNameException {
        Motorcycle motorcycle = new Motorcycle(BRAND_ONE, 0);
        motorcycle.addModel(MODEL_ONE, 1.0);
        motorcycle.addModel(MODEL_TWO, 2.0);

        motorcycle.updateName(MODEL_ONE, MODEL_THREE);
        assertEquals("[model_three, model_two]", Arrays.toString(motorcycle.getModelNames()));
        motorcycle.updateName(MODEL_THREE, MODEL_THREE);
        assertEquals("[model_three, model_two]", Arrays.toString(motorcycle.getModelNames()));
    }

    @Test
    public void testUpdateNameWhenNoSuchModelName() throws DuplicateModelNameException, NoSuchModelNameException {
        Motorcycle motorcycle = new Motorcycle(BRAND_ONE, 0);
        motorcycle.addModel(MODEL_ONE, 1.0);
        motorcycle.addModel(MODEL_TWO, 2.0);

        assertThrows(NoSuchModelNameException.class, () -> motorcycle.updateName(MODEL_ONE, null));
        assertThrows(NoSuchModelNameException.class, () -> motorcycle.updateName(null, MODEL_ONE));
        assertThrows(NoSuchModelNameException.class, () -> motorcycle.updateName(null, null));
    }

    @Test
    public void testUpdateNameWhenDuplicateModelName() throws DuplicateModelNameException, NoSuchModelNameException {
        Motorcycle motorcycle = new Motorcycle(BRAND_ONE, 0);
        motorcycle.addModel(MODEL_ONE, 1.0);
        motorcycle.addModel(MODEL_TWO, 2.0);

        assertThrows(DuplicateModelNameException.class, () -> motorcycle.updateName(MODEL_ONE, MODEL_TWO));
    }

    @Test
    public void testDeleteModelFromStart() throws NoSuchModelNameException, DuplicateModelNameException {
        Motorcycle motorcycle = new Motorcycle(BRAND_ONE, 0);
        motorcycle.addModel(MODEL_ONE, 1.0);
        motorcycle.addModel(MODEL_TWO, 2.0);
        motorcycle.addModel(MODEL_THREE, 3.0);
        motorcycle.addModel(MODEL_FOUR, 4.0);

        motorcycle.deleteModel(MODEL_ONE, 1.0);
        assertEquals("[model_two, model_three, model_four]", Arrays.toString(motorcycle.getModelNames()));
        assertEquals("[2.0, 3.0, 4.0]", Arrays.toString(motorcycle.getModelPrices()));
    }

    @Test
    public void testDeleteModelFromCenter() throws NoSuchModelNameException, DuplicateModelNameException {
        Motorcycle motorcycle = new Motorcycle(BRAND_ONE, 0);
        motorcycle.addModel(MODEL_ONE, 1.0);
        motorcycle.addModel(MODEL_TWO, 2.0);
        motorcycle.addModel(MODEL_THREE, 3.0);
        motorcycle.addModel(MODEL_FOUR, 4.0);

        motorcycle.deleteModel(MODEL_TWO, 2.0);
        assertEquals("[model_one, model_three, model_four]", Arrays.toString(motorcycle.getModelNames()));
        assertEquals("[1.0, 3.0, 4.0]", Arrays.toString(motorcycle.getModelPrices()));
    }

    @Test
    public void testDeleteModelFromEnd() throws NoSuchModelNameException, DuplicateModelNameException {
        Motorcycle motorcycle = new Motorcycle(BRAND_ONE, 0);
        motorcycle.addModel(MODEL_ONE, 1.0);
        motorcycle.addModel(MODEL_TWO, 2.0);
        motorcycle.addModel(MODEL_THREE, 3.0);
        motorcycle.addModel(MODEL_FOUR, 4.0);

        motorcycle.deleteModel(MODEL_FOUR, 4.0);
        assertEquals("[model_one, model_two, model_three]", Arrays.toString(motorcycle.getModelNames()));
        assertEquals("[1.0, 2.0, 3.0]", Arrays.toString(motorcycle.getModelPrices()));
    }

    @Test
    public void testDeleteModelWhenNoSuchModelName() throws NoSuchModelNameException, DuplicateModelNameException {
        Motorcycle motorcycle = new Motorcycle(BRAND_ONE, 0);
        motorcycle.addModel(MODEL_ONE, 1.0);
        motorcycle.addModel(MODEL_TWO, 2.0);
        motorcycle.addModel(MODEL_THREE, 3.0);

        assertThrows(NoSuchModelNameException.class, () -> motorcycle.deleteModel(MODEL_FOUR, 4.0));
        assertThrows(NoSuchModelNameException.class, () -> motorcycle.deleteModel(null, 4.0));
    }

    @Test
    public void testDeleteModelWhenModelPriceOutOfBounds() throws NoSuchModelNameException, DuplicateModelNameException {
        Motorcycle motorcycle = new Motorcycle(BRAND_ONE, 0);
        motorcycle.addModel(MODEL_ONE, 1.0);
        motorcycle.addModel(MODEL_TWO, 2.0);
        motorcycle.addModel(MODEL_THREE, 3.0);

        assertThrows(ModelPriceOutOfBoundsException.class, () -> motorcycle.deleteModel(MODEL_ONE, 0.0));
        assertThrows(ModelPriceOutOfBoundsException.class, () -> motorcycle.deleteModel(MODEL_ONE, null));
    }

    @Test
    public void testToString() throws NoSuchModelNameException, DuplicateModelNameException {
        Motorcycle motorcycle = new Motorcycle(BRAND_ONE, 0);
        motorcycle.addModel(MODEL_ONE, 1.0);
        motorcycle.addModel(MODEL_TWO, 2.0);
        motorcycle.addModel(MODEL_THREE, 3.0);

        assertEquals("Motorcycle{brand='brand_one', models=[Model{name='model_one', price=1.0}, " +
                "Model{name='model_two', price=2.0}, " +
                "Model{name='model_three', price=3.0}]}", motorcycle.toString());
    }
}
