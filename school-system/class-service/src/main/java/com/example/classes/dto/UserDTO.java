package com.example.classes.dto;

/**
 * A Data Transfer Object representing a simplified User.
 * We use a "record" for a concise, immutable data carrier.
 */
public record UserDTO(
    Long id,
    String name,
    String email,
    String role // It can be useful to know the user's role
) {}