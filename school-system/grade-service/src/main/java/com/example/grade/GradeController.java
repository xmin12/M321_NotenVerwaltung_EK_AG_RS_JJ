package com.example.grade;



import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/grades")
@CrossOrigin(origins = "*")
public class GradeController {
  private final GradeRepository repo;
  public GradeController(GradeRepository repo){ this.repo = repo; }

  @GetMapping public List<Grade> getAll(){ return repo.findAll(); }
  @PostMapping public Grade addGrade(@RequestBody Grade g){ return repo.save(g); }
}
