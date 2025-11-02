package com.example.classes;

import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/classes")
@CrossOrigin(origins = "*")  // erlaubt Zugriff von allen Domains
public class ClassController {

    private final ClassService classService;

    public ClassController(ClassService classService) {
        this.classService = classService;
    }

    @GetMapping
    public List<ClassEntity> getAll() {
        return classService.getAllClasses();
    }

    @GetMapping("/{id}")
    public ClassEntity getOne(@PathVariable Long id) {
        return classService.getClassById(id)
                .orElseThrow(() -> new RuntimeException("Class not found"));
    }

    @PostMapping
    public ClassEntity addClass(@RequestBody ClassEntity c) {
        return classService.createClass(c);
    }

    @PutMapping("/{id}")
    public ClassEntity updateClass(@PathVariable Long id, @RequestBody ClassEntity c) {
        c.setId(id);
        return classService.updateClass(id, c);
    }

    @DeleteMapping("/{id}")
    public void deleteClass(@PathVariable Long id) {
        classService.deleteClass(id);
    }

    @PostMapping("/{classId}/students/{studentId}")
    public ClassEntity addStudent(@PathVariable Long classId, @PathVariable Long studentId) {
        return classService.addStudentToClass(classId, studentId);
    }

    @DeleteMapping("/{classId}/students/{studentId}")
    public ClassEntity removeStudent(@PathVariable Long classId, @PathVariable Long studentId) {
        return classService.removeStudentFromClass(classId, studentId);
    }

    @PutMapping("/{classId}/teacher/{teacherId}")
    public ClassEntity changeTeacher(@PathVariable Long classId, @PathVariable Long teacherId) {
        return classService.changeTeacher(classId, teacherId);
    }
}
