package com.example.moviereviews.repository;

import com.example.moviereviews.domain.Review;
import com.example.moviereviews.domain.Movie;
import com.example.moviereviews.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ReviewRepository extends JpaRepository<Review, UUID> {
    Page<Review> findByMovie(Movie movie, Pageable pageable);
    Page<Review> findByUser(User user, Pageable pageable);
}
