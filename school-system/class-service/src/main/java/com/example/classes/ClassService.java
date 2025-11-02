package com.example.classes;

import com.example.classes.dto.ClassDetailDTO;
import com.example.classes.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ClassService {

    @Autowired
    private ClassRepository classRepository;

    // Inject the WebClient bean we configured earlier.
    @Autowired
    private WebClient userServiceWebClient;

    // This new method will be our primary way of getting detailed class info.
    public ClassDetailDTO getClassDetailsById(Long id) {
        // 1. Get the basic class info from our own database.
        ClassEntity classEntity = classRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Class not found with id: " + id));

        // 2. Make an API call to the user-service to get the teacher's details.
        // The .block() method waits for the network call to complete.
        UserDTO teacher = fetchUserById(classEntity.getTeacherId()).block();

        // 3. Make another API call to get details for all students in the class.
        List<UserDTO> students = Collections.emptyList(); // Default to empty list
        if (classEntity.getStudentIds() != null && !classEntity.getStudentIds().isEmpty()) {
            students = fetchUsersByIds(classEntity.getStudentIds());
        }

        // 4. Assemble the final, rich DTO and return it.
        return new ClassDetailDTO(
                classEntity.getId(),
                classEntity.getName(),
                teacher,
                students
        );
    }

    // Helper method to fetch a single user by their ID from the user-service.
    private Mono<UserDTO> fetchUserById(Long userId) {
        return userServiceWebClient.get()
                .uri("/api/users/{id}", userId) // Calls GET http://user-service:8081/api/users/{id}
                .retrieve()
                .bodyToMono(UserDTO.class)
                // Add error handling for when a user is not found in the other service
                .onErrorResume(error -> Mono.just(new UserDTO(userId, "Unknown User", "", "")));
    }

    // Helper method to fetch a list of users by their IDs.
    private List<UserDTO> fetchUsersByIds(List<Long> userIds) {
        String ids = userIds.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));

        return userServiceWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/users")
                        .queryParam("ids", ids)
                        .build())
                .retrieve()
                .bodyToFlux(UserDTO.class)
                .collectList()
                .block();
    }

    // --- EXISTING METHODS (No changes needed below this line) ---

    public List<ClassEntity> getAllClasses() {
        return classRepository.findAll();
    }

    public Optional<ClassEntity> getClassById(Long id) {
        return classRepository.findById(id);
    }

    public ClassEntity createClass(ClassEntity classEntity) {
        return classRepository.save(classEntity);
    }

    public ClassEntity updateClass(Long id, ClassEntity classDetails) {
        ClassEntity classEntity = classRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Class not found with id: " + id));

        classEntity.setName(classDetails.getName());
        classEntity.setTeacherId(classDetails.getTeacherId());
        classEntity.setStudentIds(classDetails.getStudentIds());
        return classRepository.save(classEntity);
    }

    public void deleteClass(Long id) {
        classRepository.deleteById(id);
    }

    public ClassEntity addStudentToClass(Long classId, Long studentId) {
        ClassEntity classEntity = classRepository.findById(classId)
                .orElseThrow(() -> new RuntimeException("Class not found with id: " + classId));
        if (!classEntity.getStudentIds().contains(studentId)) {
            classEntity.getStudentIds().add(studentId);
        }
        return classRepository.save(classEntity);
    }

    public ClassEntity removeStudentFromClass(Long classId, Long studentId) {
        ClassEntity classEntity = classRepository.findById(classId)
                .orElseThrow(() -> new RuntimeException("Class not found with id: " + classId));
        classEntity.getStudentIds().remove(studentId);
        return classRepository.save(classEntity);
    }

    public ClassEntity changeTeacher(Long classId, Long teacherId) {
        ClassEntity classEntity = classRepository.findById(classId)
                .orElseThrow(() -> new RuntimeException("Class not found with id: " + classId));
        classEntity.setTeacherId(teacherId);
        return classRepository.save(classEntity);
    }
}