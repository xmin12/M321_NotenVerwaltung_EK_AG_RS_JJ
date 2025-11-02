package com.example.classes;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.test.context.jdbc.Sql;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
class ClassServiceTest {

    @Mock
    private ClassRepository classRepository;

    @InjectMocks
    private ClassService classService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllClasses() {
        List<ClassEntity> classes = Arrays.asList(new ClassEntity(), new ClassEntity());
        when(classRepository.findAll()).thenReturn(classes);
        assertEquals(classes, classService.getAllClasses());
    }

    @Test
    void testGetClassByIdFound() {
        ClassEntity entity = new ClassEntity();
        when(classRepository.findById(1L)).thenReturn(Optional.of(entity));
        assertTrue(classService.getClassById(1L).isPresent());
    }

    @Test
    void testGetClassByIdNotFound() {
        when(classRepository.findById(1L)).thenReturn(Optional.empty());
        assertFalse(classService.getClassById(1L).isPresent());
    }

    @Test
    void testCreateClass() {
        ClassEntity entity = new ClassEntity();
        when(classRepository.save(entity)).thenReturn(entity);
        assertEquals(entity, classService.createClass(entity));
    }

    @Test
    void testUpdateClassSuccess() {
        ClassEntity entity = new ClassEntity();
        entity.setName("Old");
        entity.setTeacherId(1L);
        entity.setStudentIds(new ArrayList<>());
        ClassEntity details = new ClassEntity();
        details.setName("New");
        details.setTeacherId(2L);
        details.setStudentIds(Arrays.asList(10L, 20L));
        when(classRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(classRepository.save(any())).thenReturn(entity);
        ClassEntity updated = classService.updateClass(1L, details);
        assertEquals("New", updated.getName());
        assertEquals(2L, updated.getTeacherId());
        assertEquals(Arrays.asList(10L, 20L), updated.getStudentIds());
    }

    @Test
    void testUpdateClassNotFound() {
        when(classRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> classService.updateClass(1L, new ClassEntity()));
    }

    @Test
    void testDeleteClass() {
        classService.deleteClass(1L);
        verify(classRepository).deleteById(1L);
    }

    @Test
    void testAddStudentToClass() {
        ClassEntity entity = new ClassEntity();
        entity.setStudentIds(new ArrayList<>());
        when(classRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(classRepository.save(entity)).thenReturn(entity);
        ClassEntity result = classService.addStudentToClass(1L, 100L);
        assertTrue(result.getStudentIds().contains(100L));
    }

    @Test
    void testAddStudentToClassAlreadyPresent() {
        ClassEntity entity = new ClassEntity();
        entity.setStudentIds(new ArrayList<>(Arrays.asList(100L)));
        when(classRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(classRepository.save(entity)).thenReturn(entity);
        ClassEntity result = classService.addStudentToClass(1L, 100L);
        assertEquals(1, result.getStudentIds().size());
    }

    @Test
    void testAddStudentToClassNotFound() {
        when(classRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> classService.addStudentToClass(1L, 100L));
    }

    @Test
    void testRemoveStudentFromClass() {
        ClassEntity entity = new ClassEntity();
        entity.setStudentIds(new ArrayList<>(Arrays.asList(100L, 200L)));
        when(classRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(classRepository.save(entity)).thenReturn(entity);
        ClassEntity result = classService.removeStudentFromClass(1L, 100L);
        assertFalse(result.getStudentIds().contains(100L));
    }

    @Test
    void testRemoveStudentFromClassNotFound() {
        when(classRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> classService.removeStudentFromClass(1L, 100L));
    }

    @Test
    void testChangeTeacher() {
        ClassEntity entity = new ClassEntity();
        entity.setTeacherId(1L);
        when(classRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(classRepository.save(entity)).thenReturn(entity);
        ClassEntity result = classService.changeTeacher(1L, 2L);
        assertEquals(2L, result.getTeacherId());
    }

    @Test
    void testChangeTeacherNotFound() {
        when(classRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> classService.changeTeacher(1L, 2L));
    }
}
