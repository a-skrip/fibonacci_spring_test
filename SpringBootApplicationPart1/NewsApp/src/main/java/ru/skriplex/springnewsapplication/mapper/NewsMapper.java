package ru.skriplex.springnewsapplication.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.skriplex.springnewsapplication.dtos.NewsDto;
import ru.skriplex.springnewsapplication.entities.News;

@Mapper(componentModel = "spring", uses = CategoryMapper.class)
public interface NewsMapper {

    @Mapping(source = "category", target = "category", qualifiedByName = "categoryToTitle")
    @Mapping(source = "text", target = "text")
    NewsDto toDto(News news);

    @Mapping(source = "category", target = "category", qualifiedByName = "titleToCategory")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "date", ignore = true)
    News toEntity(NewsDto newsDto);
}
