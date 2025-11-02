package com.example.classes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service class for managing classes.
 */
@Service
public class ClassService {

    @Autowired
    private ClassRepository classRepository;

    /**
     * Get all classes.
     * @return list of ClassEntity
     */
    public List<ClassEntity> getAllClasses() {
        return classRepository.findAll();
    }

    /**
     * Get a class by its ID.
     * @param id the class ID
     * @return Optional of ClassEntity
     */
    public Optional<ClassEntity> getClassById(Long id) {
        return classRepository.findById(id);
    }

    /**
     * Create a new class.
     * @param classEntity the class entity
     * @return created ClassEntity
     */
    public ClassEntity createClass(ClassEntity classEntity) {
        return classRepository.save(classEntity);
    }

    /**
     * Update an existing class.
     * @param id the class ID
     * @param classDetails the updated class details
     * @return updated ClassEntity
     */
    public ClassEntity updateClass(Long id, ClassEntity classDetails) {
        ClassEntity classEntity = classRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Class not found with id: " + id));

        classEntity.setName(classDetails.getName());
        classEntity.setTeacherId(classDetails.getTeacherId());
        classEntity.setStudentIds(classDetails.getStudentIds());
        return classRepository.save(classEntity);
    }

    /**
     * Delete a class by its ID.
     * @param id the class ID
     */
    public void deleteClass(Long id) {
        classRepository.deleteById(id);
    }

    /**
     * Add a student to a class.
     * @param classId the class ID
     * @param studentId the student ID
     * @return updated ClassEntity
     */
    public ClassEntity addStudentToClass(Long classId, Long studentId) {
        ClassEntity classEntity = classRepository.findById(classId)
                .orElseThrow(() -> new RuntimeException("Class not found with id: " + classId));
        if (!classEntity.getStudentIds().contains(studentId)) {
            classEntity.getStudentIds().add(studentId);
        }
        return classRepository.save(classEntity);
    }

    /**
     * Remove a student from a class.
     * @param classId the class ID
     * @param studentId the student ID
     * @return updated ClassEntity
     */
    public ClassEntity removeStudentFromClass(Long classId, Long studentId) {
        ClassEntity classEntity = classRepository.findById(classId)
                .orElseThrow(() -> new RuntimeException("Class not found with id: " + classId));
        classEntity.getStudentIds().remove(studentId);
        return classRepository.save(classEntity);
    }

    /**
     * Change the teacher of a class.
     * @param classId the class ID
     * @param teacherId the new teacher ID
     * @return updated ClassEntity
     */
    public ClassEntity changeTeacher(Long classId, Long teacherId) {
        ClassEntity classEntity = classRepository.findById(classId)
                .orElseThrow(() -> new RuntimeException("Class not found with id: " + classId));
        classEntity.setTeacherId(teacherId);
        return classRepository.save(classEntity);
    }
}