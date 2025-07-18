package com.gradeportal.dao;

import com.gradeportal.model.Mark;
import com.gradeportal.util.DatabaseManager;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Data Access Object for Mark operations
 */
public class MarkDAO {
    
    /**
     * Add a new mark to the database
     * @param mark Mark object to add
     * @return true if successful, false otherwise
     * @throws SQLException if database error occurs
     */
    public boolean addMark(Mark mark) throws SQLException {
        String sql = "INSERT INTO marks (student_id, subject_id, marks_obtained, grade, entry_date) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setInt(1, mark.getStudentId());
            pstmt.setInt(2, mark.getSubjectId());
            pstmt.setDouble(3, mark.getMarksObtained());
            pstmt.setString(4, mark.getGrade());
            pstmt.setDate(5, Date.valueOf(mark.getEntryDate()));
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                // Get the generated ID
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        mark.setId(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
            
            return false;
        }
    }
    
    /**
     * Update an existing mark
     * @param mark Mark object with updated information
     * @return true if successful, false otherwise
     * @throws SQLException if database error occurs
     */
    public boolean updateMark(Mark mark) throws SQLException {
        String sql = "UPDATE marks SET marks_obtained = ?, grade = ? WHERE id = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setDouble(1, mark.getMarksObtained());
            pstmt.setString(2, mark.getGrade());
            pstmt.setInt(3, mark.getId());
            
            return pstmt.executeUpdate() > 0;
        }
    }
    
    /**
     * Delete a mark by ID
     * @param markId ID of mark to delete
     * @return true if successful, false otherwise
     * @throws SQLException if database error occurs
     */
    public boolean deleteMark(int markId) throws SQLException {
        String sql = "DELETE FROM marks WHERE id = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, markId);
            return pstmt.executeUpdate() > 0;
        }
    }
    
    /**
     * Get a mark by ID
     * @param markId ID of mark to retrieve
     * @return Mark object or null if not found
     * @throws SQLException if database error occurs
     */
    public Mark getMarkById(int markId) throws SQLException {
        String sql = "SELECT m.*, s.name as student_name, s.roll_number, sub.subject_name " +
                    "FROM marks m " +
                    "JOIN students s ON m.student_id = s.id " +
                    "JOIN subjects sub ON m.subject_id = sub.id " +
                    "WHERE m.id = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, markId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToMark(rs);
                }
            }
        }
        
        return null;
    }
    
    /**
     * Get all marks with student and subject information
     * @return List of all marks
     * @throws SQLException if database error occurs
     */
    public List<Mark> getAllMarks() throws SQLException {
        String sql = "SELECT m.*, s.name as student_name, s.roll_number, sub.subject_name " +
                    "FROM marks m " +
                    "JOIN students s ON m.student_id = s.id " +
                    "JOIN subjects sub ON m.subject_id = sub.id " +
                    "ORDER BY s.name, sub.subject_name";
        
        List<Mark> marks = new ArrayList<>();
        
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                marks.add(mapResultSetToMark(rs));
            }
        }
        
        return marks;
    }
    
    /**
     * Get marks for a specific student
     * @param studentId Student ID
     * @return List of marks for the student
     * @throws SQLException if database error occurs
     */
    public List<Mark> getMarksByStudentId(int studentId) throws SQLException {
        String sql = "SELECT m.*, s.name as student_name, s.roll_number, sub.subject_name " +
                    "FROM marks m " +
                    "JOIN students s ON m.student_id = s.id " +
                    "JOIN subjects sub ON m.subject_id = sub.id " +
                    "WHERE m.student_id = ? " +
                    "ORDER BY sub.subject_name";
        
        List<Mark> marks = new ArrayList<>();
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, studentId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    marks.add(mapResultSetToMark(rs));
                }
            }
        }
        
        return marks;
    }
    
    /**
     * Get marks for a specific subject
     * @param subjectId Subject ID
     * @return List of marks for the subject
     * @throws SQLException if database error occurs
     */
    public List<Mark> getMarksBySubjectId(int subjectId) throws SQLException {
        String sql = "SELECT m.*, s.name as student_name, s.roll_number, sub.subject_name " +
                    "FROM marks m " +
                    "JOIN students s ON m.student_id = s.id " +
                    "JOIN subjects sub ON m.subject_id = sub.id " +
                    "WHERE m.subject_id = ? " +
                    "ORDER BY s.name";
        
        List<Mark> marks = new ArrayList<>();
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, subjectId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    marks.add(mapResultSetToMark(rs));
                }
            }
        }
        
        return marks;
    }
    
    /**
     * Check if mark already exists for student and subject
     * @param studentId Student ID
     * @param subjectId Subject ID
     * @param excludeId ID to exclude from check (for updates)
     * @return true if mark exists, false otherwise
     * @throws SQLException if database error occurs
     */
    public boolean markExists(int studentId, int subjectId, int excludeId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM marks WHERE student_id = ? AND subject_id = ? AND id != ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, studentId);
            pstmt.setInt(2, subjectId);
            pstmt.setInt(3, excludeId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        
        return false;
    }
    
    /**
     * Get average marks for a student
     * @param studentId Student ID
     * @return Average marks
     * @throws SQLException if database error occurs
     */
    public double getAverageMarksByStudentId(int studentId) throws SQLException {
        String sql = "SELECT AVG(marks_obtained) FROM marks WHERE student_id = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, studentId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble(1);
                }
            }
        }
        
        return 0.0;
    }
    
    /**
     * Get class average for a subject
     * @param subjectId Subject ID
     * @return Class average
     * @throws SQLException if database error occurs
     */
    public double getClassAverageBySubjectId(int subjectId) throws SQLException {
        String sql = "SELECT AVG(marks_obtained) FROM marks WHERE subject_id = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, subjectId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble(1);
                }
            }
        }
        
        return 0.0;
    }
    
    /**
     * Get grade distribution
     * @return Map of grade to count
     * @throws SQLException if database error occurs
     */
    public Map<String, Integer> getGradeDistribution() throws SQLException {
        String sql = "SELECT grade, COUNT(*) as count FROM marks GROUP BY grade ORDER BY grade";
        Map<String, Integer> distribution = new HashMap<>();
        
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                distribution.put(rs.getString("grade"), rs.getInt("count"));
            }
        }
        
        return distribution;
    }
    
    /**
     * Get top performers (students with highest average marks)
     * @param limit Number of top performers to return
     * @return List of student IDs and their average marks
     * @throws SQLException if database error occurs
     */
    public List<Map<String, Object>> getTopPerformers(int limit) throws SQLException {
        String sql = "SELECT s.id, s.name, s.roll_number, AVG(m.marks_obtained) as average_marks " +
                    "FROM students s " +
                    "JOIN marks m ON s.id = m.student_id " +
                    "GROUP BY s.id, s.name, s.roll_number " +
                    "ORDER BY average_marks DESC " +
                    "LIMIT ?";
        
        List<Map<String, Object>> topPerformers = new ArrayList<>();
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, limit);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> performer = new HashMap<>();
                    performer.put("id", rs.getInt("id"));
                    performer.put("name", rs.getString("name"));
                    performer.put("rollNumber", rs.getString("roll_number"));
                    performer.put("averageMarks", rs.getDouble("average_marks"));
                    topPerformers.add(performer);
                }
            }
        }
        
        return topPerformers;
    }
    
    /**
     * Map ResultSet to Mark object
     * @param rs ResultSet
     * @return Mark object
     * @throws SQLException if database error occurs
     */
    private Mark mapResultSetToMark(ResultSet rs) throws SQLException {
        Mark mark = new Mark();
        mark.setId(rs.getInt("id"));
        mark.setStudentId(rs.getInt("student_id"));
        mark.setSubjectId(rs.getInt("subject_id"));
        mark.setMarksObtained(rs.getDouble("marks_obtained"));
        mark.setGrade(rs.getString("grade"));
        
        Date entryDate = rs.getDate("entry_date");
        if (entryDate != null) {
            mark.setEntryDate(entryDate.toLocalDate());
        }
        
        // Set display fields if available
        try {
            mark.setStudentName(rs.getString("student_name"));
            mark.setRollNumber(rs.getString("roll_number"));
            mark.setSubjectName(rs.getString("subject_name"));
        } catch (SQLException e) {
            // These fields might not be available in all queries
        }
        
        return mark;
    }
}