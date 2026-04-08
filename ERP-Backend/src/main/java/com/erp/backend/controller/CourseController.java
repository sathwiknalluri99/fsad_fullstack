package com.erp.backend.controller;

import com.erp.backend.entity.Course;
import com.erp.backend.entity.Enrollment;
import com.erp.backend.entity.User;
import com.erp.backend.repository.CourseRepository;
import com.erp.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/courses")
@CrossOrigin(origins = "*")
public class CourseController {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserRepository userRepository;

    // Get all available courses (for students to browse)
    @GetMapping
    @Transactional(readOnly = true)
    public ResponseEntity<List<Map<String, Object>>> getAllCourses() {
        List<Course> courses = courseRepository.findAll();
        List<Map<String, Object>> mapped = courses.stream()
                .map(this::mapCourse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(mapped);
    }

    // Get course by ID
    @GetMapping("/{id}")
    @Transactional(readOnly = true)
    public ResponseEntity<Map<String, Object>> getCourseById(@PathVariable Long id) {
        Optional<Course> course = courseRepository.findById(id);
        return course.map(value -> ResponseEntity.ok(mapCourse(value)))
                .orElse(ResponseEntity.notFound().build());
    }

    // Get courses taught by a teacher
    @GetMapping("/teacher/{teacherId}")
    @Transactional(readOnly = true)
    public ResponseEntity<List<Map<String, Object>>> getCoursesByTeacher(@PathVariable Long teacherId) {
        Optional<User> teacher = userRepository.findById(teacherId);
        if (teacher.isPresent()) {
            List<Course> courses = courseRepository.findByTeacher(teacher.get());
            List<Map<String, Object>> mapped = courses.stream()
                    .map(this::mapCourse)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(mapped);
        }
        return ResponseEntity.notFound().build();
    }

    // Create a new course (Admin only)
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> createCourse(@RequestBody Course course) {
        try {
            // Check if course code already exists
            if (courseRepository.existsByCourseCode(course.getCourseCode())) {
                return ResponseEntity.badRequest()
                        .body(Map.of("message", "Course code already exists"));
            }

            Course saved = courseRepository.save(course);
            return ResponseEntity.ok(Map.of(
                    "message", "Course created successfully",
                    "course", saved
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Error creating course: " + e.getMessage()));
        }
    }

    // Update course (Admin or Instructor only)
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<Map<String, Object>> updateCourse(@PathVariable Long id, @RequestBody Course courseDetails) {
        try {
            Optional<Course> courseOpt = courseRepository.findById(id);
            if (!courseOpt.isPresent()) {
                return ResponseEntity.notFound().build();
            }

            Course course = courseOpt.get();
            
            if (courseDetails.getCourseName() != null) {
                course.setCourseName(courseDetails.getCourseName());
            }
            if (courseDetails.getDescription() != null) {
                course.setDescription(courseDetails.getDescription());
            }
            if (courseDetails.getCapacity() != null) {
                course.setCapacity(courseDetails.getCapacity());
            }
            if (courseDetails.getCredits() != null) {
                course.setCredits(courseDetails.getCredits());
            }
            if (courseDetails.getSemester() != null) {
                course.setSemester(courseDetails.getSemester());
            }
            if (courseDetails.getSchedule() != null) {
                course.setSchedule(courseDetails.getSchedule());
            }
            if (courseDetails.getLocation() != null) {
                course.setLocation(courseDetails.getLocation());
            }
            if (courseDetails.getPrerequisites() != null) {
                course.setPrerequisites(courseDetails.getPrerequisites());
            }

            Course updated = courseRepository.save(course);
            return ResponseEntity.ok(Map.of(
                    "message", "Course updated successfully",
                    "course", updated
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Error updating course: " + e.getMessage()));
        }
    }

    // Delete course (Admin only)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, String>> deleteCourse(@PathVariable Long id) {
        try {
            if (!courseRepository.existsById(id)) {
                return ResponseEntity.notFound().build();
            }
            courseRepository.deleteById(id);
            return ResponseEntity.ok(Map.of("message", "Course deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Error deleting course: " + e.getMessage()));
        }
    }

    // Search courses by name or code
    @GetMapping("/search")
    @Transactional(readOnly = true)
    public ResponseEntity<List<Map<String, Object>>> searchCourses(@RequestParam String query) {
        List<Course> courses = courseRepository.findByNameOrCodeContaining(query, query);
        List<Map<String, Object>> mapped = courses.stream()
                .map(this::mapCourse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(mapped);
    }

    // Get course details including enrollment info
    @GetMapping("/{id}/details")
    public ResponseEntity<Map<String, Object>> getCourseDetails(@PathVariable Long id) {
        Optional<Course> courseOpt = courseRepository.findById(id);
        if (!courseOpt.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Course course = courseOpt.get();
        Map<String, Object> details = new HashMap<>();
        details.put("id", course.getId());
        details.put("courseCode", course.getCourseCode());
        details.put("courseName", course.getCourseName());
        details.put("description", course.getDescription());
        details.put("capacity", course.getCapacity());
        details.put("enrolledCount", course.getEnrolledCount());
        details.put("availableSeats", course.getCapacity() - course.getEnrolledCount());
        details.put("credits", course.getCredits());
        details.put("semester", course.getSemester());
        details.put("schedule", course.getSchedule());
        details.put("location", course.getLocation());
        details.put("prerequisites", course.getPrerequisites());
        details.put("isAvailable", course.isAvailable());
        if (course.getTeacher() != null) {
            details.put("instructor", course.getTeacher().getUsername());
        }

        return ResponseEntity.ok(details);
    }

    private Map<String, Object> mapCourse(Course course) {
        Map<String, Object> result = new HashMap<>();
        result.put("id", course.getId());
        result.put("courseCode", course.getCourseCode());
        result.put("courseName", course.getCourseName());
        result.put("description", course.getDescription());
        result.put("capacity", course.getCapacity());
        result.put("credits", course.getCredits());
        result.put("semester", course.getSemester());
        result.put("schedule", course.getSchedule());
        result.put("location", course.getLocation());
        result.put("prerequisites", course.getPrerequisites());
        result.put("enrollments", course.getEnrollments().stream().map(Enrollment::getId).collect(Collectors.toList()));
        result.put("enrolledCount", course.getEnrolledCount());
        result.put("availableSeats", Math.max(0, course.getCapacity() - course.getEnrolledCount()));
        result.put("isAvailable", course.isAvailable());

        if (course.getTeacher() != null) {
            result.put("teacher", Map.of(
                    "id", course.getTeacher().getId(),
                    "username", course.getTeacher().getUsername()
            ));
            result.put("instructor", course.getTeacher().getUsername());
        } else {
            result.put("teacher", null);
            result.put("instructor", "TBA");
        }

        return result;
    }
}
