package com.serenitask.model;

import com.calendarfx.model.Interval;

import java.time.LocalDate;

/**
 * Represents a calendar event with properties like title, location, time interval, recurrence rules, and more.
 * This class enforces data integrity through input validation within its methods.
 */
public class Event {

    /**
     * Unique identifier of the event.
     */
    private String id;
    /**
     * Title or name of the event.
     * */
    private String title;
    /**
     * Location where the event takes place.
     */
    private String location;
    /**
     * Time interval during which the event occurs.
     */
    private Interval interval;
    /**
     * Flag indicating if the event spans the entire day.
     */
    private Boolean fullDay;
    /**
     * Flag indicating if the event has a fixed position (not yet implemented).
     */
    private Boolean staticPos;
    /**
     * Name of the calendar to which the event belongs.
     */
    private String calendar;
    /** Recurrence rules specifying how the event repeats.
     *
     */
    private String recurrenceRules;
    /** Date until which the event's recurrence is valid.
     *
     */
    private LocalDate allocatedUntil;

    /**
     * Default constructor for creating an empty Event object.
     */
    public Event() {}

    /**
     * Constructor to create an Event object with all its properties.
     *
     * @param id              Unique identifier for the event. Cannot be null or empty.
     * @param title           Title of the event. Cannot be null or empty.
     * @param location        Location of the event.
     * @param interval        Time interval of the event.
     * @param fullDay         Indicates if the event spans the entire day.
     * @param staticPos       Indicates if the event has a fixed position (not yet implemented).
     * @param calendar        Name of the calendar the event belongs to.
     * @param recurrenceRules Rules for event recurrence.
     * @param allocatedUntil  Date until which the recurrence is valid.
     * @throws IllegalArgumentException If id or title are null or empty.
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
        // Validate input
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("ID cannot be null or empty");
        }
        if (title == null || title.isEmpty()) {
            throw new IllegalArgumentException("Title cannot be null or empty");
        }
        // Set properties
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
     * Gets the unique identifier of the event.
     * @return The event ID.
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the unique identifier of the event.
     * @param id The new ID for the event. Cannot be null or empty.
     * @throws IllegalArgumentException If the provided ID is null or empty.
     */
    public void setId(String id) {
        // Validate input
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("ID cannot be empty or null");
        }
        // Set the ID
        this.id = id;
    }

    /**
     * Gets the title of the event.
     * @return The event title.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title of the event.
     * @param title The new title for the event. Cannot be null or empty.
     * @throws IllegalArgumentException If the provided title is null or empty.
     */
    public void setTitle(String title) {
        // Validate input
        if (title == null || title.isEmpty()) {
            throw new IllegalArgumentException("Title cannot be empty or null");
        }
        // Set the title
        this.title = title;
    }

    /**
     * Gets the location of the event.
     * @return The event location.
     */
    public String getLocation() {
        return location;
    }

    /**
     * Sets the location of the event.
     * @param location The new location for the event.
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * Gets the time interval of the event.
     * @return The event's time interval.
     */
    public Interval getInterval() {
        return interval;
    }

    /**
     * Sets the time interval of the event.
     * @param interval The new time interval for the event.
     */
    public void setInterval(Interval interval) {
        this.interval = interval;
    }

    /**
     * Checks if the event spans the whole day.
     * @return True if the event is a full-day event, false otherwise.
     */
    public Boolean getFullDay() {
        return fullDay;
    }

    /**
     * Sets the full-day status of the event.
     * @param fullDay The new full-day status for the event.
     */
    public void setFullDay(Boolean fullDay) {
        this.fullDay = fullDay;
    }

    /**
     * Checks if the event has a static position.
     * @return True if the event has a static position, false otherwise.
     */
    public Boolean getStaticPos() {
        return staticPos;
    }

    /**
     * Sets the static position status of the event.
     * @param staticPos The new static position status for the event.
     */
    public void setStaticPos(Boolean staticPos) {
        this.staticPos = staticPos;
    }

    /**
     * Gets the name of the calendar this event belongs to.
     * @return The name of the event's calendar.
     */
    public String getCalendar() {
        return calendar;
    }

    /**
     * Sets the calendar to which this event belongs.
     * @param calendar The new calendar name for the event.
     */
    public void setCalendar(String calendar) {
        this.calendar = calendar;
    }

    /**
     * Gets the recurrence rules for this event.
     * @return The event's recurrence rules.
     */
    public String getRecurrenceRules() {
        return recurrenceRules;
    }


    /**
     * Gets the date until which the event's recurrence is valid.
     * @return The date until which the event recurs.
     */
    public LocalDate getAllocatedUntil() {
        return allocatedUntil;
    }

    /**
     * Sets the date until which the event's recurrence is valid.
     * @param allocatedUntil The new date until which the event recurs.
     */
    public void setAllocatedUntil(LocalDate allocatedUntil) {
        this.allocatedUntil = allocatedUntil;
    }
}