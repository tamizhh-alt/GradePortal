-- Grades & Marks Portal Database Setup
-- MySQL Database Schema

-- Create database
CREATE DATABASE IF NOT EXISTS grades_portal_db;
USE grades_portal_db;

-- Create students table
CREATE TABLE IF NOT EXISTS students (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    roll_number VARCHAR(20) UNIQUE NOT NULL,
    class VARCHAR(20) NOT NULL,
    registration_date DATE NOT NULL DEFAULT CURRENT_DATE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Create subjects table
CREATE TABLE IF NOT EXISTS subjects (
    id INT AUTO_INCREMENT PRIMARY KEY,
    subject_name VARCHAR(100) UNIQUE NOT NULL,
    max_marks INT NOT NULL DEFAULT 100,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create marks table
CREATE TABLE IF NOT EXISTS marks (
    id INT AUTO_INCREMENT PRIMARY KEY,
    student_id INT NOT NULL,
    subject_id INT NOT NULL,
    marks_obtained DECIMAL(5,2) NOT NULL,
    grade CHAR(2) NOT NULL,
    entry_date DATE NOT NULL DEFAULT CURRENT_DATE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (student_id) REFERENCES students(id) ON DELETE CASCADE,
    FOREIGN KEY (subject_id) REFERENCES subjects(id) ON DELETE CASCADE,
    UNIQUE KEY unique_student_subject (student_id, subject_id)
);

-- Insert sample subjects
INSERT INTO subjects (subject_name, max_marks) VALUES
('Mathematics', 100),
('English', 100),
('Science', 100),
('History', 100),
('Geography', 100),
('Computer Science', 100),
('Physics', 100),
('Chemistry', 100),
('Biology', 100),
('Literature', 100);

-- Insert sample students
INSERT INTO students (name, roll_number, class) VALUES
('John Smith', 'STU001', 'Grade 10'),
('Emma Johnson', 'STU002', 'Grade 10'),
('Michael Brown', 'STU003', 'Grade 10'),
('Sarah Davis', 'STU004', 'Grade 11'),
('Robert Wilson', 'STU005', 'Grade 11'),
('Lisa Anderson', 'STU006', 'Grade 11'),
('David Martinez', 'STU007', 'Grade 12'),
('Jennifer Taylor', 'STU008', 'Grade 12'),
('Christopher Lee', 'STU009', 'Grade 12'),
('Amanda White', 'STU010', 'Grade 12');

-- Insert sample marks (demonstrating different grade levels)
INSERT INTO marks (student_id, subject_id, marks_obtained, grade) VALUES
-- John Smith (STU001)
(1, 1, 85.5, 'B+'),
(1, 2, 92.0, 'A-'),
(1, 3, 78.5, 'B'),
(1, 4, 88.0, 'B+'),
(1, 5, 91.5, 'A-'),

-- Emma Johnson (STU002)
(2, 1, 95.0, 'A'),
(2, 2, 97.5, 'A+'),
(2, 3, 93.0, 'A-'),
(2, 4, 89.5, 'B+'),
(2, 5, 94.0, 'A'),

-- Michael Brown (STU003)
(3, 1, 72.5, 'B-'),
(3, 2, 79.0, 'B'),
(3, 3, 81.5, 'B'),
(3, 4, 76.0, 'B-'),
(3, 5, 83.0, 'B+'),

-- Sarah Davis (STU004)
(4, 1, 88.5, 'B+'),
(4, 2, 91.0, 'A-'),
(4, 3, 87.0, 'B+'),
(4, 6, 94.5, 'A'),
(4, 7, 89.0, 'B+'),

-- Robert Wilson (STU005)
(5, 1, 76.0, 'B-'),
(5, 2, 82.5, 'B+'),
(5, 6, 90.0, 'A-'),
(5, 7, 85.5, 'B+'),
(5, 8, 87.0, 'B+');

-- Create indexes for better performance
CREATE INDEX idx_students_roll_number ON students(roll_number);
CREATE INDEX idx_students_class ON students(class);
CREATE INDEX idx_marks_student_id ON marks(student_id);
CREATE INDEX idx_marks_subject_id ON marks(subject_id);
CREATE INDEX idx_marks_grade ON marks(grade);

-- Create view for student performance summary
CREATE VIEW student_performance_summary AS
SELECT 
    s.id,
    s.name,
    s.roll_number,
    s.class,
    COUNT(m.id) as subjects_taken,
    ROUND(AVG(m.marks_obtained), 2) as average_marks,
    (SELECT grade FROM marks WHERE student_id = s.id ORDER BY marks_obtained DESC LIMIT 1) as best_grade,
    (SELECT grade FROM marks WHERE student_id = s.id ORDER BY marks_obtained ASC LIMIT 1) as lowest_grade
FROM students s
LEFT JOIN marks m ON s.id = m.student_id
GROUP BY s.id, s.name, s.roll_number, s.class;

-- Grant privileges (adjust as needed for your setup)
-- GRANT ALL PRIVILEGES ON grades_portal_db.* TO 'gradeportal_user'@'localhost' IDENTIFIED BY 'secure_password';
-- FLUSH PRIVILEGES;