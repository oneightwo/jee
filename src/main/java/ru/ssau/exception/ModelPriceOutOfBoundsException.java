package ru.ssau.exception;

public class ModelPriceOutOfBoundsException extends RuntimeException {

    public ModelPriceOutOfBoundsException() {
        super("Задание неверной цены модели");
    }
}
