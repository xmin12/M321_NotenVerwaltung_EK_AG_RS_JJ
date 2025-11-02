package com.example.user;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void testDefaultConstructor() {
        User user = new User();
        assertNotNull(user);
    }

    @Test
    void testParameterizedConstructorAndGetters() {
        User user = new User(1L, "John Doe", "john.doe@example.com", "password123", User.Role.STUDENT);

        assertEquals(1L, user.getId());
        assertEquals("John Doe", user.getName());
        assertEquals("john.doe@example.com", user.getEmail());
        assertEquals("password123", user.getPassword());
        assertEquals(User.Role.STUDENT, user.getRole());
    }

    @Test
    void testSetters() {
        User user = new User();
        user.setId(2L);
        user.setName("Jane Smith");
        user.setEmail("jane.smith@example.com");
        user.setPassword("newpassword");
        user.setRole(User.Role.TEACHER);

        assertEquals(2L, user.getId());
        assertEquals("Jane Smith", user.getName());
        assertEquals("jane.smith@example.com", user.getEmail());
        assertEquals("newpassword", user.getPassword());
        assertEquals(User.Role.TEACHER, user.getRole());
    }

    @Test
    void testRoleEnum() {
        assertEquals("ADMIN", User.Role.ADMIN.name());
        assertEquals("TEACHER", User.Role.TEACHER.name());
        assertEquals("STUDENT", User.Role.STUDENT.name());
    }
}
