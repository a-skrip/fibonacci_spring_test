package com.example.moviereviews.controller;

import com.example.moviereviews.domain.User;
import com.example.moviereviews.dto.ReviewDto;
import com.example.moviereviews.dto.UserDto;
import com.example.moviereviews.dto.requests.CreateReviewRequest;
import com.example.moviereviews.dto.requests.UpdateReviewRequest;
import com.example.moviereviews.enums.Role;
import com.example.moviereviews.service.ReviewService;
import com.example.moviereviews.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping
@Tag(name = "Reviews")
public class ReviewController {

    private final ReviewService reviewService;
    private final UserService userService;

    public ReviewController(ReviewService reviewService, UserService userService) {
        this.reviewService = reviewService;
        this.userService = userService;
    }

    @GetMapping("/api/movies/{movieId}/reviews")
    @Operation(summary = "List reviews for a movie (paged)")
    @PreAuthorize("hasRole('ROLE_USER')")
    public Page<ReviewDto> listByMovie(@PathVariable UUID movieId, Pageable pageable) {
        return reviewService.listByMovie(movieId, pageable);
    }

    @GetMapping("/api/reviews/{reviewId}")
    @Operation(summary = "Get a review by id")
    public ReviewDto get(@PathVariable UUID reviewId) {
        return reviewService.get(reviewId);
    }

    @PostMapping("/api/reviews")
    @Operation(summary = "Create a review")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<ReviewDto> create(@AuthenticationPrincipal UserDetails currentUser,
                                            @Parameter(description = "Optional current user id (temporary demo). Will be replaced by Security.")
                                            @RequestBody CreateReviewRequest req) {
        String username = currentUser.getUsername();
        UUID userId = userService.getByName(username).getId();
        ReviewDto dto = reviewService.create(userId, req);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @PutMapping("/api/reviews/{reviewId}")
    @Operation(summary = "Update a review (owner only)")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ReviewDto update(@AuthenticationPrincipal UserDetails currentUser,
                            @PathVariable UUID reviewId,
                            @RequestBody UpdateReviewRequest req) {
        UUID userId = userService.getByName(currentUser.getUsername()).getId();
        return reviewService.update(userId, reviewId, req);
    }

    @DeleteMapping("/api/reviews/{reviewId}")
    @Operation(summary = "Delete a review (owner only)")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<Void> delete(@AuthenticationPrincipal UserDetails currentUser,
                                       @PathVariable UUID reviewId) {
        UserDto userDto = userService.getByName(currentUser.getUsername());

        if (userDto.getRole().equals(Role.ROLE_ADMIN)) {
            reviewService.deleteAsAdmin(reviewId);
        } else {
            reviewService.delete(userDto.getId(), reviewId);
        }

        return ResponseEntity.noContent().build();
    }


}
