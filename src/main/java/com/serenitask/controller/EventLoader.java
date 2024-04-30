package com.serenitask.controller;

import com.calendarfx.model.Calendar;
import com.serenitask.util.DatabaseManager.SqliteConnection;
import com.serenitask.model.Event;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventLoader {

    public void loadEventsFromDatabase() {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = SqliteConnection.getConnection();
            String query = "SELECT * FROM events";
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            //map to store events based on calendar name
            Map<String, List<Event>> eventsByCalendar = new HashMap<>();

            while (resultSet.next()) {
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

                //creates new event object and adds the event to teh cal
                Event event = new Event(id, title, description, location, startTime, duration, fullDay, staticPos, calendar, recurrenceRules, recurrenceEnd);
                List<Event> events = eventsByCalendar.getOrDefault(calendar, new ArrayList<>());
                events.add(event);
                eventsByCalendar.put(calendar, events);
            }


//handle the exceptions (if any)
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            //try and close the connection to the DB (we should be doing this)
            try {
                if (resultSet != null) resultSet.close();
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
                //check if there are any exceptions to catch from the closure of the DB connections.
           } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
