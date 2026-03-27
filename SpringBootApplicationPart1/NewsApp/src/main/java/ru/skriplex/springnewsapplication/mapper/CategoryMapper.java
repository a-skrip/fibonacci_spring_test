package ru.skriplex.springnewsapplication.mapper;

import ru.skriplex.springnewsapplication.dtos.CategoryDto;
import ru.skriplex.springnewsapplication.entities.Category;

public class CategoryMapper {

    public static CategoryDto mapToDto(Category categoryEntity) {
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(categoryEntity.getId());
        categoryDto.setTitle(categoryEntity.getTitle());
        return categoryDto;
    }

    public static Category mapToEntity(CategoryDto categoryDto) {
        Category categoryEntity = new Category();
        categoryEntity.setId(categoryDto.getId());
        categoryEntity.setTitle(categoryDto.getTitle());

        return categoryEntity;
    }
}
