package com.gradeportal.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.control.Tab;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

/**
 * Main Controller for the application window
 * Manages the main tab interface and navigation
 */
public class MainController implements Initializable {
    
    @FXML
    private TabPane mainTabPane;
    
    @FXML
    private Label statusLabel;
    
    @FXML
    private Label timeLabel;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupTabs();
        updateStatusAndTime();
        
        // Update time every minute
        javafx.animation.Timeline timeline = new javafx.animation.Timeline(
            new javafx.animation.KeyFrame(javafx.util.Duration.minutes(1), e -> updateStatusAndTime())
        );
        timeline.setCycleCount(javafx.animation.Timeline.INDEFINITE);
        timeline.play();
    }
    
    /**
     * Set up all tabs with their respective FXML content
     */
    private void setupTabs() {
        try {
            // Dashboard Tab
            Tab dashboardTab = new Tab("Dashboard");
            dashboardTab.setClosable(false);
            AnchorPane dashboardContent = FXMLLoader.load(getClass().getResource("/fxml/Dashboard.fxml"));
            dashboardTab.setContent(dashboardContent);
            
            // Students Tab
            Tab studentsTab = new Tab("Students");
            studentsTab.setClosable(false);
            AnchorPane studentsContent = FXMLLoader.load(getClass().getResource("/fxml/Students.fxml"));
            studentsTab.setContent(studentsContent);
            
            // Marks Tab
            Tab marksTab = new Tab("Marks Entry");
            marksTab.setClosable(false);
            AnchorPane marksContent = FXMLLoader.load(getClass().getResource("/fxml/Marks.fxml"));
            marksTab.setContent(marksContent);
            
            // Results Tab
            Tab resultsTab = new Tab("Results & Reports");
            resultsTab.setClosable(false);
            AnchorPane resultsContent = FXMLLoader.load(getClass().getResource("/fxml/Results.fxml"));
            resultsTab.setContent(resultsContent);
            
            // Subjects Tab
            Tab subjectsTab = new Tab("Subjects");
            subjectsTab.setClosable(false);
            AnchorPane subjectsContent = FXMLLoader.load(getClass().getResource("/fxml/Subjects.fxml"));
            subjectsTab.setContent(subjectsContent);
            
            // Add all tabs to the TabPane
            mainTabPane.getTabs().addAll(dashboardTab, studentsTab, marksTab, resultsTab, subjectsTab);
            
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading FXML files: " + e.getMessage());
        }
    }
    
    /**
     * Update status and time labels
     */
    private void updateStatusAndTime() {
        statusLabel.setText("System Ready");
        timeLabel.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
    }
}