package com.example.moviereviews.service;

import com.example.moviereviews.domain.User;
import com.example.moviereviews.dto.UserDto;
import com.example.moviereviews.dto.requests.RegisterUserRequest;
import com.example.moviereviews.dto.requests.UpdateUserPasswordRequest;
import com.example.moviereviews.exception.NotFoundException;
import com.example.moviereviews.exception.UsernameAlreadyExistException;
import com.example.moviereviews.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    UserRepository users;
    @Mock
    PasswordEncoder passwordEncoder;
    @InjectMocks
    UserService userService;

    @Test
    void register_success() {
        RegisterUserRequest req = new RegisterUserRequest();
        req.setUsername("TestUserName");
        req.setPassword("123456");
        req.setDisplayName("XoX");

        Mockito.when(users.findByUsername("TestUserName"))
                .thenReturn(Optional.empty());

        Mockito.when(passwordEncoder.encode("123456"))
                .thenReturn("encoded");

        Mockito.when(users.save(any(User.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        UserDto result = userService.register(req);

        assertEquals("TestUserName", result.getUsername());
        assertEquals("XoX", result.getDisplayName());
    }

    @Test
    void register_username_exists() {
        RegisterUserRequest req = new RegisterUserRequest();
        req.setUsername("TestUserName");
        req.setPassword("123");
        req.setDisplayName("XoX");

        Mockito.when(users.findByUsername("TestUserName"))
                .thenReturn(Optional.of(new User()));

        assertThrows(UsernameAlreadyExistException.class,
                () -> userService.register(req));
    }

    @Test
    void register_username_blank() {
        RegisterUserRequest req = new RegisterUserRequest();
        req.setUsername(" ");
        req.setPassword("123");
        req.setDisplayName("XoX");

        assertThrows(IllegalArgumentException.class,
                () -> userService.register(req));
    }

    @Test
    void updatePassword_success() {
        User user = new User();
        user.setUsername("TestUserName");
        user.setPassword("encodedOld");

        UserDetails userDetails = Mockito.mock(UserDetails.class);
        Mockito.when(userDetails.getUsername()).thenReturn("TestUserName");

        UpdateUserPasswordRequest req = new UpdateUserPasswordRequest();
        req.setOldPassword("old");
        req.setNewPassword("new");

        Mockito.when(users.findByUsername("TestUserName"))
                .thenReturn(Optional.of(user));

        Mockito.when(passwordEncoder.matches("old", "encodedOld"))
                .thenReturn(true);

        Mockito.when(passwordEncoder.encode("new"))
                .thenReturn("encodedNew");

        Mockito.when(users.save(any()))
                .thenAnswer(inv -> inv.getArgument(0));

        UserDto result = userService.updatePassword(userDetails, req);

        assertNotNull(result);
        assertThat(result.getPassword()).isEqualTo("encodedNew");
    }
    @Test
    void getByName_not_found() {
        Mockito.when(users.findByUsername("TestUserName"))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> userService.getByName("TestUserName"));
    }

    @Test
    void getOrThrow_not_found() {
        UUID id = UUID.randomUUID();

        Mockito.when(users.findById(id))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> userService.getOrThrow(id));
    }
}
