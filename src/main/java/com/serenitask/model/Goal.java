package com.serenitask.model;

import java.time.LocalDate;

/**
 * Goal class represents a goal in the application
 */
public class Goal {
    // Private goal attributes
    private int id;
    private String title;
    private int targetAmount;
    private int minChunk;
    private int maxChunk;
    private LocalDate allocatedUntil;
    private int daysOutstanding;

    // Removed for now
    // private int periodicity;
    // private String endDate;
    // private String recurrenceRules;
    // private Boolean Simple;


    /**
     * Goal constructor for an empty goal
     * @constructor
     * @param title Title of the goal
     * @param targetAmount Target amount of the goal
     * @param minChunk Minimum amount of time to allocate
     * @param maxChunk Maximum amount of time to allocate
     * @param allocatedUntil Date until the goal is allocated
     * @param daysOutstanding Days outstanding for the goal
     */
    public Goal(
            String title,
            int targetAmount,
            int minChunk,
            int maxChunk,
            LocalDate allocatedUntil,
            int daysOutstanding
    ) {
        // Validate values
        if (title == null || title.isEmpty()) throw new IllegalArgumentException("Title cannot be null or empty");
        if (targetAmount < 0) throw new IllegalArgumentException("Goal target amount cannot be negative");
        if (minChunk < 15) throw new IllegalArgumentException("Minimum chunk cannot be below 15 minutes");
        if (maxChunk < 15) throw new IllegalArgumentException("Maximum chunk cannot be below 15 minutes");
        if (minChunk > maxChunk) throw new IllegalArgumentException("Minimum chunk cannot be greater than maximum chunk");
        // Set values
        this.title = title;
        this.targetAmount = targetAmount;
        this.minChunk = minChunk;
        this.maxChunk = maxChunk;
        this.allocatedUntil = allocatedUntil;
        this.daysOutstanding = daysOutstanding;
    }

    /**
     * Goal constructor for an simple goal
     * @constructor
     * @param title Title of the goal
     * @param minChunk Minimum amount of time to allocate
     * @param maxChunk Maximum amount of time to allocate
     */
    public Goal(String title, int minChunk, int maxChunk)
    {
        // Validate values
        if (title == null || title.isEmpty()) throw new IllegalArgumentException("Title cannot be null or empty");
        if (minChunk < 15) throw new IllegalArgumentException("Minimum chunk cannot be below 15 minutes");
        if (maxChunk < 15) throw new IllegalArgumentException("Maximum chunk cannot be below 15 minutes");
        if (minChunk > maxChunk) throw new IllegalArgumentException("Minimum chunk cannot be greater than maximum chunk");
        // Set values
        this.title = title;
        this.targetAmount = 1;
        this.minChunk = minChunk;
        this.maxChunk = maxChunk;
        this.allocatedUntil = LocalDate.now(); // Requires further implementation (simple?)
        this.daysOutstanding = 0;
    }

    // public Boolean isSimple() { return Simple; }

    /**
     * Retrieves the goal ID
     * @return Goal ID
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the goal ID
     * @param id Goal ID
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Retrieves the goal title
     * @return Goal title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the goal title
     * @param title Goal title
     */
    public void setTitle(String title) {
        if (title.isEmpty()) throw new IllegalArgumentException("Title cannot be empty");
        this.title = title;
    }

    /**
     * Retrieves the goal target amount
     * @return Goal target amount
     */
    public int getTargetAmount() {
        return targetAmount;
    }

    /**
     * Sets the goal target amount
     * @param targetAmount Goal target amount
     */
    public void setTargetAmount(int targetAmount) {
        if (targetAmount < 0) throw new IllegalArgumentException("Goal target amount cannot be negative");
        this.targetAmount = targetAmount;
    }

    /**
     * Retrieves the minimum chunk
     * @return Minimum chunk
     */
    public int getMinChunk() {
        return minChunk;
    }

    /**
     * Sets the minimum chunk
     * @param minChunk Minimum chunk
     */
    public void setMinChunk(int minChunk) {
        if (minChunk < 15) throw new IllegalArgumentException("Minimum chunk cannot be below 15 minutes");
        this.minChunk = minChunk;
    }

    /**
     * Retrieves the maximum chunk
     * @return Maximum chunk
     */
    public int getMaxChunk() {
        return maxChunk;
    }

    /**
     * Sets the maximum chunk
     * @param maxChunk Maximum chunk
     */
    public void setMaxChunk(int maxChunk) {
        if (maxChunk < 15) throw new IllegalArgumentException("Maximum chunk cannot be below 15 minutes");
        this.maxChunk = maxChunk;
    }

    /**
     * Retrieves the allocated until date
     * @return Allocated until date
     */
    public LocalDate getAllocatedUntil() {
        return allocatedUntil;
    }

    /**
     * Sets the allocated until date
     * @param allocatedUntil Allocated until date
     */
    public void setAllocatedUntil(LocalDate allocatedUntil) {
        this.allocatedUntil = allocatedUntil;
    }

    /**
     * Retrieves the days outstanding
     * @return Days outstanding
     */
    public int getDaysOutstanding() {
        return daysOutstanding;
    }

    /**
     * Sets the days outstanding
     * @param daysOutstanding Days outstanding
     */
    public void setDaysOutstanding(int daysOutstanding) {
        this.daysOutstanding = daysOutstanding;
    }

    /**
     * Subtracts days from the days outstanding
     * @param days Days to subtract
     */
    public void subtractDaysOutstanding(int days) {
        this.daysOutstanding -= days;
    }
}
