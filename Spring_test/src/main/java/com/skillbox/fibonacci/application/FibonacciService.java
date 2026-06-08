package com.skillbox.fibonacci.application;

import com.skillbox.fibonacci.adapter.persistence.FibonacciRepository;
import com.skillbox.fibonacci.adapter.persistence.entity.FibonacciNumberEntity;
import com.skillbox.fibonacci.domain.model.FibonacciIndex;
import com.skillbox.fibonacci.domain.model.FibonacciNumber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

@Service
public class FibonacciService {

    private static final Logger log = LoggerFactory.getLogger(FibonacciService.class);

    private final FibonacciRepository repository;
    private final RecursiveFibonacciCalculator calculator;

    public FibonacciService(FibonacciRepository repository, RecursiveFibonacciCalculator calculator) {
        this.repository = repository;
        this.calculator = calculator;
    }

    public FibonacciNumber fibonacciNumber(FibonacciIndex index) {
        return repository.findByIndex(index.n())
                .map(FibonacciNumberEntity::toDomain)
                .orElseGet(() ->
                {
                    log.info("В БД не найден готовый расчет. Старт подсчета числа с индексом {}", index);

                    BigInteger value = calculator.getFibonacciNumber(index);
                    repository.save(new FibonacciNumberEntity(index.n(), value.longValue()));

                    log.info("Число для индекса {} = {} и записано в БД", index, value);
                    return new FibonacciNumber(index, value);
                });
    }

}
