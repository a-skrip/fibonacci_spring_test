package com.example.moviereviews.service;

import com.example.moviereviews.domain.User;
import com.example.moviereviews.dto.UserDto;
import com.example.moviereviews.dto.requests.RegisterUserRequest;
import com.example.moviereviews.dto.requests.UpdateUserPasswordRequest;
import com.example.moviereviews.enums.Role;
import com.example.moviereviews.exception.NotFoundException;
import com.example.moviereviews.exception.PasswordNotMatchException;
import com.example.moviereviews.exception.UsernameAlreadyExistException;
import com.example.moviereviews.mapper.UserMapper;
import com.example.moviereviews.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public UserDto register(RegisterUserRequest req) {
        if (req.getUsername() == null || req.getUsername().isBlank())
            throw new IllegalArgumentException("username is required");
        if (req.getPassword() == null || req.getPassword().isBlank())
            throw new IllegalArgumentException("password is required");
        if (req.getDisplayName() == null || req.getDisplayName().isBlank())
            throw new IllegalArgumentException("displayName is required");

        User u = new User();

        //Шифруем пароль
        String encodePassword = passwordEncoder.encode(req.getPassword());
        if (users.findByUsername(req.getUsername()).isPresent()) {
            throw new UsernameAlreadyExistException("Такой пользователь уже существует");
        }
        u.setId(UUID.randomUUID());
        u.setUsername(req.getUsername().trim().toUpperCase());
        //Пишем в БД зашифрованный
        u.setPassword(encodePassword);
        u.setDisplayName(req.getDisplayName().trim());
        u.setRole(Role.ROLE_USER);
        return UserMapper.toDto(users.save(u));
    }

    @Transactional
    public UserDto updatePassword(UserDetails userDetails, UpdateUserPasswordRequest request) {
        User user = users.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        boolean matches = passwordEncoder.matches(request.getOldPassword(), user.getPassword());
        if (!matches) {
            throw new PasswordNotMatchException("Пароли не совпадают");
        }
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        return UserMapper.toDto(users.save(user));
    }

    @Transactional(readOnly = true)
    public UserDto getOrThrow(UUID id) {
        return UserMapper.toDto(users.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found: " + id)));
    }

    @Transactional(readOnly = true)
    public UserDto getByName(String name) {
        String upperCase = name.toUpperCase();
        return UserMapper.toDto(users.findByUsername(upperCase)
                .orElseThrow(() -> new NotFoundException("User not found: " + name)));
    }
}
