package com.example.classes;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for ClassEntity.
 */
@Repository
public interface ClassRepository extends JpaRepository<ClassEntity, Long> {
}