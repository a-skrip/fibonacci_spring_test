package com.skillbox.fibonacci.application;

import com.skillbox.fibonacci.domain.model.FibonacciIndex;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RecursiveFibonacciCalculatorTest {

    private RecursiveFibonacciCalculator calculator;


    @BeforeEach
    void setUp() {
        calculator = new RecursiveFibonacciCalculator();
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2})
    @DisplayName("При передаче на расчет 1 или 2 -> вернется 1")
    void whenSendToCalculateFibonacciIndexOneOrTwo_thenReturnOne(int index) {
        //Arrange
        FibonacciIndex fibonacciIndex = new FibonacciIndex(index);

        //Act
        BigInteger fibonacciNumber = calculator.getFibonacciNumber(fibonacciIndex);

        //Assert
        assertEquals(BigInteger.ONE, fibonacciNumber);
    }

    @ParameterizedTest
    @CsvSource({
            "6,8",
            "11,89",
            "16,987"
    })
    @DisplayName("При передаче на расчет индекса -> число Фибоначчи")
    void whenSendToCalculateFibonacciIndex_thenReturnFibonacciNumber(int input, int expected) {
        //Arrange
        FibonacciIndex fibonacciIndex = new FibonacciIndex(input);

        //Act
        BigInteger fibonacciNumber = calculator.getFibonacciNumber(fibonacciIndex);

        //Assert
        assertEquals(BigInteger.valueOf(expected), fibonacciNumber);
    }
}