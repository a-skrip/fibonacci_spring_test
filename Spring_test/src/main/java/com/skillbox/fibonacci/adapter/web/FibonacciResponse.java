package com.skillbox.fibonacci.adapter.web;

import com.skillbox.fibonacci.domain.model.FibonacciNumber;

import java.math.BigInteger;

/**
 * @param index порядковый индекс числа Фибоначчи
 * @param value значение числа Фибоначчи
 */
public record FibonacciResponse(
        int index,
        BigInteger value
) {

    /**
     * @param number доменный объект
     * @return представление для клиентов
     */
    public static FibonacciResponse from(FibonacciNumber number) {
        return new FibonacciResponse(number.n().n(), number.value());
    }
}
