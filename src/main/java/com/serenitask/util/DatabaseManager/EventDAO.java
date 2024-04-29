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
            String query = "CREATE TABLE IF NOT EXIST events ("
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

    public int addEvent(Event event) {
        try {
            // Create insert query
            String query = "INSERT INTO events (title, description, location, startTime, duration, fullDay, staticPos, calendar, recurrenceRules, recurrenceEnd) VALUES" +
                    "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            // Update new row with values from event
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
            // Execute update
            statement.executeUpdate();
            // Set the id of the new event
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                event.setId(generatedKeys.getInt(1));
            }
            return event.getId();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // return -1 if event wasn't created
        return -1;
    }

    public boolean updateEvent(Event event) {
        try {
            // Create update query
            String query = "UPDATE events SET " +
                    "title = ?," +
                    "description = ?," +
                    "location = ?," +
                    "startTime = ?," +
                    "duration = ?," +
                    "fullDay = ?," +
                    "staticPos = ?," +
                    "calendar = ?," +
                    "recurrenceRules = ?," +
                    "recurrenceEnd = ?" +
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
            statement.setInt(11, event.getId());
            // Execute update
            statement.executeUpdate();

            // If success
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // To be brought pack as an optional.
    /*
    public boolean deleteEvent(int id) {
        try {
            // Create delete query
            String query = "DELETE FROM events WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            // Delete row id
            statement.setInt(1, id);
            // Execute update
            statement.executeUpdate();

            // If success
            return true;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
     */

    public Event getEventById(int id) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM events WHERE id = ?");
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
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

                Event event = new Event(title, description, location, startTime, duration, fullDay, staticPos, calendar, recurrenceRules, recurrenceEnd);
                event.setId(id);
                return event;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Needed: Require the method getAllEvents to have two different parameter inputs. Dates are passed as Java dates.
    // getAllEvents(date) - To get all events associated with that day/date (Note that the date provided may be in DateTime format
    // getAllEvents(startDate, endDate) - Get all events between two dates

    public List<Event> getAllEvents() {
        List<Event> events = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            String query = "SELECT * FROM events";

            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                // Retrieve data from the result set
                int id = resultSet.getInt("id");
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
                Event event = new Event(title, description, location, startTime, duration, fullDay, staticPos, calendar, recurrenceRules, recurrenceEnd);
                event.setId(id);
                events.add(event);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return events;
    }
}
