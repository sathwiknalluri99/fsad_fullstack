package com.erp.backend.service;

import com.erp.backend.entity.Course;
import com.erp.backend.entity.User;
import com.erp.backend.repository.CourseRepository;
import com.erp.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserRepository userRepository;

    public Course createCourse(Course course) {
        if (courseRepository.existsByCourseCode(course.getCourseCode())) {
            throw new RuntimeException("Course code already exists");
        }
        return courseRepository.save(course);
    }
    
    public Course addCourse(com.erp.backend.dto.CourseDto dto) {
        if (courseRepository.existsByCourseCode(dto.getCourseCode())) {
            throw new IllegalArgumentException("Course code already exists");
        }
        if (dto.getCredits() == null || dto.getCredits() <= 0) {
            throw new IllegalArgumentException("Credits must be greater than 0");
        }
        
        User instructor = userRepository.findByUsername(dto.getInstructorName())
                .orElseThrow(() -> new IllegalArgumentException("Instructor not found"));
                
        Course course = new Course();
        course.setCourseCode(dto.getCourseCode());
        course.setCourseName(dto.getCourseName());
        course.setCredits(dto.getCredits());
        course.setTeacher(instructor);
        course.setCapacity(30);
        
        return courseRepository.save(course);
    }

    public Course updateCourse(Long id, Course courseDetails) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        course.setCourseName(courseDetails.getCourseName());
        course.setDescription(courseDetails.getDescription());
        course.setTeacher(courseDetails.getTeacher());

        return courseRepository.save(course);
    }

    public void deleteCourse(Long id) {
        courseRepository.deleteById(id);
    }

    public Optional<Course> findById(Long id) {
        return courseRepository.findById(id);
    }

    public Optional<Course> findByCourseCode(String courseCode) {
        return courseRepository.findByCourseCode(courseCode);
    }

    public List<Course> findAllCourses() {
        return courseRepository.findAll();
    }

    public List<Course> findCoursesByTeacher(User teacher) {
        return courseRepository.findByTeacher(teacher);
    }

    public List<Course> searchCourses(String searchTerm) {
        return courseRepository.findByNameOrCodeContaining(searchTerm, searchTerm);
    }

    public List<Course> findCoursesByStudent(User student) {
        return courseRepository.findCoursesByStudent(student);
    }
}