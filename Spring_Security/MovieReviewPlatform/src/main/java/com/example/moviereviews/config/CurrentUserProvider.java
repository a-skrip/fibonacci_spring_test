package com.example.moviereviews.config;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class CurrentUserProvider {

    public static final UUID DEFAULT_DEMO_USER_ID = UUID.fromString("11111111-1111-1111-1111-111111111111"); // alice
    private static final Logger log = LoggerFactory.getLogger(CurrentUserProvider.class);

    private final HttpServletRequest request;

    public CurrentUserProvider(HttpServletRequest request) {
        this.request = request;
    }

    /**
     * Reads X-User-Id header if present, otherwise falls back to demo Alice user.
     * Replace this with Spring Security later.
     */
    public UUID getCurrentUserId() {
        String header = request.getHeader("X-User-Id");
        if (header != null && !header.isBlank()) {
            try {
                return UUID.fromString(header.trim());
            } catch (IllegalArgumentException ignored) {
                log.warn("user {} not found, used demo user", header);
            }
        }
        return DEFAULT_DEMO_USER_ID;
    }
}
