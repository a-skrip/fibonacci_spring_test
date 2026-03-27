package ru.skriplex.springnewsapplication.mapper;

import lombok.RequiredArgsConstructor;
import ru.skriplex.springnewsapplication.dtos.NewsDto;
import ru.skriplex.springnewsapplication.entities.Category;
import ru.skriplex.springnewsapplication.entities.News;

@RequiredArgsConstructor
public class NewsMapper {


    public static NewsDto mapToDto(News newsEntity) {
        NewsDto newsDto = new NewsDto();
        newsDto.setId(newsEntity.getId());
        newsDto.setTitle(newsEntity.getTitle());
        newsDto.setText(newsEntity.getText());
        newsDto.setDate(newsEntity.getDate());
        newsDto.setCategory(newsEntity.getCategory().getTitle());
        return newsDto;
    }

    public static News mapToEntity(NewsDto newsDto) {
        News newsEntity = new News();
        newsEntity.setId(newsDto.getId());
        newsEntity.setTitle(newsDto.getTitle());
        newsEntity.setText(newsDto.getText());
        newsEntity.setDate(newsDto.getDate());

        return newsEntity;
    }
}
