package ru.skriplex.springnewsapplication.mapper;

import ru.skriplex.springnewsapplication.dtos.NewsDto;
import ru.skriplex.springnewsapplication.entities.News;

public class NewsMapper {
    public static NewsDto mapToDto(News newsEntity) {
        NewsDto newsDto = new NewsDto();
        newsDto.setId(newsEntity.getId());
        newsDto.setTitle(newsEntity.getTitle());
        newsDto.setText(newsEntity.getText());
        newsDto.setDate(newsEntity.getDate());
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
