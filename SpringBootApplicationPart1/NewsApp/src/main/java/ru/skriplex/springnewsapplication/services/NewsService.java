package ru.skriplex.springnewsapplication.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.skriplex.springnewsapplication.dtos.NewsDto;
import ru.skriplex.springnewsapplication.entities.Category;
import ru.skriplex.springnewsapplication.entities.News;
import ru.skriplex.springnewsapplication.exception.CategoryNotFoundException;
import ru.skriplex.springnewsapplication.exception.NewsNotFoundException;
import ru.skriplex.springnewsapplication.exception.NoTransmittedCategoryException;
import ru.skriplex.springnewsapplication.mapper.NewsMapper;
import ru.skriplex.springnewsapplication.repositories.CategoryRepository;
import ru.skriplex.springnewsapplication.repositories.NewsRepository;

import java.time.Instant;
import java.util.Collection;

@Slf4j
@Service
@RequiredArgsConstructor
public class NewsService implements CRUDServices<NewsDto> {

    private final NewsRepository newsRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public NewsDto getById(Long id) {
        log.info("Вызов метода getById({}) ", id);
        News news = newsRepository.findById(id)
                .orElseThrow(() -> new NewsNotFoundException("Новость с id:" + id + " не найдена"));
        return NewsMapper.mapToDto(news);
    }

    @Override
    public Collection<NewsDto> getAll() {
        log.info("Вызов метода getAll()");
        return newsRepository.findAll().stream()
                .map(NewsMapper::mapToDto)
                .toList();
    }

    public Collection<NewsDto> getAllNewsByCategoryId(long id) {
        Collection<News> allByCategoryId = newsRepository.getAllByCategoryId(id);
        return allByCategoryId.stream()
                .map(NewsMapper::mapToDto)
                .toList();
    }

    @Override
    public NewsDto create(NewsDto newsDto) {
        News news = NewsMapper.mapToEntity(newsDto);
        String nameCategory = newsDto.getCategory();
        if (nameCategory.isEmpty()) {
            throw new NoTransmittedCategoryException("Не передана категория новостей");
        }
        Category category = categoryRepository.findByTitle(nameCategory)
                .orElseThrow(() -> new CategoryNotFoundException("Категория:" + nameCategory + " не найдена"));
        news.setDate(Instant.now());
        news.setCategory(category);
        News savedNews = newsRepository.save(news);
        log.info("Вызов метода createNews");
        return NewsMapper.mapToDto(savedNews);
    }

    @Override
    public NewsDto update(NewsDto newsDto) {
        News newsToUpdate;
        newsToUpdate = newsRepository.findById(newsDto.getId())
                .orElseThrow(() -> new NewsNotFoundException("Новость с id:" + newsDto.getId() + " не найдена"));
        Category category = categoryRepository.findByTitle(newsDto.getCategory())
                .orElseThrow(() -> new CategoryNotFoundException("Категория:\"" + newsDto.getCategory() + "\" не найдена."));

        log.info("update newsId:{}, category:{} -> {}",
                newsToUpdate.getId(), newsToUpdate.getCategory().getTitle(), newsDto.getCategory());

        newsToUpdate.setCategory(category);
        newsToUpdate.setTitle(newsDto.getTitle());
        newsToUpdate.setText(newsDto.getText());
        return NewsMapper.mapToDto(newsRepository.save(newsToUpdate));
    }

    @Override
    public void delete(Long id) {
        log.info("Вызов метода delete для id = {}", id);
        newsRepository.findById(id)
                .orElseThrow(() -> new NewsNotFoundException("Новость с id:" + id + " не найдена"));
        newsRepository.deleteById(id);
    }
}
