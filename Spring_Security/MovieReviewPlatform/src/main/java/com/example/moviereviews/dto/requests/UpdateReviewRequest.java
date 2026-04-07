package com.example.moviereviews.dto.requests;

public class UpdateReviewRequest {
    private Integer rating;
    private String text;

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
