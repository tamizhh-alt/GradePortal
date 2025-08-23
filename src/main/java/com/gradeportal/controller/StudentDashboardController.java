package com.gradeportal.controller;

import com.gradeportal.dao.MarkDAO;
import com.gradeportal.dao.StudentDAO;
import com.gradeportal.dao.SubjectDAO;
import com.gradeportal.model.Mark;
import com.gradeportal.model.Student;
import com.gradeportal.model.Subject;
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
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Controller for the Student Dashboard view
 */
public class StudentDashboardController implements Initializable {

    @FXML
    private Label welcomeLabel;

    @FXML
    private Label studentNameLabel;

    @FXML
    private Label rollNumberLabel;

    @FXML
    private Label classLabel;

    @FXML
    private Label registrationDateLabel;

    @FXML
    private VBox profileSection;

    @FXML
    private VBox coursesSection;

    @FXML
    private VBox gradesSection;

    @FXML
    private TableView<Subject> coursesTable;

    @FXML
    private TableColumn<Subject, String> subjectNameColumn;

    @FXML
    private TableColumn<Subject, String> subjectCodeColumn;

    @FXML
    private TableColumn<Subject, Integer> creditsColumn;

    @FXML
    private TableView<Mark> gradesTable;

    @FXML
    private TableColumn<Mark, String> gradeSubjectColumn;

    @FXML
    private TableColumn<Mark, Double> gradeMarksColumn;

    @FXML
    private TableColumn<Mark, String> gradeGradeColumn;

    @FXML
    private TableColumn<Mark, LocalDate> gradeDateColumn;

    private final StudentDAO studentDAO = new StudentDAO();
    private final SubjectDAO subjectDAO = new SubjectDAO();
    private final MarkDAO markDAO = new MarkDAO();

    private ObservableList<Subject> coursesList = FXCollections.observableArrayList();
    private ObservableList<Mark> gradesList = FXCollections.observableArrayList();

    private String studentUsername;
    private Student currentStudent;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupTables();
        showProfile(); // Default view
    }

    public void setStudentUsername(String username) {
        this.studentUsername = username;
        loadStudentData();
        updateWelcome();
    }

    private void setupTables() {
        // Setup courses table
        subjectNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        subjectCodeColumn.setCellValueFactory(new PropertyValueFactory<>("code"));
        creditsColumn.setCellValueFactory(new PropertyValueFactory<>("credits"));
        coursesTable.setItems(coursesList);

        // Setup grades table
        gradeSubjectColumn.setCellValueFactory(new PropertyValueFactory<>("subjectName"));
        gradeMarksColumn.setCellValueFactory(new PropertyValueFactory<>("marksObtained"));
        gradeGradeColumn.setCellValueFactory(new PropertyValueFactory<>("grade"));
        gradeDateColumn.setCellValueFactory(new PropertyValueFactory<>("entryDate"));
        gradesTable.setItems(gradesList);
    }

    private void loadStudentData() {
        try {
            // Load student profile
            currentStudent = studentDAO.getStudentByUsername(studentUsername);
            if (currentStudent != null) {
                updateProfileDisplay();
                loadStudentCourses();
                loadStudentGrades();
            }
        } catch (SQLException e) {
            AlertUtil.showError("Database Error", "Failed to load student data", e.getMessage());
        }
    }

    private void updateProfileDisplay() {
        if (currentStudent != null) {
            studentNameLabel.setText(currentStudent.getName());
            rollNumberLabel.setText(currentStudent.getRollNumber());
            classLabel.setText(currentStudent.getStudentClass());
            registrationDateLabel.setText(currentStudent.getRegistrationDate().toString());
        }
    }

    private void loadStudentCourses() {
        try {
            // For now, load all subjects. In a real app, you'd have enrollment logic
            List<Subject> subjects = subjectDAO.getAllSubjects();
            coursesList.setAll(subjects);
        } catch (SQLException e) {
            AlertUtil.showError("Database Error", "Failed to load courses", e.getMessage());
        }
    }

    private void loadStudentGrades() {
        try {
            if (currentStudent != null) {
                List<Mark> marks = markDAO.getMarksByStudentId(currentStudent.getId());
                gradesList.setAll(marks);
            }
        } catch (SQLException e) {
            AlertUtil.showError("Database Error", "Failed to load grades", e.getMessage());
        }
    }

    private void updateWelcome() {
        if (welcomeLabel != null && currentStudent != null) {
            welcomeLabel.setText("Welcome, " + currentStudent.getName());
        }
    }

    @FXML
    private void showProfile() {
        profileSection.setVisible(true);
        coursesSection.setVisible(false);
        gradesSection.setVisible(false);
    }

    @FXML
    private void showCourses() {
        profileSection.setVisible(false);
        coursesSection.setVisible(true);
        gradesSection.setVisible(false);
    }

    @FXML
    private void showGrades() {
        profileSection.setVisible(false);
        coursesSection.setVisible(false);
        gradesSection.setVisible(true);
    }

    @FXML
    private void downloadReport() {
        if (currentStudent == null) {
            AlertUtil.showError("Error", "Student data not loaded", "Please try again later.");
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Grade Report");
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("CSV Files", "*.csv")
        );
        fileChooser.setInitialFileName(currentStudent.getName() + "_GradeReport.csv");

        File file = fileChooser.showSaveDialog(gradesTable.getScene().getWindow());
        if (file != null) {
            exportToCSV(file);
        }
    }

    private void exportToCSV(File file) {
        try (FileWriter writer = new FileWriter(file)) {
            // Write header
            writer.write("Student Grade Report\n");
            writer.write("Name: " + currentStudent.getName() + "\n");
            writer.write("Roll Number: " + currentStudent.getRollNumber() + "\n");
            writer.write("Class: " + currentStudent.getStudentClass() + "\n");
            writer.write("Report Date: " + LocalDate.now() + "\n\n");
            
            writer.write("Subject,Marks,Grade,Date\n");
            
            // Write grades
            for (Mark mark : gradesList) {
                writer.write(String.format("%s,%.1f,%s,%s\n",
                    mark.getSubjectName(),
                    mark.getMarksObtained(),
                    mark.getGrade(),
                    mark.getEntryDate()
                ));
            }
            
            AlertUtil.showInfo("Success", "Report Exported", "Grade report has been exported to " + file.getName());
            
        } catch (IOException e) {
            AlertUtil.showError("Export Error", "Failed to export report", e.getMessage());
        }
    }

    @FXML
    private void handleLogout() {
        try {
            // Switch back to login screen
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


