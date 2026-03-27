package ru.skriplex.springnewsapplication.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skriplex.springnewsapplication.dtos.CategoryDto;
import ru.skriplex.springnewsapplication.errors.ErrorResponse;
import ru.skriplex.springnewsapplication.services.CategoryService;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getCategoryById(@PathVariable long id) {
        CategoryDto response;
        try {
            response = categoryService.getById(id);
        } catch (RuntimeException e) {
            ErrorResponse errorResponse = new ErrorResponse(
                    String.format("Категория с id: %d не найдена.", id));
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<?> getAllCategories() {
        return ResponseEntity.ok(categoryService.getAll());
    }

    @PostMapping
    public ResponseEntity<?> createCategory(@RequestBody CategoryDto categoryDto) {
        CategoryDto response;
        try {
            response = categoryService.create(categoryDto);
        } catch (RuntimeException e) {
            ErrorResponse errorResponse = new ErrorResponse("Есть такая категория");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping()
    public ResponseEntity<?> updateCategory(@RequestBody CategoryDto categoryDto) {
        CategoryDto response;
        try {
           response = categoryService.update(categoryDto);
        } catch (RuntimeException e) {
            ErrorResponse errorResponse = new ErrorResponse(
                    String.format("Категория с id: %d не найдена.", categoryDto.getId()));
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable long id) {
        try {
            categoryService.delete(id);
        } catch (RuntimeException e) {
            ErrorResponse errorResponse = new ErrorResponse(
                    String.format("Категория с id: %d не найдена.", id));
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }
}