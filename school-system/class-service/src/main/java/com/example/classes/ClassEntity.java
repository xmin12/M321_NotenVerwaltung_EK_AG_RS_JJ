package com.example.classes;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "classes")
public class ClassEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name; // e.g., "3A", "2B"

    private Long teacherId; // from user-service

    @ElementCollection
    @CollectionTable(name = "class_students", joinColumns = @JoinColumn(name = "class_id"))
    @Column(name = "student_id")
    private List<Long> studentIds;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(Long teacherId) {
        this.teacherId = teacherId;
    }

    public List<Long> getStudentIds() {
        return studentIds;
    }

    public void setStudentIds(List<Long> studentIds) {
        this.studentIds = studentIds;
    }
}