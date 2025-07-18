package com.gradeportal.dao;

import com.gradeportal.model.Student;
import com.gradeportal.util.DatabaseManager;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Student operations
 */
public class StudentDAO {
    
    /**
     * Add a new student to the database
     * @param student Student object to add
     * @return true if successful, false otherwise
     * @throws SQLException if database error occurs
     */
    public boolean addStudent(Student student) throws SQLException {
        String sql = "INSERT INTO students (name, roll_number, class, registration_date) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, student.getName());
            pstmt.setString(2, student.getRollNumber());
            pstmt.setString(3, student.getStudentClass());
            pstmt.setDate(4, Date.valueOf(student.getRegistrationDate()));
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                // Get the generated ID
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        student.setId(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
            
            return false;
        }
    }
    
    /**
     * Update an existing student
     * @param student Student object with updated information
     * @return true if successful, false otherwise
     * @throws SQLException if database error occurs
     */
    public boolean updateStudent(Student student) throws SQLException {
        String sql = "UPDATE students SET name = ?, roll_number = ?, class = ? WHERE id = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, student.getName());
            pstmt.setString(2, student.getRollNumber());
            pstmt.setString(3, student.getStudentClass());
            pstmt.setInt(4, student.getId());
            
            return pstmt.executeUpdate() > 0;
        }
    }
    
    /**
     * Delete a student by ID
     * @param studentId ID of student to delete
     * @return true if successful, false otherwise
     * @throws SQLException if database error occurs
     */
    public boolean deleteStudent(int studentId) throws SQLException {
        String sql = "DELETE FROM students WHERE id = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, studentId);
            return pstmt.executeUpdate() > 0;
        }
    }
    
    /**
     * Get a student by ID
     * @param studentId ID of student to retrieve
     * @return Student object or null if not found
     * @throws SQLException if database error occurs
     */
    public Student getStudentById(int studentId) throws SQLException {
        String sql = "SELECT * FROM students WHERE id = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, studentId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToStudent(rs);
                }
            }
        }
        
        return null;
    }
    
    /**
     * Get a student by roll number
     * @param rollNumber Roll number of student to retrieve
     * @return Student object or null if not found
     * @throws SQLException if database error occurs
     */
    public Student getStudentByRollNumber(String rollNumber) throws SQLException {
        String sql = "SELECT * FROM students WHERE roll_number = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, rollNumber);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToStudent(rs);
                }
            }
        }
        
        return null;
    }
    
    /**
     * Get all students
     * @return List of all students
     * @throws SQLException if database error occurs
     */
    public List<Student> getAllStudents() throws SQLException {
        String sql = "SELECT * FROM students ORDER BY name";
        List<Student> students = new ArrayList<>();
        
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                students.add(mapResultSetToStudent(rs));
            }
        }
        
        return students;
    }
    
    /**
     * Get students by class
     * @param studentClass Class to filter by
     * @return List of students in the specified class
     * @throws SQLException if database error occurs
     */
    public List<Student> getStudentsByClass(String studentClass) throws SQLException {
        String sql = "SELECT * FROM students WHERE class = ? ORDER BY name";
        List<Student> students = new ArrayList<>();
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, studentClass);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    students.add(mapResultSetToStudent(rs));
                }
            }
        }
        
        return students;
    }
    
    /**
     * Search students by name or roll number
     * @param searchTerm Search term
     * @return List of matching students
     * @throws SQLException if database error occurs
     */
    public List<Student> searchStudents(String searchTerm) throws SQLException {
        String sql = "SELECT * FROM students WHERE name LIKE ? OR roll_number LIKE ? ORDER BY name";
        List<Student> students = new ArrayList<>();
        String searchPattern = "%" + searchTerm + "%";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, searchPattern);
            pstmt.setString(2, searchPattern);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    students.add(mapResultSetToStudent(rs));
                }
            }
        }
        
        return students;
    }
    
    /**
     * Check if roll number already exists
     * @param rollNumber Roll number to check
     * @param excludeId ID to exclude from check (for updates)
     * @return true if roll number exists, false otherwise
     * @throws SQLException if database error occurs
     */
    public boolean rollNumberExists(String rollNumber, int excludeId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM students WHERE roll_number = ? AND id != ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, rollNumber);
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
     * Get total number of students
     * @return Total count of students
     * @throws SQLException if database error occurs
     */
    public int getTotalStudentCount() throws SQLException {
        String sql = "SELECT COUNT(*) FROM students";
        
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
     * Map ResultSet to Student object
     * @param rs ResultSet
     * @return Student object
     * @throws SQLException if database error occurs
     */
    private Student mapResultSetToStudent(ResultSet rs) throws SQLException {
        Student student = new Student();
        student.setId(rs.getInt("id"));
        student.setName(rs.getString("name"));
        student.setRollNumber(rs.getString("roll_number"));
        student.setStudentClass(rs.getString("class"));
        
        Date regDate = rs.getDate("registration_date");
        if (regDate != null) {
            student.setRegistrationDate(regDate.toLocalDate());
        }
        
        return student;
    }
}