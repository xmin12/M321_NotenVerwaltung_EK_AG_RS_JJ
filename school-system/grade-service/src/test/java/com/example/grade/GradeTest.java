package com.example.grade;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class GradeTest {

    @Test
    void testDefaultConstructor() {
        Grade grade = new Grade();
        assertNotNull(grade);
    }

    @Test
    void testParameterizedConstructorAndGetters() {
        LocalDate date = LocalDate.of(2023, 10, 26);
        Grade grade = new Grade(1L, 101L, "Mathematics", 4.5, date);

        assertNull(grade.getId()); // ID is generated, not set by this constructor
        assertEquals(1L, grade.getStudentId());
        assertEquals(101L, grade.getClassId());
        assertEquals("Mathematics", grade.getSubject());
        assertEquals(4.5, grade.getValue());
        assertEquals(date, grade.getDate());
    }

    @Test
    void testSetters() {
        Grade grade = new Grade();
        grade.setId(5L);
        grade.setStudentId(2L);
        grade.setClassId(102L);
        grade.setSubject("Physics");
        grade.setValue(5.0);
        LocalDate newDate = LocalDate.of(2023, 11, 15);
        grade.setDate(newDate);

        assertEquals(5L, grade.getId());
        assertEquals(2L, grade.getStudentId());
        assertEquals(102L, grade.getClassId());
        assertEquals("Physics", grade.getSubject());
        assertEquals(5.0, grade.getValue());
        assertEquals(newDate, grade.getDate());
    }

    @Test
    void testEqualsAndHashCode() {
        LocalDate date1 = LocalDate.of(2023, 10, 26);
        LocalDate date2 = LocalDate.of(2023, 10, 27);

        Grade grade1 = new Grade(1L, 101L, "Mathematics", 4.5, date1);
        grade1.setId(1L);
        Grade grade2 = new Grade(1L, 101L, "Mathematics", 4.5, date1);
        grade2.setId(1L); // Same ID, should be equal
        Grade grade3 = new Grade(2L, 102L, "Physics", 3.0, date2);
        grade3.setId(2L); // Different ID, should not be equal
        Grade grade4 = new Grade(1L, 101L, "Mathematics", 4.5, date1);
        grade4.setId(null); // Null ID, should not be equal to grade1

        // Reflexivity
        assertEquals(grade1, grade1);
        // Symmetry
        assertEquals(grade1, grade2);
        assertEquals(grade2, grade1);
        // Transitivity
        Grade grade5 = new Grade(1L, 101L, "Mathematics", 4.5, date1);
        grade5.setId(1L);
        assertEquals(grade1, grade5);
        assertEquals(grade2, grade5);

        // Consistency
        assertEquals(grade1, grade2);
        assertEquals(grade1.hashCode(), grade2.hashCode());

        // Inequality
        assertNotEquals(grade1, grade3);
        assertNotEquals(grade1.hashCode(), grade3.hashCode());
        assertNotEquals(grade1, null);
        assertNotEquals(grade1, new Object());
        assertNotEquals(grade1, grade4); // Different IDs (one null)
    }

    @Test
    void testToString() {
        LocalDate date = LocalDate.of(2023, 10, 26);
        Grade grade = new Grade(1L, 101L, "Mathematics", 4.5, date);
        grade.setId(1L);

        String expectedToString = "Grade{" +
                "id=1" +
                ", studentId=1" +
                ", classId=101" +
                ", subject='Mathematics'" +
                ", value=4.5" +
                ", date=2023-10-26" +
                '}';
        assertEquals(expectedToString, grade.toString());
    }
}
