package com.example.classes;

import com.example.classes.dto.ClassDetailDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.annotation.PostConstruct;
import java.util.List;

@RestController
@RequestMapping("/api/classes")
@CrossOrigin(origins = "*")
public class ClassController {

    @Autowired
    private ClassService classService;

    @PostConstruct
    public void printStartupMessage() {
        System.out.println();
        System.out.println("************************************************************");
        System.out.println("****** THE NEW AND IMPROVED CONTROLLER IS NOW RUNNING! ******");
        System.out.println("************************************************************");
        System.out.println();
    }

    /**
     * Get a simple list of all classes.
     */
    @GetMapping
    public List<ClassEntity> getAllClasses() {
        return classService.getAllClasses();
    }

    /**
     * Get the full, detailed information for a single class by its ID.
     * This is the primary endpoint for class details.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ClassDetailDTO> getClassDetails(@PathVariable Long id) {
        System.out.println(">>>>>>>>> Request received for Class Details for ID: " + id);
        try {
            ClassDetailDTO classDetails = classService.getClassDetailsById(id);
            return ResponseEntity.ok(classDetails);
        } catch (RuntimeException e) {
            System.err.println("Error fetching class details for ID " + id + ": " + e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    // --- The rest of your POST, PUT, DELETE methods can stay as they are ---

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

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClass(@PathVariable(value = "id") Long id) {
        classService.deleteClass(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{classId}/students/{studentId}")
    public ResponseEntity<ClassEntity> addStudentToClass(@PathVariable Long classId, @PathVariable Long studentId) {
        try {
            return ResponseEntity.ok(classService.addStudentToClass(classId, studentId));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{classId}/students/{studentId}")
    public ResponseEntity<ClassEntity> removeStudentFromClass(@PathVariable Long classId, @PathVariable Long studentId) {
        try {
            return ResponseEntity.ok(classService.removeStudentFromClass(classId, studentId));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{classId}/teacher/{teacherId}")
    public ResponseEntity<ClassEntity> changeTeacher(@PathVariable Long classId, @PathVariable Long teacherId) {
        try {
            return ResponseEntity.ok(classService.changeTeacher(classId, teacherId));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}