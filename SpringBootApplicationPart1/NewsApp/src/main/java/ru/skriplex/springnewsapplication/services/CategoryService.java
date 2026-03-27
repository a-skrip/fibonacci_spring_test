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
    public void create(CategoryDto categoryDto) {
        Category category = CategoryMapper.mapToEntity(categoryDto);
        categoryRepository.save(category);
        log.info("create Category: {}", categoryDto.getTitle());

    }

    @Override
    public void update(CategoryDto categoryDto) {
        Category category = categoryRepository.findById(categoryDto.getId()).orElseThrow();
        category.setTitle(categoryDto.getTitle());
        categoryRepository.save(category);
        log.info("update category: {} -> {}", category.getTitle(), categoryDto.getTitle());
    }

    @Override
    public void delete(Long id) {
        Category category = categoryRepository.findById(id).orElseThrow();
        categoryRepository.delete(category);
        log.info("delete category by id: {}", id);

    }
}
