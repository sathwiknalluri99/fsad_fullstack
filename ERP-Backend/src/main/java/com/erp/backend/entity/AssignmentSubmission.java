package com.erp.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "assignment_submissions")
public class AssignmentSubmission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assignment_id")
    private Assignment assignment;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id")
    private User student;

    @Column(length = 2000)
    private String submissionText;

    @Column(length = 500)
    private String fileUrl;

    @Enumerated(EnumType.STRING)
    private SubmissionStatus status = SubmissionStatus.SUBMITTED;

    private Integer marks;

    @Column(length = 500)
    private String feedback;

    @Column(name = "submitted_at")
    private LocalDateTime submittedAt;

    @Column(name = "graded_at")
    private LocalDateTime gradedAt;

    public enum SubmissionStatus {
        SUBMITTED, GRADED, LATE
    }

    // Constructors
    public AssignmentSubmission() {}

    public AssignmentSubmission(Assignment assignment, User student, String submissionText, String fileUrl) {
        this.assignment = assignment;
        this.student = student;
        this.submissionText = submissionText;
        this.fileUrl = fileUrl;
        this.submittedAt = LocalDateTime.now();
        if (LocalDateTime.now().isAfter(assignment.getDueDate())) {
            this.status = SubmissionStatus.LATE;
        }
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Assignment getAssignment() { return assignment; }
    public void setAssignment(Assignment assignment) { this.assignment = assignment; }

    public User getStudent() { return student; }
    public void setStudent(User student) { this.student = student; }

    public String getSubmissionText() { return submissionText; }
    public void setSubmissionText(String submissionText) { this.submissionText = submissionText; }

    public String getFileUrl() { return fileUrl; }
    public void setFileUrl(String fileUrl) { this.fileUrl = fileUrl; }

    public SubmissionStatus getStatus() { return status; }
    public void setStatus(SubmissionStatus status) { this.status = status; }

    public Integer getMarks() { return marks; }
    public void setMarks(Integer marks) {
        this.marks = marks;
        this.gradedAt = LocalDateTime.now();
        this.status = SubmissionStatus.GRADED;
    }

    public String getFeedback() { return feedback; }
    public void setFeedback(String feedback) { this.feedback = feedback; }

    public LocalDateTime getSubmittedAt() { return submittedAt; }
    public void setSubmittedAt(LocalDateTime submittedAt) { this.submittedAt = submittedAt; }

    public LocalDateTime getGradedAt() { return gradedAt; }
    public void setGradedAt(LocalDateTime gradedAt) { this.gradedAt = gradedAt; }

    @PrePersist
    protected void onCreate() {
        submittedAt = LocalDateTime.now();
    }
}