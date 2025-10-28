
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/classes")
@CrossOrigin(origins = "*")
public class ClassController {
  private final ClassRepository repo;
  public ClassController(ClassRepository repo){ this.repo = repo; }

  @GetMapping public List<Class> getAll(){ return repo.findAll(); }
  @PostMapping public Class addClass(@RequestBody Class c){ return repo.save(c); }
}

