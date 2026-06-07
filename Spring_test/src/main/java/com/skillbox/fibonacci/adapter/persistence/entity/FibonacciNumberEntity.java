package com.skillbox.fibonacci.adapter.persistence.entity;

import com.skillbox.fibonacci.domain.model.FibonacciIndex;
import com.skillbox.fibonacci.domain.model.FibonacciNumber;
import jakarta.persistence.*;
import org.hibernate.annotations.SQLInsert;

import java.math.BigInteger;
import java.util.Objects;


@Entity
@Table(name = "fibonacci_number", uniqueConstraints = {@UniqueConstraint(columnNames = {"index"})})
@SQLInsert(sql = "INSERT INTO fibonacci_number(index, value) VALUES (?, ?) ON CONFLICT(index) DO UPDATE SET value = EXCLUDED.value RETURNING id")
public class FibonacciNumberEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private int index;

    private long value;

    // For Hibernate
    public FibonacciNumberEntity() {

    }

    @Override
    public String toString() {
        return "FibonacciNumberEntity{" +
                "id=" + id +
                ", index=" + index +
                ", value=" + value +
                '}';
    }

    public FibonacciNumberEntity(int index, long value) {
        this();
        this.index = index;
        this.value = value;
    }

    public FibonacciNumber toDomain() {
        return new FibonacciNumber(new FibonacciIndex(index), BigInteger.valueOf(value));
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public long getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        FibonacciNumberEntity that = (FibonacciNumberEntity) o;
        return index == that.index && value == that.value && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, index, value);
    }
}
