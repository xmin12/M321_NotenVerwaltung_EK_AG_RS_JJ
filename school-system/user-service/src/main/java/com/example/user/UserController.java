package com.example.user;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {
    private final UserRepository repo;

    public UserController(UserRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<User> getAll() {
        return repo.findAll();
    }

    @GetMapping("/{id}")
    public User getOne(@PathVariable Long id) {
        // Returns HTTP 404 Not Found if the user doesn't exist.
        return repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with id: " + id));
    }

    @GetMapping("/role/{role}")
    public List<User> getByRole(@PathVariable String role) {
        User.Role userRole;
        try {
            userRole = User.Role.valueOf(role.toUpperCase());
        } catch (IllegalArgumentException e) {
            // Returns HTTP 400 Bad Request if the role is invalid.
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid role provided: " + role);
        }
        return repo.findByRole(userRole);
    }

    @PostMapping
    public User addUser(@RequestBody User u) {
        return repo.save(u);
    }

    @PutMapping("/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody User u) {
        // First, check if the user exists to provide a clear 404 if they don't.
        if (!repo.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cannot update non-existent user with id: " + id);
        }
        u.setId(id);
        return repo.save(u);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT) // Return HTTP 204 No Content on successful deletion.
    public void deleteUser(@PathVariable Long id) {
        if (!repo.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cannot delete non-existent user with id: " + id);
        }
        repo.deleteById(id);
    }
}