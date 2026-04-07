package com.example.moviereviews.mapper;

import com.example.moviereviews.domain.Movie;
import com.example.moviereviews.dto.MovieDto;
import com.example.moviereviews.dto.requests.CreateMovieRequest;
import com.example.moviereviews.dto.requests.UpdateMovieRequest;

import java.util.Arrays;
import java.util.List;

public class MovieMapper {

    public static MovieDto toDto(Movie m) {
        List<String> genres = m.getGenres() == null ? List.of() : Arrays.asList(m.getGenres());
        return new MovieDto(m.getId(), m.getTitle(), m.getYear(), genres, m.getDescription());
    }

    public static Movie fromCreate(CreateMovieRequest req) {
        Movie m = new Movie();
        m.setTitle(req.getTitle());
        m.setYear(req.getYear() == null ? 0 : req.getYear());
        m.setGenres((req.getGenres() == null ? List.<String>of() : req.getGenres()).toArray(String[]::new));
        m.setDescription(req.getDescription());
        return m;
    }

    public static void applyUpdate(Movie m, UpdateMovieRequest req) {
        if (req.getTitle() != null) m.setTitle(req.getTitle());
        if (req.getYear() != null) m.setYear(req.getYear());
        if (req.getGenres() != null) m.setGenres(req.getGenres().toArray(String[]::new));
        if (req.getDescription() != null) m.setDescription(req.getDescription());
    }
}
