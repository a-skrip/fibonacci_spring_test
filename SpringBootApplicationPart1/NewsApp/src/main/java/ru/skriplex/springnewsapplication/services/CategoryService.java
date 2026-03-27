package ru.skriplex.springnewsapplication.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.skriplex.springnewsapplication.dtos.CategoryDto;
import ru.skriplex.springnewsapplication.entities.Category;
import ru.skriplex.springnewsapplication.mapper.CategoryMapper;
import ru.skriplex.springnewsapplication.repositories.CategoryRepository;

import java.util.Collection;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryService implements CRUDServices<CategoryDto> {

    //TODO исправить миграции на SQL

    private final CategoryRepository categoryRepository;

    @Override
    public CategoryDto getById(Long id) {
        log.info("Получить категорию по id: {}", id);
        return CategoryMapper.mapToDto(categoryRepository.findById(id).orElseThrow());
    }

    @Override
    public Collection<CategoryDto> getAll() {
        log.info("Получение всех Categories");
        return categoryRepository.findAll().stream()
                .map(CategoryMapper::mapToDto)
                .toList();
    }

    @Override
    public CategoryDto create(CategoryDto categoryDto) {
        Category save = categoryRepository.save(CategoryMapper.mapToEntity(categoryDto));
        log.info("create Category: {}", categoryDto.getTitle());
        return CategoryMapper.mapToDto(save);
    }

    @Override
    public CategoryDto update(CategoryDto categoryDto) {
        Category category = categoryRepository.findById(categoryDto.getId()).orElseThrow();
        log.info("update category: {} -> {}", category.getTitle(), categoryDto.getTitle());
        category.setTitle(categoryDto.getTitle());
        return CategoryMapper.mapToDto(categoryRepository.save(category));
    }

    @Override
    public void delete(Long id) {
        Category category = categoryRepository.findById(id).orElseThrow();
        categoryRepository.delete(category);
        log.info("delete category by id: {}", id);

    }
}
