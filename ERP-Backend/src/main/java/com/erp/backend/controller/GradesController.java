package com.erp.backend.controller;

import com.erp.backend.dto.CgpaResponseDto;
import com.erp.backend.entity.Course;
import com.erp.backend.entity.Grades;
import com.erp.backend.entity.User;
import com.erp.backend.repository.CourseRepository;
import com.erp.backend.repository.GradesRepository;
import com.erp.backend.repository.UserRepository;
import com.erp.backend.service.GradesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/grades")
@CrossOrigin(origins = "*")
public class GradesController {

    @Autowired
    private GradesRepository gradesRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private GradesService gradesService;

    // Get current authenticated user
    private User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        return userRepository.findByUsername(username).orElse(null);
    }

    // Get grades for current student
    @GetMapping("/my-grades")
    public ResponseEntity<List<Map<String, Object>>> getMyGrades() {
        User student = getCurrentUser();
        if (student == null) {
            return ResponseEntity.status(401).build();
        }

        List<Grades> grades = gradesRepository.findByStudent(student);
        List<Map<String, Object>> gradesList = new ArrayList<>();

        for (Grades grade : grades) {
            Map<String, Object> gradeInfo = new HashMap<>();
            gradeInfo.put("gradeId", grade.getId());
            gradeInfo.put("courseCode", grade.getCourse().getCourseCode());
            gradeInfo.put("courseName", grade.getCourse().getCourseName());
            gradeInfo.put("midtermMarks", grade.getMidtermMarks());
            gradeInfo.put("assignmentMarks", grade.getAssignmentMarks());
            gradeInfo.put("projectMarks", grade.getProjectMarks());
            gradeInfo.put("finalMarks", grade.getFinalMarks());
            gradeInfo.put("totalMarks", grade.getTotalMarks());
            gradeInfo.put("letterGrade", grade.getLetterGrade());
            gradeInfo.put("gradePoints", grade.getGradePoints());
            gradesList.add(gradeInfo);
        }

        return ResponseEntity.ok(gradesList);
    }

    // Get grades for a specific course (for teachers)
    @GetMapping("/course/{courseId}")
    @PreAuthorize("hasRole('TEACHER') or hasRole('ADMIN')")
    public ResponseEntity<List<Map<String, Object>>> getCourseGrades(@PathVariable Long courseId) {
        Optional<Course> courseOpt = courseRepository.findById(courseId);
        if (!courseOpt.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        List<Grades> grades = gradesRepository.findByCourse(courseOpt.get());
        List<Map<String, Object>> gradesList = new ArrayList<>();

        for (Grades grade : grades) {
            Map<String, Object> gradeInfo = new HashMap<>();
            gradeInfo.put("gradeId", grade.getId());
            gradeInfo.put("studentId", grade.getStudent().getId());
            gradeInfo.put("username", grade.getStudent().getUsername());
            gradeInfo.put("email", grade.getStudent().getEmail());
            gradeInfo.put("midtermMarks", grade.getMidtermMarks());
            gradeInfo.put("assignmentMarks", grade.getAssignmentMarks());
            gradeInfo.put("projectMarks", grade.getProjectMarks());
            gradeInfo.put("finalMarks", grade.getFinalMarks());
            gradeInfo.put("totalMarks", grade.getTotalMarks());
            gradeInfo.put("letterGrade", grade.getLetterGrade());
            gradeInfo.put("gradePoints", grade.getGradePoints());
            gradesList.add(gradeInfo);
        }

        return ResponseEntity.ok(gradesList);
    }

    // Create/initialize grades for a student in a course
    @PostMapping("/course/{courseId}/student/{studentId}")
    @PreAuthorize("hasRole('TEACHER') or hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> createGrades(@PathVariable Long courseId, @PathVariable Long studentId) {
        Optional<Course> courseOpt = courseRepository.findById(courseId);
        Optional<User> studentOpt = userRepository.findById(studentId);

        if (!courseOpt.isPresent() || !studentOpt.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        // Check if grades already exist
        Optional<Grades> existing = gradesRepository.findByStudentAndCourse(studentOpt.get(), courseOpt.get());
        if (existing.isPresent()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Grades already exist for this student in this course"));
        }

        try {
            Grades grades = new Grades(studentOpt.get(), courseOpt.get());
            Grades saved = gradesRepository.save(grades);

            return ResponseEntity.ok(Map.of(
                    "message", "Grades created successfully",
                    "gradeId", saved.getId()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Error creating grades: " + e.getMessage()));
        }
    }

    // Update grades for a student
    @PutMapping("/{gradeId}")
    @PreAuthorize("hasRole('TEACHER') or hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> updateGrades(@PathVariable Long gradeId, @RequestBody Map<String, Double> marks) {
        Optional<Grades> gradesOpt = gradesRepository.findById(gradeId);
        if (!gradesOpt.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        try {
            Grades grades = gradesOpt.get();

            if (marks.containsKey("midtermMarks")) {
                grades.setMidtermMarks(marks.get("midtermMarks"));
            }
            if (marks.containsKey("assignmentMarks")) {
                grades.setAssignmentMarks(marks.get("assignmentMarks"));
            }
            if (marks.containsKey("projectMarks")) {
                grades.setProjectMarks(marks.get("projectMarks"));
            }
            if (marks.containsKey("finalMarks")) {
                grades.setFinalMarks(marks.get("finalMarks"));
            }

            // Calculate total and letter grade
            grades.calculateTotalMarks();

            Grades updated = gradesRepository.save(grades);

            return ResponseEntity.ok(Map.of(
                    "message", "Grades updated successfully",
                    "totalMarks", updated.getTotalMarks(),
                    "letterGrade", updated.getLetterGrade(),
                    "gradePoints", updated.getGradePoints()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Error updating grades: " + e.getMessage()));
        }
    }

    // Get grade summary for student
    @GetMapping("/transcript")
    public ResponseEntity<Map<String, Object>> getTranscript() {
        User student = getCurrentUser();
        if (student == null) {
            return ResponseEntity.status(401).build();
        }

        List<Grades> grades = gradesRepository.findByStudent(student);

        double totalGradePoints = 0;
        int totalCredits = 0;
        int completedCourses = 0;

        for (Grades grade : grades) {
            if (grade.getGradePoints() != null && !grade.getGradePoints().equals("0.0")) {
                double gp = Double.parseDouble(grade.getGradePoints());
                int credits = grade.getCourse().getCredits() != null ? grade.getCourse().getCredits() : 3;
                totalGradePoints += gp * credits;
                totalCredits += credits;
                completedCourses++;
            }
        }

        double gpa = totalCredits > 0 ? totalGradePoints / totalCredits : 0;

        Map<String, Object> transcript = new HashMap<>();
        transcript.put("studentName", student.getUsername());
        transcript.put("totalCourses", grades.size());
        transcript.put("completedCourses", completedCourses);
        transcript.put("totalCredits", totalCredits);
        transcript.put("gpa", String.format("%.2f", gpa));
        transcript.put("grades", grades.stream().map(g -> Map.of(
                "courseCode", g.getCourse().getCourseCode(),
                "courseName", g.getCourse().getCourseName(),
                "credits", g.getCourse().getCredits(),
                "totalMarks", g.getTotalMarks(),
                "letterGrade", g.getLetterGrade(),
                "gradePoints", g.getGradePoints()
        )).collect(Collectors.toList()));

        return ResponseEntity.ok(transcript);
    }

    // CGPA Calculation endpoint
    @GetMapping("/cgpa/{studentId}")
    public ResponseEntity<CgpaResponseDto> getCgpa(@PathVariable Long studentId) {
        CgpaResponseDto cgpaResponse = gradesService.calculateCgpa(studentId);
        return ResponseEntity.ok(cgpaResponse);
    }
}
