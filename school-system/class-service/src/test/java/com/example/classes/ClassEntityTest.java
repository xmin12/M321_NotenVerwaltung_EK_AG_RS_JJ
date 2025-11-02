package com.example.classes;

import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class ClassEntityTest {

    @Test
    void testGettersAndSetters() {
        ClassEntity entity = new ClassEntity();
        entity.setId(1L);
        entity.setName("TestClass");
        entity.setTeacherId(2L);
        entity.setStudentIds(Arrays.asList(10L, 20L));

        assertEquals(1L, entity.getId());
        assertEquals("TestClass", entity.getName());
        assertEquals(2L, entity.getTeacherId());
        assertEquals(Arrays.asList(10L, 20L), entity.getStudentIds());
    }
}
