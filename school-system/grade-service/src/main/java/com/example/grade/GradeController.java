package com.example.grade;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/grades")
@CrossOrigin(origins = "*")
public class GradeController {

    private final GradeRepository repo;

    public GradeController(GradeRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<Grade> getAll(@RequestParam(required = false) String subject) {
        if (subject != null && !subject.isEmpty()) {
            return repo.findBySubject(subject);
        }
        return repo.findAll();
    }

    @GetMapping("/{id}")
    public Grade getOne(@PathVariable Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Grade not found"));
    }

    @GetMapping("/student/{studentId}")
    public List<Grade> getByStudent(@PathVariable Long studentId) {
        return repo.findByStudentId(studentId);
    }

    @GetMapping("/class/{classId}")
    public List<Grade> getByClass(@PathVariable Long classId) {
        return repo.findByClassId(classId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED) // <-- FIX: Return 201 Created
    public Grade addGrade(@RequestBody Grade g) {
        return repo.save(g);
    }

    @PutMapping("/{id}")
    public Grade updateGrade(@PathVariable Long id, @RequestBody Grade g) {
        return repo.findById(id)
                .map(existing -> {
                    existing.setStudentId(g.getStudentId());
                    existing.setClassId(g.getClassId());
                    existing.setSubject(g.getSubject());
                    existing.setValue(g.getValue());
                    existing.setDate(g.getDate());
                    return repo.save(existing);
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Grade not found with id " + id)); // <-- FIX: Return 404
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT) // <-- FIX: Return 204 No Content
    public void deleteGrade(@PathVariable Long id) {
        // <-- FIX: Add a check to return 404 if it doesn't exist
        if (!repo.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Grade not found with id " + id);
        }
        repo.deleteById(id);
    }
}