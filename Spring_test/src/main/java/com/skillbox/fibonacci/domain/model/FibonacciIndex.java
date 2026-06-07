package com.skillbox.fibonacci.domain.model;

import com.skillbox.fibonacci.domain.model.exception.InvalidFibonacciIndexException;

/**
 * @param n Порядковый индекс числа Фибоначчи
 */
public record FibonacciIndex(
        int n
) {

    public FibonacciIndex {
        if (n < 1) {
            throw new InvalidFibonacciIndexException(n);
        }
    }

}
