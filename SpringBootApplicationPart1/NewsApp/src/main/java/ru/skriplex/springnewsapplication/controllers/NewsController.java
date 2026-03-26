package ru.skriplex.springnewsapplication.controllers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skriplex.springnewsapplication.errors.ErrorResponse;
import ru.skriplex.springnewsapplication.dtos.NewsDto;
import ru.skriplex.springnewsapplication.services.NewsService;

import java.util.Collection;

@RestController
@RequestMapping("/api/news")
public class NewsController {

    private static final Logger log = LogManager.getLogger(NewsController.class);
    private final NewsService service;

    public NewsController(NewsService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getNewsById(@PathVariable long id) {
        NewsDto news = service.getById(id);
        if (news == null) {
            ErrorResponse errorResponse = new ErrorResponse(
                    String.format("Новость с ID %d не найдена.", id)
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
        return ResponseEntity.ok(news);
    }

    @GetMapping
    public ResponseEntity<?> getAllNews() {
        Collection<NewsDto> allNews = service.getAll();
        return ResponseEntity.ok(allNews);
    }

    @PostMapping
    public ResponseEntity<?> createNews(@RequestBody NewsDto item) {
        service.create(item);
        return ResponseEntity.status(201).body(item);
    }

    @PutMapping
    public ResponseEntity<?> updateNews(@RequestBody NewsDto item) {
        try {
            service.update(item);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(String.format("Новость с id %d не найдена", item.getId())));
        }
        return ResponseEntity.ok(item);
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
