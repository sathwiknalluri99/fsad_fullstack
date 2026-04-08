package com.erp.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "courses")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "assignments", "enrollments"})
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 100)
    @Column(unique = true)
    private String courseCode;

    @NotBlank
    @Size(max = 200)
    private String courseName;

    @Size(max = 500)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id")
    private User teacher;

    private Integer capacity = 30;
    private Integer credits = 3;
    private String semester = "Spring";
    
    @Size(max = 300)
    private String schedule; // e.g., "Mon & Wed 10:00 AM - 11:30 AM"
    
    @Size(max = 200)
    private String location; // e.g., "Room 101, Building A"
    
    @Size(max = 300)
    private String prerequisites; // e.g., "College Algebra, Intro to Programming"

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Assignment> assignments = new HashSet<>();

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Enrollment> enrollments = new HashSet<>();

    // Constructors
    public Course() {}

    public Course(String courseCode, String courseName, String description, User teacher) {
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.description = description;
        this.teacher = teacher;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCourseCode() { return courseCode; }
    public void setCourseCode(String courseCode) { this.courseCode = courseCode; }

    public String getCourseName() { return courseName; }
    public void setCourseName(String courseName) { this.courseName = courseName; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public User getTeacher() { return teacher; }
    public void setTeacher(User teacher) { this.teacher = teacher; }

    public Integer getCapacity() { return capacity; }
    public void setCapacity(Integer capacity) { this.capacity = capacity; }

    public Integer getCredits() { return credits; }
    public void setCredits(Integer credits) { this.credits = credits; }

    public String getSemester() { return semester; }
    public void setSemester(String semester) { this.semester = semester; }

    public String getSchedule() { return schedule; }
    public void setSchedule(String schedule) { this.schedule = schedule; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getPrerequisites() { return prerequisites; }
    public void setPrerequisites(String prerequisites) { this.prerequisites = prerequisites; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public Set<Assignment> getAssignments() { return assignments; }
    public void setAssignments(Set<Assignment> assignments) { this.assignments = assignments; }

    public Set<Enrollment> getEnrollments() { return enrollments; }
    public void setEnrollments(Set<Enrollment> enrollments) { this.enrollments = enrollments; }

    public int getEnrolledCount() {
        return enrollments.size();
    }

    public boolean isAvailable() {
        return getEnrolledCount() < capacity;
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