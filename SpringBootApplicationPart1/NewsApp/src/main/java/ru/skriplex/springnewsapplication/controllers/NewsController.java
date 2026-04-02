package ru.skriplex.springnewsapplication.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skriplex.springnewsapplication.dtos.NewsDto;
import ru.skriplex.springnewsapplication.services.NewsService;

import java.util.Collection;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/news")
public class NewsController {

    private final NewsService service;

    @GetMapping("/{id}")
    public ResponseEntity<?> getNewsById(@PathVariable long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping("/category/{id}")
    public ResponseEntity<?> getAllNews(@PathVariable long id) {
        Collection<NewsDto> allNews = service.getAllNewsByCategoryId(id);
        return ResponseEntity.ok(allNews);
    }

    @PostMapping()
    public ResponseEntity<?> createNews(@RequestBody NewsDto newsDto) {
        NewsDto response = service.create(newsDto);
        return ResponseEntity.status(201).body(response);
    }

    @PutMapping
    public ResponseEntity<?> updateNewsWithCategory(@RequestBody NewsDto newsDto) {
        NewsDto response = service.update(newsDto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteNews(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }
}
