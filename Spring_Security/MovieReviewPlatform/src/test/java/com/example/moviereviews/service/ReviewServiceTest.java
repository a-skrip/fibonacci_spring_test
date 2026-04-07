package com.example.moviereviews.service;

import com.example.moviereviews.domain.Movie;
import com.example.moviereviews.domain.Review;
import com.example.moviereviews.domain.User;
import com.example.moviereviews.dto.ReviewDto;
import com.example.moviereviews.dto.requests.CreateReviewRequest;
import com.example.moviereviews.dto.requests.UpdateReviewRequest;
import com.example.moviereviews.exception.ForbiddenException;
import com.example.moviereviews.exception.NotFoundException;
import com.example.moviereviews.repository.MovieRepository;
import com.example.moviereviews.repository.ReviewRepository;
import com.example.moviereviews.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ReviewServiceTest {

    private ReviewRepository reviewRepository;
    private MovieRepository movieRepository;
    private UserRepository userRepository;
    private ReviewService service;

    private final UUID movieId = UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa");
    private final UUID userId = UUID.fromString("11111111-1111-1111-1111-111111111111");

    @BeforeEach
    void setUp() {
        reviewRepository = mock(ReviewRepository.class);
        movieRepository = mock(MovieRepository.class);
        userRepository = mock(UserRepository.class);
        service = new ReviewService(reviewRepository, movieRepository, userRepository);
    }

    @Test
    void create_valid_persists_and_returns_dto() {
        Movie movie = new Movie();
        movie.setId(movieId);
        when(movieRepository.findById(movieId)).thenReturn(Optional.of(movie));

        User user = new User();
        user.setId(userId);
        user.setUsername("alice");
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        when(reviewRepository.save(any(Review.class))).thenAnswer(inv -> {
            Review r = inv.getArgument(0);
            r.setId(UUID.fromString("dddddddd-dddd-dddd-dddd-dddddddddddd"));
            r.setCreatedAt(OffsetDateTime.now());
            return r;
        });

        CreateReviewRequest req = new CreateReviewRequest();
        req.setMovieId(movieId);
        req.setRating(9);
        req.setText("Great!");

        ReviewDto dto = service.create(userId, req);

        assertThat(dto.getId()).isEqualTo(UUID.fromString("dddddddd-dddd-dddd-dddd-dddddddddddd"));
        assertThat(dto.getMovieId()).isEqualTo(movieId);
        assertThat(dto.getUserId()).isEqualTo(userId);
        assertThat(dto.getRating()).isEqualTo(9);
        verify(reviewRepository).save(any(Review.class));
    }

    @Test
    void update_notOwner_forbidden() {
        UUID reviewId = UUID.fromString("99999999-9999-9999-9999-999999999999");

        User owner = new User();
        owner.setId(UUID.fromString("22222222-2222-2222-2222-222222222222"));
        Review r = new Review();
        r.setId(reviewId);
        r.setUser(owner);
        when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(r));

        UpdateReviewRequest req = new UpdateReviewRequest();
        req.setText("nope");

        assertThatThrownBy(() -> service.update(userId, reviewId, req))
                .isInstanceOf(ForbiddenException.class);
        verify(reviewRepository, never()).save(any());
    }

    @Test
    void delete_owner_allowed() {
        UUID reviewId = UUID.fromString("88888888-8888-8888-8888-888888888888");

        User owner = new User();
        owner.setId(userId);
        Review r = new Review();
        r.setId(reviewId);
        r.setUser(owner);
        when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(r));

        service.delete(userId, reviewId);

        verify(reviewRepository).deleteById(reviewId);
    }

    @Test
    void create_invalidMovie_notFound() {
        when(movieRepository.findById(movieId)).thenReturn(Optional.empty());

        CreateReviewRequest req = new CreateReviewRequest();
        req.setMovieId(movieId);
        req.setRating(5);

        assertThatThrownBy(() -> service.create(userId, req))
                .isInstanceOf(NotFoundException.class);
    }
}
