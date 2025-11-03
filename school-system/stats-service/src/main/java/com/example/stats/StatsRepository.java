package com.example.stats;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface StatsRepository extends JpaRepository<Stats, Long> {
    List<Stats> findByStudentId(Long studentId);
    List<Stats> findBySubject(String subject);
}
