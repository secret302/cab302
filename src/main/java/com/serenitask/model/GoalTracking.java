package com.serenitask.model;

/**
 * Represents the tracking information for a specific goal on a particular date,
 * indicating whether the goal was completed on that day.
 */
public class GoalTracking {

    /**
     * Unique identifier of the goal being tracked.
     */
    private int goalId;
    /**
     * Date for which the goal completion is being tracked.
     */
    private String goalDate;
    /**
     * Flag indicating whether the goal was completed on the specified date.
     */
    private Boolean completed;

    /**
     * Constructor for creating a GoalTracking object.
     *
     * @param goalId   The unique identifier of the goal.
     * @param goalDate  The date for which goal completion is tracked (in YYYY-MM-DD format).
     * @param completed Indicates whether the goal was completed on the specified date.
     */
    public GoalTracking(int goalId, String goalDate, Boolean completed) {
        this.goalId = goalId;
        this.goalDate = goalDate;
        this.completed = completed;
    }

    /**
     * Gets the unique identifier of the goal being tracked.
     * @return The ID of the goal.
     */
    public int getId() {
        return goalId;
    }

    /**
     * Sets the unique identifier of the goal being tracked.
     * @param goalId The new ID for the goal.
     */
    public void setId(int goalId) {
        this.goalId = goalId;
    }

    /**
     * Gets the date for which the goal completion is tracked.
     * @return The date in YYYY-MM-DD format.
     */
    public String getGoalDate() {
        return goalDate;
    }

    /**
     * Gets the completion status of the goal for the specified date.
     * @return True if the goal was completed on the tracked date, false otherwise.
     */
    public Boolean getCompleted() {
        return completed;
    }
}