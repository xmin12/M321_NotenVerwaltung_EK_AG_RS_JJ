package com.example.grade;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(GradeController.class)
class GradeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GradeRepository gradeRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Grade grade1;
    private Grade grade2;

    @BeforeEach
    void setUp() {
        grade1 = new Grade(1L, 101L, "Math", 5.5, LocalDate.now());
        grade1.setId(1L);

        grade2 = new Grade(2L, 101L, "History", 4.0, LocalDate.now());
        grade2.setId(2L);
    }

    @Test
    void testGetAllGrades() throws Exception {
        when(gradeRepository.findAll()).thenReturn(List.of(grade1, grade2));

        mockMvc.perform(get("/api/grades"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void testGetOneGradeSuccess() throws Exception {
        when(gradeRepository.findById(1L)).thenReturn(Optional.of(grade1));

        mockMvc.perform(get("/api/grades/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.subject", is("Math")));
    }

    @Test
    void testGetOneGradeNotFound() throws Exception {
        when(gradeRepository.findById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/grades/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testAddGrade() throws Exception {
        when(gradeRepository.save(any(Grade.class))).thenReturn(grade1);

        mockMvc.perform(post("/api/grades")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(grade1)))
                .andExpect(status().isCreated());
    }

    @Test
    void testUpdateGradeSuccess() throws Exception {
        Grade updatedGrade = new Grade(1L, 101L, "Advanced Math", 6.0, LocalDate.now());
        updatedGrade.setId(1L);

        when(gradeRepository.findById(1L)).thenReturn(Optional.of(grade1));
        when(gradeRepository.save(any(Grade.class))).thenReturn(updatedGrade);

        mockMvc.perform(put("/api/grades/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedGrade)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.subject", is("Advanced Math")));
    }

    @Test
    void testUpdateGradeNotFound() throws Exception {
        Grade updatedGrade = new Grade(1L, 101L, "Advanced Math", 6.0, LocalDate.now());
        when(gradeRepository.findById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/grades/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedGrade)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteGradeSuccess() throws Exception {
        when(gradeRepository.existsById(1L)).thenReturn(true);
        doNothing().when(gradeRepository).deleteById(1L);

        mockMvc.perform(delete("/api/grades/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDeleteGradeNotFound() throws Exception {
        when(gradeRepository.existsById(99L)).thenReturn(false);

        mockMvc.perform(delete("/api/grades/99"))
                .andExpect(status().isNotFound());
    }
}