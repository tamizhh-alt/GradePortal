package com.gradeportal.util;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.stage.Modality;

import java.util.Optional;

/**
 * Utility class for showing consistent alert dialogs throughout the application
 */
public class AlertUtil {
    
    /**
     * Show information alert
     * @param title Alert title
     * @param header Alert header
     * @param content Alert content
     */
    public static void showInfo(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.showAndWait();
    }
    
    /**
     * Show success alert
     * @param message Success message
     */
    public static void showSuccess(String message) {
        showInfo("Success", "Operation Completed Successfully", message);
    }
    
    /**
     * Show error alert
     * @param title Alert title
     * @param header Alert header
     * @param content Alert content
     */
    public static void showError(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.showAndWait();
    }
    
    /**
     * Show error alert with exception details
     * @param message Error message
     * @param exception Exception object
     */
    public static void showError(String message, Exception exception) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("An error occurred");
        alert.setContentText(message);
        
        // Add exception details in expandable area
        if (exception != null) {
            TextArea textArea = new TextArea(exception.getMessage());
            textArea.setEditable(false);
            textArea.setWrapText(true);
            textArea.setMaxWidth(Double.MAX_VALUE);
            textArea.setMaxHeight(Double.MAX_VALUE);
            
            alert.getDialogPane().setExpandableContent(textArea);
        }
        
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.showAndWait();
    }
    
    /**
     * Show warning alert
     * @param title Alert title
     * @param header Alert header
     * @param content Alert content
     */
    public static void showWarning(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.showAndWait();
    }
    
    /**
     * Show confirmation dialog
     * @param title Dialog title
     * @param header Dialog header
     * @param content Dialog content
     * @return true if user clicked OK, false otherwise
     */
    public static boolean showConfirmation(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.initModality(Modality.APPLICATION_MODAL);
        
        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }
    
    /**
     * Show deletion confirmation dialog
     * @param itemName Name of item to be deleted
     * @return true if user confirms deletion
     */
    public static boolean showDeleteConfirmation(String itemName) {
        return showConfirmation(
            "Confirm Deletion",
            "Delete " + itemName + "?",
            "This action cannot be undone. Are you sure you want to delete this " + itemName.toLowerCase() + "?"
        );
    }
}