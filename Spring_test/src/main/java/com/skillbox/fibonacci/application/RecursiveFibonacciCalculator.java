package com.skillbox.fibonacci.application;

import com.skillbox.fibonacci.domain.model.FibonacciCalculator;
import com.skillbox.fibonacci.domain.model.FibonacciIndex;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

@Service
public class RecursiveFibonacciCalculator implements FibonacciCalculator {

    @Override
    public BigInteger getFibonacciNumber(FibonacciIndex index) {
        return recursive(index.n());
    }

    private BigInteger recursive(int index) {
        //Первые два числа в последовательности равны 1
        if (index == 1 || index == 2) {
            return BigInteger.ONE;
        }
        //Чтобы найти следующее число в последовательности, нужно сложить значение двух предыдущих
        return recursive(index - 1)
                .add(recursive(index - 2));
    }
}
