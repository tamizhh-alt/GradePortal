package com.gradeportal.model;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Model class representing a Mark/Grade entity
 */
public class Mark {
    private int id;
    private int studentId;
    private int subjectId;
    private double marksObtained;
    private String grade;
    private LocalDate entryDate;
    
    // Additional fields for display purposes (not stored in database)
    private String studentName;
    private String subjectName;
    private String rollNumber;
    
    // Constructors
    public Mark() {
        this.entryDate = LocalDate.now();
    }
    
    public Mark(int studentId, int subjectId, double marksObtained, String grade) {
        this();
        this.studentId = studentId;
        this.subjectId = subjectId;
        this.marksObtained = marksObtained;
        this.grade = grade;
    }
    
    public Mark(int id, int studentId, int subjectId, double marksObtained, String grade, LocalDate entryDate) {
        this.id = id;
        this.studentId = studentId;
        this.subjectId = subjectId;
        this.marksObtained = marksObtained;
        this.grade = grade;
        this.entryDate = entryDate;
    }
    
    // Getters and Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public int getStudentId() {
        return studentId;
    }
    
    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }
    
    public int getSubjectId() {
        return subjectId;
    }
    
    public void setSubjectId(int subjectId) {
        this.subjectId = subjectId;
    }
    
    public double getMarksObtained() {
        return marksObtained;
    }
    
    public void setMarksObtained(double marksObtained) {
        this.marksObtained = marksObtained;
    }
    
    public String getGrade() {
        return grade;
    }
    
    public void setGrade(String grade) {
        this.grade = grade;
    }
    
    public LocalDate getEntryDate() {
        return entryDate;
    }
    
    public void setEntryDate(LocalDate entryDate) {
        this.entryDate = entryDate;
    }
    
    // Display fields
    public String getStudentName() {
        return studentName;
    }
    
    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }
    
    public String getSubjectName() {
        return subjectName;
    }
    
    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }
    
    public String getRollNumber() {
        return rollNumber;
    }
    
    public void setRollNumber(String rollNumber) {
        this.rollNumber = rollNumber;
    }
    
    // Utility methods
    @Override
    public String toString() {
        return "Mark{" +
                "id=" + id +
                ", studentId=" + studentId +
                ", subjectId=" + subjectId +
                ", marksObtained=" + marksObtained +
                ", grade='" + grade + '\'' +
                ", entryDate=" + entryDate +
                '}';
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Mark mark = (Mark) o;
        return id == mark.id;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
    /**
     * Validate mark data
     * @return true if all required fields are valid
     */
    public boolean isValid() {
        return studentId > 0 && subjectId > 0 && marksObtained >= 0 && marksObtained <= 100 
               && grade != null && !grade.trim().isEmpty();
    }
    
    /**
     * Get percentage (assuming max marks is 100)
     * @return percentage value
     */
    public double getPercentage() {
        return marksObtained;
    }
}