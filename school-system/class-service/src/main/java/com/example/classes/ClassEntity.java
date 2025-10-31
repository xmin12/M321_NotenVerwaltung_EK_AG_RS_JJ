package com.example.classes;

import jakarta.persistence.*;
import java.util.List;

/**
 * Entity representing a school class.
 */
@Entity
@Table(name = "classes")
public class ClassEntity {

    /**
     * Unique identifier for the class.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Name of the class (e.g., "3A", "2B").
     */
    private String name;

    /**
     * Teacher ID associated with the class (from user-service).
     */
    private Long teacherId;

    /**
     * List of student IDs in the class.
     */
    @ElementCollection
    @CollectionTable(name = "class_students", joinColumns = @JoinColumn(name = "class_id"))
    @Column(name = "student_id")
    private List<Long> studentIds;

    /**
     * Gets the class ID.
     * @return class ID
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the class ID.
     * @param id class ID
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the class name.
     * @return class name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the class name.
     * @param name class name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the teacher ID.
     * @return teacher ID
     */
    public Long getTeacherId() {
        return teacherId;
    }

    /**
     * Sets the teacher ID.
     * @param teacherId teacher ID
     */
    public void setTeacherId(Long teacherId) {
        this.teacherId = teacherId;
    }

    /**
     * Gets the list of student IDs.
     * @return list of student IDs
     */
    public List<Long> getStudentIds() {
        return studentIds;
    }

    /**
     * Sets the list of student IDs.
     * @param studentIds list of student IDs
     */
    public void setStudentIds(List<Long> studentIds) {
        this.studentIds = studentIds;
    }
}