package com.skillbox.fibonacci.adapter.web;

import com.skillbox.fibonacci.adapter.persistence.FibonacciRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigInteger;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class FibonacciControllerTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16");


    @Autowired
    TestRestTemplate restTemplate;

    @Autowired
    FibonacciRepository repository;

    @LocalServerPort
    private int port;

    @Test
    @DisplayName("При запросе индекса > 1 -> 200.OK и JSON Тела")
    void whenIndexIsValid_thenReturnOkAndFibonacciNumber() {
        int index = 11;
        String url = "http://localhost:" + port + "/fibonacci/" + index;
        ResponseEntity<FibonacciResponse> response =
                restTemplate.getForEntity(url, FibonacciResponse.class);

        BigInteger value = BigInteger.valueOf(89);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().index()).isEqualTo(11);
        assertThat(response.getBody().value()).isEqualTo(value);

    }

    @Test
    @DisplayName("При запросе индекса < 1 -> 400 и текст ошибки")
    void sendIndexLessThanOneAndGet400AndErrorText() {
        int invalidIndex = -1;

        String url = "http://localhost:" + port + "/fibonacci/" + invalidIndex;
        ResponseEntity<String> response =
                restTemplate.getForEntity(url, String.class);


        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).contains("Число Фибоначчи не может иметь отрицательный или нулевой индекс");
        assertThat(response.getBody()).contains(String.valueOf(invalidIndex));
    }

    @Test
    @DisplayName("При рассчете числа -> возвращается и пишется в БД одно и тоже число")
    void whenCalculate47AndMore_returnsAndSaveSameNumber() {
        int overflow = 47;

        String url = "http://localhost:" + port + "/fibonacci/" + overflow;
        ResponseEntity<FibonacciResponse> response =
                restTemplate.getForEntity(url, FibonacciResponse.class);

        BigInteger responseValue = response.getBody().value();
        BigInteger databaseValue = BigInteger.valueOf(repository.findByIndex(overflow).get().getValue());

        assertThat(responseValue).isEqualTo(databaseValue);
    }

}