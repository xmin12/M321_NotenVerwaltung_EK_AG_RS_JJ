package com.example.stats;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "stats")
public class Stats {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long studentId;

    @Column(nullable = false)
    private String subject;

    @Column(nullable = false)
    private Double highestGrade;

    @Column(nullable = false)
    private Double lowestGrade;

    @Column(nullable = false)
    private Double averageGrade;

    public Stats() {}

    public Stats(Long studentId, String subject, Double highestGrade, Double lowestGrade, Double averageGrade) {
        this.studentId = studentId;
        this.subject = subject;
        this.highestGrade = highestGrade;
        this.lowestGrade = lowestGrade;
        this.averageGrade = averageGrade;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getStudentId() { return studentId; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }

    public Double getHighestGrade() { return highestGrade; }
    public void setHighestGrade(Double highestGrade) { this.highestGrade = highestGrade; }

    public Double getLowestGrade() { return lowestGrade; }
    public void setLowestGrade(Double lowestGrade) { this.lowestGrade = lowestGrade; }

    public Double getAverageGrade() { return averageGrade; }
    public void setAverageGrade(Double averageGrade) { this.averageGrade = averageGrade; }

    @Override
    public String toString() {
        return "Stats{" +
                "id=" + id +
                ", studentId=" + studentId +
                ", subject='" + subject + '\'' +
                ", highestGrade=" + highestGrade +
                ", lowestGrade=" + lowestGrade +
                ", averageGrade=" + averageGrade +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Stats)) return false;
        Stats stats = (Stats) o;
        return Objects.equals(id, stats.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
