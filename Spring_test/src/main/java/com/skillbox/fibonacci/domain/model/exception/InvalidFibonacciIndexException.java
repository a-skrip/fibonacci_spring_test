package com.skillbox.fibonacci.domain.model.exception;

public final class InvalidFibonacciIndexException extends BusinessException {
    public InvalidFibonacciIndexException(int index) {
        super("Число Фибоначчи не может иметь отрицательный или нулевой индекс." +
                "Попытка создать Число Фибоначчи с индексом " + index);
    }
}
