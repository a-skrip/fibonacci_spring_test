package com.example.moviereviews.dto;

import com.example.moviereviews.enums.Role;

import java.time.OffsetDateTime;
import java.util.UUID;

public class UserDto {

    private UUID id;
    private String username;
    private String password;
    private String displayName;
    private OffsetDateTime createdAt;
    private Role role;

    public UserDto() {
    }

    public UserDto(UUID id, String username, String password, String displayName, OffsetDateTime createdAt, Role role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.displayName = displayName;
        this.createdAt = createdAt;
        this.role = role;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
