package com.gradeportal.dao;

import com.gradeportal.model.Subject;
import com.gradeportal.util.DatabaseManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Subject operations
 */
public class SubjectDAO {
    
    /**
     * Add a new subject to the database
     * @param subject Subject object to add
     * @return true if successful, false otherwise
     * @throws SQLException if database error occurs
     */
    public boolean addSubject(Subject subject) throws SQLException {
        String sql = "INSERT INTO subjects (subject_name, max_marks) VALUES (?, ?)";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, subject.getSubjectName());
            pstmt.setInt(2, subject.getMaxMarks());
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                // Get the generated ID
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        subject.setId(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
            
            return false;
        }
    }
    
    /**
     * Update an existing subject
     * @param subject Subject object with updated information
     * @return true if successful, false otherwise
     * @throws SQLException if database error occurs
     */
    public boolean updateSubject(Subject subject) throws SQLException {
        String sql = "UPDATE subjects SET subject_name = ?, max_marks = ? WHERE id = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, subject.getSubjectName());
            pstmt.setInt(2, subject.getMaxMarks());
            pstmt.setInt(3, subject.getId());
            
            return pstmt.executeUpdate() > 0;
        }
    }
    
    /**
     * Delete a subject by ID
     * @param subjectId ID of subject to delete
     * @return true if successful, false otherwise
     * @throws SQLException if database error occurs
     */
    public boolean deleteSubject(int subjectId) throws SQLException {
        String sql = "DELETE FROM subjects WHERE id = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, subjectId);
            return pstmt.executeUpdate() > 0;
        }
    }
    
    /**
     * Get a subject by ID
     * @param subjectId ID of subject to retrieve
     * @return Subject object or null if not found
     * @throws SQLException if database error occurs
     */
    public Subject getSubjectById(int subjectId) throws SQLException {
        String sql = "SELECT * FROM subjects WHERE id = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, subjectId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToSubject(rs);
                }
            }
        }
        
        return null;
    }
    
    /**
     * Get all subjects
     * @return List of all subjects
     * @throws SQLException if database error occurs
     */
    public List<Subject> getAllSubjects() throws SQLException {
        String sql = "SELECT * FROM subjects ORDER BY subject_name";
        List<Subject> subjects = new ArrayList<>();
        
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                subjects.add(mapResultSetToSubject(rs));
            }
        }
        
        return subjects;
    }
    
    /**
     * Check if subject name already exists
     * @param subjectName Subject name to check
     * @param excludeId ID to exclude from check (for updates)
     * @return true if subject name exists, false otherwise
     * @throws SQLException if database error occurs
     */
    public boolean subjectNameExists(String subjectName, int excludeId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM subjects WHERE subject_name = ? AND id != ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, subjectName);
            pstmt.setInt(2, excludeId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        
        return false;
    }
    
    /**
     * Get total number of subjects
     * @return Total count of subjects
     * @throws SQLException if database error occurs
     */
    public int getTotalSubjectCount() throws SQLException {
        String sql = "SELECT COUNT(*) FROM subjects";
        
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        
        return 0;
    }
    
    /**
     * Map ResultSet to Subject object
     * @param rs ResultSet
     * @return Subject object
     * @throws SQLException if database error occurs
     */
    private Subject mapResultSetToSubject(ResultSet rs) throws SQLException {
        Subject subject = new Subject();
        subject.setId(rs.getInt("id"));
        subject.setSubjectName(rs.getString("subject_name"));
        subject.setMaxMarks(rs.getInt("max_marks"));
        return subject;
    }
}