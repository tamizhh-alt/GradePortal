package com.gradeportal.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Database connection manager for the Grades Portal application.
 * Handles MySQL database connections using JDBC.
 */
public class DatabaseManager {

    // ✅ Updated Database configuration constants
    private static final String DB_URL = "jdbc:mysql://localhost:3306/grades_portal_db";
    private static final String DB_USERNAME = "root";
    private static final String DB_PASSWORD = "Mysqltamil1";
    private static final String DB_DRIVER = "com.mysql.cj.jdbc.Driver";

    private static Connection connection = null;

    /**
     * Get database connection (singleton pattern).
     * 
     * @return Connection object
     * @throws SQLException if connection fails
     */
    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                // Load MySQL JDBC driver
                Class.forName(DB_DRIVER);

                // Establish connection
                connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
                System.out.println("✅ Database connection established successfully.");

            } catch (ClassNotFoundException e) {
                throw new SQLException("❌ MySQL JDBC Driver not found: " + e.getMessage());
            } catch (SQLException e) {
                throw new SQLException("❌ Failed to connect to database: " + e.getMessage());
            }
        }

        return connection;
    }

    /**
     * Close database connection.
     */
    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("🔌 Database connection closed.");
            } catch (SQLException e) {
                System.err.println("⚠️ Error closing database connection: " + e.getMessage());
            }
        }
    }

    /**
     * Test database connection.
     * 
     * @return true if connection is successful, false otherwise
     */
    public static boolean testConnection() {
        try {
            Connection testConn = getConnection();
            return testConn != null && !testConn.isClosed();
        } catch (SQLException e) {
            System.err.println("❌ Database connection test failed: " + e.getMessage());
            return false;
        }
    }
}
