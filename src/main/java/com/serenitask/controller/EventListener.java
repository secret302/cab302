package com.serenitask.controller;

import com.calendarfx.model.Interval;
import com.serenitask.util.DatabaseManager.EventDAO;
import com.serenitask.model.Event;

import com.calendarfx.model.Calendar;
import com.calendarfx.model.CalendarEvent;
import com.calendarfx.model.Entry;

import javafx.event.EventHandler;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Class responsible for managing calendar events and interfacing with the EventDAO for database operations.
 */
public class EventListener {

    private Calendar calendar;
    EventDAO eventDao = new EventDAO();

    /**
     * Default Constructor for Event Listen
     */
    public EventListener() {

    }

    /**
     * Sets up event handlers for the calendar events.
     *
     * @param calendar The Calendar instance where event listeners are to be added.
     * @return The Calendar instance with attached event handlers.
     */
    public Calendar setupListeners(Calendar calendar) {
        this.calendar = calendar;
        calendar = setUpEventListeners();
        return calendar;
    }

    /**
     * Attaches specific event listeners to the calendar for handling CRUD operations.
     *
     * @return The modified Calendar instance with attached event handlers.
     */
    private Calendar setUpEventListeners() {
        EventHandler<CalendarEvent> addHandler = new EventHandler<CalendarEvent>() {
            public void handle(CalendarEvent incomingEvent) {
                try {
                    if (incomingEvent.isEntryAdded()) {
                        Entry<?> newEntry = incomingEvent.getEntry();
                        // Convert CalendarFX Entry to your Event model and save to database
                        Event event = convertToEventModel(newEntry);
                        System.out.println("New Event: " + event.getId());

                        eventDao.addEvent(event);
                    } else if (incomingEvent.isEntryRemoved()) {
                        Entry<?> newEntry = incomingEvent.getEntry();
                        // Convert CalendarFX Entry to your Event model and save to database
                        System.out.println("Removed Event: " + newEntry.getId());

                        eventDao.deleteEvent(newEntry.getId());
                    } else {
                        Entry<?> newEntry = incomingEvent.getEntry();
                        // Convert CalendarFX Entry to your Event model and save to database
                        Event event = convertToEventModel(newEntry);
                        System.out.println("Updating Event: " + event.getId());

                        eventDao.updateEvent(event);

                    }
                } catch(Exception e) {
                    System.err.println("An Error has Occured:" + e.getMessage());
                    e.printStackTrace();
                }
            }
        };
        calendar.addEventHandler(addHandler);
        return calendar;
    }

    /**
     * Converts a CalendarFX Entry to a custom Event model.
     *
     * @param entry The CalendarFX Entry to be converted.
     * @return An Event object populated with data from the Entry.
     */
    private Event convertToEventModel(Entry<?> entry) {

        // This needs to turn from an entry to an Event class and retreive ID if it is not stored;

        String id = entry.getId();
        String title = entry.getTitle();
        String location = entry.getLocation();
        Interval interval = entry.getInterval();
        Boolean fullDay = entry.isFullDay();
        Boolean staticPos = false;
        String calendar = entry.getCalendar().getName();
        String recurrenceRules = entry.getRecurrenceRule();
        LocalDate allocatedUntil = LocalDate.now().plusDays(7); // entry.getRecurrenceRule(); This is a temporary solution until recurrence end can be grabbed from the String saved recurrence rules


        Event event = new Event(id, title, location, interval, fullDay, staticPos, calendar, recurrenceRules, allocatedUntil);
        return event;
    }

}
