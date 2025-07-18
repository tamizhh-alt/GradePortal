package com.gradeportal.model;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Model class representing a Student entity
 */
public class Student {
    private int id;
    private String name;
    private String rollNumber;
    private String studentClass;
    private LocalDate registrationDate;
    
    // Constructors
    public Student() {
        this.registrationDate = LocalDate.now();
    }
    
    public Student(String name, String rollNumber, String studentClass) {
        this();
        this.name = name;
        this.rollNumber = rollNumber;
        this.studentClass = studentClass;
    }
    
    public Student(int id, String name, String rollNumber, String studentClass, LocalDate registrationDate) {
        this.id = id;
        this.name = name;
        this.rollNumber = rollNumber;
        this.studentClass = studentClass;
        this.registrationDate = registrationDate;
    }
    
    // Getters and Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getRollNumber() {
        return rollNumber;
    }
    
    public void setRollNumber(String rollNumber) {
        this.rollNumber = rollNumber;
    }
    
    public String getStudentClass() {
        return studentClass;
    }
    
    public void setStudentClass(String studentClass) {
        this.studentClass = studentClass;
    }
    
    public LocalDate getRegistrationDate() {
        return registrationDate;
    }
    
    public void setRegistrationDate(LocalDate registrationDate) {
        this.registrationDate = registrationDate;
    }
    
    // Utility methods
    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", rollNumber='" + rollNumber + '\'' +
                ", studentClass='" + studentClass + '\'' +
                ", registrationDate=" + registrationDate +
                '}';
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return id == student.id && Objects.equals(rollNumber, student.rollNumber);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id, rollNumber);
    }
    
    /**
     * Validate student data
     * @return true if all required fields are valid
     */
    public boolean isValid() {
        return name != null && !name.trim().isEmpty() &&
               rollNumber != null && !rollNumber.trim().isEmpty() &&
               studentClass != null && !studentClass.trim().isEmpty();
    }
}