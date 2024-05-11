package com.serenitask.util.DatabaseManager;
import com.serenitask.model.Event;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * EventDAO class is used to interact with the database for events
 */
public class EventDAO {
    // Connection to the database
    private Connection connection;

    /**
     * EventDAO constructor
     * @constructor
     */
    public EventDAO() {
        // Get connection to the database
        connection = SqliteConnection.getConnection();
        // Create table if it doesn't exist
        createTable();
        // Used for debugging
        // addSampleEntries();
    }

    // DEBUGGING
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


    /**
     * Create SQLite table if it doesn't exist
     */
    private void createTable() {
        try {
            // All table requirements / Create Query
            String query = "CREATE TABLE IF NOT EXISTS events ("
                    + "id               TEXT        PRIMARY KEY,"
                    + "title            TEXT        NOT NULL,"
                    + "location         TEXT,       "
                    + "startTime        TIMESTAMP,  "
                    + "duration         INTEGER     NOT NULL,"
                    + "fullDay          BOOLEAN     NOT NULL DEFAULT (false),"
                    + "staticPos        BOOLEAN     NOT NULL DEFAULT (false),"
                    + "calendar         TEXT        NOT NULL DEFAULT 'default',"
                    + "recurrenceRules  TEXT,       "
                    + "allocatedUntil   DATE        );";
            // Create and execute statement
            Statement statement = connection.createStatement();
            statement.execute(query);
        } catch (Exception e) {
            // Print error if table creation fails
            e.printStackTrace();
        }
    }

    /**
     * Add event to the database
     * @param event - Event to add
     * @return - Event ID
     */
    public String addEvent(Event event) {
        try {
            // Create insert query
            String query = "INSERT INTO events ("
                    + "id,"
                    + "title,"
                    + "location,"
                    + "startTime,"
                    + "duration,"
                    + "fullDay,"
                    + "staticPos,"
                    + "calendar,"
                    + "recurrenceRules,"
                    + "allocatedUntil)"
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            // Create prepared statement
            PreparedStatement statement = connection.prepareStatement(query);
            // Insert values from event
            statement.setString(1, event.getId());
            statement.setString(2, event.getTitle());
            statement.setString(3, event.getLocation());
            statement.setTimestamp(4, Timestamp.valueOf(event.getStartTime()));
            statement.setInt(5, event.getDuration());
            statement.setBoolean(6, event.getFullDay());
            statement.setBoolean(7, event.getStaticPos());
            statement.setString(8, event.getCalendar());
            statement.setString(9, event.getRecurrenceRules());
            statement.setDate(10, java.sql.Date.valueOf(event.getAllocatedUntil()));
            // Execute update
            statement.executeUpdate();

            // Return the ID of the event if successful
            return event.getId();
        } catch (Exception e) {
            // Print error if event creation fails
            e.printStackTrace();
        }
        // return -1 if event wasn't created
        return null;
    }

    /**
     * Update event in the database
     * @param event - Event to update
     * @return - Success boolean
     */
    public boolean updateEvent(Event event) {
        try {
            // Create update query
            String query = "UPDATE events SET "
                    + "title              = ?,"
                    + "location           = ?,"
                    + "startTime          = ?,"
                    + "duration           = ?,"
                    + "fullDay            = ?,"
                    + "staticPos          = ?,"
                    + "calendar           = ?,"
                    + "recurrenceRules    = ?,"
                    + "allocatedUntil     = ? "
                    + "WHERE id           = ?";
            // Create prepared statement
            PreparedStatement statement = connection.prepareStatement(query);
            // Update row with values from event
            statement.setString(1, event.getTitle());
            statement.setString(2, event.getLocation());
            statement.setTimestamp(3, Timestamp.valueOf(event.getStartTime()));
            statement.setInt(4, event.getDuration());
            statement.setBoolean(4, event.getFullDay());
            statement.setBoolean(5, event.getStaticPos());
            statement.setString(6, event.getCalendar());
            statement.setString(7, event.getRecurrenceRules());
            statement.setDate(8, java.sql.Date.valueOf(event.getAllocatedUntil()));
            statement.setString(9, event.getId());
            // Execute update
            statement.executeUpdate();

            // If success
            return true;
        } catch (Exception e) {
            // Print error if event update fails
            e.printStackTrace();
        }
        // return false if event wasn't updated
        return false;
    }

    /**
     * Delete event from the database
     * @param id - Event ID
     * @return - Success boolean
     */
    public boolean deleteEvent(String id) {
        try {
            // Create delete query
            String query = "DELETE FROM events WHERE id = ?";
            // Create prepared statement
            PreparedStatement statement = connection.prepareStatement(query);
            // Delete row id
            statement.setString(1, id);
            // Execute update
            statement.executeUpdate();

            // If success
            return true;

        } catch (Exception e) {
            // Print error if event deletion fails
            e.printStackTrace();
        }
        // return false if event wasn't deleted
        return false;
    }


    /**
     * Retrieve event from the database by ID
     * @param id - Event ID
     * @return - Event
     */
    public Event getEventById(String id) {
        try {
            // Create Query
            String query = "SELECT * FROM events WHERE id = ?";
            // Create Search Statement
            PreparedStatement statement = connection.prepareStatement(query);
            // Insert ID into statement
            statement.setString(1, id);
            // Execute Query
            ResultSet resultSet = statement.executeQuery();

            // If Found, collect results
            if (resultSet.next()) {
                String title = resultSet.getString("title");
                String location = resultSet.getString("location");
                LocalDateTime startTime = resultSet.getTimestamp("startTime").toLocalDateTime();
                int duration = resultSet.getInt("duration");
                Boolean fullDay = resultSet.getBoolean("fullDay");
                Boolean staticPos = resultSet.getBoolean("staticPos");
                String calendar = resultSet.getString("calendar");
                String recurrenceRules = resultSet.getString("recurrenceRules");
                LocalDate allocatedUntil = resultSet.getDate("allocatedUntil").toLocalDate();

                // Return new event object
                return new Event(id, title, location, startTime, duration, fullDay, staticPos, calendar, recurrenceRules, allocatedUntil);
            }
        } catch (Exception e) {
            // Print error if event retrieval fails
            e.printStackTrace();
        }
        // return null if failed to retrieve event
        return null;
    }

    /**
     * Retrieve all events from the database
     * @return - List of events
     */
    public List<Event> getAllEvents() {
        // Create empty list of events to return
        List<Event> events = new ArrayList<>();

        try {
            // Create statement
            Statement statement = connection.createStatement();
            // Create get all query
            String query = "SELECT * FROM events";
            // Execute query
            ResultSet resultSet = statement.executeQuery(query);

            // For each event in the result set
            while (resultSet.next()) {
                // Retrieve data from the result set
                String id = resultSet.getString("id");
                String title = resultSet.getString("title");
                String location = resultSet.getString("location");
                LocalDateTime startTime = resultSet.getTimestamp("startTime").toLocalDateTime();
                int duration = resultSet.getInt("duration");
                Boolean fullDay = resultSet.getBoolean("fullDay");
                Boolean staticPos = resultSet.getBoolean("staticPos");
                String calendar = resultSet.getString("calendar");
                String recurrenceRules = resultSet.getString("recurrenceRules");
                LocalDate allocatedUntil = resultSet.getDate("allocatedUntil").toLocalDate();

                // Create a new event object
                Event event = new Event(id, title, location, startTime, duration, fullDay, staticPos, calendar, recurrenceRules, allocatedUntil);
                // Add event to list
                events.add(event);
            }
        } catch (Exception e) {
            // Print error if event retrieval fails
            e.printStackTrace();
        }
        // return list of events (regardless of none found)
        return events;
    }
}
