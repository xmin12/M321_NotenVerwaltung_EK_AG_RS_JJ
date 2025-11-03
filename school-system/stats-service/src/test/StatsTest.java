package com.example.stats;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StatsTest {

@Test
void testDefaultConstructor() {
    Stats stats = new Stats();
    assertNotNull(stats);
}

@Test
void testParameterizedConstructorAndGetters() {
    Stats stats = new Stats(101L, "Math", 6.0, 4.0, 5.0);

    assertNull(stats.getId());
    assertEquals(101L, stats.getStudentId());
    assertEquals("Math", stats.getSubject());
    assertEquals(6.0, stats.getHighestGrade());
    assertEquals(4.0, stats.getLowestGrade());
    assertEquals(5.0, stats.getAverageGrade());
}

@Test
void testSetters() {
    Stats stats = new Stats();
    stats.setId(1L);
    stats.setStudentId(102L);
    stats.setSubject("History");
    stats.setHighestGrade(5.0);
    stats.setLowestGrade(2.0);
    stats.setAverageGrade(3.5);

    assertEquals(1L, stats.getId());
    assertEquals(102L, stats.getStudentId());
    assertEquals("History", stats.getSubject());
    assertEquals(5.0, stats.getHighestGrade());
    assertEquals(2.0, stats.getLowestGrade());
    assertEquals(3.5, stats.getAverageGrade());
}

@Test
void testEqualsAndHashCode() {
    Stats stats1 = new Stats(101L, "Math", 6.0, 4.0, 5.0);
    stats1.setId(1L);
    Stats stats2 = new Stats(101L, "Math", 6.0, 4.0, 5.0);
    stats2.setId(1L);
    Stats stats3 = new Stats(102L, "History", 5.0, 2.0, 3.5);
    stats3.setId(2L); 
    Stats stats4 = new Stats(101L, "Math", 6.0, 4.0, 5.0);
    stats4.setId(null); 

    assertEquals(stats1, stats1);
    assertEquals(stats1, stats2);
    assertEquals(stats2, stats1);
    Stats stats5 = new Stats(101L, "Math", 6.0, 4.0, 5.0);
    stats5.setId(1L);
    assertEquals(stats1, stats5);
    assertEquals(stats2, stats5);

    assertEquals(stats1, stats2);
    assertEquals(stats1.hashCode(), stats2.hashCode());

    assertNotEquals(stats1, stats3);
    assertNotEquals(stats1.hashCode(), stats3.hashCode());
    assertNotEquals(stats1, null);
    assertNotEquals(stats1, new Object());
    assertNotEquals(stats1, stats4); 
}

@Test
void testToString() {
    Stats stats = new Stats(101L, "Math", 6.0, 4.0, 5.0);
    stats.setId(1L);

    String expectedToString = "Stats{" +
            "id=1" +
            ", studentId=101" +
            ", subject='Math'" +
            ", highestGrade=6.0" +
            ", lowestGrade=4.0" +
            ", averageGrade=5.0" +
            '}';
    assertEquals(expectedToString, stats.toString());
}

}
