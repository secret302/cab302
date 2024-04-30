package com.serenitask.model;

public class Goal {

    private int id;
    private String title;
    private String description;
    private int minChunk;
    private int maxChunk;
    private int periodicity;
    private String endDate;
    private String recurrenceRules;


    public Goal(String title, String description, int minChunk, int maxChunk, int periodicity, String endDate,
                String recurrenceRules)
    {
        this.title = title;
        this.description = description;
        this.minChunk = minChunk;
        this.maxChunk = maxChunk;
        this.periodicity = periodicity;
        this.endDate = endDate;
        this.recurrenceRules = recurrenceRules;
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

    public int getMinChunk() {
        return minChunk;
    }

    public void setMinChunk(int minChunk) {
        this.minChunk = minChunk;
    }

    public int getMaxChunk() {
        return maxChunk;
    }

    public void setMaxChunk(int maxChunk) {
        this.maxChunk = maxChunk;
    }

    public int getPeriodicity() {
        return periodicity;
    }

    public void setPeriodicity(int periodicity) {
        this.periodicity = periodicity;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getRecurrenceRules() {
        return recurrenceRules;
    }

    public void setRecurrenceRules(String recurrenceRules) {
        this.recurrenceRules = recurrenceRules;
    }

}
