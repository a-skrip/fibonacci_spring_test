package ru.skriplex.springnewsapplication.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.skriplex.springnewsapplication.exception.CategoryIsPresentException;
import ru.skriplex.springnewsapplication.exception.NewsNotFoundException;
import ru.skriplex.springnewsapplication.exception.NoTransmittedCategoryException;
import ru.skriplex.springnewsapplication.exception.CategoryNotFoundException;
import ru.skriplex.springnewsapplication.response.ErrorResponse;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleExceptionCategoryNotFound(CategoryNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler(CategoryIsPresentException.class)
    public ResponseEntity<ErrorResponse> handleExceptionCategoryIsPresent(CategoryIsPresentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler(NoTransmittedCategoryException.class)
    public ResponseEntity<ErrorResponse> handleExceptionNoTransmittedCategory(NoTransmittedCategoryException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler(NewsNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleExceptionNewsNotFound(NewsNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(ex.getMessage()));
    }
}
