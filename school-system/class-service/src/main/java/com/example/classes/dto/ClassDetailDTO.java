package com.example.classes.dto;

import java.util.List;

/**
 * The rich Data Transfer Object for a Class, including full teacher
 * and student details.
 */
public record ClassDetailDTO(
    Long id,
    String name,
    UserDTO teacher,
    List<UserDTO> students
) {}