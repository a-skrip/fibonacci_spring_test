package com.skillbox.fibonacci.domain.model;

import java.math.BigInteger;

/**
 * @param n     порядковый индекс числа Фибоначчи
 * @param value значение числа Фибоначчи
 */
public record FibonacciNumber(
        FibonacciIndex n,
        BigInteger value) {
}
