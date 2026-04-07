package com.example.moviereviews.controller;

import com.example.moviereviews.dto.MovieDto;
import com.example.moviereviews.dto.requests.CreateMovieRequest;
import com.example.moviereviews.dto.requests.UpdateMovieRequest;
import com.example.moviereviews.service.MovieService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/movies")
@Tag(name = "Movies")
public class MovieController {

    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping
    @Operation(summary = "List movies (paged)")
    public Page<MovieDto> list(Pageable pageable) {
        return movieService.list(pageable);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a movie by id")
    public MovieDto get(@PathVariable UUID id) {
        return movieService.get(id);
    }

    @PostMapping
    @Operation(summary = "Create a movie")
    public ResponseEntity<MovieDto> create(@RequestBody CreateMovieRequest req) {
        MovieDto dto = movieService.create(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a movie")
    public MovieDto update(@PathVariable UUID id, @RequestBody UpdateMovieRequest req) {
        return movieService.update(id, req);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a movie")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        movieService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
