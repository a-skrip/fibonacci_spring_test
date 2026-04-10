package com.example.moviereviews.mapper;

import com.example.moviereviews.domain.User;
import com.example.moviereviews.dto.UserDto;

public class UserMapper {
    public static UserDto toDto(User user) {
        return new UserDto(
                user.getId(),
                user.getUsername(),
                user.getPassword(),
                user.getDisplayName(),
                user.getCreatedAt(),
                user.getRole()
        );
    }

    public static User toEntity(UserDto userDto) {
        User user = new User();
        user.setId(userDto.getId());
        user.setPassword(userDto.getPassword());
        user.setUsername(userDto.getUsername());
        user.setDisplayName(userDto.getDisplayName());
        user.setCreatedAt(userDto.getCreatedAt());
        user.setRole(userDto.getRole());
        return user;
    }
}
