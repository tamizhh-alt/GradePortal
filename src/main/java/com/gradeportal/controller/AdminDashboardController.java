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
}



