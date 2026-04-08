package com.erp.backend.service;

import com.erp.backend.dto.CgpaResponseDto;
import com.erp.backend.entity.Grades;
import com.erp.backend.entity.User;
import com.erp.backend.repository.GradesRepository;
import com.erp.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class GradesService {

    @Autowired
    private GradesRepository gradesRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Calculate CGPA for a student using the weighted formula:
     * CGPA = SUM(gradePoints × credits) / SUM(credits)
     *
     * Only courses that have been graded (gradePoints != null) are included.
     */
    public CgpaResponseDto calculateCgpa(Long studentId) {
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found with ID: " + studentId));

        List<Grades> allGrades = gradesRepository.findByStudent(student);

        double totalWeightedPoints = 0.0;
        int totalCredits = 0;

        for (Grades grade : allGrades) {
            // Skip courses that haven't been graded yet
            if (grade.getGradePoints() == null || grade.getGradePoints().isEmpty()) {
                continue;
            }

            double gradePoint = Double.parseDouble(grade.getGradePoints());
            int credits = grade.getCourse().getCredits() != null ? grade.getCourse().getCredits() : 3;

            totalWeightedPoints += gradePoint * credits;
            totalCredits += credits;
        }

        double cgpa = totalCredits > 0 ? totalWeightedPoints / totalCredits : 0.0;

        // Round to 2 decimal places
        cgpa = Math.round(cgpa * 100.0) / 100.0;

        return new CgpaResponseDto(studentId, cgpa);
    }
}
