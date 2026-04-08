package com.example.moviereviews.exception;

public class PasswordUncorectedException extends RuntimeException {
    public PasswordUncorectedException(String message) {
        super(message);
    }
}
