package com.example.user;

import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {
    private final UserRepository repo;

    public UserController(UserRepository repo) { this.repo = repo; }

    @GetMapping
    public List<User> getAll() { return repo.findAll(); }

    @GetMapping("/{id}")
    public User getOne(@PathVariable Long id) {
        return repo.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
    }

    @GetMapping("/role/{role}")
    public List<User> getByRole(@PathVariable String role) {
        User.Role userRole;
        try {
            userRole = User.Role.valueOf(role.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid role: " + role);
        }
        return repo.findByRole(userRole);
    }

    @PostMapping
    public User addUser(@RequestBody User u) { return repo.save(u); }

    @PutMapping("/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody User u) {
        u.setId(id);
        return repo.save(u);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) { repo.deleteById(id); }
}
