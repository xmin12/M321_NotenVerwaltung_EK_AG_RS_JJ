package com.example.classes;

import org.springframework.web.bind.annotation.*;
import java.util.List;

import com.example.classes.ClassEntity;
import com.example.classes.ClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

/**
 * REST controller for managing classes.
 */
@RestController
@RequestMapping("/api/classes")
public class ClassController {

    @Autowired
    private ClassService classService;

    /**
     * Get all classes.
     * @return list of ClassEntity
     */
    @GetMapping
    public List<ClassEntity> getAllClasses() {
        return classService.getAllClasses();
    }

    /**
     * Get a class by its ID.
     * @param classId the class ID
     * @return ResponseEntity with ClassEntity or 404
     */
    @GetMapping("/{id}")
    public ResponseEntity<ClassEntity> getClassById(@PathVariable(value = "id") Long classId) {
        return classService.getClassById(classId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Create a new class.
     * @param classEntity the class entity
     * @return created ClassEntity
     */
    @PostMapping
    public ClassEntity createClass(@RequestBody ClassEntity classEntity) {
        return classService.createClass(classEntity);
    }

    /**
     * Update an existing class.
     * @param classId the class ID
     * @param classDetails the updated class details
     * @return ResponseEntity with updated ClassEntity or 404
     */
    @PutMapping("/{id}")
    public ResponseEntity<ClassEntity> updateClass(@PathVariable(value = "id") Long classId,
                                                   @RequestBody ClassEntity classDetails) {
        try {
            return ResponseEntity.ok(classService.updateClass(classId, classDetails));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Delete a class by its ID.
     * @param classId the class ID
     * @return ResponseEntity with status
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClass(@PathVariable(value = "id") Long classId) {
        classService.deleteClass(classId);
        return ResponseEntity.ok().build();
    }

    /**
     * Add a student to a class.
     * @param classId the class ID
     * @param studentId the student ID
     * @return ResponseEntity with updated ClassEntity or 404
     */
    @PostMapping("/{classId}/students/{studentId}")
    public ResponseEntity<ClassEntity> addStudentToClass(@PathVariable Long classId, @PathVariable Long studentId) {
        try {
            return ResponseEntity.ok(classService.addStudentToClass(classId, studentId));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Remove a student from a class.
     * @param classId the class ID
     * @param studentId the student ID
     * @return ResponseEntity with updated ClassEntity or 404
     */
    @DeleteMapping("/{classId}/students/{studentId}")
    public ResponseEntity<ClassEntity> removeStudentFromClass(@PathVariable Long classId, @PathVariable Long studentId) {
        try {
            return ResponseEntity.ok(classService.removeStudentFromClass(classId, studentId));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Change the teacher of a class.
     * @param classId the class ID
     * @param teacherId the new teacher ID
     * @return ResponseEntity with updated ClassEntity or 404
     */
    @PutMapping("/{classId}/teacher/{teacherId}")
    public ResponseEntity<ClassEntity> changeTeacher(@PathVariable Long classId, @PathVariable Long teacherId) {
        try {
            return ResponseEntity.ok(classService.changeTeacher(classId, teacherId));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
