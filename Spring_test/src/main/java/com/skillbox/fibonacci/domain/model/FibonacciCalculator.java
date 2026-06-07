package com.skillbox.fibonacci.domain.model;

import java.math.BigInteger;

/**
 * Расчет числа Фибоначчи
 */
public interface FibonacciCalculator {

    /**
     * @param index индекс искомого числа
     * @return значение числа по запрошенному индексу
     */
    BigInteger getFibonacciNumber(FibonacciIndex index);
}
