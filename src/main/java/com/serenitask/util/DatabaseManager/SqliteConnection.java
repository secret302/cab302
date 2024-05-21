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
        // SQLite connection URL
        String url = "jdbc:sqlite:database.db";

        // Attempt to establish a connection to the database
        try {
            instance = DriverManager.getConnection(url);
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