package com.erp.backend.repository;

import com.erp.backend.entity.Assignment;
import com.erp.backend.entity.Course;
import com.erp.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, Long> {

    List<Assignment> findByCourse(Course course);

    List<Assignment> findByTeacher(User teacher);

    List<Assignment> findByCourseAndTeacher(Course course, User teacher);

    @Query("SELECT a FROM Assignment a WHERE a.dueDate > :now ORDER BY a.dueDate ASC")
    List<Assignment> findUpcomingAssignments(@Param("now") LocalDateTime now);

    @Query("SELECT a FROM Assignment a WHERE a.dueDate < :now ORDER BY a.dueDate DESC")
    List<Assignment> findOverdueAssignments(@Param("now") LocalDateTime now);

    @Query("SELECT a FROM Assignment a WHERE a.course IN :courses")
    List<Assignment> findAssignmentsByCourses(@Param("courses") List<Course> courses);
}