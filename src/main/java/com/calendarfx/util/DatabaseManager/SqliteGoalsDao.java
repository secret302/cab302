package com.calendarfx.util.DatabaseManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class SqliteGoalsDao {
    private Connection connection;
    public SqliteGoalsDao() {
        connection = SqliteConnection.getInstance();
        createTable();
        // Used for debugging
        // addSampleEntries();
    }

    private void addSampleEntries() {
        try {
            // Clear before inserting
            Statement clearStatement = connection.createStatement();
            String clearQuery = "DELETE FROM goals";
            clearStatement.execute(clearQuery);
            Statement insertStatement = connection.createStatement();
            String insertQuery =
                    "INSERT INTO events (title, description, location, startTime, duration, fullDay, staticPos, calendar, recurrenceRules, recurrenceEnd) VALUES "
                            + "('Event 1', 'description of event 1', 'Australia','placeholder for DATETIME', '2 hours', FALSE, TRUE, 'main cal','recurr string', 'recurr end'),"
                            + "('Event 2', 'description of event 2', 'Australia','placeholder for DATETIME', '2 hours', FALSE, TRUE, 'main cal','recurr string', 'recurr end'),"
                            + "('Event 3', 'description of event 2', 'Australia','placeholder for DATETIME', '2 hours', FALSE, TRUE, 'main cal','recurr string', 'recurr end')";
            insertStatement.execute(insertQuery);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createTable() {
        try {
            String query = "CREATE TABLE IF NOT EXIST goals ("
                    + "id INTEGER  PRIMARY KEY AUTOINCREMENT,"
                    + "title TEXT NOT NULL,"
                    + "description TEXT,"
                    + "location TEXT,"
                    + "start_time TEXT NOT NULL,"
                    + "duration INTEGER NOT NULL,"
                    + "full_day BOOLEAN NOT NULL DEFAULT (false),"
                    + "static_pos BOOLEAN NOT NULL DEFAULT (false),"
                    + "calendar TEXT NOT NULL DEFAULT 'default',"
                    + "recurrence_rules TEXT,"
                    + "recurrence_end DATETIME"
                    + ");";
            Statement statement = connection.createStatement();
            statement.execute(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
