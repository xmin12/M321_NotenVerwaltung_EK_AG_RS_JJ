package com.example.classes;

import com.example.classes.dto.ClassDetailDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/classes")
public class ClassController {

    @Autowired
    private ClassService classService;

    /**
     * Get a simple list of all classes.
     * @return A list of ClassEntity objects with only IDs.
     */
    @GetMapping
    public List<ClassEntity> getAllClasses() {
        return classService.getAllClasses();
    }

    /**
     * Get the full, detailed information for a single class,
     * including teacher and student objects.
     * @param id The ID of the class.
     * @return The rich ClassDetailDTO.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ClassDetailDTO> getClassDetailsById(@PathVariable Long id) {
        try {
            ClassDetailDTO classDetails = classService.getClassDetailsById(id);
            return ResponseEntity.ok(classDetails);
        } catch (RuntimeException e) {
            // This will catch the "Class not found" exception from the service
            return ResponseEntity.notFound().build();
        }
    }

    // --- The rest of your POST, PUT, DELETE methods can remain as they were ---
    // They still work with the simple ClassEntity, which is fine for now.

    @PostMapping
    public ClassEntity createClass(@RequestBody ClassEntity classEntity) {
        return classService.createClass(classEntity);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClassEntity> updateClass(@PathVariable(value = "id") Long id,
                                                   @RequestBody ClassEntity classDetails) {
        try {
            return ResponseEntity.ok(classService.updateClass(id, classDetails));
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
