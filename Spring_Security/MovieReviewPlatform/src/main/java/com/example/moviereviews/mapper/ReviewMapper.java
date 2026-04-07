package com.example.moviereviews.mapper;

import com.example.moviereviews.domain.Review;
import com.example.moviereviews.dto.ReviewDto;

public class ReviewMapper {
    public static ReviewDto toDto(Review r) {
        return new ReviewDto(
                r.getId(),
                r.getMovie().getId(),
                r.getUser().getId(),
                r.getRating(),
                r.getText(),
                r.getCreatedAt(),
                r.getUpdatedAt()
        );
    }
}
