package com.serenitask.util.DatabaseManager;
import com.calendarfx.model.Interval;
import com.serenitask.model.Event;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
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
     */
    public EventDAO() {
        // Get connection to the database
        connection = SqliteConnection.getConnection();
        // Create table if it doesn't exist
        createTable();
        // Used for debugging
        // addSampleEntries();
    }


    // Private (Maybe to be moved to a utility class)
    /**
     * Re-loads an interval saved as a string back to an Interval object
     * @param intervalString Interval saved as a string
     * @return Interval object
     */
    private static Interval reloadInterval(String intervalString) {
        String[] parts = intervalString.replace("Interval [", "").replace("]", "").split(", ");
        LocalDate startDate = LocalDate.parse(parts[0].split("=")[1]);
        LocalDate endDate = LocalDate.parse(parts[1].split("=")[1]);
        LocalTime startTime = LocalTime.parse(parts[2].split("=")[1]);
        LocalTime endTime = LocalTime.parse(parts[3].split("=")[1]);
        ZoneId zoneId = ZoneId.of(parts[4].split("=")[1]);
        return new Interval(startDate, startTime, endDate, endTime, zoneId);
    }

    // Private
    /**
     * Create Events SQLite table if it doesn't exist
     */
    private void createTable() {
        try {
            // Table Create Query
            String query = "CREATE TABLE IF NOT EXISTS events ("
                    + "id               TEXT        PRIMARY KEY,"
                    + "title            TEXT        NOT NULL,"
                    + "location         TEXT,       "
                    + "interval         STRING,     "
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
     * @param event Event to add
     * @return Event ID, null if failed
     */
    public String addEvent(Event event) {
        try {
            // Create insert query
            String query = "INSERT INTO events ("
                    + "id,"
                    + "title,"
                    + "location,"
                    + "interval,"
                    + "fullDay,"
                    + "staticPos,"
                    + "calendar,"
                    + "recurrenceRules,"
                    + "allocatedUntil)"
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            // Create prepared statement
            PreparedStatement statement = connection.prepareStatement(query);
            // Insert values from event
            statement.setString(1, event.getId());
            statement.setString(2, event.getTitle());
            statement.setString(3, event.getLocation());
            statement.setString(4, event.getInterval().toString());
            statement.setBoolean(5, event.getFullDay());
            statement.setBoolean(6, event.getStaticPos());
            statement.setString(7, event.getCalendar());
            statement.setString(8, event.getRecurrenceRules());
            statement.setDate(9, java.sql.Date.valueOf(event.getAllocatedUntil()));
            // Execute update
            statement.executeUpdate();

            // Return the ID of the event if successful
            return event.getId();
        } catch (Exception e) {
            // Print error if event creation fails
            e.printStackTrace();
        }
        // return null if event wasn't created
        return null;
    }

    /**
     * Update event in the database
     * @param event Event to update
     * @return True if successful, false otherwise
     */
    public boolean updateEvent(Event event) {
        try {
            // Create update query
            String query = "UPDATE events SET "
                    + "title              = ?,"
                    + "location           = ?,"
                    + "interval           = ?,"
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
            statement.setString(3, event.getInterval().toString());
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
        // return false if update fails
        return false;
    }

    /**
     * Delete event from the database
     * @param id Event ID
     * @return True if successful, false otherwise
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
            int rowsDeleted = statement.executeUpdate();

            // Return if goal was deleted
            return rowsDeleted > 0;
        } catch (Exception e) {
            // Print error if event deletion fails
            e.printStackTrace();
        }
        // return false if delete fails
        return false;
    }


    /**
     * Retrieve event from the database by ID
     * @param id Event ID
     * @return Event object, null if not found
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

            // If found, return event
            if (resultSet.next()) {
                String title = resultSet.getString("title");
                String location = resultSet.getString("location");
                Interval interval = reloadInterval(resultSet.getString("interval"));
                Boolean fullDay = resultSet.getBoolean("fullDay");
                Boolean staticPos = resultSet.getBoolean("staticPos");
                String calendar = resultSet.getString("calendar");
                String recurrenceRules = resultSet.getString("recurrenceRules");
                LocalDate allocatedUntil = resultSet.getDate("allocatedUntil").toLocalDate();

                // Return new event object
                return new Event(id, title, location, interval, fullDay, staticPos, calendar, recurrenceRules, allocatedUntil);
            }
        } catch (Exception e) {
            // Print error if event retrieval fails
            e.printStackTrace();
        }
        // return null if event not found
        return null;
    }

    /**
     * Retrieve all events from the database
     * @return List of events
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
                Interval interval = reloadInterval(resultSet.getString("interval"));
                Boolean fullDay = resultSet.getBoolean("fullDay");
                Boolean staticPos = resultSet.getBoolean("staticPos");
                String calendar = resultSet.getString("calendar");
                String recurrenceRules = resultSet.getString("recurrenceRules");
                LocalDate allocatedUntil = resultSet.getDate("allocatedUntil").toLocalDate();

                // Create a new event object
                Event event = new Event(id, title, location, interval, fullDay, staticPos, calendar, recurrenceRules, allocatedUntil);
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
