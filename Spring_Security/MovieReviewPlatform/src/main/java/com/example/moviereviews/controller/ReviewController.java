package com.example.moviereviews.controller;

import com.example.moviereviews.domain.User;
import com.example.moviereviews.dto.ReviewDto;
import com.example.moviereviews.dto.requests.CreateReviewRequest;
import com.example.moviereviews.dto.requests.UpdateReviewRequest;
import com.example.moviereviews.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping
@Tag(name = "Reviews")
public class ReviewController {

    private final ReviewService reviewService;
//    private final CurrentUserProvider currentUser;

    public ReviewController(ReviewService reviewService/*, CurrentUserProvider currentUser*/) {
        this.reviewService = reviewService;
//        this.currentUser = currentUser;
    }

    @GetMapping("/api/movies/{movieId}/reviews")
    @Operation(summary = "List reviews for a movie (paged)")
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    public Page<ReviewDto> listByMovie(@PathVariable UUID movieId, Pageable pageable) {
        return reviewService.listByMovie(movieId, pageable);
    }

    @GetMapping("/api/reviews/{id}")
    @Operation(summary = "Get a review by id")
    public ReviewDto get(@PathVariable UUID id) {
        return reviewService.get(id);
    }

    @PostMapping("/api/reviews")
    @Operation(summary = "Create a review",
            description = "Demo ownership uses the X-User-Id header (UUID). When omitted, defaults to Alice.")
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    public ResponseEntity<ReviewDto> create(@AuthenticationPrincipal User currentUser,
                                            @Parameter(description = "Optional current user id (temporary demo). Will be replaced by Security.")
//            @RequestHeader(value = "X-User-Id", required = false) String userIdHeader,
                                            @RequestBody CreateReviewRequest req) {
        UUID userId = currentUser.getId();
        ReviewDto dto = reviewService.create(userId, req);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @PutMapping("/api/reviews/{id}")
    @Operation(summary = "Update a review (owner only)")
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    public ReviewDto update(@AuthenticationPrincipal User currentUser,
                            @PathVariable UUID id,
                            @RequestBody UpdateReviewRequest req) {
        UUID userId = currentUser.getId();
        return reviewService.update(userId, id, req);
    }

    @DeleteMapping("/api/reviews/{id}")
    @Operation(summary = "Delete a review (owner only)")
    public ResponseEntity<Void> delete(@AuthenticationPrincipal User currentUser,
                                       @PathVariable UUID id) {
        UUID userId = currentUser.getId();
        reviewService.delete(userId, id);
        return ResponseEntity.noContent().build();
    }
}
