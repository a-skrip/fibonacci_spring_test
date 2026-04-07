package com.example.moviereviews.service;

import com.example.moviereviews.domain.User;
import com.example.moviereviews.dto.requests.RegisterUserRequest;
import com.example.moviereviews.exception.NotFoundException;
import com.example.moviereviews.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class UserService {

    private final UserRepository users;

    public UserService(UserRepository users) {
        this.users = users;
    }

    @Transactional
    public User register(RegisterUserRequest req) {
        if (req.getUsername() == null || req.getUsername().isBlank()) throw new IllegalArgumentException("username is required");
        if (req.getPassword() == null || req.getPassword().isBlank()) throw new IllegalArgumentException("password is required");
        if (req.getDisplayName() == null || req.getDisplayName().isBlank()) throw new IllegalArgumentException("displayName is required");

        User u = new User();
        u.setId(UUID.randomUUID());
        // NOTE: not hashing here; replace with encoder once Security is added
        u.setUsername(req.getUsername().trim());
        u.setPassword(req.getPassword());
        u.setDisplayName(req.getDisplayName().trim());
        return users.save(u);
    }

    @Transactional(readOnly = true)
    public User getOrThrow(UUID id) {
        return users.findById(id).orElseThrow(() -> new NotFoundException("User not found: " + id));
    }
}
