package com.gradeportal;

import com.gradeportal.util.DatabaseManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.scene.Parent;

/**
 * Main Application class for Grades & Marks Portal System
 * 
 * This JavaFX application provides a comprehensive solution for managing
 * student grades and academic records in educational institutions.
 * 
 * @author
 * @version 1.0.0
 */
public class GradePortalApp extends Application {

    private static final String APP_TITLE = "Grades & Marks Portal System";
    private static final String LOGIN_FXML = "/com/gradeportal/view/Login.fxml";
    private static final String CSS_FILE = "/css/application.css";

    private static Stage primaryStage; // Needed to switch scenes after login

    @Override
    public void start(Stage stage) {
        // Save primary stage reference
        primaryStage = stage;

        // Initialize database
        initializeDatabase();

        try {
            // Load login screen
            FXMLLoader loader = new FXMLLoader(getClass().getResource(LOGIN_FXML));
            Parent root = loader.load();

            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource(CSS_FILE).toExternalForm());

            stage.setTitle("Login - " + APP_TITLE);
            stage.setScene(scene);

            stage.setMinWidth(1000);
            stage.setMinHeight(600);
            stage.setMaximized(true); // 👈 Launch fullscreen

            try {
                stage.getIcons().add(new Image(getClass().getResourceAsStream("/images/app-icon.png")));
            } catch (Exception e) {
                System.out.println("Application icon not found, using default.");
            }

            stage.show();

            // Handle app exit
            stage.setOnCloseRequest(event -> {
                DatabaseManager.closeConnection();
                System.exit(0);
            });

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Failed to load Login.fxml: " + e.getMessage());
        }
    }

    /**
     * Initializes database connection
     */
    private void initializeDatabase() {
        try {
            DatabaseManager.getConnection();
            System.out.println("✅ Database connected.");
        } catch (Exception e) {
            System.err.println("❌ Database connection failed: " + e.getMessage());
        }
    }

    /**
     * Allows switching to different scenes (e.g., after login)
     * 
     * @param fxmlPath path to FXML
     */
    public static void switchScene(String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(GradePortalApp.class.getResource(fxmlPath));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(GradePortalApp.class.getResource(CSS_FILE).toExternalForm());

            primaryStage.setScene(scene);
            primaryStage.setTitle(title + " - " + APP_TITLE);
            primaryStage.setMaximized(true);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("⚠️ Scene switch failed: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
