package com.example.moviereviews.service;

import com.example.moviereviews.domain.Movie;
import com.example.moviereviews.dto.MovieDto;
import com.example.moviereviews.dto.requests.CreateMovieRequest;
import com.example.moviereviews.dto.requests.UpdateMovieRequest;
import com.example.moviereviews.exception.NotFoundException;
import com.example.moviereviews.mapper.MovieMapper;
import com.example.moviereviews.repository.MovieRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class MovieService {

    private final MovieRepository movies;

    public MovieService(MovieRepository movies) {
        this.movies = movies;
    }

    @Transactional(readOnly = true)
    public Page<MovieDto> list(Pageable pageable) {
        return movies.findAll(pageable)
                .map(MovieMapper::toDto);
    }

    @Transactional(readOnly = true)
    public MovieDto get(UUID id) {
        Movie m = movies.findById(id).orElseThrow(() -> new NotFoundException("Movie not found: " + id));
        return MovieMapper.toDto(m);
    }

    @Transactional
    public MovieDto create(CreateMovieRequest req) {
        validate(req.getTitle() != null && !req.getTitle().isBlank(), "title is required");
        validate(req.getYear() != null && req.getYear() > 1870 && req.getYear() < 3000, "year is invalid");
        Movie m = MovieMapper.fromCreate(req);
        return MovieMapper.toDto(movies.save(m));
    }

    @Transactional
    public MovieDto update(UUID id, UpdateMovieRequest req) {
        Movie m = movies.findById(id).orElseThrow(() -> new NotFoundException("Movie not found: " + id));
        MovieMapper.applyUpdate(m, req);
        return MovieMapper.toDto(movies.save(m));
    }

    @Transactional
    public void delete(UUID id) {
        if (!movies.existsById(id)) throw new NotFoundException("Movie not found: " + id);
        movies.deleteById(id);
    }

    private void validate(boolean condition, String message) {
        if (!condition) throw new IllegalArgumentException(message);
    }
}
