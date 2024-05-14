package com.serenitask.model;
import com.calendarfx.model.Interval;

import java.time.LocalDate;
import java.time.LocalDateTime;


/**
 * Event class represents an event in the calendar
 */
public class Event {
    // Private Event attributes
    private String id; // Event ID
    private String title; // Title of the event
    private String location; // Location of the event
    private Interval interval; // Interval of the event (stores start and end date/time)
    private Boolean fullDay; // If the event is full day
    private Boolean staticPos; // If the event is static
    private String calendar; // Calendar the event belongs to
    private String recurrenceRules; // Recurrence rules for the event
    private LocalDate allocatedUntil; // Date until the events recurrence ends

    /**
     * Event constructor for an empty event
     */
    public Event() {}

    /**
     * Main Event constructor
     * @param id Event ID
     * @param title Title of the event
     * @param location Location of the event
     * @param interval Interval of the event
     * @param fullDay Boolean value to check if the event is full day
     * @param staticPos Boolean value to check if the event is static
     * @param calendar Calendar the event belongs to
     * @param recurrenceRules Recurrence rules for the event
     * @param allocatedUntil Date until the events recurrence ends
     */
    public Event(
            String id,
            String title,
            String location,
            Interval interval,
            Boolean fullDay,
            Boolean staticPos,
            String calendar,
            String recurrenceRules,
            LocalDate allocatedUntil
    ) {
        // Validate values
        if (id == null || id.isEmpty()) throw new IllegalArgumentException("ID cannot be null or empty");
        if (title == null || title.isEmpty()) throw new IllegalArgumentException("Title cannot be null or empty");
        // Set values
        this.id = id;
        this.title = title;
        this.location = location;
        this.interval = interval;
        this.fullDay = fullDay;
        this.staticPos = staticPos;
        this.calendar = calendar;
        this.recurrenceRules = recurrenceRules;
        this.allocatedUntil = allocatedUntil;
    }

    /**
     * Retrieves the event ID
     * @return Event ID
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the event ID
     * @param id Event ID
     */
    public void setId(String id) {
        // Validate ID
        if (id == null || id.isEmpty()) throw new IllegalArgumentException("ID cannot be empty or null");
        this.id = id;
    }

    /**
     * Retrieves the event title
     * @return Event title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the event title
     * @param title Event title
     */
    public void setTitle(String title) {
        // Validate title
        if (title == null || title.isEmpty()) throw new IllegalArgumentException("Title cannot be empty or null");
        this.title = title;
    }

    /**
     * Retrieves the event location
     * @return Event location
     */
    public String getLocation() {
        return location;
    }

    /**
     * Sets the event location
     * @param location Event location
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * Retrieves the event interval
     * @return Event interval
     */
    public Interval getInterval() {
        return interval;
    }

    /**
     * Sets the event interval
     * @param interval Event interval
     */
    public void setInterval(Interval interval) {
        this.interval = interval;
    }

    /**
     * Retrieves the full day status of the event
     * @return Event full day status
     */
    public Boolean getFullDay() {
        return fullDay;
    }

    /**
     * Sets the full day status of the event
     * @param fullDay Event full day status
     */
    public void setFullDay(Boolean fullDay) {
        this.fullDay = fullDay;
    }

    /**
     * Retrieves the static position status of the event
     * @return Event static position status
     */
    public Boolean getStaticPos() {
        return staticPos;
    }

    /**
     * Sets the static position status of the event
     * @param staticPos Event static position status
     */
    public void setStaticPos(Boolean staticPos) {
        this.staticPos = staticPos;
    }

    /**
     * Retrieves the calendar the event belongs to
     * @return Event calendar
     */
    public String getCalendar() {
        return calendar;
    }

    /**
     * Sets the calendar the event belongs to
     * @param calendar Event calendar
     */
    public void setCalendar(String calendar) {
        this.calendar = calendar;
    }

    /**
     * Retrieves the recurrence rules for the event
     * @return Event recurrence rules
     */
    public String getRecurrenceRules() {
        return recurrenceRules;
    }

    /**
     * Sets the recurrence rules for the event
     * @param recurrenceRules Event recurrence rules
     */
    public void setRecurrenceRules(String recurrenceRules) {
        // Validate recurrence rules. To be implemented
        this.recurrenceRules = recurrenceRules;
    }

    /**
     * Retrieves the date until the events recurrence ends
     * @return Event allocated until date
     */
    public LocalDate getAllocatedUntil() {
        return allocatedUntil;
    }

    /**
     * Sets the date until the events recurrence ends
     * @param allocatedUntil Event allocated until date
     */
    public void setAllocatedUntil(LocalDate allocatedUntil) {
        // Validate allocated until. To be implemented
        this.allocatedUntil = allocatedUntil;
    }
}
