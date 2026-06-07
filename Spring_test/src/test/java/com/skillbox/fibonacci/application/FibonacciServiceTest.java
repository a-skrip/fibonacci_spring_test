package com.skillbox.fibonacci.application;

import com.skillbox.fibonacci.adapter.persistence.FibonacciRepository;
import com.skillbox.fibonacci.adapter.persistence.entity.FibonacciNumberEntity;
import com.skillbox.fibonacci.domain.model.FibonacciIndex;
import com.skillbox.fibonacci.domain.model.FibonacciNumber;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigInteger;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FibonacciServiceTest {

    @InjectMocks
    private FibonacciService service;
    @Mock
    private RecursiveFibonacciCalculator calculator;
    @Mock
    private FibonacciRepository repository;


    @Test
    @DisplayName("Если индекс есть в БД -> возвращаем из БД, не вызывая калькулятор")
    void whenSendIndexExistInDbForCalculate_thenReturnFromDbNotCalculate() {
        //Arrange
        FibonacciIndex index = new FibonacciIndex(7);
        BigInteger fibonacci = BigInteger.valueOf(13);
        FibonacciNumberEntity entity = new FibonacciNumberEntity(7, 13L);

        when(repository.findByIndex(7))
                .thenReturn(Optional.of(entity));

        //Act
        FibonacciNumber fibonacciNumber = service.fibonacciNumber(index);

        //Assert
        assertThat(fibonacciNumber).isNotNull()
                .extracting(FibonacciNumber::value)
                .isEqualTo(fibonacci);

        verify(calculator, never()).getFibonacciNumber(any(FibonacciIndex.class));
        verify(repository, never()).save(any(FibonacciNumberEntity.class));
        verify(repository, times(1)).findByIndex(7);
    }

    @Test
    @DisplayName("Если индекса нет в БД -> рассчитывает калькулятор")
    void whenSendIndexDontExistInDbForCalculate_thenCalculateFibonacci() {
        //Arrange
        FibonacciIndex index = new FibonacciIndex(7);
        BigInteger fibonacci = BigInteger.valueOf(13);

        when(repository.findByIndex(any(Integer.class)))
                .thenReturn(Optional.empty());
        when(calculator.getFibonacciNumber(eq(index)))
                .thenReturn(fibonacci);
        //Act
        FibonacciNumber fibonacciNumber = service.fibonacciNumber(index);

        //Assert
        assertThat(fibonacciNumber).isNotNull()
                .extracting(FibonacciNumber::value)
                .isEqualTo(fibonacci);

        verify(repository).findByIndex(7);
        verify(calculator).getFibonacciNumber(eq(index));
        verify(repository).save(any(FibonacciNumberEntity.class));
    }
}