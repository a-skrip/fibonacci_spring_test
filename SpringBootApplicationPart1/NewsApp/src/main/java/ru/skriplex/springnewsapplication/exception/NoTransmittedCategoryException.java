package ru.skriplex.springnewsapplication.exception;

public class NoTransmittedCategoryException extends RuntimeException {
    public NoTransmittedCategoryException(String message) {
        super(message);
    }
}
