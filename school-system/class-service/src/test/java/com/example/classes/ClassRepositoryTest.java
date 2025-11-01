package com.example.classes;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@TestPropertySource(properties = "spring.sql.init.mode=never")
@Sql("/test-data.sql")
public class ClassRepositoryTest {

    @Autowired
    private ClassRepository classRepository;

    @Test
    void testDataWasLoadedAndCanBeFound() { // Renamed the test for clarity
        // ARRANGE: The @Sql annotation has already loaded data for us.

        // ACT: We try to find the class with ID=1, which should exist.
        ClassEntity foundClass = classRepository.findById(1L).orElse(null);

        // ASSERT: Check that the class was found and its data is correct.
        assertThat(foundClass).isNotNull();
        assertThat(foundClass.getId()).isEqualTo(1L);
        assertThat(foundClass.getName()).isEqualTo("Test Class A");
        assertThat(foundClass.getStudentIds()).hasSize(2).contains(101L, 102L);
    }
}