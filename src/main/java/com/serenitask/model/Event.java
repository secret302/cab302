package com.serenitask.model;
import java.time.LocalDate;
import java.time.LocalDateTime;


public class Event {

    private String id;
    private String title;
    private String description;
    private String location;
    private LocalDateTime start_time;
    private LocalDate start_date;
    private LocalDate end_date;
    private int duration;
    private Boolean fullDay;
    private Boolean staticPos;
    private String calendar;
    private String recurrenceRules;
    private String recurrenceEnd;

    public Event(String id, String title, String description, String location, LocalDateTime start_time, LocalDate start_date, LocalDate end_date, int duration, Boolean fullDay,
                 Boolean staticPos, String calendar, String recurrenceRules, String recurrenceEnd)
    {
        this.id = id;
        this.title = title;
        this.description = description;
        this.location = location;
        this.start_time = start_time;
        this.start_date = start_date;
        this.end_date = end_date;
        this.duration = duration;
        this.fullDay = fullDay;
        this.staticPos = staticPos;
        this.calendar = calendar;
        this.recurrenceRules = recurrenceRules;
        this.recurrenceEnd = recurrenceEnd;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public LocalDateTime getStartTime() {
        return start_time;
    }
    public LocalDate getStartDate() {
        return start_date;
    }

    public void setStartDate(LocalDate start_date) {
        this.start_date = start_date;
    }

    public LocalDate getEndDate() {
        return end_date;
    }

    public void setEndDate(LocalDate end_date) {
        this.end_date = end_date;
    }

    public void setStartTime(LocalDateTime start_time) {
        this.start_time = start_time;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public Boolean getFullDay() {
        return fullDay;
    }

    public void setFullDay(Boolean fullDay) {
        this.fullDay = fullDay;
    }

    public Boolean getStaticPos() {
        return staticPos;
    }

    public void setStaticPos(Boolean staticPos) {
        this.staticPos = staticPos;
    }

    public String getCalendar() {
        return calendar;
    }

    public void setCalendar(String calendar) {
        this.calendar = calendar;
    }

    public String getRecurrenceRules() {
        return recurrenceRules;
    }

    public void setRecurrenceRules(String recurrenceRules) {
        this.recurrenceRules = recurrenceRules;
    }

    public String getRecurrenceEnd() {
        return recurrenceEnd;
    }

    public void setRecurrenceEnd(String recurrenceEnd) {
        this.recurrenceEnd = recurrenceEnd;
    }

}
