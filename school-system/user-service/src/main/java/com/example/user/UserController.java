package main.java.com.example.user;

import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {
  private final UserRepository repo;
  public UserController(UserRepository repo){ this.repo = repo; }

  @GetMapping public List<User> getAll(){ return repo.findAll(); }
  @PostMapping public User addUser(@RequestBody User u){ return repo.save(u); }
}
