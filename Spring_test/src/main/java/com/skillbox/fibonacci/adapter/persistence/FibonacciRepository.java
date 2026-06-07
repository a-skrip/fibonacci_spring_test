package com.skillbox.fibonacci.adapter.persistence;

import com.skillbox.fibonacci.adapter.persistence.entity.FibonacciNumberEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FibonacciRepository extends CrudRepository<FibonacciNumberEntity, Integer> {

    Optional<FibonacciNumberEntity> findByIndex(int index);
}
