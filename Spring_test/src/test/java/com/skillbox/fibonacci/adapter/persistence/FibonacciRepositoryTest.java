package com.skillbox.fibonacci.adapter.persistence;

import com.skillbox.fibonacci.adapter.persistence.entity.FibonacciNumberEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers // Активируем Testcontainers для этого класса
@DataJpaTest // чтобы не поднимать весь контекст
class FibonacciRepositoryTest {

    // Объявляем контейнер PostgreSQL, static - для запуска один раз на класс, а не на метод
    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16");

    @Autowired
    private FibonacciRepository repository;

    @Autowired
    private TestEntityManager entityManager;


    @Test
    @DisplayName("Новые числа Фибоначчи сохраняются в БД")
    void whenSaveNewNumber_thenSavedToDatabase() {
        // Arrange
        FibonacciNumberEntity entity = new FibonacciNumberEntity(8, 21);
        FibonacciNumberEntity entity2 = new FibonacciNumberEntity(10, 55);

        // Act
        FibonacciNumberEntity saved = repository.save(entity);
        FibonacciNumberEntity saved2 = repository.save(entity2);

        entityManager.flush(); // Принудительно сохраняем изменения в БД
        entityManager.detach(saved); // Отвязываем объект от контекста Hibernate
        entityManager.detach(saved2); // Отвязываем объект от контекста Hibernate

        // Assert
        FibonacciNumberEntity found = repository.findById(saved.getId()).orElse(null);
        FibonacciNumberEntity found2 = repository.findById(saved2.getId()).orElse(null);
        Iterable<FibonacciNumberEntity> entities = repository.findAll();

        assertThat(found).isNotNull();
        assertThat(found.getIndex()).isEqualTo(8);
        assertThat(found.getValue()).isEqualTo(21);

        assertThat(found2).isNotNull();
        assertThat(found2.getIndex()).isEqualTo(10);
        assertThat(found2.getValue()).isEqualTo(55);

        assertThat(entities).hasSize(2);
    }

    @Test
    @DisplayName("При поиске по порядковому номеру возвращается соответствующее число из БД")
    void whenFindByIndex_thenReturnNumberFromDatabase() {
        // Arrange
        FibonacciNumberEntity entity = new FibonacciNumberEntity(10, 55);
        FibonacciNumberEntity saved = repository.save(entity);
        entityManager.flush();
        entityManager.detach(saved);

        // Act
        var found = repository.findByIndex(10);

        // Assert
        assertThat(found).isPresent();
        assertThat(found.get().getIndex()).isEqualTo(10);
        assertThat(found.get().getValue()).isEqualTo(55);
    }

    @Test
    @DisplayName("При повторной вставке одного и того же числа не возникает исключений и дубликатов")
    void whenSaveSameIndexMultipleTimes_thenNoExceptionsAndNoDuplicates() {
        // Arrange
        FibonacciNumberEntity first = new FibonacciNumberEntity(16, 987);
        FibonacciNumberEntity second = new FibonacciNumberEntity(16, 1987);

        // Act - первая вставка
        FibonacciNumberEntity firstSave = repository.save(first);
        entityManager.flush();
        Integer savedId = firstSave.getId();
        entityManager.detach(first);

        // Act - вторая вставка
        FibonacciNumberEntity secondSave = repository.save(second);
        entityManager.detach(second);

        //Assert
        var found = repository.findByIndex(16);
        assertThat(repository.findAll()).hasSize(1);
        assertThat(found).isPresent();
        assertThat(found.get().getValue()).isEqualTo(1987);
        assertThat(found.get().getId()).isEqualTo(savedId);

    }
}