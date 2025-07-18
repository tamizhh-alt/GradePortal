package com.gradeportal.controller;

import com.gradeportal.util.DatabaseManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.Node;

import java.io.IOException;
import java.sql.*;

public class LoginController {

    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;

    @FXML
    public void handleLogin(ActionEvent event) {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        try (Connection conn = DatabaseManager.getConnection()) {
            String query = "SELECT * FROM users WHERE username = ? AND password = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, username);
            stmt.setString(2, password); // 🔒 Use hashed password in production

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                showAlert("Login Successful", "Welcome " + username);

                // Load main dashboard scene
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MainWindow.fxml"));
                Parent mainRoot = loader.load();

                Scene mainScene = new Scene(mainRoot, 1200, 800);
                mainScene.getStylesheets().add(getClass().getResource("/css/application.css").toExternalForm());

                // Switch stage
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(mainScene);
                stage.setTitle("Grades & Marks Portal System");
                stage.setMinWidth(1000);
                stage.setMinHeight(600);
                stage.show();

            } else {
                showAlert("Login Failed", "Invalid credentials. Please try again.");
            }
        } catch (SQLException e) {
            showAlert("DB Error", e.getMessage());
        } catch (IOException e) {
            showAlert("UI Error", "Failed to load dashboard: " + e.getMessage());
        }
    }

    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
