module gradesportal {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;

    exports com.gradeportal;
    exports com.gradeportal.controller;
    exports com.gradeportal.model;
    exports com.gradeportal.dao;
    exports com.gradeportal.util;
}