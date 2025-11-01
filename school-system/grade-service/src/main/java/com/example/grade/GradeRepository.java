package com.example.grade;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface GradeRepository extends JpaRepository<Grade, Long> {
    List<Grade> findByStudentId(Long studentId);
    List<Grade> findByClassId(Long classId);
    List<Grade> findBySubject(String subject);
}