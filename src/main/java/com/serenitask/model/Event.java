package com.serenitask.model;

public class Event {

    private int id;
    private String title;
    private String description;
    private String location;
    private String start_time;
    private int duration;
    private Boolean fullDay;
    private Boolean staticPos;
    private String calendar;
    private String recurrenceRules;
    private String recurrenceEnd;

    public Event(String title, String description, String location, String start_time, int duration, Boolean fullDay,
                 Boolean staticPos, String calendar, String recurrenceRules, String recurrenceEnd)
    {

    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
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

    public String getStartTime() {
        return start_time;
    }

    public void setStartTime(String start_time) {
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
