package com.gradeportal.util;

/**
 * Utility class for calculating grades based on marks obtained
 * Provides configurable grading scale for educational institutions
 */
public class GradeCalculator {
    
    // Default grading scale - can be made configurable
    private static final double A_PLUS_THRESHOLD = 95.0;
    private static final double A_THRESHOLD = 90.0;
    private static final double A_MINUS_THRESHOLD = 85.0;
    private static final double B_PLUS_THRESHOLD = 80.0;
    private static final double B_THRESHOLD = 75.0;
    private static final double B_MINUS_THRESHOLD = 70.0;
    private static final double C_PLUS_THRESHOLD = 65.0;
    private static final double C_THRESHOLD = 60.0;
    private static final double C_MINUS_THRESHOLD = 55.0;
    private static final double D_THRESHOLD = 50.0;
    
    /**
     * Calculate letter grade based on percentage marks
     * @param marks The marks obtained (percentage)
     * @return Letter grade (A+, A, A-, B+, B, B-, C+, C, C-, D, F)
     */
    public static String calculateGrade(double marks) {
        if (marks >= A_PLUS_THRESHOLD) {
            return "A+";
        } else if (marks >= A_THRESHOLD) {
            return "A";
        } else if (marks >= A_MINUS_THRESHOLD) {
            return "A-";
        } else if (marks >= B_PLUS_THRESHOLD) {
            return "B+";
        } else if (marks >= B_THRESHOLD) {
            return "B";
        } else if (marks >= B_MINUS_THRESHOLD) {
            return "B-";
        } else if (marks >= C_PLUS_THRESHOLD) {
            return "C+";
        } else if (marks >= C_THRESHOLD) {
            return "C";
        } else if (marks >= C_MINUS_THRESHOLD) {
            return "C-";
        } else if (marks >= D_THRESHOLD) {
            return "D";
        } else {
            return "F";
        }
    }
    
    /**
     * Get grade point value for GPA calculations
     * @param grade Letter grade
     * @return Grade point value (4.0 scale)
     */
    public static double getGradePoint(String grade) {
        switch (grade) {
            case "A+": return 4.0;
            case "A": return 4.0;
            case "A-": return 3.7;
            case "B+": return 3.3;
            case "B": return 3.0;
            case "B-": return 2.7;
            case "C+": return 2.3;
            case "C": return 2.0;
            case "C-": return 1.7;
            case "D": return 1.0;
            case "F": return 0.0;
            default: return 0.0;
        }
    }
    
    /**
     * Check if a grade is passing
     * @param grade Letter grade
     * @return true if passing, false otherwise
     */
    public static boolean isPassingGrade(String grade) {
        return !grade.equals("F");
    }
    
    /**
     * Get descriptive text for grade
     * @param grade Letter grade
     * @return Descriptive text
     */
    public static String getGradeDescription(String grade) {
        switch (grade) {
            case "A+": return "Excellent (95-100%)";
            case "A": return "Excellent (90-94%)";
            case "A-": return "Very Good (85-89%)";
            case "B+": return "Good (80-84%)";
            case "B": return "Good (75-79%)";
            case "B-": return "Above Average (70-74%)";
            case "C+": return "Average (65-69%)";
            case "C": return "Average (60-64%)";
            case "C-": return "Below Average (55-59%)";
            case "D": return "Poor (50-54%)";
            case "F": return "Fail (Below 50%)";
            default: return "Unknown Grade";
        }
    }
}