package com.example.stats;


import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/stats")
@CrossOrigin(origins = "*")
public class StatsController {
  private final StatsRepository repo;
  public StatsController(StatsRepository repo){ this.repo = repo; }

  @GetMapping public List<Stats> getAll(){ return repo.findAll(); }
  @PostMapping public Stats addStats(@RequestBody Stats s){ return repo.save(s); }
}
