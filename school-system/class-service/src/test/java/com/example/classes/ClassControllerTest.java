package com.example.classes;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import java.util.*;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.test.context.TestPropertySource; // <-- ADD THIS IMPORT


@WebMvcTest(ClassController.class)
@TestPropertySource(properties = "spring.sql.init.mode=never")
class ClassControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClassService classService;

    @Test
    void testGetAllClasses() throws Exception {
        List<ClassEntity> classes = Arrays.asList(new ClassEntity(), new ClassEntity());
        when(classService.getAllClasses()).thenReturn(classes);
        mockMvc.perform(get("/api/classes"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetClassByIdFound() throws Exception {
        ClassEntity entity = new ClassEntity();
        when(classService.getClassById(1L)).thenReturn(Optional.of(entity));
        mockMvc.perform(get("/api/classes/1"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetClassByIdNotFound() throws Exception {
        when(classService.getClassById(1L)).thenReturn(Optional.empty());
        mockMvc.perform(get("/api/classes/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateClass() throws Exception {
        ClassEntity entity = new ClassEntity();
        when(classService.createClass(any())).thenReturn(entity);
        mockMvc.perform(post("/api/classes")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Test\",\"teacherId\":1,\"studentIds\":[]}"))
                .andExpect(status().isOk());
    }

    @Test
    void testUpdateClassSuccess() throws Exception {
        ClassEntity entity = new ClassEntity();
        when(classService.updateClass(eq(1L), any())).thenReturn(entity);
        mockMvc.perform(put("/api/classes/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Test\",\"teacherId\":1,\"studentIds\":[]}"))
                .andExpect(status().isOk());
    }

    @Test
    void testUpdateClassNotFound() throws Exception {
        when(classService.updateClass(eq(1L), any())).thenThrow(new RuntimeException());
        mockMvc.perform(put("/api/classes/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Test\",\"teacherId\":1,\"studentIds\":[]}"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteClass() throws Exception {
        mockMvc.perform(delete("/api/classes/1"))
                .andExpect(status().isOk());
    }

    @Test
    void testAddStudentToClassSuccess() throws Exception {
        ClassEntity entity = new ClassEntity();
        when(classService.addStudentToClass(1L, 2L)).thenReturn(entity);
        mockMvc.perform(post("/api/classes/1/students/2"))
                .andExpect(status().isOk());
    }

    @Test
    void testAddStudentToClassNotFound() throws Exception {
        when(classService.addStudentToClass(1L, 2L)).thenThrow(new RuntimeException());
        mockMvc.perform(post("/api/classes/1/students/2"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testRemoveStudentFromClassSuccess() throws Exception {
        ClassEntity entity = new ClassEntity();
        when(classService.removeStudentFromClass(1L, 2L)).thenReturn(entity);
        mockMvc.perform(delete("/api/classes/1/students/2"))
                .andExpect(status().isOk());
    }

    @Test
    void testRemoveStudentFromClassNotFound() throws Exception {
        when(classService.removeStudentFromClass(1L, 2L)).thenThrow(new RuntimeException());
        mockMvc.perform(delete("/api/classes/1/students/2"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testChangeTeacherSuccess() throws Exception {
        ClassEntity entity = new ClassEntity();
        when(classService.changeTeacher(1L, 2L)).thenReturn(entity);
        mockMvc.perform(put("/api/classes/1/teacher/2"))
                .andExpect(status().isOk());
    }

    @Test
    void testChangeTeacherNotFound() throws Exception {
        when(classService.changeTeacher(1L, 2L)).thenThrow(new RuntimeException());
        mockMvc.perform(put("/api/classes/1/teacher/2"))
                .andExpect(status().isNotFound());
    }
}
