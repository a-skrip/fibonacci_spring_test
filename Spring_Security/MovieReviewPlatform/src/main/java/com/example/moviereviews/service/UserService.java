package com.example.moviereviews.service;

import com.example.moviereviews.domain.User;
import com.example.moviereviews.dto.requests.RegisterUserRequest;
import com.example.moviereviews.enums.Role;
import com.example.moviereviews.exception.NotFoundException;
import com.example.moviereviews.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository users;
    private final PasswordEncoder passwordEncoder;


    public UserService(UserRepository users, PasswordEncoder passwordEncoder) {
        this.users = users;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public User register(RegisterUserRequest req) {
        if (req.getUsername() == null || req.getUsername().isBlank())
            throw new IllegalArgumentException("username is required");
        if (req.getPassword() == null || req.getPassword().isBlank())
            throw new IllegalArgumentException("password is required");
        if (req.getDisplayName() == null || req.getDisplayName().isBlank())
            throw new IllegalArgumentException("displayName is required");

        User u = new User();

        //Шифруем пароль
        String encodePassword = passwordEncoder.encode(req.getPassword());

        u.setId(UUID.randomUUID());
        u.setUsername(req.getUsername().trim());
        //Пишем в БД зашифрованный
        u.setPassword(encodePassword);
        u.setDisplayName(req.getDisplayName().trim());
        u.setRole(Role.ROLE_USER);
        return users.save(u);
    }

    @Transactional(readOnly = true)
    public User getOrThrow(UUID id) {
        return users.findById(id).orElseThrow(() -> new NotFoundException("User not found: " + id));
    }

    @Transactional(readOnly = true)
    public User geByName(String name) {
        return users.findByUsername(name).orElseThrow(() -> new NotFoundException("User not found: " + name));
    }
}
