package com.erp.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "enrollments")
public class Enrollment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id")
    private User student;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    private Course course;

    @Column(name = "enrolled_at")
    private LocalDateTime enrolledAt;

    @Enumerated(EnumType.STRING)
    private EnrollmentStatus status = EnrollmentStatus.ACTIVE;

    public enum EnrollmentStatus {
        ACTIVE, COMPLETED, DROPPED
    }

    // Constructors
    public Enrollment() {}

    public Enrollment(User student, Course course) {
        this.student = student;
        this.course = course;
        this.enrolledAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getStudent() { return student; }
    public void setStudent(User student) { this.student = student; }

    public Course getCourse() { return course; }
    public void setCourse(Course course) { this.course = course; }

    public LocalDateTime getEnrolledAt() { return enrolledAt; }
    public void setEnrolledAt(LocalDateTime enrolledAt) { this.enrolledAt = enrolledAt; }

    public EnrollmentStatus getStatus() { return status; }
    public void setStatus(EnrollmentStatus status) { this.status = status; }

    @PrePersist
    protected void onCreate() {
        enrolledAt = LocalDateTime.now();
    }
}