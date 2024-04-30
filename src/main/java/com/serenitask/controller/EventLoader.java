package com.serenitask.controller;

import com.calendarfx.model.Calendar;
import com.calendarfx.model.Calendar.Style;
import com.calendarfx.model.CalendarSource;
import com.calendarfx.model.Entry;
import com.calendarfx.model.Interval;

import com.serenitask.util.DatabaseManager.*;

import com.serenitask.model.Event;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.time.Duration;

public class EventLoader {

    private List<String> defaultCalendars =  new ArrayList<>();

    public EventLoader()
    {
        defaultCalendars.add("Personal Events");
        defaultCalendars.add("Study");
        defaultCalendars.add("Work");
    }

    private Calendar setColor(Calendar calendar)
    {
        switch(calendar.getName())
        {
            case "Personal Events":
                calendar.setStyle(Style.STYLE5);
                return calendar;
            case "Study":
                calendar.setStyle(Style.STYLE6);
                return calendar;
            case "Work":
                calendar.setStyle(Style.STYLE7);
                return calendar;
            default:
                return calendar;
        }
    }

    private Entry<?> convertEventToEntry(Event event){

        // This needs to turn from an entry to an Event class and retreive ID if it is not stored;

        Entry<?> newEntry = new Entry<>(event.getTitle(),event.getId());
        Interval dummyInt = new Interval();


        newEntry.setLocation(event.getLocation());
        newEntry.setInterval(dummyInt);
        newEntry.setFullDay(event.getFullDay());
        newEntry.setRecurrenceRule(event.getRecurrenceRules());    
        return newEntry;
    }
    

    private CalendarSource createNew()
    {
        Calendar personal = new Calendar("Personal Events");
        Calendar study = new Calendar("Study");
        Calendar work = new Calendar("Work");
        
        personal.setShortName("P");
        study.setShortName("S");
        work.setShortName("W");

        EventListener setEventListener = new EventListener();
        setEventListener.setupListeners(personal);
        setEventListener.setupListeners(study);
        setEventListener.setupListeners(work);

        // Colours can be specified to meet colour blind needs
        personal.setStyle(Style.STYLE5);
        study.setStyle(Style.STYLE7);
        work.setStyle(Style.STYLE6);

        CalendarSource mainCalendarSource = new CalendarSource("Main");
        mainCalendarSource.getCalendars().addAll(personal, study, work);

        return mainCalendarSource;
    }


    public CalendarSource loadEventsFromDatabase() {
        
        EventDAO eventDAO = new EventDAO();
        List<Event> unsortedEvents = eventDAO.getAllEvents();

        if (unsortedEvents.size() == 0)
        {
            return createNew();
        }
        else
        {

            List<String> uniqueCalendars = new ArrayList<>();

            for (String name : defaultCalendars)
            {
                uniqueCalendars.add(name);
            }

            for(Event event : unsortedEvents)
            {
                if (!uniqueCalendars.contains((event.getCalendar())))
                {
                    uniqueCalendars.add(event.getCalendar());
                }
            }

            List<Calendar> calendars = new ArrayList<>();
            EventListener eventListener = new EventListener();

            for(String calendarName : uniqueCalendars)
            {

                Calendar newCalendar = new Calendar<>(calendarName);

                newCalendar = setColor(newCalendar);
                calendars.add(newCalendar);
            }

            for(Event event : unsortedEvents)
            {
                for(Calendar calendar : calendars)
                {
                    if (Objects.equals(event.getCalendar(), calendar.getName()))
                    {
                        Entry<?> entry = convertEventToEntry(event);
                        entry.setCalendar(calendar);
                        calendar.addEntry(entry);
                    }
                }
            }

            CalendarSource loadedCalendar = new CalendarSource();
            for(Calendar calendar : calendars)
            {
                calendar = eventListener.setupListeners(calendar);
                loadedCalendar.getCalendars().add(calendar);
            }

            return loadedCalendar;
        
    }
    }
}