package com.example.grade;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "grades")
public class Grade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long studentId;

    @Column(nullable = false)
    private Long classId;

    @Column(nullable = false)
    private String subject;

    // --- THIS IS THE FIX ---
    @Column(name = "\"value\"", nullable = false)
    private Double value;

    @Column(nullable = false)
    private LocalDate date;

    public Grade() {}

    public Grade(Long studentId, Long classId, String subject, Double value, LocalDate date) {
        this.studentId = studentId;
        this.classId = classId;
        this.subject = subject;
        this.value = value;
        this.date = date;
    }

    // Getters and Setters remain the same...
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getStudentId() { return studentId; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }
    public Long getClassId() { return classId; }
    public void setClassId(Long classId) { this.classId = classId; }
    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }
    public Double getValue() { return value; }
    public void setValue(Double value) { this.value = value; }
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    @Override
    public String toString() {
        return "Grade{" +
                "id=" + id +
                ", studentId=" + studentId +
                ", classId=" + classId +
                ", subject='" + subject + '\'' +
                ", value=" + value +
                ", date=" + date +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Grade)) return false;
        Grade grade = (Grade) o;
        return Objects.equals(id, grade.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}