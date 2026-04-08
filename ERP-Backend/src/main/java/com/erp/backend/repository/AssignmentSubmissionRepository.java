package com.erp.backend.repository;

import com.erp.backend.entity.Assignment;
import com.erp.backend.entity.AssignmentSubmission;
import com.erp.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AssignmentSubmissionRepository extends JpaRepository<AssignmentSubmission, Long> {

    Optional<AssignmentSubmission> findByAssignmentAndStudent(Assignment assignment, User student);

    List<AssignmentSubmission> findByAssignment(Assignment assignment);

    List<AssignmentSubmission> findByStudent(User student);

    List<AssignmentSubmission> findByAssignmentOrderBySubmittedAtDesc(Assignment assignment);

    @Query("SELECT s FROM AssignmentSubmission s WHERE s.assignment = :assignment AND s.status = 'GRADED'")
    List<AssignmentSubmission> findGradedSubmissionsByAssignment(@Param("assignment") Assignment assignment);

    @Query("SELECT s FROM AssignmentSubmission s WHERE s.student = :student AND s.status = 'GRADED'")
    List<AssignmentSubmission> findGradedSubmissionsByStudent(@Param("student") User student);

    @Query("SELECT COUNT(s) FROM AssignmentSubmission s WHERE s.assignment = :assignment")
    Long countSubmissionsByAssignment(@Param("assignment") Assignment assignment);

    @Query("SELECT COUNT(s) FROM AssignmentSubmission s WHERE s.assignment = :assignment AND s.status = 'LATE'")
    Long countLateSubmissionsByAssignment(@Param("assignment") Assignment assignment);
}