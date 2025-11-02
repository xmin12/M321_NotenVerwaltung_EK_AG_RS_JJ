package com.example.classes;

import com.example.classes.dto.ClassDetailDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.annotation.PostConstruct;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;
import org.springframework.web.client.RestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// Define nested DTOs for the response, as external DTOs are not provided for modification.
// In a real application, these would typically be separate DTO files in a 'dto' package.
record UserDTO(Long id, String name) {}
record ClassDetailWithUsersDTO(Long id, String name, UserDTO teacher, List<UserDTO> students) {}


@RestController
@RequestMapping("/api/classes")
@CrossOrigin(origins = "*")
public class ClassController {

    @Autowired
    private ClassService classService;

    // Autowire RestTemplate for making calls to the user service
    @Autowired
    private RestTemplate restTemplate;

    // Define a base URL for the user service. In a real application, this would be configurable.
    private static final String USER_SERVICE_BASE_URL = "http://localhost:8080/api/users"; // Assuming user service runs on 8080

    @PostConstruct
    public void printStartupMessage() {
        System.out.println();
        System.out.println("************************************************************");
        System.out.println("****** THE NEW AND IMPROVED CONTROLLER IS NOW RUNNING! ******");
        System.out.println("************************************************************");
        System.out.println();
    }

    /**
     * Get a simple list of all classes.
     */
    @GetMapping
    public List<ClassEntity> getAllClasses() {
        return classService.getAllClasses();
    }

    /**
     * Get the full, detailed information for a single class by its ID,
     * including actual user details for teacher and students.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ClassDetailWithUsersDTO> getClassDetails(@PathVariable Long id) {
        System.out.println(">>>>>>>>> Request received for Class Details for ID: " + id);
        try {
            // 1. Get basic class details from the ClassService
            ClassDetailDTO classDetails = classService.getClassDetailsById(id);

            // 2. Fetch teacher details from the User Service
            UserDTO teacher = null;
            if (classDetails.getTeacherId() != null) {
                try {
                    teacher = restTemplate.getForObject(USER_SERVICE_BASE_URL + "/" + classDetails.getTeacherId(), UserDTO.class);
                } catch (Exception e) {
                    System.err.println("Error fetching teacher details for ID " + classDetails.getTeacherId() + ": " + e.getMessage());
                    // In a production app, you might want to return a specific error or a default user.
                }
            }

            // 3. Fetch student details from the User Service
            List<UserDTO> students = new ArrayList<>();
            if (classDetails.getStudentIds() != null && !classDetails.getStudentIds().isEmpty()) {
                students = classDetails.getStudentIds().stream()
                        .map(studentId -> {
                            try {
                                return restTemplate.getForObject(USER_SERVICE_BASE_URL + "/" + studentId, UserDTO.class);
                            } catch (Exception e) {
                                System.err.println("Error fetching student details for ID " + studentId + ": " + e.getMessage());
                                return null; // Filtered out later
                            }
                        })
                        .filter(java.util.Objects::nonNull) // Filter out any nulls from failed fetches
                        .collect(Collectors.toList());
            }

            // 4. Construct the new DTO with full user objects
            ClassDetailWithUsersDTO classDetailWithUsers = new ClassDetailWithUsersDTO(
                    classDetails.getId(),
                    classDetails.getName(),
                    teacher,
                    students
            );

            return ResponseEntity.ok(classDetailWithUsers);
        } catch (RuntimeException e) {
            System.err.println("Error fetching class details for ID " + id + ": " + e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Create a new class.
     */
    @PostMapping
    public ClassEntity createClass(@RequestBody ClassEntity classEntity) {
        return classService.createClass(classEntity);
    }

    /**
     * Update an existing class by its ID.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ClassEntity> updateClass(@PathVariable(value = "id") Long id,
                                                   @RequestBody ClassEntity classDetails) {
        try {
            return ResponseEntity.ok(classService.updateClass(id, classDetails));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Delete a class by its ID.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClass(@PathVariable(value = "id") Long id) {
        classService.deleteClass(id);
        return ResponseEntity.ok().build();
    }

    /**
     * Add a student to a class.
     */
    @PostMapping("/{classId}/students/{studentId}")
    public ResponseEntity<ClassEntity> addStudentToClass(@PathVariable Long classId, @PathVariable Long studentId) {
        try {
            return ResponseEntity.ok(classService.addStudentToClass(classId, studentId));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Remove a student from a class.
     */
    @DeleteMapping("/{classId}/students/{studentId}")
    public ResponseEntity<ClassEntity> removeStudentFromClass(@PathVariable Long classId, @PathVariable Long studentId) {
        try {
            return ResponseEntity.ok(classService.removeStudentFromClass(classId, studentId));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Change the teacher of a class.
     */
    @PutMapping("/{classId}/teacher/{teacherId}")
    public ResponseEntity<ClassEntity> changeTeacher(@PathVariable Long classId, @PathVariable Long teacherId) {
        try {
            return ResponseEntity.ok(classService.changeTeacher(classId, teacherId));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}

// Configuration class to provide a RestTemplate bean
@Configuration
class RestTemplateConfig {
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}