package com.serenitask.model;


public class GoalTracking {

    private int goal_id;
    private String goal_date;
    private Boolean completed;

    public GoalTracking(int id, String goalDate, Boolean completed)
    {
        this.goal_id = id;
        this.goal_date = goalDate;
        this.completed = completed;
    }

    public int getId() {
        return goal_id;
    }

    public void setId(int id) {
        this.goal_id = id;
    }

    public String getGoalDate() {
        return goal_date;
    }

    public void setGoalDate(String goalDate) {
        this.goal_date = goalDate;
    }

    public Boolean getCompleted() {
        return completed;
    }

    public void setCompleted(Boolean acheived) {
        this.completed = acheived;
    }

}
