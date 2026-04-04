package ru.skriplex.springhotelapplication.service;

import java.util.Collection;

public interface CRUDServices<T> {
    T getById(Long id);

    Collection<T> getAll();

    T create(T item);

    T update(T item);

    void delete(Long id);

}