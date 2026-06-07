package com.skillbox.fibonacci.domain.model.exception;

/**
 * Базовый тип для всех бизнес-исключений
 */
public abstract sealed class BusinessException
        extends RuntimeException
        permits InvalidFibonacciIndexException {

    protected BusinessException(String message) {
        super(message);
    }
}
