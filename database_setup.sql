-- GradePortal Database Setup Script
-- Run this script in your MySQL database to set up the required tables

-- Create database if it doesn't exist
CREATE DATABASE IF NOT EXISTS grades_portal_db;
USE grades_portal_db;

-- Create users table for authentication
CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role ENUM('admin', 'student') NOT NULL DEFAULT 'student',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create students table
CREATE TABLE IF NOT EXISTS students (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    roll_number VARCHAR(20) UNIQUE NOT NULL,
    class VARCHAR(20) NOT NULL,
    registration_date DATE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create subjects table
CREATE TABLE IF NOT EXISTS subjects (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    code VARCHAR(20) UNIQUE NOT NULL,
    credits INT NOT NULL DEFAULT 3,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create marks table
CREATE TABLE IF NOT EXISTS marks (
    id INT AUTO_INCREMENT PRIMARY KEY,
    student_id INT NOT NULL,
    subject_id INT NOT NULL,
    marks_obtained DECIMAL(5,2) NOT NULL,
    grade VARCHAR(2),
    entry_date DATE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (student_id) REFERENCES students(id) ON DELETE CASCADE,
    FOREIGN KEY (subject_id) REFERENCES subjects(id) ON DELETE CASCADE
);

-- Insert sample admin user
INSERT INTO users (username, password, role) VALUES 
('admin', 'admin123', 'admin');

-- Insert sample students
INSERT INTO students (name, roll_number, class, registration_date) VALUES 
('John Doe', 'ST001', 'Class 10', '2024-01-15'),
('Jane Smith', 'ST002', 'Class 10', '2024-01-15'),
('Mike Johnson', 'ST003', 'Class 11', '2024-01-16'),
('Sarah Wilson', 'ST004', 'Class 11', '2024-01-16');

-- Insert sample subjects
INSERT INTO subjects (name, code, credits) VALUES 
('Mathematics', 'MATH101', 4),
('English', 'ENG101', 3),
('Science', 'SCI101', 4),
('History', 'HIST101', 3),
('Computer Science', 'CS101', 4);

-- Insert sample marks
INSERT INTO marks (student_id, subject_id, marks_obtained, grade, entry_date) VALUES 
(1, 1, 85.5, 'A', '2024-02-01'),
(1, 2, 78.0, 'B+', '2024-02-01'),
(1, 3, 92.0, 'A+', '2024-02-01'),
(2, 1, 76.5, 'B', '2024-02-01'),
(2, 2, 88.0, 'A', '2024-02-01'),
(2, 3, 81.5, 'B+', '2024-02-01'),
(3, 1, 95.0, 'A+', '2024-02-01'),
(3, 2, 82.0, 'B+', '2024-02-01'),
(3, 4, 79.0, 'B', '2024-02-01'),
(4, 1, 87.5, 'A', '2024-02-01'),
(4, 3, 90.0, 'A+', '2024-02-01'),
(4, 5, 85.0, 'A', '2024-02-01');

-- Create student user accounts (linking students to users table)
INSERT INTO users (username, password, role) VALUES 
('ST001', 'student123', 'student'),
('ST002', 'student123', 'student'),
('ST003', 'student123', 'student'),
('ST004', 'student123', 'student');

-- Create indexes for better performance
CREATE INDEX idx_student_roll_number ON students(roll_number);
CREATE INDEX idx_marks_student_subject ON marks(student_id, subject_id);
CREATE INDEX idx_users_username ON users(username);

-- Show created tables
SHOW TABLES;

-- Show sample data
SELECT 'Users Table:' as info;
SELECT username, role FROM users;

SELECT 'Students Table:' as info;
SELECT * FROM students;

SELECT 'Subjects Table:' as info;
SELECT * FROM subjects;

SELECT 'Marks Table:' as info;
SELECT m.*, s.name as subject_name, st.name as student_name 
FROM marks m 
JOIN subjects s ON m.subject_id = s.id 
JOIN students st ON m.student_id = st.id;


