package com.serenitask.util.DatabaseManager;

import com.serenitask.util.ErrorHandler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * SqliteConnection provides a singleton instance of the database connection.
 * Ensures that only one connection to the SQLite database is established throughout the application.
 */
public class SqliteConnection {

    /**
     * The singleton instance of the database connection.
     */
    private static Connection instance;

    /**
     * Private constructor to prevent external instantiation.
     * Initializes the connection to the SQLite database when the class is loaded.
     */
    private SqliteConnection() {
        // Initialize the SQLite url
        String url;

        // Get if the thread is a Junit test
        boolean isJunitTest = isJunitTest();

        // Set the url based on if the thread is a Junit test
        if (isJunitTest) {
            // If the thread is a Junit test, use the test-database.db
            System.out.println("Running Junit test, using test-database.db");
            url = "jdbc:sqlite:test-database.db";
        } else {
            // If the thread is not a Junit test, use the database.db
            url = "jdbc:sqlite:database.db";
        }

        // Attempt to establish a connection to the database
        try {
            instance = DriverManager.getConnection(url);
        } catch (SQLException e) {
            // Handle SQL errors
            ErrorHandler.handleException(e);
        }

        // On end of program, close the connection and if test, delete the test-database.db
        Runtime.getRuntime().addShutdownHook(new Thread(() -> closeConnection(isJunitTest)));
    }

    /**
     * Get if the thread is a Junit test
     * @return true if the thread is a Junit test, false otherwise
     */
    public static boolean isJunitTest() {
        // For each element in the stack trace, check if the class name contains junit
        for (StackTraceElement element : Thread.currentThread().getStackTrace()) {
            // If the class name contains junit, return true
            if (element.getClassName().contains("junit")) {
                return true;
            }
        }
        // If no class name contains junit, return false
        return false;
    }

    /**
     * Close the connection to the database.
     * Deleting the test-database.db file if the thread is a Junit test.
     * @param isJunitTest true if the thread is a Junit test, false otherwise
     */
    public static void closeConnection(boolean isJunitTest) {
        try {
            instance.close();
            if (isJunitTest) {
                System.out.println("Deleting test-database.db file");
                // Delete the test-database.db file
                new java.io.File("test-database.db").delete();
            }
        } catch (SQLException e) {
            // Handle SQL errors
            ErrorHandler.handleException(e);
        }
    }

    /**
     * Provides the singleton instance of the database connection.
     * If the connection doesn't exist, it initializes a new connection.
     *
     * @return The Connection object representing the connection to the database.
     */
    public static Connection getConnection() {
        // Create a new connection if it doesn't exist
        if (instance == null) {
            new SqliteConnection();
        }
        // Return the existing connection
        return instance;
    }
}