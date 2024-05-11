package com.serenitask.model;
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
    private LocalDateTime startTime; // Start time of the event
    private int duration; // Duration of the event
    private Boolean fullDay; // If the event is full day
    private Boolean staticPos; // If the event is static
    private String calendar; // Calendar the event belongs to
    private String recurrenceRules; // Recurrence rules for the event
    private LocalDate allocatedUntil; // Date until the events recurrence ends

    /**
     * Event constructor for an empty event
     * @constructor
     */
    public Event() {}

    /**
     * Main Event constructor
     * @constructor
     * @param id - Event ID
     * @param title - Title of the event
     * @param location - Location of the event
     * @param startTime - Start time of the event
     * @param duration - Duration of the event
     * @param fullDay - Boolean value to check if the event is full day
     * @param staticPos - Boolean value to check if the event is static
     * @param calendar - Calendar the event belongs to
     * @param recurrenceRules - Recurrence rules for the event
     * @param allocatedUntil - Date until the events recurrence ends
     */
    public Event(
            String id,
            String title,
            String location,
            LocalDateTime startTime,
            int duration,
            Boolean fullDay,
            Boolean staticPos,
            String calendar,
            String recurrenceRules,
            LocalDate allocatedUntil
    ) {
        // Validate values
        if (title == null || title.isEmpty()) throw new IllegalArgumentException("Title cannot be null or empty");
        if (startTime == null) throw new IllegalArgumentException("Start time cannot be null");
        if (duration < 0) throw new IllegalArgumentException("Duration cannot be negative");
        // Set values
        this.id = id;
        this.title = title;
        this.location = location;
        this.startTime = startTime;
        this.duration = duration;
        this.fullDay = fullDay;
        this.staticPos = staticPos;
        this.calendar = calendar;
        this.recurrenceRules = recurrenceRules;
        this.allocatedUntil = allocatedUntil;
    }

    /**
     * Retrieves the event ID
     * @return - Event ID
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the event ID
     * @param id - Event ID
     */
    public void setId(String id) {
        // Validate ID
        if (id.isEmpty()) throw new IllegalArgumentException("ID cannot be empty");
        this.id = id;
    }

    /**
     * Retrieves the event title
     * @return - Event title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the event title
     * @param title - Event title
     */
    public void setTitle(String title) {
        // Validate title
        if (title.isEmpty()) throw new IllegalArgumentException("Title cannot be empty");
        this.title = title;
    }

    /**
     * Retrieves the event location
     * @return - Event location
     */
    public String getLocation() {
        return location;
    }

    /**
     * Sets the event location
     * @param location - Event location
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * Retrieves the start time of the event
     * @return - Event start time
     */
    public LocalDateTime getStartTime() {
        return startTime;
    }

    /**
     * Sets the start time of the event
     * @param startTime - Event start time
     */
    public void setStartTime(LocalDateTime startTime) {
        // Validate start time
        if (startTime == null) throw new IllegalArgumentException("Start time cannot be null");
        this.startTime = startTime;
    }

    /**
     * Retrieves the duration of the event
     * @return - Event duration
     */
    public int getDuration() {
        return duration;
    }

    /**
     * Sets the duration of the event
     * @param duration - Event duration
     */
    public void setDuration(int duration) {
        // Validate duration
        if (duration < 0) throw new IllegalArgumentException("Duration cannot be negative");
        this.duration = duration;
    }

    /**
     * Retrieves the full day status of the event
     * @return - Event full day status
     */
    public Boolean getFullDay() {
        return fullDay;
    }

    /**
     * Sets the full day status of the event
     * @param fullDay - Event full day status
     */
    public void setFullDay(Boolean fullDay) {
        this.fullDay = fullDay;
    }

    /**
     * Retrieves the static position status of the event
     * @return - Event static position status
     */
    public Boolean getStaticPos() {
        return staticPos;
    }

    /**
     * Sets the static position status of the event
     * @param staticPos - Event static position status
     */
    public void setStaticPos(Boolean staticPos) {
        this.staticPos = staticPos;
    }

    /**
     * Retrieves the calendar the event belongs to
     * @return - Event calendar
     */
    public String getCalendar() {
        return calendar;
    }

    /**
     * Sets the calendar the event belongs to
     * @param calendar - Event calendar
     */
    public void setCalendar(String calendar) {
        this.calendar = calendar;
    }

    /**
     * Retrieves the recurrence rules for the event
     * @return - Event recurrence rules
     */
    public String getRecurrenceRules() {
        return recurrenceRules;
    }

    /**
     * Sets the recurrence rules for the event
     * @param recurrenceRules - Event recurrence rules
     */
    public void setRecurrenceRules(String recurrenceRules) {
        // Validate recurrence rules. To be implemented
        this.recurrenceRules = recurrenceRules;
    }

    /**
     * Retrieves the date until the events recurrence ends
     * @return - Event allocated until date
     */
    public LocalDate getAllocatedUntil() {
        return allocatedUntil;
    }

    /**
     * Sets the date until the events recurrence ends
     * @param allocatedUntil - Event allocated until date
     */
    public void setAllocatedUntil(LocalDate allocatedUntil) {
        // Validate allocated until. To be implemented
        this.allocatedUntil = allocatedUntil;
    }
}
