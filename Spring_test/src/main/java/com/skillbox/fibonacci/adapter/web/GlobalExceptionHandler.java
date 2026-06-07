package com.skillbox.fibonacci.adapter.web;

import com.skillbox.fibonacci.domain.model.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // Обрабатываем бизнес-исключение
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorMessageResponse> handleBusinessException(BusinessException e) {
        log.warn("Произошла бизнес ошибка!", e);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ErrorMessageResponse.warn(e.getMessage()));
    }

    // Обработка любых других исключений
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorMessageResponse> handleOtherExceptions(Exception e) {
        log.error("Произошла неожиданная ошибка!", e);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorMessageResponse.internalError());
    }
}
