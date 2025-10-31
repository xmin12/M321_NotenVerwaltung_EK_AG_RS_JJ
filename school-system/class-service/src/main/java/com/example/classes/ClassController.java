package com.example.classes;

import org.springframework.web.bind.annotation.*;
import java.util.List;

import com.example.classes.ClassEntity;
import com.example.classes.ClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/classes")
public class ClassController {

    @Autowired
    private ClassService classService;

    @GetMapping
    public List<ClassEntity> getAllClasses() {
        return classService.getAllClasses();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClassEntity> getClassById(@PathVariable(value = "id") Long classId) {
        return classService.getClassById(classId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ClassEntity createClass(@RequestBody ClassEntity classEntity) {
        return classService.createClass(classEntity);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClassEntity> updateClass(@PathVariable(value = "id") Long classId,
                                                   @RequestBody ClassEntity classDetails) {
        try {
            return ResponseEntity.ok(classService.updateClass(classId, classDetails));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClass(@PathVariable(value = "id") Long classId) {
        classService.deleteClass(classId);
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
