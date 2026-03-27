package ru.skriplex.springnewsapplication.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.skriplex.springnewsapplication.dtos.NewsDto;
import ru.skriplex.springnewsapplication.entities.Category;
import ru.skriplex.springnewsapplication.entities.News;
import ru.skriplex.springnewsapplication.mapper.NewsMapper;
import ru.skriplex.springnewsapplication.repositories.CategoryRepository;
import ru.skriplex.springnewsapplication.repositories.NewsRepository;

import java.time.Instant;
import java.util.Collection;

@Slf4j
@Service
@RequiredArgsConstructor
public class NewsService implements CRUDServices<NewsDto> {

    //TODO исправить миграции на SQL

    private final NewsRepository newsRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public NewsDto getById(Long id) {
        log.info("Вызов метода getById({}) ", id);
        News news = newsRepository.findById(id).orElseThrow();
        return NewsMapper.mapToDto(news);
    }

    @Override
    public Collection<NewsDto> getAll() {
        log.info("Вызов метода getAll()");
        return newsRepository.findAll().stream()
                .map(NewsMapper::mapToDto)
                .toList();
    }

    @Override
    public NewsDto create(NewsDto newsDto) {
        News news = NewsMapper.mapToEntity(newsDto);
        Long categoryId = newsDto.getCategoryId();
        Category category = categoryRepository.findById(categoryId).orElseThrow();
        news.setCategory(category);
        news.setDate(Instant.now());
        log.info("Вызов метода creat");
        return NewsMapper.mapToDto(newsRepository.save(news));
    }

    @Override
    public NewsDto update(NewsDto newsDto) {
        News news = newsRepository.findById(newsDto.getId()).orElseThrow();
        news.setTitle(newsDto.getTitle());
        news.setText(newsDto.getText());
        log.info("Вызов метода create");
        return NewsMapper.mapToDto(newsRepository.save(news));
    }

    @Override
    public void delete(Long id) {
        log.info("Вызов метода delete для id = {}", id);
        newsRepository.findById(id).orElseThrow();
        newsRepository.deleteById(id);
    }
}
