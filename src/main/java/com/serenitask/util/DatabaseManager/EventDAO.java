package com.serenitask.util.DatabaseManager;

import com.calendarfx.model.Calendar;
import com.calendarfx.model.Entry;
import com.calendarfx.model.Interval;
import com.serenitask.model.Event;
import com.serenitask.util.ErrorHandler;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

/**
 * EventDAO (Data Access Object) class for interacting with the 'events' table in the database.
 * Handles CRUD operations for Event objects, converting between Event model and database representation.
 */
public class EventDAO {

    /**
     * Database connection object.
     */
    private final Connection connection;

    /**
     * Constructor for EventDAO.
     * Initializes the database connection and creates the 'events' table if it doesn't exist.
     */
    public EventDAO() {
        // Get connection to the database
        connection = SqliteConnection.getConnection();
        // Create table if it doesn't exist
        createTable();
    }


    /**
     * Reconstructs an Interval object from its string representation stored in the database.
     *
     * @param intervalString The string representation of the Interval as stored in the database.
     * @return The reconstructed Interval object.
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
     * Creates the 'events' table in the database if it doesn't exist.
     * Defines the table schema with columns for event attributes.
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
                    + "calendar         TEXT        NOT NULL DEFAULT 'default');";
            // Create and execute statement
            Statement statement = connection.createStatement();
            statement.execute(query);
        } catch (SQLException e) {
            // Print error if table creation fails
            ErrorHandler.handleException(e);
        }
    }

    /**
     * Constructs an Event object from a ResultSet obtained from the database.
     *
     * @param resultSet The ResultSet containing data for an event.
     * @return The constructed Event object.
     * @throws SQLException If an error occurs while processing the ResultSet.
     */
    private Event constructEventFromResultSet(ResultSet resultSet) throws SQLException {
        String id = resultSet.getString("id");
        String title = resultSet.getString("title");
        String location = resultSet.getString("location");
        Interval interval = reloadInterval(resultSet.getString("interval"));
        boolean fullDay = resultSet.getBoolean("fullDay");
        boolean staticPos = resultSet.getBoolean("staticPos");
        String calendar = resultSet.getString("calendar");

        return new Event(id, title, location, interval, fullDay, staticPos, calendar);
    }

    /**
     * Adds a new Event to the database.
     *
     * @param event The Event object to be added to the database.
     * @return The ID of the newly added event, or null if the operation fails.
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
                    + "calendar)"
                    + "VALUES (?, ?, ?, ?, ?, ?, ?)";
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
            // Execute update
            statement.executeUpdate();

            // Return the ID of the event if successful
            return event.getId();
        } catch (SQLException e) {
            // Print error if event creation fails
            ErrorHandler.handleException(e);
        }
        // return null if event wasn't created
        return null;
    }

    /**
     * Updates an existing Event in the database.
     *
     * @param event The Event object with updated information.
     * @return True if the update was successful, false otherwise.
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
                    + "calendar           = ?"
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
            statement.setString(7, event.getId());
            // Execute update
            statement.executeUpdate();

            // If success
            return true;
        } catch (SQLException e) {
            // Print error if event update fails
            ErrorHandler.handleException(e);
        }
        // return false if update fails
        return false;
    }

    /**
     * Deletes an Event from the database.
     *
     * @param id The ID of the Event to be deleted.
     * @return True if the deletion was successful, false otherwise.
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
        } catch (SQLException e) {
            // Print error if event deletion fails
            ErrorHandler.handleException(e);
        }
        // return false if delete fails
        return false;
    }

    /**
     * Deletes all events with a specified name and a date greater than or equal to the given date.
     *
     * @param name The name of the events to delete.
     * @param date The date from which to delete events (inclusive).
     */
    public void deleteEventsByNameAndDate(String name, LocalDate date, Calendar calendar) {
        try {
            List<Event> allEventsByTitle = getAllEventsByTitle(name);
            List<String> idsToRemove = new ArrayList<>();

            for (Event event : allEventsByTitle) {
                // Check if the event date is strictly after the specified date
                if (event.getInterval().getStartDate().isAfter(date.minusDays(1))) {
                    String idToDelete = event.getId();
                    deleteEvent(idToDelete);
                    idsToRemove.add(idToDelete);
                }
            }
            removeEntriesById(calendar, idsToRemove);
        } catch (Exception e) { // Catch any Exception type
            System.err.println("SQL error: " + e.getMessage());
        }
    }




    /**
     * Retrieves all Events from the database with matching title.
     *
     * @param title title of event to be retrieved
     * @return A list of all Event objects stored in the database.
     */
    public List<Event> getAllEventsByTitle(String title) {
        // Create empty list of events to return
        List<Event> events = new ArrayList<>();

        try {
            // Create Query
            String query = "SELECT * FROM events WHERE title = ?";
            // Create Search Statement
            PreparedStatement statement = connection.prepareStatement(query);
            // Insert ID into statement
            statement.setString(1, title);

            ResultSet resultSet = statement.executeQuery();

            // For each event in the result set
            while (resultSet.next()) {
                // Retrieve data from the result set
                events.add(constructEventFromResultSet(resultSet));
            }
        } catch (Exception e) {
            // Print error if event retrieval fails
            ErrorHandler.handleException(e);
        }
        // return list of events (regardless of none found)
        return events;
    }

    /**
     * Removes calendar entries based on a list of IDs.
     *
     * @param calendar The calendar from which entries will be removed.
     * @param entryIdsToRemove List of entry IDs targeted for removal.
     */
    public void removeEntriesById(Calendar calendar, List<String> entryIdsToRemove) {
        List<Entry<?>> entries = new ArrayList<>(calendar.findEntries("")); // Retrieves all entries

        entries.stream()
                .filter(entry -> entryIdsToRemove.contains(entry.getId()))
                .forEach(entry -> {
                    calendar.removeEntry(entry);
                });
    }





    /**
     * Retrieves an Event from the database by its ID.
     *
     * @param id The ID of the Event to be retrieved.
     * @return The Event object if found, or null if no matching event is found.
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
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return constructEventFromResultSet(resultSet);
                }
            }
        } catch (Exception e) {
            // Print error if event retrieval fails
            ErrorHandler.handleException(e);
        }
        // return null if event not found
        return null;
    }

    /**
     * Retrieves all Events from the database.
     *
     * @return A list of all Event objects stored in the database.
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
                events.add(constructEventFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            // Print error if event retrieval fails
            ErrorHandler.handleException(e);
        }
        // return list of events (regardless of none found)
        return events;
    }
}