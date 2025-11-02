package com.example.grade;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class GradeRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private GradeRepository gradeRepository;

    private Grade grade1;
    private Grade grade2;
    private Grade grade3;

    @BeforeEach
    void setUp() {
        // Clear previous data
        gradeRepository.deleteAll();

        LocalDate today = LocalDate.now();

        grade1 = new Grade(1L, 101L, "Mathematics", 4.5, today);
        grade2 = new Grade(1L, 102L, "Physics", 3.0, today.minusDays(1));
        grade3 = new Grade(2L, 101L, "Mathematics", 5.0, today.minusDays(2));

        entityManager.persist(grade1);
        entityManager.persist(grade2);
        entityManager.persist(grade3);
        entityManager.flush(); // Ensure data is written to the database
    }

    @Test
    void testSaveGrade() {
        Grade newGrade = new Grade(3L, 103L, "Chemistry", 3.5, LocalDate.now());
        Grade savedGrade = gradeRepository.save(newGrade);

        assertThat(savedGrade).isNotNull();
        assertThat(savedGrade.getId()).isNotNull();
        assertThat(savedGrade.getSubject()).isEqualTo("Chemistry");
    }

    @Test
    void testFindById() {
        Optional<Grade> foundGrade = gradeRepository.findById(grade1.getId());

        assertThat(foundGrade).isPresent();
        assertThat(foundGrade.get().getSubject()).isEqualTo("Mathematics");
    }

    @Test
    void testFindByIdNotFound() {
        Optional<Grade> foundGrade = gradeRepository.findById(999L); // Non-existent ID
        assertThat(foundGrade).isNotPresent();
    }

    @Test
    void testFindAllGrades() {
        List<Grade> grades = gradeRepository.findAll();
        assertThat(grades).hasSize(3);
        assertThat(grades).extracting(Grade::getSubject)
                .containsExactlyInAnyOrder("Mathematics", "Physics", "Mathematics");
    }

    @Test
    void testFindByStudentId() {
        List<Grade> student1Grades = gradeRepository.findByStudentId(1L);
        assertThat(student1Grades).hasSize(2);
        assertThat(student1Grades).extracting(Grade::getSubject)
                .containsExactlyInAnyOrder("Mathematics", "Physics");

        List<Grade> student2Grades = gradeRepository.findByStudentId(2L);
        assertThat(student2Grades).hasSize(1);
        assertThat(student2Grades.get(0).getSubject()).isEqualTo("Mathematics");

        List<Grade> student3Grades = gradeRepository.findByStudentId(3L);
        assertThat(student3Grades).isEmpty();
    }

    @Test
    void testFindByClassId() {
        List<Grade> class101Grades = gradeRepository.findByClassId(101L);
        assertThat(class101Grades).hasSize(2);
        assertThat(class101Grades).extracting(Grade::getStudentId)
                .containsExactlyInAnyOrder(1L, 2L);

        List<Grade> class102Grades = gradeRepository.findByClassId(102L);
        assertThat(class102Grades).hasSize(1);
        assertThat(class102Grades.get(0).getStudentId()).isEqualTo(1L);

        List<Grade> class103Grades = gradeRepository.findByClassId(103L);
        assertThat(class103Grades).isEmpty();
    }

    @Test
    void testFindBySubject() {
        List<Grade> mathGrades = gradeRepository.findBySubject("Mathematics");
        assertThat(mathGrades).hasSize(2);
        assertThat(mathGrades).extracting(Grade::getStudentId)
                .containsExactlyInAnyOrder(1L, 2L);

        List<Grade> physicsGrades = gradeRepository.findBySubject("Physics");
        assertThat(physicsGrades).hasSize(1);
        assertThat(physicsGrades.get(0).getStudentId()).isEqualTo(1L);

        List<Grade> historyGrades = gradeRepository.findBySubject("History");
        assertThat(historyGrades).isEmpty();
    }

    @Test
    void testUpdateGrade() {
        Grade gradeToUpdate = gradeRepository.findById(grade1.getId()).get();
        gradeToUpdate.setValue(5.5);
        gradeToUpdate.setSubject("Advanced Mathematics");
        gradeToUpdate.setDate(LocalDate.of(2024, 1, 1));

        Grade updatedGrade = gradeRepository.save(gradeToUpdate);

        assertThat(updatedGrade.getValue()).isEqualTo(5.5);
        assertThat(updatedGrade.getSubject()).isEqualTo("Advanced Mathematics");
        assertThat(updatedGrade.getDate()).isEqualTo(LocalDate.of(2024, 1, 1));
    }

    @Test
    void testDeleteGrade() {
        gradeRepository.deleteById(grade1.getId());
        Optional<Grade> deletedGrade = gradeRepository.findById(grade1.getId());
        assertThat(deletedGrade).isNotPresent();

        List<Grade> remainingGrades = gradeRepository.findAll();
        assertThat(remainingGrades).hasSize(2);
    }
}
