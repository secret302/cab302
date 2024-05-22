package com.serenitask.model;

import java.time.LocalDate;

/**
 * Represents a goal with attributes like title, target amount, time allocation chunks, and more.
 * This class enforces data integrity through input validation in its constructors and setters.
 */
public class Goal {

    /**
     * Unique identifier for the goal.
     */
    private int id;
    /**
     * Title or name of the goal.
     */
    private String title;
    /**
     * Target amount or quantity to achieve for the goal (e.g., hours per week).
     */
    private int targetAmount;
    /**
     * Minimum duration in minutes for a single allocation chunk of the goal.
     */
    private int minChunk;
    /**
     * Maximum duration in minutes for a single allocation chunk of the goal. *
     */
    private int maxChunk;
    /**
     * Date until which the goal has been allocated in the calendar.
     */
    private LocalDate allocatedUntil;
    /**
     * Number of days remaining for which the goal needs to be allocated.
     */
    private int daysOutstanding;

    /**
     * Constructor for creating a Goal object with all its properties.
     *
     * @param title           Title of the goal. Cannot be null or empty.
     * @param targetAmount    Target amount for the goal. Must be non-negative.
     * @param minChunk        Minimum duration in minutes for a goal chunk. Must be at least 15 minutes.
     * @param maxChunk        Maximum duration in minutes for a goal chunk. Must be at least 15 minutes and not less than minChunk.
     * @param allocatedUntil  Date until which the goal is already allocated.
     * @param daysOutstanding Number of days for which the goal still needs allocation.
     * @throws IllegalArgumentException If any of the validation rules are violated.
     */
    public Goal(
            String title,
            int targetAmount,
            int minChunk,
            int maxChunk,
            LocalDate allocatedUntil,
            int daysOutstanding
    ) {
        // Validate the common parameters used in all Goal constructors
        validateGoalParameters(title, targetAmount, minChunk, maxChunk);
        // Set the goal properties
        this.title = title;
        this.targetAmount = targetAmount;
        this.minChunk = minChunk;
        this.maxChunk = maxChunk;
        this.allocatedUntil = allocatedUntil;
        this.daysOutstanding = daysOutstanding;
    }

    /**
     * Constructor for creating a simple Goal object with default values.
     * This constructor assumes a target amount of 1 and sets the allocatedUntil date to the current date.
     *
     * @param title    Title of the goal. Cannot be null or empty.
     * @param minChunk Minimum duration in minutes for a goal chunk. Must be at least 15 minutes.
     * @param maxChunk Maximum duration in minutes for a goal chunk. Must be at least 15 minutes and not less than minChunk.
     * @throws IllegalArgumentException If any of the validation rules are violated.
     */
    public Goal(String title, int minChunk, int maxChunk) {
        // Validate the common parameters used in all Goal constructors
        validateGoalParameters(title, 1, minChunk, maxChunk); // Assuming targetAmount = 1 for simple goals
        // Set the goal properties
        this.title = title;
        this.targetAmount = 1;
        this.minChunk = minChunk;
        this.maxChunk = maxChunk;
        this.allocatedUntil = LocalDate.now();
        this.daysOutstanding = 0;
    }

    /**
     * Constructor for creating a Goal object with a specified allocatedUntil date but defaulting daysOutstanding to 0.
     *
     * @param title           Title of the goal. Cannot be null or empty.
     * @param targetAmount    Target amount for the goal. Must be non-negative.
     * @param minChunk        Minimum duration in minutes for a goal chunk. Must be at least 15 minutes.
     * @param maxChunk        Maximum duration in minutes for a goal chunk. Must be at least 15 minutes and not less than minChunk.
     * @param allocatedUntil  Date until which the goal is already allocated.
     * @throws IllegalArgumentException If any of the validation rules are violated.
     */
    public Goal(String title, int targetAmount, int minChunk, int maxChunk, LocalDate allocatedUntil) {
        this(title, targetAmount, minChunk, maxChunk, allocatedUntil, 0);
    }

    /**
     * Validates the common parameters used in Goal constructors.
     *
     * @param title        The title of the goal to be validated.
     * @param targetAmount The target amount of the goal to be validated.
     * @param minChunk     The minimum chunk duration to be validated.
     * @param maxChunk     The maximum chunk duration to be validated.
     * @throws IllegalArgumentException If any of the validation rules are violated.
     */
    private void validateGoalParameters(String title, int targetAmount, int minChunk, int maxChunk) {
        // Validate the title
        if (title == null || title.isEmpty()) {
            throw new IllegalArgumentException("Title cannot be null or empty");
        }
        // Validate the target amount
        if (targetAmount < 0) {
            throw new IllegalArgumentException("Goal target amount cannot be negative");
        }
        // Validate the chunk durations
        if (minChunk < 15) {
            throw new IllegalArgumentException("Minimum chunk cannot be below 15 minutes");
        }
        if (maxChunk < 15) {
            throw new IllegalArgumentException("Maximum chunk cannot be below 15 minutes");
        }
        if (minChunk > maxChunk) {
            throw new IllegalArgumentException("Minimum chunk cannot be greater than maximum chunk");
        }
    }

    /**
     * Gets the unique identifier of the goal.
     * @return The goal ID.
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the unique identifier of the goal.
     * @param id The new ID for the goal. Must be non-negative.
     * @throws IllegalArgumentException If the provided ID is negative.
     */
    public void setId(int id) {
        // Validate the ID
        if (id < 0) {
            throw new IllegalArgumentException("Goal ID cannot be negative");
        }
        // Set the ID
        this.id = id;
    }

    /**
     * Gets the title of the goal.
     * @return The goal title.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title of the goal.
     * @param title The new title for the goal. Cannot be null or empty.
     * @throws IllegalArgumentException If the provided title is null or empty.
     */
    public void setTitle(String title) {
        // Validate the title
        if (title == null || title.isEmpty()) {
            throw new IllegalArgumentException("Title cannot be empty or null");
        }
        // Set the title
        this.title = title;
    }

    /**
     * Gets the target amount for the goal.
     * @return The goal target amount.
     */
    public int getTargetAmount() {
        return targetAmount;
    }

    /**
     * Sets the target amount for the goal.
     * @param targetAmount The new target amount for the goal. Must be non-negative.
     * @throws IllegalArgumentException If the provided target amount is negative.
     */
    public void setTargetAmount(int targetAmount) {
        // Validate the target amount
        if (targetAmount < 0) {
            throw new IllegalArgumentException("Goal target amount cannot be negative");
        }
        // Set the target amount
        this.targetAmount = targetAmount;
    }

    /**
     * Gets the minimum chunk duration for allocating the goal.
     * @return The minimum chunk duration in minutes.
     */
    public int getMinChunk() {
        return minChunk;
    }

    /**
     * Sets the minimum chunk duration for allocating the goal.
     * @param minChunk The new minimum chunk duration. Must be at least 15 minutes.
     * @throws IllegalArgumentException If the provided minimum chunk is less than 15 minutes.
     */
    public void setMinChunk(int minChunk) {
        // Validate the minimum chunk
        if (minChunk < 15) {
            throw new IllegalArgumentException("Minimum chunk cannot be below 15 minutes");
        }
        // Set the minimum chunk
        this.minChunk = minChunk;
    }

    /**
     * Gets the maximum chunk duration for allocating the goal.
     * @return The maximum chunk duration in minutes.
     */
    public int getMaxChunk() {
        return maxChunk;
    }

    /**
     * Sets the maximum chunk duration for allocating the goal.
     * @param maxChunk The new maximum chunk duration. Must be at least 15 minutes.
     * @throws IllegalArgumentException If the provided maximum chunk is less than 15 minutes.
     */
    public void setMaxChunk(int maxChunk) {
        // Validate the maximum chunk
        if (maxChunk < 15) {
            throw new IllegalArgumentException("Maximum chunk cannot be below 15 minutes");
        }
        // Set the maximum chunk
        this.maxChunk = maxChunk;
    }

    /**
     * Gets the date until which the goal is allocated in the calendar.
     * @return The date until which the goal is allocated.
     */
    public LocalDate getAllocatedUntil() {
        return allocatedUntil;
    }

    /**
     * Sets the date until which the goal is allocated in the calendar.
     * @param allocatedUntil The new date until which the goal is allocated.
     */
    public void setAllocatedUntil(LocalDate allocatedUntil) {
        this.allocatedUntil = allocatedUntil;
    }

    /**
     * Gets the number of days for which the goal still needs allocation.
     * @return The number of days outstanding.
     */
    public int getDaysOutstanding() {
        return daysOutstanding;
    }

    /**
     * Sets the number of days for which the goal still needs allocation.
     * @param daysOutstanding The new number of days outstanding.
     */
    public void setDaysOutstanding(int daysOutstanding) {
        this.daysOutstanding = daysOutstanding;
    }

    /**
     * Subtracts a specified number of days from the outstanding days.
     * @param days The number of days to subtract.
     */
    public void subtractDaysOutstanding(int days) {
        this.daysOutstanding -= days;
    }
}