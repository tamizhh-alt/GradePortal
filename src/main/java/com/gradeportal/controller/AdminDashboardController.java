package com.gradeportal.controller;

import com.gradeportal.dao.StudentDAO;
import com.gradeportal.model.Student;
import com.gradeportal.util.AlertUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public class AdminDashboardController implements Initializable {

    @FXML
    private Label welcomeLabel;

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

    private final StudentDAO studentDAO = new StudentDAO();
    private final ObservableList<Student> studentsList = FXCollections.observableArrayList();

    private String adminName = "Admin";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupTable();
        loadStudents();
        updateWelcome();
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName != null && !adminName.isBlank() ? adminName : "Admin";
        updateWelcome();
    }

    private void updateWelcome() {
        if (welcomeLabel != null) {
            welcomeLabel.setText("Welcome, " + adminName);
        }
    }

    private void setupTable() {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        rollNumberColumn.setCellValueFactory(new PropertyValueFactory<>("rollNumber"));
        classColumn.setCellValueFactory(new PropertyValueFactory<>("studentClass"));
        registrationDateColumn.setCellValueFactory(new PropertyValueFactory<>("registrationDate"));
        studentsTable.setItems(studentsList);
    }

    private void loadStudents() {
        try {
            List<Student> students = studentDAO.getAllStudents();
            studentsList.setAll(students);
        } catch (SQLException e) {
            AlertUtil.showError("Database Error", "Failed to load students", e.getMessage());
        }
    }

    // Navigation methods
    @FXML
    private void showDashboard() {
        // Already on dashboard, do nothing
    }

    @FXML
    private void showStudents() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Students.fxml"));
            Parent root = loader.load();
            
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/css/application.css").toExternalForm());
            
            Stage stage = (Stage) welcomeLabel.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Students Management - Grades & Marks Portal System");
            stage.show();
        } catch (IOException e) {
            AlertUtil.showError("Navigation Error", "Failed to load Students view", e.getMessage());
        }
    }

    @FXML
    private void showSubjects() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Subjects.fxml"));
            Parent root = loader.load();
            
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/css/application.css").toExternalForm());
            
            Stage stage = (Stage) welcomeLabel.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Subjects Management - Grades & Marks Portal System");
            stage.show();
        } catch (IOException e) {
            AlertUtil.showError("Navigation Error", "Failed to load Subjects view", e.getMessage());
        }
    }

    @FXML
    private void showMarks() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Marks.fxml"));
            Parent root = loader.load();
            
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/css/application.css").toExternalForm());
            
            Stage stage = (Stage) welcomeLabel.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Marks Management - Grades & Marks Portal System");
            stage.show();
        } catch (IOException e) {
            AlertUtil.showError("Navigation Error", "Failed to load Marks view", e.getMessage());
        }
    }

    @FXML
    private void showResults() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Results.fxml"));
            Parent root = loader.load();
            
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/css/application.css").toExternalForm());
            
            Stage stage = (Stage) welcomeLabel.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Results & Reports - Grades & Marks Portal System");
            stage.show();
        } catch (IOException e) {
            AlertUtil.showError("Navigation Error", "Failed to load Results view", e.getMessage());
        }
    }

    @FXML
    private void handleLogout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/gradeportal/view/Login.fxml"));
            Parent root = loader.load();
            
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/css/application.css").toExternalForm());
            
            Stage stage = (Stage) welcomeLabel.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Login - Grades & Marks Portal System");
            stage.show();
        } catch (IOException e) {
            AlertUtil.showError("Navigation Error", "Failed to return to login", e.getMessage());
        }
    }
}



