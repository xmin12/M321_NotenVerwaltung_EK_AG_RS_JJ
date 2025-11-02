package com.example.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private User studentUser;
    private User teacherUser;
    private User adminUser;

    @BeforeEach
    void setUp() {
        studentUser = new User(1L, "Student One", "student1@example.com", "pass1", User.Role.STUDENT);
        teacherUser = new User(2L, "Teacher One", "teacher1@example.com", "pass2", User.Role.TEACHER);
        adminUser = new User(3L, "Admin One", "admin1@example.com", "pass3", User.Role.ADMIN);
    }

    @Test
    void testGetAllUsers() throws Exception {
        List<User> allUsers = Arrays.asList(studentUser, teacherUser, adminUser);
        when(userRepository.findAll()).thenReturn(allUsers);

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));
    }

    @Test
    void testGetOneUserSuccess() throws Exception {
        when(userRepository.findById(1L)).thenReturn(Optional.of(studentUser));

        mockMvc.perform(get("/api/users/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Student One")));
    }

    @Test
    void testGetOneUserNotFound() throws Exception {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        // MODIFIED: Assert the correct HTTP 404 Not Found status.
        mockMvc.perform(get("/api/users/{id}", 99L))
                .andExpect(status().isNotFound());

        verify(userRepository, times(1)).findById(99L);
    }

    @Test
    void testGetUsersByRoleSuccess() throws Exception {
        List<User> students = Collections.singletonList(studentUser);
        when(userRepository.findByRole(User.Role.STUDENT)).thenReturn(students);

        mockMvc.perform(get("/api/users/role/{role}", "student"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", is("Student One")));
    }

    @Test
    void testGetUsersByRoleInvalidRole() throws Exception {
        // MODIFIED: Assert the correct HTTP 400 Bad Request status.
        mockMvc.perform(get("/api/users/role/{role}", "invalid_role"))
                .andExpect(status().isBadRequest());

        verify(userRepository, never()).findByRole(any());
    }

    @Test
    void testAddUser() throws Exception {
        User newUser = new User(null, "New User", "new@example.com", "newpass", User.Role.STUDENT);
        User savedUser = new User(4L, "New User", "new@example.com", "newpass", User.Role.STUDENT);
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(4)));
    }

    @Test
    void testUpdateUserSuccess() throws Exception {
        User updatedDetails = new User(null, "Updated Teacher", "updated@example.com", null, User.Role.TEACHER);
        User savedUser = new User(2L, "Updated Teacher", "updated@example.com", "pass2", User.Role.TEACHER);
        
        // Mock the check for existence
        when(userRepository.existsById(2L)).thenReturn(true);
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        mockMvc.perform(put("/api/users/{id}", 2L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedDetails)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Updated Teacher")));
    }

    @Test
    void testUpdateUserNotFound() throws Exception {
        User updatedDetails = new User(null, "Updated Teacher", "updated@example.com", null, User.Role.TEACHER);
        
        // Mock that the user does NOT exist
        when(userRepository.existsById(99L)).thenReturn(false);

        mockMvc.perform(put("/api/users/{id}", 99L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedDetails)))
                .andExpect(status().isNotFound());
        
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testDeleteUserSuccess() throws Exception {
        when(userRepository.existsById(1L)).thenReturn(true);
        doNothing().when(userRepository).deleteById(1L);

        // MODIFIED: Assert the correct HTTP 204 No Content status.
        mockMvc.perform(delete("/api/users/{id}", 1L))
                .andExpect(status().isNoContent());

        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteUserNotFound() throws Exception {
        when(userRepository.existsById(99L)).thenReturn(false);

        mockMvc.perform(delete("/api/users/{id}", 99L))
                .andExpect(status().isNotFound());
        
        verify(userRepository, never()).deleteById(anyLong());
    }
}