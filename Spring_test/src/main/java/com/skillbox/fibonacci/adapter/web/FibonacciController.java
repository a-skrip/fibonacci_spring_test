package com.skillbox.fibonacci.adapter.web;

import com.skillbox.fibonacci.application.FibonacciService;
import com.skillbox.fibonacci.domain.model.FibonacciIndex;
import com.skillbox.fibonacci.domain.model.FibonacciNumber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FibonacciController {

    private static final Logger log = LoggerFactory.getLogger(FibonacciController.class);

    private final FibonacciService service;

    public FibonacciController(FibonacciService service) {
        this.service = service;
    }

    @GetMapping("/fibonacci/{index}")
    public ResponseEntity<FibonacciResponse> getNumber(@PathVariable("index") int n) {
        log.info("Запрос на подсчет числа Фибоначи с индексом {}", n);
        FibonacciIndex index = new FibonacciIndex(n);
        FibonacciNumber number = service.fibonacciNumber(index);
        return ResponseEntity.ok(FibonacciResponse.from(number));
    }

}
