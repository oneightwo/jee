package ru.ssau.exception;

public class DuplicateModelNameException extends Exception {

    public DuplicateModelNameException() {
        super("Дублирование названия модели");
    }
}
