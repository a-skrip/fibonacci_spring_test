package ru.skriplex.springnewsapplication.exception;

public class CategoryIsPresentException extends RuntimeException {
    public CategoryIsPresentException(String message) {
        super(message);
    }
}
