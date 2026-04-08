package com.erp.backend.controller;

import com.erp.backend.entity.Attendance;
import com.erp.backend.entity.Enrollment;
import com.erp.backend.entity.User;
import com.erp.backend.repository.EnrollmentRepository;
import com.erp.backend.repository.UserRepository;
import com.erp.backend.service.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/api/attendance")
@CrossOrigin(origins = "*")
public class AttendanceController {

    @Autowired
    private AttendanceService attendanceService;

    @Autowired
    private UserRepository userRepository;
 
    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private com.erp.backend.repository.CourseRepository courseRepository;

    // Helper: get current authenticated user
    private User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findByUsername(auth.getName()).orElse(null);
    }

    // ─── TEACHER: Mark attendance for a student ───
    @PostMapping
    @PreAuthorize("hasRole('TEACHER') or hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> markAttendance(@RequestBody Map<String, Object> request) {
        try {
            Long studentId = Long.valueOf(request.get("studentId").toString());
            String dateStr = request.get("date").toString();
            String statusStr = request.get("status").toString().toUpperCase();
            String remarks = request.getOrDefault("remarks", "").toString();

            User teacher = getCurrentUser();
            if (teacher == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("message", "Unauthorized"));
            }

            User student = userRepository.findById(studentId)
                    .orElseThrow(() -> new IllegalArgumentException("Student not found with ID: " + studentId));

            LocalDate date = LocalDate.parse(dateStr);
            Attendance.AttendanceStatus status = Attendance.AttendanceStatus.valueOf(statusStr);

            String initialCourseCode = request.get("courseCode") != null ? request.get("courseCode").toString() : null;
            if (initialCourseCode == null && request.get("courseId") != null) {
                initialCourseCode = request.get("courseId").toString();
            }
            final String finalCourseCode = initialCourseCode;

            com.erp.backend.entity.Course course = courseRepository.findByCourseCode(finalCourseCode)
                    .orElseThrow(() -> new IllegalArgumentException("Course not found: " + finalCourseCode));

            Attendance attendance = new Attendance(student, teacher, course, date, status, remarks);
            Attendance saved = attendanceService.markAttendance(attendance);

            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                    "message", "Attendance marked successfully",
                    "attendanceId", saved.getId(),
                    "student", student.getUsername(),
                    "course", course.getCourseName(),
                    "date", saved.getDate().toString(),
                    "status", saved.getStatus().toString()
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    // ─── TEACHER: Mark bulk attendance for multiple students ───
    @PostMapping("/bulk")
    @PreAuthorize("hasRole('TEACHER') or hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> markBulkAttendance(@RequestBody Map<String, Object> request) {
        try {
            User teacher = getCurrentUser();
            if (teacher == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("message", "Unauthorized"));
            }

            String dateStr = request.get("date").toString();
            LocalDate date = LocalDate.parse(dateStr);

            String courseCode = request.get("courseId") != null ? request.get("courseId").toString() : null;
            com.erp.backend.entity.Course course = null;
            if (courseCode != null) {
                course = courseRepository.findByCourseCode(courseCode).orElse(null);
            }

            if (course == null) {
                return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Invalid or missing course code: " + courseCode));
            }

            @SuppressWarnings("unchecked")
            List<Map<String, Object>> records = (List<Map<String, Object>>) request.get("records");

            int marked = 0;
            for (Map<String, Object> record : records) {
                Long studentId = Long.valueOf(record.get("studentId").toString());
                String statusStr = record.get("status").toString().toUpperCase();
                String remarks = record.getOrDefault("remarks", "").toString();

                User student = userRepository.findById(studentId).orElse(null);
                if (student == null) continue;

                Attendance.AttendanceStatus status = Attendance.AttendanceStatus.valueOf(statusStr);
                Attendance attendance = new Attendance(student, teacher, course, date, status, remarks);
                attendanceService.markAttendance(attendance);
                marked++;
            }

            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                    "message", marked + " attendance records saved successfully",
                    "count", marked
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", "Error: " + e.getMessage()));
        }
    }

    // ─── STUDENT: View own attendance ───
    @GetMapping("/my-attendance")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<List<Map<String, Object>>> getMyAttendance() {
        User student = getCurrentUser();
        if (student == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        List<Attendance> records = attendanceService.getAttendanceByStudent(student);
        List<Map<String, Object>> response = new ArrayList<>();

        for (Attendance a : records) {
            Map<String, Object> entry = new HashMap<>();
            entry.put("id", a.getId());
            entry.put("date", a.getDate().toString());
            entry.put("status", a.getStatus().toString());
            entry.put("remarks", a.getRemarks());
            entry.put("markedBy", a.getTeacher().getUsername());
            entry.put("markedAt", a.getMarkedAt() != null ? a.getMarkedAt().toString() : null);
            response.add(entry);
        }

        return ResponseEntity.ok(response);
    }

    // ─── ANY: View attendance by studentId (teachers and admin can use this) ───
    @GetMapping("/{studentId}")
    public ResponseEntity<List<Map<String, Object>>> getStudentAttendance(@PathVariable Long studentId) {
        User student = userRepository.findById(studentId).orElse(null);
        if (student == null) {
            return ResponseEntity.notFound().build();
        }

        List<Attendance> records = attendanceService.getAttendanceByStudent(student);
        List<Map<String, Object>> response = new ArrayList<>();

        for (Attendance a : records) {
            Map<String, Object> entry = new HashMap<>();
            entry.put("id", a.getId());
            entry.put("date", a.getDate().toString());
            entry.put("status", a.getStatus().toString());
            entry.put("remarks", a.getRemarks());
            entry.put("markedBy", a.getTeacher().getUsername());
            entry.put("markedAt", a.getMarkedAt() != null ? a.getMarkedAt().toString() : null);
            response.add(entry);
        }

        return ResponseEntity.ok(response);
    }

    // ─── STUDENT: Get own attendance summary/percentage ───
    @GetMapping("/my-summary")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<Map<String, Object>> getMyAttendanceSummary() {
        User student = getCurrentUser();
        if (student == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        List<Attendance> records = attendanceService.getAttendanceByStudent(student);

        long totalClasses = records.size();
        long presentCount = records.stream()
                .filter(a -> a.getStatus() == Attendance.AttendanceStatus.PRESENT).count();
        long absentCount = records.stream()
                .filter(a -> a.getStatus() == Attendance.AttendanceStatus.ABSENT).count();
        long lateCount = records.stream()
                .filter(a -> a.getStatus() == Attendance.AttendanceStatus.LATE).count();
        long excusedCount = records.stream()
                .filter(a -> a.getStatus() == Attendance.AttendanceStatus.EXCUSED).count();

        double percentage = totalClasses > 0 ? (double) presentCount / totalClasses * 100.0 : 0.0;
        percentage = Math.round(percentage * 100.0) / 100.0;

        Map<String, Object> summary = new HashMap<>();
        summary.put("studentId", student.getId());
        summary.put("studentName", student.getUsername());
        summary.put("totalClasses", totalClasses);
        summary.put("present", presentCount);
        summary.put("absent", absentCount);
        summary.put("late", lateCount);
        summary.put("excused", excusedCount);
        summary.put("attendancePercentage", percentage);

        return ResponseEntity.ok(summary);
    }

    // ─── TEACHER: Get attendance for a specific date ───
    @GetMapping("/date/{date}")
    @PreAuthorize("hasRole('TEACHER') or hasRole('ADMIN')")
    public ResponseEntity<List<Map<String, Object>>> getAttendanceByDate(@PathVariable String date) {
        LocalDate localDate = LocalDate.parse(date);
        List<Attendance> records = attendanceService.getAttendanceByDate(localDate);
        return ResponseEntity.ok(mapAttendanceList(records));
    }

    // ─── TEACHER: Search attendance by date AND course (for editing) ───
    @GetMapping("/search")
    @PreAuthorize("hasRole('TEACHER') or hasRole('ADMIN')")
    public ResponseEntity<List<Map<String, Object>>> searchAttendance(
            @RequestParam String date,
            @RequestParam String courseCode) {
        LocalDate localDate = LocalDate.parse(date);
        List<Attendance> records = attendanceService.getAttendanceByDate(localDate);
        
        // Filter by course code
        final String finalCourseCode = courseCode;
        List<Attendance> filtered = records.stream()
                .filter(a -> a.getCourse() != null && a.getCourse().getCourseCode().equals(finalCourseCode))
                .toList();

        return ResponseEntity.ok(mapAttendanceList(filtered));
    }

    // ─── STUDENT: Get full attendance report grouped by semester ───
    @GetMapping("/my-report")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<Map<String, Map<String, Object>>> getMyReport() {
        User student = getCurrentUser();
        if (student == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        List<Attendance> records = attendanceService.getAttendanceByStudent(student);
        List<Enrollment> enrollments = enrollmentRepository.findByStudent(student);
        
        // Group by semester (using "1" as default for now)
        Map<String, Map<String, Object>> report = new HashMap<>();
        
        Map<String, Object> semester1 = new HashMap<>();
        List<Map<String, Object>> mappedRecords = mapAttendanceList(records);
        
        // Subject-wise stats
        Map<String, Map<String, Object>> subjectStats = new HashMap<>();
        
        // 1. Initialize with all enrolled subjects (set to 0)
        for (Enrollment e : enrollments) {
            String cName = e.getCourse().getCourseName();
            Map<String, Object> stats = new HashMap<>();
            stats.put("present", 0L);
            stats.put("total", 0L);
            stats.put("courseName", cName);
            subjectStats.put(cName, stats);
        }
        
        // 2. Populate from attendance records
        for (Attendance a : records) {
            String cName = a.getCourse() != null ? a.getCourse().getCourseName() : "General";
            Map<String, Object> stats = subjectStats.computeIfAbsent(cName, k -> {
                Map<String, Object> s = new HashMap<>();
                s.put("present", 0L);
                s.put("total", 0L);
                s.put("courseName", cName);
                return s;
            });
            stats.put("total", (long)stats.get("total") + 1);
            if (a.getStatus() == Attendance.AttendanceStatus.PRESENT) {
                stats.put("present", (long)stats.get("present") + 1);
            }
        }
        
        // 3. Calculate percentages
        for (Map<String, Object> s : subjectStats.values()) {
            long p = (long) s.get("present");
            long t = (long) s.get("total");
            double pct = t > 0 ? (double) p / t * 100.0 : 0.0;
            s.put("percentage", Math.round(pct * 10.0) / 10.0);
            s.put("absent", t - p);
        }

        long overallPresent = records.stream().filter(a -> a.getStatus() == Attendance.AttendanceStatus.PRESENT).count();
        long overallTotal = records.size();
        double overallPct = overallTotal > 0 ? (double) overallPresent / overallTotal * 100.0 : 0.0;
        
        semester1.put("totalClasses", overallTotal);
        semester1.put("present", overallPresent);
        semester1.put("absent", overallTotal - overallPresent);
        semester1.put("percentage", Math.round(overallPct * 100.0) / 100.0);
        semester1.put("records", mappedRecords);
        semester1.put("subjects", subjectStats);
        
        report.put("1", semester1);
        
        return ResponseEntity.ok(report);
    }

    private List<Map<String, Object>> mapAttendanceList(List<Attendance> records) {
        List<Map<String, Object>> response = new ArrayList<>();
        for (Attendance a : records) {
            Map<String, Object> entry = new HashMap<>();
            entry.put("id", a.getId());
            entry.put("studentId", a.getStudent().getId());
            entry.put("studentName", a.getStudent().getFirstName() + " " + a.getStudent().getLastName());
            entry.put("courseName", a.getCourse() != null ? a.getCourse().getCourseName() : "General");
            entry.put("date", a.getDate().toString());
            entry.put("status", a.getStatus().toString().toLowerCase());
            entry.put("remarks", a.getRemarks());
            response.add(entry);
        }
        return response;
    }
}
