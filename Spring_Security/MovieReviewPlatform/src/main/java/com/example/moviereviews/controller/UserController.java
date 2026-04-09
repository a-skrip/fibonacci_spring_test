package com.example.moviereviews.controller;

import com.example.moviereviews.domain.User;
import com.example.moviereviews.dto.UserDto;
import com.example.moviereviews.dto.requests.RegisterUserRequest;
import com.example.moviereviews.dto.requests.UpdateUserPasswordRequest;
import com.example.moviereviews.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@Tag(name = "Users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    @Operation(summary = "Register a user (no auth yet)")
    public ResponseEntity<?> register(@RequestBody RegisterUserRequest req) {
        UserDto register = userService.register(req);
        return ResponseEntity
                .created(URI.create("/api/users/" + register.getId()))
                .body(Map.of(
                        "id", register.getId(),
                        "username", register.getUsername(),
                        "displayName", register.getDisplayName(),
                        "createdAt", register.getCreatedAt()
                ));
    }

    @GetMapping("/me")
    @Operation(summary = "Get current user info")
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
    public ResponseEntity<?> getCurrentUser(@AuthenticationPrincipal UserDetails currentUser) {
        UserDto userDto = userService.getByName(currentUser.getUsername().toUpperCase());
        return ResponseEntity.ok(Map.of(
                "id", userDto.getId(),
                "username", userDto.getUsername(),
                "displayName", userDto.getDisplayName(),
                "role", userDto.getRole().name()
        ));
    }

    @PutMapping("/password")
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
    public ResponseEntity<?> updatePassword(@AuthenticationPrincipal UserDetails currentUser,
                                            @RequestBody UpdateUserPasswordRequest request) {
        UserDto userDto = userService.updatePassword(currentUser, request);
        return ResponseEntity.ok().body(Map.of(
                "message", "Пароль успешно изменен",
                "username", userDto.getUsername(),
                "note", "Для дальнейших запросов используйте новый пароль"
        ));
    }

}
