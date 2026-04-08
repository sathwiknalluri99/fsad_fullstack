package com.erp.backend.repository;

import com.erp.backend.entity.Course;
import com.erp.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    Optional<Course> findByCourseCode(String courseCode);

    List<Course> findByTeacher(User teacher);

    boolean existsByCourseCode(String courseCode);

    @Query("SELECT c FROM Course c WHERE c.courseName LIKE %:name% OR c.courseCode LIKE %:code%")
    List<Course> findByNameOrCodeContaining(@Param("name") String name, @Param("code") String code);

    @Query("SELECT c FROM Course c JOIN c.enrollments e WHERE e.student = :student")
    List<Course> findCoursesByStudent(@Param("student") User student);
}