package com.skillbox.fibonacci.domain.model;

import com.skillbox.fibonacci.domain.model.exception.InvalidFibonacciIndexException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FibonacciIndexTest {

    @ParameterizedTest
    @ValueSource(ints = {0, -1, -5, -100})
    @DisplayName("При создании индекса Фибоначчи менее 1 -> Ошибка")
    void whenCreateFibonacciIndexLessOne_thenThrowsException(int invalidIndex) {
        assertThrows(InvalidFibonacciIndexException.class, () -> new FibonacciIndex(invalidIndex));
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 12, 31, 144})
    @DisplayName("При создании индекса -> объект содержит индекс")
    void whenCreateFibonacciIndex_thenContainsNumber(int correctIndex) {
        FibonacciIndex fibonacciIndex = new FibonacciIndex(correctIndex);
        assertEquals(correctIndex, fibonacciIndex.n());
    }
}