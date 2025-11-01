package com.example.classes;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ClassRepositoryTest {

    @Autowired
    private ClassRepository classRepository;

    @Test
    void testSaveAndFind() {
        ClassEntity entity = new ClassEntity();
        entity.setName("RepoTest");
        entity.setTeacherId(1L);
        entity.setStudentIds(Arrays.asList(100L, 101L));
        ClassEntity saved = classRepository.save(entity);

        assertNotNull(saved.getId());
        assertEquals("RepoTest", saved.getName());

        ClassEntity found = classRepository.findById(saved.getId()).orElse(null);
        assertNotNull(found);
        assertEquals("RepoTest", found.getName());
        assertEquals(Arrays.asList(100L, 101L), found.getStudentIds());
    }
}
