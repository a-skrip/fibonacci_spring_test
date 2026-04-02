package ru.skriplex.springnewsapplication.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Named;
import ru.skriplex.springnewsapplication.dtos.CategoryDto;
import ru.skriplex.springnewsapplication.entities.Category;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    CategoryDto toDto(Category category);

    Category toEntity(CategoryDto categoryDto);

    @Named("categoryToTitle")
    static String categoryToTitle(Category category) {
        return category != null ? category.getTitle() : null;
    }

    @Named("titleToCategory")
    static Category titleToCategory(String title) {
        if (title == null) {
            return null;
        }
        Category category = new Category();
        category.setTitle(title);
        return category;
    }
}