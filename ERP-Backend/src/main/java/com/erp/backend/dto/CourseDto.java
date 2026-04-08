package com.erp.backend.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CourseDto {

    private Long id;

    @NotBlank(message = "Course code is mandatory")
    @Size(max = 100)
    private String courseCode;

    @NotBlank(message = "Course name is mandatory")
    @Size(max = 200)
    private String courseName;

    @NotNull(message = "Credits are mandatory")
    @Min(value = 1, message = "Credits must be greater than 0")
    private Integer credits;

    @NotBlank(message = "Instructor name is mandatory")
    private String instructorName;

    public CourseDto() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCourseCode() { return courseCode; }
    public void setCourseCode(String courseCode) { this.courseCode = courseCode; }

    public String getCourseName() { return courseName; }
    public void setCourseName(String courseName) { this.courseName = courseName; }

    public Integer getCredits() { return credits; }
    public void setCredits(Integer credits) { this.credits = credits; }

    public String getInstructorName() { return instructorName; }
    public void setInstructorName(String instructorName) { this.instructorName = instructorName; }
}
