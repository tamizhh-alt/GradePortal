package com.gradeportal.controller;

import com.gradeportal.dao.StudentDAO;
import com.gradeportal.model.Student;
import com.gradeportal.util.AlertUtil;
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
 * Controller for the Students management view
 */
public class StudentsController implements Initializable {
    
    @FXML
    private TextField nameTextField;
    
    @FXML
    private TextField rollNumberTextField;
    
    @FXML
    private ComboBox<String> classComboBox;
    
    @FXML
    private DatePicker registrationDatePicker;
    
    @FXML
    private Button addButton;
    
    @FXML
    private Button updateButton;
    
    @FXML
    private Button deleteButton;
    
    @FXML
    private Button clearButton;
    
    @FXML
    private TextField searchTextField;
    
    @FXML
    private TableView<Student> studentsTable;
    
    @FXML
    private TableColumn<Student, String> nameColumn;
    
    @FXML
    private TableColumn<Student, String> rollNumberColumn;
    
    @FXML
    private TableColumn<Student, String> classColumn;
    
    @FXML
    private TableColumn<Student, LocalDate> registrationDateColumn;
    
    private StudentDAO studentDAO;
    private ObservableList<Student> studentsList;
    private Student selectedStudent;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        studentDAO = new StudentDAO();
        studentsList = FXCollections.observableArrayList();
        
        setupTable();
        setupComboBoxes();
        setupEventHandlers();
        loadAllStudents();
        
        // Set default registration date
        registrationDatePicker.setValue(LocalDate.now());
        
        // Initially disable update and delete buttons
        updateButton.setDisable(true);
        deleteButton.setDisable(true);
    }
    
    /**
     * Set up the students table
     */
    private void setupTable() {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        rollNumberColumn.setCellValueFactory(new PropertyValueFactory<>("rollNumber"));
        classColumn.setCellValueFactory(new PropertyValueFactory<>("studentClass"));
        registrationDateColumn.setCellValueFactory(new PropertyValueFactory<>("registrationDate"));
        
        studentsTable.setItems(studentsList);
        
        // Handle table selection
        studentsTable.getSelectionModel().selectedItemProperty().addListener(
            (observable, oldValue, newValue) -> {
                selectedStudent = newValue;
                populateForm(newValue);
                updateButtonStates();
            }
        );
    }
    
    /**
     * Set up combo boxes with default values
     */
    private void setupComboBoxes() {
        classComboBox.getItems().addAll(
            "Grade 1", "Grade 2", "Grade 3", "Grade 4", "Grade 5",
            "Grade 6", "Grade 7", "Grade 8", "Grade 9", "Grade 10",
            "Grade 11", "Grade 12"
        );
    }
    
    /**
     * Set up event handlers
     */
    private void setupEventHandlers() {
        // Search functionality
        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null || newValue.trim().isEmpty()) {
                loadAllStudents();
            } else {
                searchStudents(newValue.trim());
            }
        });
    }
    
    /**
     * Load all students from database
     */
    private void loadAllStudents() {
        try {
            List<Student> students = studentDAO.getAllStudents();
            studentsList.clear();
            studentsList.addAll(students);
        } catch (SQLException e) {
            AlertUtil.showError("Database Error", "Failed to load students", e.getMessage());
        }
    }
    
    /**
     * Search students by name or roll number
     */
    private void searchStudents(String searchTerm) {
        try {
            List<Student> students = studentDAO.searchStudents(searchTerm);
            studentsList.clear();
            studentsList.addAll(students);
        } catch (SQLException e) {
            AlertUtil.showError("Database Error", "Failed to search students", e.getMessage());
        }
    }
    
    /**
     * Populate form with selected student data
     */
    private void populateForm(Student student) {
        if (student != null) {
            nameTextField.setText(student.getName());
            rollNumberTextField.setText(student.getRollNumber());
            classComboBox.setValue(student.getStudentClass());
            registrationDatePicker.setValue(student.getRegistrationDate());
        }
    }
    
    /**
     * Update button states based on selection
     */
    private void updateButtonStates() {
        boolean hasSelection = selectedStudent != null;
        updateButton.setDisable(!hasSelection);
        deleteButton.setDisable(!hasSelection);
    }
    
    /**
     * Clear form fields
     */
    @FXML
    private void handleClear() {
        nameTextField.clear();
        rollNumberTextField.clear();
        classComboBox.setValue(null);
        registrationDatePicker.setValue(LocalDate.now());
        
        studentsTable.getSelectionModel().clearSelection();
        selectedStudent = null;
        updateButtonStates();
    }
    
    /**
     * Add new student
     */
    @FXML
    private void handleAdd() {
        if (!validateForm()) {
            return;
        }
        
        try {
            // Check if roll number already exists
            if (studentDAO.rollNumberExists(rollNumberTextField.getText().trim(), -1)) {
                AlertUtil.showError("Validation Error", "Duplicate Roll Number", 
                    "A student with this roll number already exists.");
                return;
            }
            
            Student student = new Student();
            student.setName(nameTextField.getText().trim());
            student.setRollNumber(rollNumberTextField.getText().trim());
            student.setStudentClass(classComboBox.getValue());
            student.setRegistrationDate(registrationDatePicker.getValue());
            
            if (studentDAO.addStudent(student)) {
                AlertUtil.showSuccess("Student added successfully.");
                loadAllStudents();
                handleClear();
            } else {
                AlertUtil.showError("Error", "Failed to add student", "Please try again.");
            }
            
        } catch (SQLException e) {
            AlertUtil.showError("Database Error", "Failed to add student", e.getMessage());
        }
    }
    
    /**
     * Update selected student
     */
    @FXML
    private void handleUpdate() {
        if (selectedStudent == null || !validateForm()) {
            return;
        }
        
        try {
            // Check if roll number already exists (excluding current student)
            if (studentDAO.rollNumberExists(rollNumberTextField.getText().trim(), selectedStudent.getId())) {
                AlertUtil.showError("Validation Error", "Duplicate Roll Number", 
                    "A student with this roll number already exists.");
                return;
            }
            
            selectedStudent.setName(nameTextField.getText().trim());
            selectedStudent.setRollNumber(rollNumberTextField.getText().trim());
            selectedStudent.setStudentClass(classComboBox.getValue());
            
            if (studentDAO.updateStudent(selectedStudent)) {
                AlertUtil.showSuccess("Student updated successfully.");
                loadAllStudents();
                handleClear();
            } else {
                AlertUtil.showError("Error", "Failed to update student", "Please try again.");
            }
            
        } catch (SQLException e) {
            AlertUtil.showError("Database Error", "Failed to update student", e.getMessage());
        }
    }
    
    /**
     * Delete selected student
     */
    @FXML
    private void handleDelete() {
        if (selectedStudent == null) {
            return;
        }
        
        if (!AlertUtil.showDeleteConfirmation("student '" + selectedStudent.getName() + "'")) {
            return;
        }
        
        try {
            if (studentDAO.deleteStudent(selectedStudent.getId())) {
                AlertUtil.showSuccess("Student deleted successfully.");
                loadAllStudents();
                handleClear();
            } else {
                AlertUtil.showError("Error", "Failed to delete student", "Please try again.");
            }
            
        } catch (SQLException e) {
            AlertUtil.showError("Database Error", "Failed to delete student", e.getMessage());
        }
    }
    
    /**
     * Validate form fields
     */
    private boolean validateForm() {
        if (nameTextField.getText() == null || nameTextField.getText().trim().isEmpty()) {
            AlertUtil.showError("Validation Error", "Invalid Input", "Student name is required.");
            nameTextField.requestFocus();
            return false;
        }
        
        if (rollNumberTextField.getText() == null || rollNumberTextField.getText().trim().isEmpty()) {
            AlertUtil.showError("Validation Error", "Invalid Input", "Roll number is required.");
            rollNumberTextField.requestFocus();
            return false;
        }
        
        if (classComboBox.getValue() == null) {
            AlertUtil.showError("Validation Error", "Invalid Input", "Student class is required.");
            classComboBox.requestFocus();
            return false;
        }
        
        if (registrationDatePicker.getValue() == null) {
            AlertUtil.showError("Validation Error", "Invalid Input", "Registration date is required.");
            registrationDatePicker.requestFocus();
            return false;
        }
        
        return true;
    }
}