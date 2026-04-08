package com.erp.backend.repository;

import com.erp.backend.entity.Course;
import com.erp.backend.entity.Enrollment;
import com.erp.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    Optional<Enrollment> findByStudentAndCourse(User student, Course course);

    List<Enrollment> findByStudent(User student);

    List<Enrollment> findByCourse(Course course);

    boolean existsByStudentAndCourse(User student, Course course);

    @Query("SELECT e FROM Enrollment e WHERE e.student = :student AND e.status = 'ACTIVE'")
    List<Enrollment> findActiveEnrollmentsByStudent(@Param("student") User student);

    @Query("SELECT e FROM Enrollment e WHERE e.course = :course AND e.status = 'ACTIVE'")
    List<Enrollment> findActiveEnrollmentsByCourse(@Param("course") Course course);

    @Query("SELECT COUNT(e) FROM Enrollment e WHERE e.course = :course AND e.status = 'ACTIVE'")
    Long countActiveEnrollmentsByCourse(@Param("course") Course course);
}