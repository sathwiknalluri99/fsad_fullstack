package com.erp.backend.service;

import com.erp.backend.entity.Assignment;
import com.erp.backend.entity.Course;
import com.erp.backend.entity.User;
import com.erp.backend.repository.AssignmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AssignmentService {

    @Autowired
    private AssignmentRepository assignmentRepository;

    public Assignment createAssignment(Assignment assignment) {
        return assignmentRepository.save(assignment);
    }

    public Assignment updateAssignment(Long id, Assignment assignmentDetails) {
        Assignment assignment = assignmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Assignment not found"));

        assignment.setTitle(assignmentDetails.getTitle());
        assignment.setDescription(assignmentDetails.getDescription());
        assignment.setDueDate(assignmentDetails.getDueDate());

        return assignmentRepository.save(assignment);
    }

    public void deleteAssignment(Long id) {
        assignmentRepository.deleteById(id);
    }

    public Optional<Assignment> findById(Long id) {
        return assignmentRepository.findById(id);
    }

    public List<Assignment> findAllAssignments() {
        return assignmentRepository.findAll();
    }

    public List<Assignment> findAssignmentsByCourse(Course course) {
        return assignmentRepository.findByCourse(course);
    }

    public List<Assignment> findAssignmentsByTeacher(User teacher) {
        return assignmentRepository.findByTeacher(teacher);
    }

    public List<Assignment> findAssignmentsByCourseAndTeacher(Course course, User teacher) {
        return assignmentRepository.findByCourseAndTeacher(course, teacher);
    }

    public List<Assignment> findUpcomingAssignments() {
        return assignmentRepository.findUpcomingAssignments(LocalDateTime.now());
    }

    public List<Assignment> findOverdueAssignments() {
        return assignmentRepository.findOverdueAssignments(LocalDateTime.now());
    }

    public List<Assignment> findAssignmentsByCourses(List<Course> courses) {
        return assignmentRepository.findAssignmentsByCourses(courses);
    }
}