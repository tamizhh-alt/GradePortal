package com.gradeportal.controller;

import com.gradeportal.dao.MarkDAO;
import com.gradeportal.dao.StudentDAO;
import com.gradeportal.dao.SubjectDAO;
import com.gradeportal.model.Mark;
import com.gradeportal.model.Student;
import com.gradeportal.model.Subject;
import com.gradeportal.util.AlertUtil;
import com.gradeportal.util.GradeCalculator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Controller for the Marks Entry view
 */
public class MarksController implements Initializable {
    
    @FXML
    private ComboBox<Student> studentComboBox;
    
    @FXML
    private ComboBox<Subject> subjectComboBox;
    
    @FXML
    private TextField marksTextField;
    
    @FXML
    private Label gradeLabel;
    
    @FXML
    private DatePicker entryDatePicker;
    
    @FXML
    private Button addButton;
    
    @FXML
    private Button updateButton;
    
    @FXML
    private Button deleteButton;
    
    @FXML
    private Button clearButton;
    
    @FXML
    private TableView<Mark> marksTable;
    
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
    
    private StudentDAO studentDAO;
    private SubjectDAO subjectDAO;
    private MarkDAO markDAO;
    private ObservableList<Mark> marksList;
    private Mark selectedMark;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        studentDAO = new StudentDAO();
        subjectDAO = new SubjectDAO();
        markDAO = new MarkDAO();
        marksList = FXCollections.observableArrayList();
        
        setupTable();
        setupComboBoxes();
        setupEventHandlers();
        loadAllMarks();
        
        // Set default entry date
        entryDatePicker.setValue(LocalDate.now());
        
        // Initially disable update and delete buttons
        updateButton.setDisable(true);
        deleteButton.setDisable(true);
    }
    
    /**
     * Set up the marks table
     */
    private void setupTable() {
        studentNameColumn.setCellValueFactory(new PropertyValueFactory<>("studentName"));
        rollNumberColumn.setCellValueFactory(new PropertyValueFactory<>("rollNumber"));
        subjectNameColumn.setCellValueFactory(new PropertyValueFactory<>("subjectName"));
        marksColumn.setCellValueFactory(new PropertyValueFactory<>("marksObtained"));
        gradeColumn.setCellValueFactory(new PropertyValueFactory<>("grade"));
        entryDateColumn.setCellValueFactory(new PropertyValueFactory<>("entryDate"));
        
        marksTable.setItems(marksList);
        
        // Handle table selection
        marksTable.getSelectionModel().selectedItemProperty().addListener(
            (observable, oldValue, newValue) -> {
                selectedMark = newValue;
                populateForm(newValue);
                updateButtonStates();
            }
        );
    }
    
    /**
     * Set up combo boxes
     */
    private void setupComboBoxes() {
        try {
            // Load students
            List<Student> students = studentDAO.getAllStudents();
            studentComboBox.setItems(FXCollections.observableArrayList(students));
            
            // Load subjects
            List<Subject> subjects = subjectDAO.getAllSubjects();
            subjectComboBox.setItems(FXCollections.observableArrayList(subjects));
            
        } catch (SQLException e) {
            AlertUtil.showError("Database Error", "Failed to load data", e.getMessage());
        }
    }
    
    /**
     * Set up event handlers
     */
    private void setupEventHandlers() {
        // Auto-calculate grade when marks change
        marksTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            updateGradeLabel();
        });
    }
    
    /**
     * Update grade label based on marks entered
     */
    private void updateGradeLabel() {
        try {
            String marksText = marksTextField.getText().trim();
            if (!marksText.isEmpty()) {
                double marks = Double.parseDouble(marksText);
                if (marks >= 0 && marks <= 100) {
                    String grade = GradeCalculator.calculateGrade(marks);
                    gradeLabel.setText(grade + " - " + GradeCalculator.getGradeDescription(grade));
                    gradeLabel.setStyle("-fx-text-fill: #2e8b57;");
                } else {
                    gradeLabel.setText("Invalid marks (0-100)");
                    gradeLabel.setStyle("-fx-text-fill: #dc143c;");
                }
            } else {
                gradeLabel.setText("Enter marks to see grade");
                gradeLabel.setStyle("-fx-text-fill: #666666;");
            }
        } catch (NumberFormatException e) {
            gradeLabel.setText("Invalid number format");
            gradeLabel.setStyle("-fx-text-fill: #dc143c;");
        }
    }
    
    /**
     * Load all marks from database
     */
    private void loadAllMarks() {
        try {
            List<Mark> marks = markDAO.getAllMarks();
            marksList.clear();
            marksList.addAll(marks);
        } catch (SQLException e) {
            AlertUtil.showError("Database Error", "Failed to load marks", e.getMessage());
        }
    }
    
    /**
     * Populate form with selected mark data
     */
    private void populateForm(Mark mark) {
        if (mark != null) {
            // Find and select student
            for (Student student : studentComboBox.getItems()) {
                if (student.getId() == mark.getStudentId()) {
                    studentComboBox.setValue(student);
                    break;
                }
            }
            
            // Find and select subject
            for (Subject subject : subjectComboBox.getItems()) {
                if (subject.getId() == mark.getSubjectId()) {
                    subjectComboBox.setValue(subject);
                    break;
                }
            }
            
            marksTextField.setText(String.valueOf(mark.getMarksObtained()));
            entryDatePicker.setValue(mark.getEntryDate());
            updateGradeLabel();
        }
    }
    
    /**
     * Update button states based on selection
     */
    private void updateButtonStates() {
        boolean hasSelection = selectedMark != null;
        updateButton.setDisable(!hasSelection);
        deleteButton.setDisable(!hasSelection);
    }
    
    /**
     * Clear form fields
     */
    @FXML
    private void handleClear() {
        studentComboBox.setValue(null);
        subjectComboBox.setValue(null);
        marksTextField.clear();
        entryDatePicker.setValue(LocalDate.now());
        gradeLabel.setText("Enter marks to see grade");
        gradeLabel.setStyle("-fx-text-fill: #666666;");
        
        marksTable.getSelectionModel().clearSelection();
        selectedMark = null;
        updateButtonStates();
    }
    
    /**
     * Add new mark
     */
    @FXML
    private void handleAdd() {
        if (!validateForm()) {
            return;
        }
        
        try {
            Student student = studentComboBox.getValue();
            Subject subject = subjectComboBox.getValue();
            double marks = Double.parseDouble(marksTextField.getText().trim());
            
            // Check if mark already exists for this student and subject
            if (markDAO.markExists(student.getId(), subject.getId(), -1)) {
                AlertUtil.showError("Validation Error", "Duplicate Entry", 
                    "Marks already exist for this student and subject combination.");
                return;
            }
            
            String grade = GradeCalculator.calculateGrade(marks);
            
            Mark mark = new Mark();
            mark.setStudentId(student.getId());
            mark.setSubjectId(subject.getId());
            mark.setMarksObtained(marks);
            mark.setGrade(grade);
            mark.setEntryDate(entryDatePicker.getValue());
            
            if (markDAO.addMark(mark)) {
                AlertUtil.showSuccess("Marks added successfully.");
                loadAllMarks();
                handleClear();
            } else {
                AlertUtil.showError("Error", "Failed to add marks", "Please try again.");
            }
            
        } catch (SQLException e) {
            AlertUtil.showError("Database Error", "Failed to add marks", e.getMessage());
        }
    }
    
    /**
     * Update selected mark
     */
    @FXML
    private void handleUpdate() {
        if (selectedMark == null || !validateForm()) {
            return;
        }
        
        try {
            double marks = Double.parseDouble(marksTextField.getText().trim());
            String grade = GradeCalculator.calculateGrade(marks);
            
            selectedMark.setMarksObtained(marks);
            selectedMark.setGrade(grade);
            
            if (markDAO.updateMark(selectedMark)) {
                AlertUtil.showSuccess("Marks updated successfully.");
                loadAllMarks();
                handleClear();
            } else {
                AlertUtil.showError("Error", "Failed to update marks", "Please try again.");
            }
            
        } catch (SQLException e) {
            AlertUtil.showError("Database Error", "Failed to update marks", e.getMessage());
        }
    }
    
    /**
     * Delete selected mark
     */
    @FXML
    private void handleDelete() {
        if (selectedMark == null) {
            return;
        }
        
        String confirmMessage = String.format("marks for %s in %s", 
            selectedMark.getStudentName(), selectedMark.getSubjectName());
        
        if (!AlertUtil.showDeleteConfirmation(confirmMessage)) {
            return;
        }
        
        try {
            if (markDAO.deleteMark(selectedMark.getId())) {
                AlertUtil.showSuccess("Marks deleted successfully.");
                loadAllMarks();
                handleClear();
            } else {
                AlertUtil.showError("Error", "Failed to delete marks", "Please try again.");
            }
            
        } catch (SQLException e) {
            AlertUtil.showError("Database Error", "Failed to delete marks", e.getMessage());
        }
    }
    
    /**
     * Validate form fields
     */
    private boolean validateForm() {
        if (studentComboBox.getValue() == null) {
            AlertUtil.showError("Validation Error", "Invalid Input", "Please select a student.");
            studentComboBox.requestFocus();
            return false;
        }
        
        if (subjectComboBox.getValue() == null) {
            AlertUtil.showError("Validation Error", "Invalid Input", "Please select a subject.");
            subjectComboBox.requestFocus();
            return false;
        }
        
        String marksText = marksTextField.getText().trim();
        if (marksText.isEmpty()) {
            AlertUtil.showError("Validation Error", "Invalid Input", "Marks are required.");
            marksTextField.requestFocus();
            return false;
        }
        
        try {
            double marks = Double.parseDouble(marksText);
            if (marks < 0 || marks > 100) {
                AlertUtil.showError("Validation Error", "Invalid Input", 
                    "Marks must be between 0 and 100.");
                marksTextField.requestFocus();
                return false;
            }
        } catch (NumberFormatException e) {
            AlertUtil.showError("Validation Error", "Invalid Input", 
                "Marks must be a valid number.");
            marksTextField.requestFocus();
            return false;
        }
        
        if (entryDatePicker.getValue() == null) {
            AlertUtil.showError("Validation Error", "Invalid Input", "Entry date is required.");
            entryDatePicker.requestFocus();
            return false;
        }
        
        return true;
    }
}