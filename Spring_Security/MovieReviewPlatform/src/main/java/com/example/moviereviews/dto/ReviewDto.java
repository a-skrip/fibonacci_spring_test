package com.example.moviereviews.dto;

import java.time.OffsetDateTime;
import java.util.UUID;

public class ReviewDto {
    private UUID id;
    private UUID movieId;
    private UUID userId;
    private int rating;
    private String text;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;

    public ReviewDto() {}

    public ReviewDto(UUID id, UUID movieId, UUID userId, int rating, String text, OffsetDateTime createdAt, OffsetDateTime updatedAt) {
        this.id = id;
        this.movieId = movieId;
        this.userId = userId;
        this.rating = rating;
        this.text = text;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public UUID getMovieId() { return movieId; }
    public void setMovieId(UUID movieId) { this.movieId = movieId; }
    public UUID getUserId() { return userId; }
    public void setUserId(UUID userId) { this.userId = userId; }
    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }
    public String getText() { return text; }
    public void setText(String text) { this.text = text; }
    public OffsetDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }
    public OffsetDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(OffsetDateTime updatedAt) { this.updatedAt = updatedAt; }
}
