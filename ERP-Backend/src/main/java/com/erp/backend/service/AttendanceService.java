package com.erp.backend.service;

import com.erp.backend.entity.Attendance;
import com.erp.backend.entity.User;
import com.erp.backend.repository.AttendanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AttendanceService {

    @Autowired
    private AttendanceRepository attendanceRepository;

    public Attendance markAttendance(Attendance attendance) {
        // Check if attendance already exists for this student, date, and course
        Optional<Attendance> existingAttendance = attendanceRepository
                .findByStudentAndDateAndCourse(attendance.getStudent(), attendance.getDate(), attendance.getCourse());

        if (existingAttendance.isPresent()) {
            // Update existing attendance
            Attendance existing = existingAttendance.get();
            existing.setStatus(attendance.getStatus());
            existing.setRemarks(attendance.getRemarks());
            return attendanceRepository.save(existing);
        } else {
            // Create new attendance record
            return attendanceRepository.save(attendance);
        }
    }

    public List<Attendance> getAttendanceByStudent(User student) {
        return attendanceRepository.findByStudent(student);
    }

    public List<Attendance> getAttendanceByDate(LocalDate date) {
        return attendanceRepository.findAttendanceByDateOrdered(date);
    }

    public List<Attendance> getAttendanceByStudentAndDateRange(User student, LocalDate startDate, LocalDate endDate) {
        return attendanceRepository.findAttendanceByStudentAndDateRange(student, startDate, endDate);
    }

    public Optional<Attendance> getAttendanceByStudentAndDate(User student, LocalDate date) {
        return attendanceRepository.findByStudentAndDate(student, date);
    }

    public double getAttendancePercentage(User student, LocalDate startDate, LocalDate endDate) {
        Long presentDays = attendanceRepository.countPresentDaysByStudent(student);
        Long totalDays = attendanceRepository.countTotalDaysByStudentAndDateRange(student, startDate, endDate);

        if (totalDays == 0) {
            return 0.0;
        }

        return (presentDays.doubleValue() / totalDays.doubleValue()) * 100.0;
    }

    public void deleteAttendance(Long id) {
        attendanceRepository.deleteById(id);
    }

    public Optional<Attendance> findById(Long id) {
        return attendanceRepository.findById(id);
    }
}