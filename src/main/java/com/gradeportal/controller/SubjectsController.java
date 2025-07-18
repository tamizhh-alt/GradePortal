package com.gradeportal.controller;

import com.gradeportal.dao.SubjectDAO;
import com.gradeportal.model.Subject;
import com.gradeportal.util.AlertUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Controller for the Subjects management view
 */
public class SubjectsController implements Initializable {
    
    @FXML
    private TextField subjectNameTextField;
    
    @FXML
    private TextField maxMarksTextField;
    
    @FXML
    private Button addButton;
    
    @FXML
    private Button updateButton;
    
    @FXML
    private Button deleteButton;
    
    @FXML
    private Button clearButton;
    
    @FXML
    private TableView<Subject> subjectsTable;
    
    @FXML
    private TableColumn<Subject, String> subjectNameColumn;
    
    @FXML
    private TableColumn<Subject, Integer> maxMarksColumn;
    
    private SubjectDAO subjectDAO;
    private ObservableList<Subject> subjectsList;
    private Subject selectedSubject;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        subjectDAO = new SubjectDAO();
        subjectsList = FXCollections.observableArrayList();
        
        setupTable();
        loadAllSubjects();
        
        // Set default max marks
        maxMarksTextField.setText("100");
        
        // Initially disable update and delete buttons
        updateButton.setDisable(true);
        deleteButton.setDisable(true);
    }
    
    /**
     * Set up the subjects table
     */
    private void setupTable() {
        subjectNameColumn.setCellValueFactory(new PropertyValueFactory<>("subjectName"));
        maxMarksColumn.setCellValueFactory(new PropertyValueFactory<>("maxMarks"));
        
        subjectsTable.setItems(subjectsList);
        
        // Handle table selection
        subjectsTable.getSelectionModel().selectedItemProperty().addListener(
            (observable, oldValue, newValue) -> {
                selectedSubject = newValue;
                populateForm(newValue);
                updateButtonStates();
            }
        );
    }
    
    /**
     * Load all subjects from database
     */
    private void loadAllSubjects() {
        try {
            List<Subject> subjects = subjectDAO.getAllSubjects();
            subjectsList.clear();
            subjectsList.addAll(subjects);
        } catch (SQLException e) {
            AlertUtil.showError("Database Error", "Failed to load subjects", e.getMessage());
        }
    }
    
    /**
     * Populate form with selected subject data
     */
    private void populateForm(Subject subject) {
        if (subject != null) {
            subjectNameTextField.setText(subject.getSubjectName());
            maxMarksTextField.setText(String.valueOf(subject.getMaxMarks()));
        }
    }
    
    /**
     * Update button states based on selection
     */
    private void updateButtonStates() {
        boolean hasSelection = selectedSubject != null;
        updateButton.setDisable(!hasSelection);
        deleteButton.setDisable(!hasSelection);
    }
    
    /**
     * Clear form fields
     */
    @FXML
    private void handleClear() {
        subjectNameTextField.clear();
        maxMarksTextField.setText("100");
        
        subjectsTable.getSelectionModel().clearSelection();
        selectedSubject = null;
        updateButtonStates();
    }
    
    /**
     * Add new subject
     */
    @FXML
    private void handleAdd() {
        if (!validateForm()) {
            return;
        }
        
        try {
            // Check if subject name already exists
            if (subjectDAO.subjectNameExists(subjectNameTextField.getText().trim(), -1)) {
                AlertUtil.showError("Validation Error", "Duplicate Subject Name", 
                    "A subject with this name already exists.");
                return;
            }
            
            Subject subject = new Subject();
            subject.setSubjectName(subjectNameTextField.getText().trim());
            subject.setMaxMarks(Integer.parseInt(maxMarksTextField.getText().trim()));
            
            if (subjectDAO.addSubject(subject)) {
                AlertUtil.showSuccess("Subject added successfully.");
                loadAllSubjects();
                handleClear();
            } else {
                AlertUtil.showError("Error", "Failed to add subject", "Please try again.");
            }
            
        } catch (SQLException e) {
            AlertUtil.showError("Database Error", "Failed to add subject", e.getMessage());
        }
    }
    
    /**
     * Update selected subject
     */
    @FXML
    private void handleUpdate() {
        if (selectedSubject == null || !validateForm()) {
            return;
        }
        
        try {
            // Check if subject name already exists (excluding current subject)
            if (subjectDAO.subjectNameExists(subjectNameTextField.getText().trim(), selectedSubject.getId())) {
                AlertUtil.showError("Validation Error", "Duplicate Subject Name", 
                    "A subject with this name already exists.");
                return;
            }
            
            selectedSubject.setSubjectName(subjectNameTextField.getText().trim());
            selectedSubject.setMaxMarks(Integer.parseInt(maxMarksTextField.getText().trim()));
            
            if (subjectDAO.updateSubject(selectedSubject)) {
                AlertUtil.showSuccess("Subject updated successfully.");
                loadAllSubjects();
                handleClear();
            } else {
                AlertUtil.showError("Error", "Failed to update subject", "Please try again.");
            }
            
        } catch (SQLException e) {
            AlertUtil.showError("Database Error", "Failed to update subject", e.getMessage());
        }
    }
    
    /**
     * Delete selected subject
     */
    @FXML
    private void handleDelete() {
        if (selectedSubject == null) {
            return;
        }
        
        if (!AlertUtil.showDeleteConfirmation("subject '" + selectedSubject.getSubjectName() + "'")) {
            return;
        }
        
        try {
            if (subjectDAO.deleteSubject(selectedSubject.getId())) {
                AlertUtil.showSuccess("Subject deleted successfully.");
                loadAllSubjects();
                handleClear();
            } else {
                AlertUtil.showError("Error", "Failed to delete subject", "Please try again.");
            }
            
        } catch (SQLException e) {
            if (e.getMessage().contains("foreign key constraint")) {
                AlertUtil.showError("Cannot Delete Subject", 
                    "Subject is in use", 
                    "This subject cannot be deleted because it has associated marks. " +
                    "Please remove all marks for this subject first.");
            } else {
                AlertUtil.showError("Database Error", "Failed to delete subject", e.getMessage());
            }
        }
    }
    
    /**
     * Validate form fields
     */
    private boolean validateForm() {
        if (subjectNameTextField.getText() == null || subjectNameTextField.getText().trim().isEmpty()) {
            AlertUtil.showError("Validation Error", "Invalid Input", "Subject name is required.");
            subjectNameTextField.requestFocus();
            return false;
        }
        
        String maxMarksText = maxMarksTextField.getText().trim();
        if (maxMarksText.isEmpty()) {
            AlertUtil.showError("Validation Error", "Invalid Input", "Maximum marks is required.");
            maxMarksTextField.requestFocus();
            return false;
        }
        
        try {
            int maxMarks = Integer.parseInt(maxMarksText);
            if (maxMarks <= 0) {
                AlertUtil.showError("Validation Error", "Invalid Input", 
                    "Maximum marks must be greater than 0.");
                maxMarksTextField.requestFocus();
                return false;
            }
        } catch (NumberFormatException e) {
            AlertUtil.showError("Validation Error", "Invalid Input", 
                "Maximum marks must be a valid integer.");
            maxMarksTextField.requestFocus();
            return false;
        }
        
        return true;
    }
}