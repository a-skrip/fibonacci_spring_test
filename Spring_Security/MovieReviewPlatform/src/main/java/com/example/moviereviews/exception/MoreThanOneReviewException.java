package com.example.moviereviews.exception;

public class MoreThanOneReviewException extends RuntimeException {
    public MoreThanOneReviewException(String message) {
        super(message);
    }
}
