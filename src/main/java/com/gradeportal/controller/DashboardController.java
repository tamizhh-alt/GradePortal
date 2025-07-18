package com.gradeportal.controller;

import com.gradeportal.dao.MarkDAO;
import com.gradeportal.dao.StudentDAO;
import com.gradeportal.dao.SubjectDAO;
import com.gradeportal.util.AlertUtil;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Controller for the Dashboard view
 * Displays key statistics and performance metrics
 */
public class DashboardController implements Initializable {
    
    @FXML
    private Label totalStudentsLabel;
    
    @FXML
    private Label totalSubjectsLabel;
    
    @FXML
    private Label totalMarksLabel;
    
    @FXML
    private ListView<String> topPerformersListView;
    
    @FXML
    private VBox gradeDistributionBox;
    
    private StudentDAO studentDAO;
    private SubjectDAO subjectDAO;
    private MarkDAO markDAO;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        studentDAO = new StudentDAO();
        subjectDAO = new SubjectDAO();
        markDAO = new MarkDAO();
        
        loadDashboardData();
    }
    
    /**
     * Load and display dashboard statistics
     */
    private void loadDashboardData() {
        try {
            // Load basic counts
            int totalStudents = studentDAO.getTotalStudentCount();
            int totalSubjects = subjectDAO.getTotalSubjectCount();
            
            totalStudentsLabel.setText(String.valueOf(totalStudents));
            totalSubjectsLabel.setText(String.valueOf(totalSubjects));
            
            // Load top performers
            loadTopPerformers();
            
            // Load grade distribution
            loadGradeDistribution();
            
        } catch (SQLException e) {
            AlertUtil.showError("Database Error", "Failed to load dashboard data", e.getMessage());
        }
    }
    
    /**
     * Load and display top performing students
     */
    private void loadTopPerformers() {
        try {
            List<Map<String, Object>> topPerformers = markDAO.getTopPerformers(10);
            
            topPerformersListView.getItems().clear();
            
            for (Map<String, Object> performer : topPerformers) {
                String name = (String) performer.get("name");
                String rollNumber = (String) performer.get("rollNumber");
                double averageMarks = (Double) performer.get("averageMarks");
                
                String displayText = String.format("%s (%s) - %.1f%%", name, rollNumber, averageMarks);
                topPerformersListView.getItems().add(displayText);
            }
            
        } catch (SQLException e) {
            AlertUtil.showError("Database Error", "Failed to load top performers", e.getMessage());
        }
    }
    
    /**
     * Load and display grade distribution
     */
    private void loadGradeDistribution() {
        try {
            Map<String, Integer> distribution = markDAO.getGradeDistribution();
            
            gradeDistributionBox.getChildren().clear();
            
            int totalMarks = distribution.values().stream().mapToInt(Integer::intValue).sum();
            totalMarksLabel.setText(String.valueOf(totalMarks));
            
            // Display grade distribution
            for (Map.Entry<String, Integer> entry : distribution.entrySet()) {
                String grade = entry.getKey();
                int count = entry.getValue();
                double percentage = totalMarks > 0 ? (count * 100.0 / totalMarks) : 0;
                
                Label gradeLabel = new Label(String.format("%s: %d (%.1f%%)", grade, count, percentage));
                gradeLabel.getStyleClass().add("grade-distribution-item");
                gradeDistributionBox.getChildren().add(gradeLabel);
            }
            
        } catch (SQLException e) {
            AlertUtil.showError("Database Error", "Failed to load grade distribution", e.getMessage());
        }
    }
    
    /**
     * Refresh dashboard data
     */
    @FXML
    private void handleRefresh() {
        loadDashboardData();
        AlertUtil.showSuccess("Dashboard data refreshed successfully.");
    }
    
}