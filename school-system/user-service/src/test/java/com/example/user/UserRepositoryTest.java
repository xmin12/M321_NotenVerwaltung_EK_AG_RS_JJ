package com.example.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    private User studentUser;
    private User teacherUser;
    private User adminUser;

    @BeforeEach
    void setUp() {
        // Clear previous data
        userRepository.deleteAll();

        studentUser = new User(null, "Student One", "student1@example.com", "pass1", User.Role.STUDENT);
        teacherUser = new User(null, "Teacher One", "teacher1@example.com", "pass2", User.Role.TEACHER);
        adminUser = new User(null, "Admin One", "admin1@example.com", "pass3", User.Role.ADMIN);

        entityManager.persist(studentUser);
        entityManager.persist(teacherUser);
        entityManager.persist(adminUser);
        entityManager.flush(); // Ensure data is written to the database
    }

    @Test
    void testSaveUser() {
        User newUser = new User(null, "New User", "newuser@example.com", "newpass", User.Role.STUDENT);
        User savedUser = userRepository.save(newUser);

        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getId()).isNotNull();
        assertThat(savedUser.getName()).isEqualTo("New User");
    }

    @Test
    void testFindById() {
        Optional<User> foundUser = userRepository.findById(studentUser.getId());

        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getName()).isEqualTo("Student One");
    }

    @Test
    void testFindByIdNotFound() {
        Optional<User> foundUser = userRepository.findById(999L); // Non-existent ID
        assertThat(foundUser).isNotPresent();
    }

    @Test
    void testFindAllUsers() {
        List<User> users = userRepository.findAll();
        assertThat(users).hasSize(3);
        assertThat(users).extracting(User::getName)
                .containsExactlyInAnyOrder("Student One", "Teacher One", "Admin One");
    }

    @Test
    void testFindByRole() {
        List<User> students = userRepository.findByRole(User.Role.STUDENT);
        assertThat(students).hasSize(1);
        assertThat(students.get(0).getName()).isEqualTo("Student One");

        List<User> teachers = userRepository.findByRole(User.Role.TEACHER);
        assertThat(teachers).hasSize(1);
        assertThat(teachers.get(0).getName()).isEqualTo("Teacher One");

        List<User> admins = userRepository.findByRole(User.Role.ADMIN);
        assertThat(admins).hasSize(1);
        assertThat(admins.get(0).getName()).isEqualTo("Admin One");

        List<User> nonExistentRole = userRepository.findByRole(User.Role.valueOf("ADMIN")); // Example of using valueOf
        assertThat(nonExistentRole).hasSize(1);
    }

    @Test
    void testUpdateUser() {
        User userToUpdate = userRepository.findById(studentUser.getId()).get();
        userToUpdate.setName("Updated Student");
        userToUpdate.setEmail("updated.student@example.com");
        userToUpdate.setRole(User.Role.TEACHER);

        User updatedUser = userRepository.save(userToUpdate);

        assertThat(updatedUser.getName()).isEqualTo("Updated Student");
        assertThat(updatedUser.getEmail()).isEqualTo("updated.student@example.com");
        assertThat(updatedUser.getRole()).isEqualTo(User.Role.TEACHER);
    }

    @Test
    void testDeleteUser() {
        userRepository.deleteById(studentUser.getId());
        Optional<User> deletedUser = userRepository.findById(studentUser.getId());
        assertThat(deletedUser).isNotPresent();

        List<User> remainingUsers = userRepository.findAll();
        assertThat(remainingUsers).hasSize(2);
    }
}
