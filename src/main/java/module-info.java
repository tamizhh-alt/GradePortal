module gradesportal {
	requires javafx.controls;
	requires javafx.fxml;
	requires java.sql;
	requires java.desktop;

	// Needed for FXML field injection
	opens com.gradeportal.controller to javafx.fxml;
	// Needed for PropertyValueFactory to access model getters
	opens com.gradeportal.model to javafx.base;

	exports com.gradeportal;
	exports com.gradeportal.controller;
	exports com.gradeportal.model;
	exports com.gradeportal.dao;
	exports com.gradeportal.util;
}