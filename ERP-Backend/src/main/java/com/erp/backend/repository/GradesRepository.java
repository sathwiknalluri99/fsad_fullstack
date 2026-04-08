package com.erp.backend.repository;

import com.erp.backend.entity.Course;
import com.erp.backend.entity.Grades;
import com.erp.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GradesRepository extends JpaRepository<Grades, Long> {
    List<Grades> findByStudent(User student);
    List<Grades> findByCourse(Course course);
    Optional<Grades> findByStudentAndCourse(User student, Course course);
    List<Grades> findByStudentAndCourseId(User student, Long courseId);
}
