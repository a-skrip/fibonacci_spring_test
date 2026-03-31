package ru.skriplex.springnewsapplication.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skriplex.springnewsapplication.dtos.NewsDto;
import ru.skriplex.springnewsapplication.response.ErrorResponse;
import ru.skriplex.springnewsapplication.services.NewsService;

import java.util.Collection;
import java.util.NoSuchElementException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/news")
public class NewsController {

    private final NewsService service;

    @GetMapping("/{id}")
    public ResponseEntity<?> getNewsById(@PathVariable long id) {
        NewsDto newsDto;
        try {
            newsDto = service.getById(id);
        } catch (NoSuchElementException e) {
            ErrorResponse errorResponse = new ErrorResponse(
                    String.format("Новость с id: %d не найдена.", id));
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
        return ResponseEntity.ok(newsDto);
    }

    @GetMapping("/category/{id}")
    public ResponseEntity<?> getAllNews(@PathVariable long id) {
        Collection<NewsDto> allNews = service.getAllNewsByCategoryId(id);
        return ResponseEntity.ok(allNews);
    }

    @PostMapping()
    public ResponseEntity<?> createNews(@RequestBody NewsDto newsDto) {
        NewsDto response;
        try {
            response = service.create(newsDto);
        } catch (Exception e) {
            if (newsDto.getCategory() == null) {
                ErrorResponse errorResponse = new ErrorResponse(
                        "Не передана категория новостей");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
            }
            ErrorResponse errorResponse = new ErrorResponse(
                    String.format("Категория: %s не найдена.", newsDto.getTitle()));
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
        return ResponseEntity.status(201).body(response);
    }

    @PutMapping
    public ResponseEntity<?> updateNewsWithCategory(@RequestBody NewsDto newsDto) {
        NewsDto response;
        try {
            response = service.update(newsDto);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(e.getMessage()));
        }
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteNews(@PathVariable Long id) {
        try {
            service.delete(id);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(String.format("Новость с id %d не найдена", id)));

        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }
}
