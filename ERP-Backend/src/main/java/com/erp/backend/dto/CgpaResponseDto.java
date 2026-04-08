package com.erp.backend.dto;

public class CgpaResponseDto {
    private Long studentId;
    private Double cgpa;

    public CgpaResponseDto() {}

    public CgpaResponseDto(Long studentId, Double cgpa) {
        this.studentId = studentId;
        this.cgpa = cgpa;
    }

    public Long getStudentId() { return studentId; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }

    public Double getCgpa() { return cgpa; }
    public void setCgpa(Double cgpa) { this.cgpa = cgpa; }
}
