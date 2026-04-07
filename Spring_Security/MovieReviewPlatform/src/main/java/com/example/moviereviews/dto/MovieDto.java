package com.example.moviereviews.dto;

import java.util.List;
import java.util.UUID;

public class MovieDto {
    private UUID id;
    private String title;
    private int year;
    private List<String> genres;
    private String description;

    public MovieDto() {}

    public MovieDto(UUID id, String title, int year, List<String> genres, String description) {
        this.id = id;
        this.title = title;
        this.year = year;
        this.genres = genres;
        this.description = description;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }
    public List<String> getGenres() { return genres; }
    public void setGenres(List<String> genres) { this.genres = genres; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
