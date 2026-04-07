package com.example.moviereviews.enums;

public enum Role {
    ROLE_USER,
    ROLE_ADMIN;

    // Для Spring Security
    public String getAuthority() {
        return name();
    }
}
