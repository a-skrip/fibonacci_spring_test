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
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class NewsService implements CRUDServices<NewsDto> {

    //TODO исправить миграции на SQL

    private final NewsRepository newsRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public NewsDto getById(Long id) {
//        if (id == null) {
//            throw new RuntimeException("Не передан id");
//        }
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
    public void create(NewsDto newsDto) {

//        long nextId = storage.isEmpty() ? 1 : storage.keySet().stream()
//                .max(Long::compareTo).get() + 1;
//        item.setId(nextId);
//        item.setDate(Instant.now());
        News news = NewsMapper.mapToEntity(newsDto);
        Long categoryId = newsDto.getCategoryId();
        Category category = categoryRepository.findById(categoryId).orElseThrow();
        news.setCategory(category);
        newsRepository.save(news);
        log.info("Вызов метода creat");
    }

    @Override
    public void update(NewsDto newsDto) {
//        Long newsId = item.getId();
//        if (!storage.containsKey(newsId)) {
//            throw new RuntimeException();
//        }
//        item.setDate(Instant.now());
//        storage.put(newsId, item);
        News news = NewsMapper.mapToEntity(newsDto);
        Long categoryId = newsDto.getCategoryId();
        Category category = categoryRepository.findById(categoryId).orElseThrow();
        news.setCategory(category);
        newsRepository.save(news);
        log.info("Вызов метода create");
    }

    @Override
    public void delete(Long id) {
        log.info("Вызов метода delete для id = {}", id);
        newsRepository.findById(id).orElseThrow();
        newsRepository.deleteById(id);
    }
}
