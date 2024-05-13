package com.serenitask.model;

/**
 * Represents the tracking information of a goal, including its completion status and date.
 */
public class GoalTracking {

    private int goal_id;
    private String goal_date;
    private Boolean completed;

    /**
     * Constuctor for GoalTracking; Takes in an id, the goals date and whether it has been completed
     *
     * @param id        The unique identifier for the goal.
     * @param goalDate  The date of the goal.
     * @param completed The completion status of the goal.
     */
    public GoalTracking(int id, String goalDate, Boolean completed) {
        this.goal_id = id;
        this.goal_date = goalDate;
        this.completed = completed;
    }

    /**
     * Returns the goal ID.
     *
     * @return the goal ID.
     */
    public int getId() {
        return goal_id;
    }

    /**
     * Sets the goal ID.
     *
     * @param id The new ID for the goal.
     */
    public void setId(int id) {
        this.goal_id = id;
    }

    /**
     * Returns the date associated with the goal.
     *
     * @return the goal date.
     */
    public String getGoalDate() {
        return goal_date;
    }

    /**
     * Sets the date of the goal.
     *
     * @param goalDate The new date for the goal.
     */
    public void setGoalDate(String goalDate) {
        this.goal_date = goalDate;
    }

    /**
     * Returns the completion status of the goal.
     *
     * @return true if the goal is completed, false otherwise.
     */
    public Boolean getCompleted() {
        return completed;
    }

    /**
     * Sets the completion status of the goal.
     *
     * @param achieved The new completion status of the goal.
     */
    public void setCompleted(Boolean achieved) {
        this.completed = achieved;
    }
}