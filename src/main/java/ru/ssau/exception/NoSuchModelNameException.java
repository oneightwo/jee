package ru.ssau.exception;

public class NoSuchModelNameException extends Exception {

    public NoSuchModelNameException() {
        super("Задание несуществующего имени модели");
    }
}
