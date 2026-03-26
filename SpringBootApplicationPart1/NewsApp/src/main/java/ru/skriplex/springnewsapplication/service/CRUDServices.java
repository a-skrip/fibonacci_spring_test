package ru.skriplex.springnewsapplication.service;

import java.util.Collection;

public interface CRUDServices<T> {
    T getById(Long id);

    Collection<T> getAll();

    void create(T item);

    void update(T item);

    void delete(Long id);

}