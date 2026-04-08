package com.erp.backend.repository;

import com.erp.backend.entity.Attendance;
import com.erp.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    Optional<Attendance> findByStudentAndDate(User student, LocalDate date);

    Optional<Attendance> findByStudentAndDateAndCourse(User student, LocalDate date, com.erp.backend.entity.Course course);

    List<Attendance> findByStudent(User student);

    List<Attendance> findByTeacher(User teacher);

    List<Attendance> findByDate(LocalDate date);

    List<Attendance> findByStudentAndDateBetween(User student, LocalDate startDate, LocalDate endDate);

    @Query("SELECT a FROM Attendance a WHERE a.student = :student AND a.date >= :startDate AND a.date <= :endDate")
    List<Attendance> findAttendanceByStudentAndDateRange(@Param("student") User student,
                                                        @Param("startDate") LocalDate startDate,
                                                        @Param("endDate") LocalDate endDate);

    @Query("SELECT COUNT(a) FROM Attendance a WHERE a.student = :student AND a.status = 'PRESENT'")
    Long countPresentDaysByStudent(@Param("student") User student);

    @Query("SELECT COUNT(a) FROM Attendance a WHERE a.student = :student AND a.date >= :startDate AND a.date <= :endDate")
    Long countTotalDaysByStudentAndDateRange(@Param("student") User student,
                                            @Param("startDate") LocalDate startDate,
                                            @Param("endDate") LocalDate endDate);

    @Query("SELECT a FROM Attendance a WHERE a.date = :date ORDER BY a.student.lastName, a.student.firstName")
    List<Attendance> findAttendanceByDateOrdered(@Param("date") LocalDate date);
}