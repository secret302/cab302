package com.serenitask.controller;

import com.serenitask.util.DatabaseManager.EventDAO;
import com.serenitask.model.Event;

import com.calendarfx.model.Calendar;
import com.calendarfx.model.CalendarEvent;
import com.calendarfx.model.Entry;

import javafx.event.EventHandler;




public class EventListener {

    private Calendar calendar;
    EventDAO eventDao = new EventDAO();
    
    public EventListener() 
    {
        
    }

    public Calendar setupListeners(Calendar calendar)
    {
        this.calendar = calendar;
        calendar = setUpEventListeners();
        return calendar;
    }


    private Calendar setUpEventListeners() {
        EventHandler<CalendarEvent> addHandler = new EventHandler<CalendarEvent>() {
            
            public void handle(CalendarEvent incomingEvent) {
                if (incomingEvent.isEntryAdded()) {
                    Entry<?> newEntry = incomingEvent.getEntry();
                    // Convert CalendarFX Entry to your Event model and save to database
                    Event event = convertToEventModel(newEntry);
                    System.out.println("New Event: " + event.getId());
                    
                    eventDao.addEvent(event);
                }
                else if (incomingEvent.isEntryRemoved())
                {
                    Entry<?> newEntry = incomingEvent.getEntry();
                    // Convert CalendarFX Entry to your Event model and save to database
                    System.out.println("Removed Event: " + newEntry.getId());

                    eventDao.deleteEvent(newEntry.getId());
                }
                else
                {
                    Entry<?> newEntry = incomingEvent.getEntry();
                    // Convert CalendarFX Entry to your Event model and save to database
                    Event event = convertToEventModel(newEntry);
                    System.out.println("Updating Event: " + event.getId());
                    
                    eventDao.updateEvent(event);

                }
            }
        };
        calendar.addEventHandler(addHandler);
        return calendar;
    }


    private Event convertToEventModel(Entry<?> entry){

        // This needs to turn from an entry to an Event class and retreive ID if it is not stored;

        String id = entry.getId();
        String title = entry.getTitle();
        String description = "Doesnt exist yet";
        String location = entry.getLocation();
        String startTime = "Start time value"; // Doesnt work yet
        int duration = entry.getDuration().toSecondsPart();
        Boolean fullDay = entry.isFullDay();
        Boolean staticPos = false;
        String calendar = entry.getCalendar().getName();
        String recurrenceRules = entry.getRecurrenceRule();
        String recurrenceEnd = null; // Date objects break
        

        Event event = new Event(id, title, description, location, startTime, duration, fullDay, staticPos, calendar, recurrenceRules, recurrenceEnd);
        return event;
    }
   
}