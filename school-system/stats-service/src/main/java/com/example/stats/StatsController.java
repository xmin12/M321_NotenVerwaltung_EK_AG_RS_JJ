package com.example.stats;

import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/stats")
@CrossOrigin(origins = "*")
public class StatsController {

    private final StatsRepository repo;
    private final StatsService statsService;

    public StatsController(StatsRepository repo, StatsService statsService) {
        this.repo = repo;
        this.statsService = statsService;
    }

    @GetMapping
    public List<Stats> getAllStats() {
        return repo.findAll();
    }

    @GetMapping("/student/{studentId}")
    public List<Stats> getStatsByStudent(@PathVariable Long studentId) {
        return repo.findByStudentId(studentId);
    }

    @GetMapping("/subject/{subject}")
    public List<Stats> getStatsBySubject(@PathVariable String subject) {
        return repo.findBySubject(subject);
    }

    @PostMapping("/refresh")
    public List<Stats> refreshStats() {
        repo.deleteAll();
        List<Stats> newStats = statsService.generateStats();
        return repo.saveAll(newStats);
    }
}
