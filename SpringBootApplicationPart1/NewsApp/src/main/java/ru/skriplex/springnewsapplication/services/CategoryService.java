package ru.skriplex.springnewsapplication.services;

import ru.skriplex.springnewsapplication.dtos.CategoryDto;

import java.util.Collection;
import java.util.List;

public class CategoryService implements CRUDServices<CategoryDto> {
    @Override
    public CategoryDto getById(Long id) {
        return null;
    }

    @Override
    public Collection<CategoryDto> getAll() {
        return List.of();
    }

    @Override
    public void create(CategoryDto item) {

    }

    @Override
    public void update(CategoryDto item) {

    }

    @Override
    public void delete(Long id) {

    }
}
