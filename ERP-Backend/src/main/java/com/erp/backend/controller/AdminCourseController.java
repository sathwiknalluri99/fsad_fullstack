package com.erp.backend.controller;

import com.erp.backend.entity.Course;
import com.erp.backend.entity.Enrollment;
import com.erp.backend.entity.User;
import com.erp.backend.repository.CourseRepository;
import com.erp.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin/courses")
@CrossOrigin(origins = "*")
public class AdminCourseController {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    @Transactional(readOnly = true)
    public ResponseEntity<Map<String, Object>> listCourses() {
        List<Course> courses = courseRepository.findAll();
        List<Map<String, Object>> mapped = courses.stream().map(this::mapCourse).collect(Collectors.toList());
        return ResponseEntity.ok(Map.of("success", true, "data", mapped));
    }

    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> createCourse(@RequestBody Map<String, Object> payload) {
        String courseCode = getString(payload, "code", "courseCode");
        String courseName = getString(payload, "name", "courseName");
        String description = getString(payload, "description");
        String semester = getString(payload, "semester", "Spring");
        String schedule = getString(payload, "schedule");
        String location = getString(payload, "location");
        String prerequisites = getString(payload, "prerequisites");
        String instructorName = getString(payload, "instructor");
        Integer credits = getInteger(payload, "credits", 3);
        Integer capacity = getInteger(payload, "capacity", 30);

        if (courseCode == null || courseCode.isBlank() || courseName == null || courseName.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "error", "Course code and name are required"));
        }
        if (courseRepository.existsByCourseCode(courseCode)) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "error", "Course code already exists"));
        }

        Course course = new Course();
        course.setCourseCode(courseCode);
        course.setCourseName(courseName);
        course.setDescription(description);
        course.setSemester(semester);
        course.setSchedule(schedule);
        course.setLocation(location);
        course.setPrerequisites(prerequisites);
        course.setCredits(credits);
        course.setCapacity(capacity);

        if (instructorName != null && !instructorName.isBlank()) {
            Optional<User> teacher = userRepository.findByUsername(instructorName);
            if (teacher.isEmpty()) {
                var matches = userRepository.findByNameContaining(instructorName);
                if (!matches.isEmpty()) {
                    teacher = Optional.of(matches.get(0));
                }
            }
            teacher.ifPresent(course::setTeacher);
        }

        Course saved = courseRepository.save(course);
        return ResponseEntity.ok(Map.of("success", true, "data", mapCourse(saved)));
    }

    @PostMapping("/update")
    public ResponseEntity<Map<String, Object>> updateCourse(@RequestBody Map<String, Object> payload) {
        Long id = getLong(payload, "id");
        String courseCode = getString(payload, "code", "courseCode");

        Optional<Course> courseOpt = Optional.empty();
        if (id != null) {
            courseOpt = courseRepository.findById(id);
        }
        if (courseOpt.isEmpty() && courseCode != null) {
            courseOpt = courseRepository.findByCourseCode(courseCode);
        }
        if (courseOpt.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "error", "Course not found"));
        }

        Course course = courseOpt.get();
        overwriteIfPresent(payload, "name", course::setCourseName);
        overwriteIfPresent(payload, "courseName", course::setCourseName);
        overwriteIfPresent(payload, "description", course::setDescription);
        overwriteIfPresent(payload, "semester", course::setSemester);
        overwriteIfPresent(payload, "schedule", course::setSchedule);
        overwriteIfPresent(payload, "location", course::setLocation);
        overwriteIfPresent(payload, "prerequisites", course::setPrerequisites);
        if (payload.containsKey("credits")) {
            course.setCredits(getInteger(payload, "credits", course.getCredits()));
        }
        if (payload.containsKey("capacity")) {
            course.setCapacity(getInteger(payload, "capacity", course.getCapacity()));
        }
        String instructorName = getString(payload, "instructor");
        if (instructorName != null && !instructorName.isBlank()) {
            Optional<User> teacher = userRepository.findByUsername(instructorName);
            if (teacher.isEmpty()) {
                var matches = userRepository.findByNameContaining(instructorName);
                if (!matches.isEmpty()) {
                    teacher = Optional.of(matches.get(0));
                }
            }
            teacher.ifPresent(course::setTeacher);
        }

        Course updated = courseRepository.save(course);
        return ResponseEntity.ok(Map.of("success", true, "data", mapCourse(updated)));
    }

    @PostMapping("/delete")
    public ResponseEntity<Map<String, Object>> deleteCourse(@RequestBody Map<String, Object> payload) {
        Long id = getLong(payload, "id");
        String courseCode = getString(payload, "code", "courseCode");

        Optional<Course> courseOpt = Optional.empty();
        if (id != null) {
            courseOpt = courseRepository.findById(id);
        }
        if (courseOpt.isEmpty() && courseCode != null) {
            courseOpt = courseRepository.findByCourseCode(courseCode);
        }
        if (courseOpt.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "error", "Course not found"));
        }

        courseRepository.delete(courseOpt.get());
        return ResponseEntity.ok(Map.of("success", true));
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
            result.put("teacher", Map.of("id", course.getTeacher().getId(), "username", course.getTeacher().getUsername()));
            result.put("instructor", course.getTeacher().getUsername());
        } else {
            result.put("teacher", null);
            result.put("instructor", "TBA");
        }
        return result;
    }

    private String getString(Map<String, Object> payload, String key) {
        return getString(payload, key, null);
    }

    private String getString(Map<String, Object> payload, String key, String defaultValue) {
        Object value = payload.get(key);
        if (value instanceof String s) {
            return s.isBlank() ? defaultValue : s;
        }
        return defaultValue;
    }

    private Integer getInteger(Map<String, Object> payload, String key, Integer defaultValue) {
        Object value = payload.get(key);
        if (value instanceof Integer i) {
            return i;
        }
        if (value instanceof String s && !s.isBlank()) {
            try {
                return Integer.parseInt(s);
            } catch (NumberFormatException ignored) {
            }
        }
        return defaultValue;
    }

    private Long getLong(Map<String, Object> payload, String key) {
        Object value = payload.get(key);
        if (value instanceof Number n) {
            return n.longValue();
        }
        if (value instanceof String s && !s.isBlank()) {
            try {
                return Long.parseLong(s);
            } catch (NumberFormatException ignored) {
            }
        }
        return null;
    }

    private void overwriteIfPresent(Map<String, Object> payload, String key, java.util.function.Consumer<String> setter) {
        String value = getString(payload, key);
        if (value != null) {
            setter.accept(value);
        }
    }
}
