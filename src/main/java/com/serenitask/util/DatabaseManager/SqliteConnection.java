package com.serenitask.util.DatabaseManager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SqliteConnection {
        private static Connection instance = null;


    // Create SQLite Connection to database
    private SqliteConnection() {
        String url = "jdbc:sqlite:database.db";
        try {
            instance = DriverManager.getConnection(url);
        } catch (SQLException sqlEx) {
            System.err.println(sqlEx);
        }
    }

    public static Connection getConnection() {
        if (instance == null) {
            new SqliteConnection();
        }
        return instance;
    }

}
