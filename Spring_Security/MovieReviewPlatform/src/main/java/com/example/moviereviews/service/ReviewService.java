package com.example.moviereviews.service;

import com.example.moviereviews.domain.Movie;
import com.example.moviereviews.domain.Review;
import com.example.moviereviews.domain.User;
import com.example.moviereviews.dto.ReviewDto;
import com.example.moviereviews.dto.requests.CreateReviewRequest;
import com.example.moviereviews.dto.requests.UpdateReviewRequest;
import com.example.moviereviews.exception.ForbiddenException;
import com.example.moviereviews.exception.NotFoundException;
import com.example.moviereviews.mapper.ReviewMapper;
import com.example.moviereviews.repository.MovieRepository;
import com.example.moviereviews.repository.ReviewRepository;
import com.example.moviereviews.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class ReviewService {

    private final ReviewRepository reviews;
    private final MovieRepository movies;
    private final UserRepository users;

    public ReviewService(ReviewRepository reviews, MovieRepository movies, UserRepository users) {
        this.reviews = reviews;
        this.movies = movies;
        this.users = users;
    }

    @Transactional(readOnly = true)
    public Page<ReviewDto> listByMovie(UUID movieId, Pageable pageable) {
        Movie m = movies.findById(movieId).orElseThrow(() -> new NotFoundException("Movie not found: " + movieId));
        return reviews.findByMovie(m, pageable).map(ReviewMapper::toDto);
    }

    @Transactional(readOnly = true)
    public ReviewDto get(UUID id) {
        Review r = reviews.findById(id).orElseThrow(() -> new NotFoundException("Review not found: " + id));
        return ReviewMapper.toDto(r);
    }

    @Transactional
    public ReviewDto create(UUID currentUserId, CreateReviewRequest req) {
        validate(req.getMovieId() != null, "movieId is required");
        validate(req.getRating() != null && req.getRating() >= 1 && req.getRating() <= 10, "rating must be 1..10");

        Movie movie = movies.findById(req.getMovieId())
                .orElseThrow(() -> new NotFoundException("Movie not found: " + req.getMovieId()));
        User user = users.findById(currentUserId)
                .orElseThrow(() -> new NotFoundException("User not found: " + currentUserId));

        Review r = new Review();
        r.setMovie(movie);
        r.setUser(user);
        r.setRating(req.getRating());
        r.setText(req.getText());
        return ReviewMapper.toDto(reviews.save(r));
    }

    @Transactional
    public ReviewDto update(UUID currentUserId, UUID reviewId, UpdateReviewRequest req) {
        Review r = reviews.findById(reviewId).orElseThrow(() -> new NotFoundException("Review not found: " + reviewId));
        if (!r.getUser().getId().equals(currentUserId)) {
            throw new ForbiddenException("You do not own this review");
        }
        if (req.getRating() != null) {
            validate(req.getRating() >= 1 && req.getRating() <= 10, "rating must be 1..10");
            r.setRating(req.getRating());
        }
        if (req.getText() != null) {
            r.setText(req.getText());
        }
        return ReviewMapper.toDto(reviews.save(r));
    }

    @Transactional
    public void delete(UUID currentUserId, UUID reviewId) {
        Review r = reviews.findById(reviewId).orElseThrow(() -> new NotFoundException("Review not found: " + reviewId));
        if (!r.getUser().getId().equals(currentUserId)) {
            throw new ForbiddenException("You do not own this review");
        }
        reviews.deleteById(reviewId);
    }

    private void validate(boolean condition, String message) {
        if (!condition) throw new IllegalArgumentException(message);
    }
}
