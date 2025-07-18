package com.gradeportal.model;

import java.util.Objects;

/**
 * Model class representing a Subject entity
 */
public class Subject {
    private int id;
    private String subjectName;
    private int maxMarks;
    
    // Constructors
    public Subject() {
        this.maxMarks = 100; // Default max marks
    }
    
    public Subject(String subjectName) {
        this();
        this.subjectName = subjectName;
    }
    
    public Subject(String subjectName, int maxMarks) {
        this.subjectName = subjectName;
        this.maxMarks = maxMarks;
    }
    
    public Subject(int id, String subjectName, int maxMarks) {
        this.id = id;
        this.subjectName = subjectName;
        this.maxMarks = maxMarks;
    }
    
    // Getters and Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getSubjectName() {
        return subjectName;
    }
    
    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }
    
    public int getMaxMarks() {
        return maxMarks;
    }
    
    public void setMaxMarks(int maxMarks) {
        this.maxMarks = maxMarks;
    }
    
    // Utility methods
    @Override
    public String toString() {
        return subjectName + " (Max: " + maxMarks + ")";
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Subject subject = (Subject) o;
        return id == subject.id && Objects.equals(subjectName, subject.subjectName);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id, subjectName);
    }
    
    /**
     * Validate subject data
     * @return true if all required fields are valid
     */
    public boolean isValid() {
        return subjectName != null && !subjectName.trim().isEmpty() && maxMarks > 0;
    }
}