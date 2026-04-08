package com.example.moviereviews.service;

import com.example.moviereviews.domain.User;
import com.example.moviereviews.dto.requests.RegisterUserRequest;
import com.example.moviereviews.dto.requests.UpdateUserPasswordRequest;
import com.example.moviereviews.enums.Role;
import com.example.moviereviews.exception.NotFoundException;
import com.example.moviereviews.exception.PasswordUncorectedException;
import com.example.moviereviews.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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

//    @Transactional
//    public User updatePassword(UserDetails currentUser, String password) {
//        User user = users.findByUsername(currentUser.getUsername())
//                .orElseThrow(() -> new NotFoundException("User not found: "));
//        String encode = passwordEncoder.encode(password);
//        user.setPassword(encode);
//
//        return users.save(user);
//    }

    public User updatePassword(UserDetails userDetails, UpdateUserPasswordRequest request) {
        User user = users.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new PasswordUncorectedException("Current password is incorrect");
        }
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        return users.save(user);
    }

    @Transactional(readOnly = true)
    public User getOrThrow(UUID id) {
        return users.findById(id).orElseThrow(() -> new NotFoundException("User not found: " + id));
    }

    @Transactional(readOnly = true)
    public User getByName(String name) {
        return users.findByUsername(name).orElseThrow(() -> new NotFoundException("User not found: " + name));
    }
}
