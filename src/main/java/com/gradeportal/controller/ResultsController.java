package com.gradeportal.controller;

import com.gradeportal.dao.MarkDAO;
import com.gradeportal.dao.StudentDAO;
import com.gradeportal.model.Mark;
import com.gradeportal.model.Student;
import com.gradeportal.util.AlertUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Controller for the Results and Reports view
 */
public class ResultsController implements Initializable {
    
    @FXML
    private ComboBox<Student> studentFilterComboBox;
    
    @FXML
    private TextField searchTextField;
    
    @FXML
    private Button exportButton;
    
    @FXML
    private Button generateReportButton;
    
    @FXML
    private TableView<Mark> resultsTable;
    
    @FXML
    private TableColumn<Mark, String> studentNameColumn;
    
    @FXML
    private TableColumn<Mark, String> rollNumberColumn;
    
    @FXML
    private TableColumn<Mark, String> subjectNameColumn;
    
    @FXML
    private TableColumn<Mark, Double> marksColumn;
    
    @FXML
    private TableColumn<Mark, String> gradeColumn;
    
    @FXML
    private TableColumn<Mark, LocalDate> entryDateColumn;
    
    @FXML
    private TextArea reportTextArea;
    
    private StudentDAO studentDAO;
    private MarkDAO markDAO;
    private ObservableList<Mark> resultsList;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        studentDAO = new StudentDAO();
        markDAO = new MarkDAO();
        resultsList = FXCollections.observableArrayList();
        
        setupTable();
        setupComboBoxes();
        setupEventHandlers();
        loadAllResults();
    }
    
    /**
     * Set up the results table
     */
    private void setupTable() {
        studentNameColumn.setCellValueFactory(new PropertyValueFactory<>("studentName"));
        rollNumberColumn.setCellValueFactory(new PropertyValueFactory<>("rollNumber"));
        subjectNameColumn.setCellValueFactory(new PropertyValueFactory<>("subjectName"));
        marksColumn.setCellValueFactory(new PropertyValueFactory<>("marksObtained"));
        gradeColumn.setCellValueFactory(new PropertyValueFactory<>("grade"));
        entryDateColumn.setCellValueFactory(new PropertyValueFactory<>("entryDate"));
        
        resultsTable.setItems(resultsList);
        
        // Handle table selection for report generation
        resultsTable.getSelectionModel().selectedItemProperty().addListener(
            (observable, oldValue, newValue) -> {
                if (newValue != null) {
                    generateStudentReport(newValue.getStudentId());
                }
            }
        );
    }
    
    /**
     * Set up combo boxes
     */
    private void setupComboBoxes() {
        try {
            // Load students with "All Students" option
            List<Student> students = studentDAO.getAllStudents();
            ObservableList<Student> studentOptions = FXCollections.observableArrayList();
            
            // Add a dummy student for "All Students" option
            Student allStudents = new Student();
            allStudents.setId(-1);
            allStudents.setName("All Students");
            studentOptions.add(allStudents);
            studentOptions.addAll(students);
            
            studentFilterComboBox.setItems(studentOptions);
            studentFilterComboBox.setValue(allStudents);
            
        } catch (SQLException e) {
            AlertUtil.showError("Database Error", "Failed to load students", e.getMessage());
        }
    }
    
    /**
     * Set up event handlers
     */
    private void setupEventHandlers() {
        // Student filter change
        studentFilterComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            filterResults();
        });
        
        // Search functionality
        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            filterResults();
        });
    }
    
    /**
     * Load all results from database
     */
    private void loadAllResults() {
        try {
            List<Mark> marks = markDAO.getAllMarks();
            resultsList.clear();
            resultsList.addAll(marks);
        } catch (SQLException e) {
            AlertUtil.showError("Database Error", "Failed to load results", e.getMessage());
        }
    }
    
    /**
     * Filter results based on selected student and search term
     */
    private void filterResults() {
        try {
            Student selectedStudent = studentFilterComboBox.getValue();
            String searchTerm = searchTextField.getText();
            
            List<Mark> marks;
            
            if (selectedStudent != null && selectedStudent.getId() != -1) {
                // Filter by specific student
                marks = markDAO.getMarksByStudentId(selectedStudent.getId());
            } else {
                // Show all marks
                marks = markDAO.getAllMarks();
            }
            
            // Apply search filter if search term is provided
            if (searchTerm != null && !searchTerm.trim().isEmpty()) {
                marks = marks.stream()
                    .filter(mark -> 
                        mark.getStudentName().toLowerCase().contains(searchTerm.toLowerCase()) ||
                        mark.getRollNumber().toLowerCase().contains(searchTerm.toLowerCase()) ||
                        mark.getSubjectName().toLowerCase().contains(searchTerm.toLowerCase())
                    )
                    .collect(java.util.stream.Collectors.toList());
            }
            
            resultsList.clear();
            resultsList.addAll(marks);
            
        } catch (SQLException e) {
            AlertUtil.showError("Database Error", "Failed to filter results", e.getMessage());
        }
    }
    
    /**
     * Generate student report
     */
    private void generateStudentReport(int studentId) {
        try {
            Student student = studentDAO.getStudentById(studentId);
            List<Mark> marks = markDAO.getMarksByStudentId(studentId);
            
            if (student == null) {
                return;
            }
            
            StringBuilder report = new StringBuilder();
            report.append("═══════════════════════════════════════════════════════════════\n");
            report.append("                    STUDENT ACADEMIC REPORT                    \n");
            report.append("═══════════════════════════════════════════════════════════════\n\n");
            
            report.append("Student Information:\n");
            report.append("-------------------\n");
            report.append(String.format("Name: %s\n", student.getName()));
            report.append(String.format("Roll Number: %s\n", student.getRollNumber()));
            report.append(String.format("Class: %s\n", student.getStudentClass()));
            report.append(String.format("Registration Date: %s\n\n", student.getRegistrationDate()));
            
            if (marks.isEmpty()) {
                report.append("No marks recorded for this student.\n");
            } else {
                report.append("Academic Performance:\n");
                report.append("--------------------\n");
                report.append(String.format("%-20s | %-8s | %-5s | %-12s\n", 
                    "Subject", "Marks", "Grade", "Entry Date"));
                report.append("─────────────────────────────────────────────────────────\n");
                
                double totalMarks = 0;
                int subjectCount = 0;
                
                for (Mark mark : marks) {
                    report.append(String.format("%-20s | %8.1f | %-5s | %-12s\n",
                        mark.getSubjectName(),
                        mark.getMarksObtained(),
                        mark.getGrade(),
                        mark.getEntryDate()));
                    
                    totalMarks += mark.getMarksObtained();
                    subjectCount++;
                }
                
                report.append("─────────────────────────────────────────────────────────\n");
                
                double averageMarks = totalMarks / subjectCount;
                report.append(String.format("Average Marks: %.2f%%\n", averageMarks));
                report.append(String.format("Overall Grade: %s\n", 
                    com.gradeportal.util.GradeCalculator.calculateGrade(averageMarks)));
                report.append(String.format("Subjects Completed: %d\n", subjectCount));
            }
            
            report.append("\n═══════════════════════════════════════════════════════════════\n");
            report.append("Report generated on: ").append(LocalDate.now()).append("\n");
            report.append("Grades & Marks Portal System\n");
            report.append("═══════════════════════════════════════════════════════════════");
            
            reportTextArea.setText(report.toString());
            
        } catch (SQLException e) {
            AlertUtil.showError("Database Error", "Failed to generate report", e.getMessage());
        }
    }
    
    /**
     * Generate comprehensive report for selected student
     */
    @FXML
    private void handleGenerateReport() {
        Student selectedStudent = studentFilterComboBox.getValue();
        
        if (selectedStudent == null || selectedStudent.getId() == -1) {
            AlertUtil.showWarning("Selection Required", "No Student Selected", 
                "Please select a specific student to generate a report.");
            return;
        }
        
        generateStudentReport(selectedStudent.getId());
        AlertUtil.showSuccess("Student report generated successfully.");
    }
    
    /**
     * Export results to CSV file
     */
    @FXML
    private void handleExport() {
        if (resultsList.isEmpty()) {
            AlertUtil.showWarning("No Data", "Nothing to Export", 
                "There are no results to export. Please ensure data is loaded.");
            return;
        }
        
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Export Results");
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("CSV Files", "*.csv"),
            new FileChooser.ExtensionFilter("Text Files", "*.txt")
        );
        fileChooser.setInitialFileName("student_results_" + LocalDate.now() + ".csv");
        
        File file = fileChooser.showSaveDialog(exportButton.getScene().getWindow());
        
        if (file != null) {
            try {
                exportToFile(file);
                AlertUtil.showSuccess("Results exported successfully to: " + file.getAbsolutePath());
            } catch (IOException e) {
                AlertUtil.showError("Export Error", "Failed to export results", e.getMessage());
            }
        }
    }
    
    /**
     * Export results to file
     */
    private void exportToFile(File file) throws IOException {
        try (FileWriter writer = new FileWriter(file)) {
            // Write header
            writer.write("Student Name,Roll Number,Subject,Marks,Grade,Entry Date\n");
            
            // Write data
            for (Mark mark : resultsList) {
                writer.write(String.format("%s,%s,%s,%.1f,%s,%s\n",
                    mark.getStudentName(),
                    mark.getRollNumber(),
                    mark.getSubjectName(),
                    mark.getMarksObtained(),
                    mark.getGrade(),
                    mark.getEntryDate()));
            }
        }
    }
}