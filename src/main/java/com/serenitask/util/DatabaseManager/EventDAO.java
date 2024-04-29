package com.serenitask.util.DatabaseManager;

import com.serenitask.model.Event;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class EventDAO {

    private Connection connection;

    public EventDAO() {
        connection = SqliteConnection.getConnection();
        createTable();
        // Used for debugging
        // addSampleEntries();
    }

    private void addSampleEntries() {
        try {
            // Clear before inserting
            Statement clearStatement = connection.createStatement();
            String clearQuery = "DELETE FROM events";
            clearStatement.execute(clearQuery);
            Statement insertStatement = connection.createStatement();
            String insertQuery =
                    "INSERT INTO events (id, title, description, location, startTime, duration, fullDay, staticPos, calendar, recurrenceRules, recurrenceEnd) VALUES "
                    + "('sha256', 'Event 1', 'description of event 1', 'Australia','placeholder for DATETIME', '2 hours', FALSE, TRUE, 'main cal','recurr string', 'recurr end'),"
                    + "('sha256', 'Event 2', 'description of event 2', 'Australia','placeholder for DATETIME', '2 hours', FALSE, TRUE, 'main cal','recurr string', 'recurr end'),"
                    + "('sha256', 'Event 3', 'description of event 2', 'Australia','placeholder for DATETIME', '2 hours', FALSE, TRUE, 'main cal','recurr string', 'recurr end')";
            insertStatement.execute(insertQuery);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

// Creating Table if doesn't exist
    private void createTable() {
        try {
            // All table requirements / Create Query
            String query = "CREATE TABLE IF NOT EXISTS events ("
                    + "id TEXT  PRIMARY KEY,"
                    + "title TEXT NOT NULL,"
                    + "description TEXT,"
                    + "location TEXT,"
                    + "start_time TEXT NOT NULL,"
                    + "duration INTEGER NOT NULL,"
                    + "full_day BOOLEAN NOT NULL DEFAULT (false),"
                    + "static_pos BOOLEAN NOT NULL DEFAULT (false),"
                    + "calendar TEXT NOT NULL DEFAULT 'default',"
                    + "recurrence_rules TEXT,"
                    + "recurrence_end TEXT"
                    + ");";
            Statement statement = connection.createStatement();
            statement.execute(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

// Adding event if doesn't exist
    public String addEvent(Event event) {
        try {
            // Create insert query
            String query = "INSERT INTO events (id, title, description, location, start_time, duration, full_day, static_pos, calendar, recurrence_rules, recurrence_end) VALUES" +
                    "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            // Update new row with values from event
            statement.setString(1, event.getId());
            statement.setString(2, event.getTitle());
            statement.setString(3, event.getDescription());
            statement.setString(4, event.getLocation());
            statement.setString(5, event.getStartTime());
            statement.setInt(6, event.getDuration());
            statement.setBoolean(7, event.getFullDay());
            statement.setBoolean(8, event.getStaticPos());
            statement.setString(9, event.getCalendar());
            statement.setString(10, event.getRecurrenceRules());
            statement.setString(11, event.getRecurrenceEnd());
            // Execute update
            statement.executeUpdate();
            // Set the id of the new event

            return event.getId();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // return -1 if event wasn't created
        return null;
    }

    public boolean updateEvent(Event event) {
        try {
            // Create update query
            String query = "UPDATE events SET " +
                    "title = ?," +
                    "description = ?," +
                    "location = ?," +
                    "start_time = ?," +
                    "duration = ?," +
                    "full_day = ?," +
                    "static_pos = ?," +
                    "calendar = ?," +
                    "recurrence_rules = ?," +
                    "recurrence_end = ?" +
                    "WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            // Update row with values from event
            statement.setString(1, event.getTitle());
            statement.setString(2, event.getDescription());
            statement.setString(3, event.getLocation());
            statement.setString(4, event.getStartTime());
            statement.setInt(5, event.getDuration());
            statement.setBoolean(6, event.getFullDay());
            statement.setBoolean(7, event.getStaticPos());
            statement.setString(8, event.getCalendar());
            statement.setString(9, event.getRecurrenceRules());
            statement.setString(10, event.getRecurrenceEnd());
            statement.setString(11, event.getId());
            // Execute update
            statement.executeUpdate();

            // If success
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteEvent(String id) {
        try {
            // Create delete query
            String query = "DELETE FROM events WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            // Delete row id
            statement.setString(1, id);
            // Execute update
            statement.executeUpdate();

            // If success
            return true;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

// Getting the event via ID
    public Event getEventById(String id) {
        try {
            // Search Query
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM events WHERE id = ?");
            statement.setString(1, id);
            ResultSet resultSet = statement.executeQuery();

            // If Found, collect results
            if (resultSet.next()) {
                String title = resultSet.getString("title");
                String description = resultSet.getString("description");
                String location = resultSet.getString("location");
                String startTime = resultSet.getString("start_time");
                int duration = resultSet.getInt("duration");
                Boolean fullDay = resultSet.getBoolean("full_day");
                Boolean staticPos = resultSet.getBoolean("static_pos");
                String calendar = resultSet.getString("calendar");
                String recurrenceRules = resultSet.getString("recurrence_rules");
                String recurrenceEnd = resultSet.getString("recurrence_end");

                // Create new event
                Event event = new Event(id, title, description, location, startTime, duration, fullDay, staticPos, calendar, recurrenceRules, recurrenceEnd);
                return event;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // List all events
    public List<Event> getAllEvents() {
        List<Event> events = new ArrayList<>();
        try { // Collect all events
            Statement statement = connection.createStatement();
            String query = "SELECT * FROM events";

            ResultSet resultSet = statement.executeQuery(query);

            // Create variables for all results
            while (resultSet.next()) {
                // Retrieve data from the result set
                String id = resultSet.getString("id");
                String title = resultSet.getString("title");
                String description = resultSet.getString("description");
                String location = resultSet.getString("location");
                String startTime = resultSet.getString("start_time");
                int duration = resultSet.getInt("duration");
                Boolean fullDay = resultSet.getBoolean("full_day");
                Boolean staticPos = resultSet.getBoolean("static_pos");
                String calendar = resultSet.getString("calendar");
                String recurrenceRules = resultSet.getString("recurrence_rules");
                String recurrenceEnd = resultSet.getString("recurrence_end");
                // Create a new event object
                Event event = new Event(id, title, description, location, startTime, duration, fullDay, staticPos, calendar, recurrenceRules, recurrenceEnd);
                events.add(event);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return events;
    }
}
