package com.serenitask.controller;

import com.calendarfx.model.Calendar;
import com.calendarfx.model.Calendar.Style;
import com.calendarfx.model.CalendarSource;
import com.calendarfx.model.Entry;
import com.calendarfx.model.Interval;

import com.serenitask.util.DatabaseManager.*;

import com.serenitask.model.Event;

import java.time.LocalDateTime;
import java.util.*;

/**
 * Class handling loading and creation of calendars. Handles loading in persist entries from database or creating a new
 * calendar set if one does not exist.
 */
public class EventLoader {

    private List<String> defaultCalendars = new ArrayList<>();

    /**
     * Default constructor for EventLoader. Sets a default calendar for loading.
     */
    public EventLoader() {
        defaultCalendars.add("Personal Events");
        defaultCalendars.add("Goals");
        defaultCalendars.add("Health");
    }

    /**
     * Sets the color palette of the given calendar
     *
     * @param calendar Object representing a calendar
     * @return Calendar object with style adjusted
     */
    private Calendar setColor(Calendar calendar) {
        switch (calendar.getName()) {
            case "Personal Events":
                calendar.setStyle(Style.STYLE1);
                return calendar;
            case "Goals":
                calendar.setStyle(Style.STYLE2);
                return calendar;
            case "Health":
                calendar.setStyle(Style.STYLE3);
                return calendar;
            default:
                return calendar;
        }
    }

    /**
     * Converts a model Event object to a calendarFX Entry<>
     *
     * @param event Object representing an event in the calendar
     * @return Entry object representing an event in the calendar
     */
    private Entry<?> convertEventToEntry(Event event) {
        Entry<?> newEntry = new Entry<>(event.getTitle(), event.getId());

        Interval interval = event.getInterval();

        newEntry.setLocation(event.getLocation());
        newEntry.setInterval(interval);
        //newEntry.setInterval(new Interval(event.getStartDate(), startTime.toLocalTime(), event.getEndDate(), endTime.toLocalTime()));

        newEntry.setFullDay(event.getFullDay());
        newEntry.setRecurrenceRule(event.getRecurrenceRules());
        return newEntry;
    }


    /**
     * Creates a new set of calendars based on presets. Runs on first launch only.
     *
     * @return CalendarSource object containing empty calendars
     */
    private CalendarSource createNew() {
        Calendar personal = new Calendar("Personal Events");
        Calendar goals = new Calendar("Goals");
        Calendar health = new Calendar("Health");

        personal.setShortName("P");
        goals.setShortName("G");
        health.setShortName("H");

        EventListener setEventListener = new EventListener();
        setEventListener.setupListeners(personal);
        setEventListener.setupListeners(goals);
        setEventListener.setupListeners(health);

        // Colours can be specified to meet colour blind needs
        personal.setStyle(Style.STYLE1);
        goals.setStyle(Style.STYLE2);
        health.setStyle(Style.STYLE3);

        CalendarSource mainCalendarSource = new CalendarSource("Main");
        mainCalendarSource.getCalendars().addAll(personal, goals, health);

        return mainCalendarSource;
    }


    /**
     * Loads in events from Events Database table. Events are loaded and allocated to their preexisting calendar object
     *
     * @return CalendarSource object containing populated calendars
     */
    public CalendarSource loadEventsFromDatabase() {

        EventDAO eventDAO = new EventDAO();
        List<Event> unsortedEvents = eventDAO.getAllEvents();

        if (unsortedEvents.size() == 0) {
            return createNew();
        } else {

            List<String> uniqueCalendars = new ArrayList<>();

            for (String name : defaultCalendars) {
                uniqueCalendars.add(name);
            }

            for (Event event : unsortedEvents) {
                if (!uniqueCalendars.contains((event.getCalendar()))) {
                    uniqueCalendars.add(event.getCalendar());
                }
            }

            List<Calendar> calendars = new ArrayList<>();
            EventListener eventListener = new EventListener();

            for (String calendarName : uniqueCalendars) {

                Calendar newCalendar = new Calendar<>(calendarName);

                newCalendar = setColor(newCalendar);
                calendars.add(newCalendar);
            }

            for (Event event : unsortedEvents) {
                for (Calendar calendar : calendars) {
                    if (Objects.equals(event.getCalendar(), calendar.getName())) {
                        Entry<?> entry = convertEventToEntry(event);
                        entry.setCalendar(calendar);
                        calendar.addEntry(entry);
                    }
                }
            }

            CalendarSource loadedCalendar = new CalendarSource();
            for (Calendar calendar : calendars) {
                calendar = eventListener.setupListeners(calendar);
                loadedCalendar.getCalendars().add(calendar);
            }

            return loadedCalendar;

        }
    }
}