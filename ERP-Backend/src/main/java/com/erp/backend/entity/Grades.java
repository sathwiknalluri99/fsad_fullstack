package com.erp.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "grades")
public class Grades {

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

    private Double midtermMarks;    // Out of 30
    private Double assignmentMarks; // Out of 20
    private Double projectMarks;    // Out of 20
    private Double finalMarks;      // Out of 30
    
    private Double totalMarks;      // Out of 100
    private String letterGrade;     // A, B, C, D, F
    private String gradePoints;     // 4.0, 3.5, etc.

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Constructors
    public Grades() {}

    public Grades(User student, Course course) {
        this.student = student;
        this.course = course;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getStudent() { return student; }
    public void setStudent(User student) { this.student = student; }

    public Course getCourse() { return course; }
    public void setCourse(Course course) { this.course = course; }

    public Double getMidtermMarks() { return midtermMarks; }
    public void setMidtermMarks(Double midtermMarks) { this.midtermMarks = midtermMarks; }

    public Double getAssignmentMarks() { return assignmentMarks; }
    public void setAssignmentMarks(Double assignmentMarks) { this.assignmentMarks = assignmentMarks; }

    public Double getProjectMarks() { return projectMarks; }
    public void setProjectMarks(Double projectMarks) { this.projectMarks = projectMarks; }

    public Double getFinalMarks() { return finalMarks; }
    public void setFinalMarks(Double finalMarks) { this.finalMarks = finalMarks; }

    public Double getTotalMarks() { return totalMarks; }
    public void setTotalMarks(Double totalMarks) { this.totalMarks = totalMarks; }

    public String getLetterGrade() { return letterGrade; }
    public void setLetterGrade(String letterGrade) { this.letterGrade = letterGrade; }

    public String getGradePoints() { return gradePoints; }
    public void setGradePoints(String gradePoints) { this.gradePoints = gradePoints; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    // Helper method to calculate total marks
    public void calculateTotalMarks() {
        double total = 0;
        if (midtermMarks != null) total += midtermMarks;
        if (assignmentMarks != null) total += assignmentMarks;
        if (projectMarks != null) total += projectMarks;
        if (finalMarks != null) total += finalMarks;
        
        this.totalMarks = total;
        calculateLetterGrade();
    }

    // Helper method to calculate letter grade (10-point CGPA scale)
    public void calculateLetterGrade() {
        if (totalMarks == null) return;
        
        if (totalMarks >= 90) {
            this.letterGrade = "A+";
            this.gradePoints = "10.0";
        } else if (totalMarks >= 80) {
            this.letterGrade = "A";
            this.gradePoints = "9.0";
        } else if (totalMarks >= 70) {
            this.letterGrade = "B+";
            this.gradePoints = "8.0";
        } else if (totalMarks >= 60) {
            this.letterGrade = "B";
            this.gradePoints = "7.0";
        } else if (totalMarks >= 50) {
            this.letterGrade = "C";
            this.gradePoints = "6.0";
        } else if (totalMarks >= 40) {
            this.letterGrade = "D";
            this.gradePoints = "4.0";
        } else {
            this.letterGrade = "F";
            this.gradePoints = "0.0";
        }
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
