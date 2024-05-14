package com.serenitask.util.DatabaseManager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * SqliteConnection class creates a connection to the SQLite database
 * Singleton pattern is used to ensure only one connection is created
 */
public class SqliteConnection {
    // Singleton instance
    private static Connection instance = null;


    /**
     * SqliteConnection constructor
     * Creates a connection to the SQLite database
     */
    private SqliteConnection() {
        // SQLite connection URL
        String url = "jdbc:sqlite:database.db";
        try {
            // Attempt to create a new connection to the SQLite database
            instance = DriverManager.getConnection(url);
        } catch (SQLException sqlEx) {
            // Print the error message
            System.err.println(sqlEx);
        }
    }

    /**
     * Get the singleton connection instance to the SQLite database
     * @return Connection object
     */
    public static Connection getConnection() {
        if (instance == null) {
            // Create a new connection if one does not exist
            new SqliteConnection();
        }
        // Return the connection instance
        return instance;
    }
}
