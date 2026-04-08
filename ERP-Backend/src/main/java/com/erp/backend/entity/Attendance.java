package com.erp.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "attendance")
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id")
    private User student;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id")
    private User teacher;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    private Course course;

    @NotNull
    private LocalDate date;

    @Enumerated(EnumType.STRING)
    @NotNull
    private AttendanceStatus status;

    @Column(length = 200)
    private String remarks;

    @Column(name = "marked_at")
    private LocalDateTime markedAt;

    public enum AttendanceStatus {
        PRESENT, ABSENT, LATE, EXCUSED
    }

    // Constructors
    public Attendance() {}

    public Attendance(User student, User teacher, Course course, LocalDate date, AttendanceStatus status, String remarks) {
        this.student = student;
        this.teacher = teacher;
        this.course = course;
        this.date = date;
        this.status = status;
        this.remarks = remarks;
        this.markedAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getStudent() { return student; }
    public void setStudent(User student) { this.student = student; }

    public User getTeacher() { return teacher; }
    public void setTeacher(User teacher) { this.teacher = teacher; }

    public Course getCourse() { return course; }
    public void setCourse(Course course) { this.course = course; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public AttendanceStatus getStatus() { return status; }
    public void setStatus(AttendanceStatus status) { this.status = status; }

    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }

    public LocalDateTime getMarkedAt() { return markedAt; }
    public void setMarkedAt(LocalDateTime markedAt) { this.markedAt = markedAt; }

    @PrePersist
    protected void onCreate() {
        markedAt = LocalDateTime.now();
    }
}