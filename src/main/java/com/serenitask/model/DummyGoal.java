package com.serenitask.model;
import com.serenitask.model.Goal;

import java.time.LocalDate;
import java.time.Month;

public class DummyGoal extends Goal {

    private int goalTargetAmount;
    private LocalDate AllocatedUpTo;
    private int daysOutstanding;



    public DummyGoal(String title, String description, int minChunk, int maxChunk, int periodicity, String endDate, String recurrenceRules) {
        super(title, description, minChunk, maxChunk, periodicity, endDate, recurrenceRules);

        if(description == "null")
        {
            AllocatedUpTo = LocalDate.of(2024, Month.MAY, 6);
        }
        else
        {
            AllocatedUpTo = LocalDate.of(2024, Month.MAY, 19);
        }

        goalTargetAmount = 600;
    }

    public void subtractDaysOutstanding(int days)
    {
        daysOutstanding -= days;
    }

    public DummyGoal(String title) {
        super(title);
    }

    public int getGoalTargetAmount() {
        return goalTargetAmount;
    }

    public void setGoalTargetAmount(int goalTargetAmount) {
        this.goalTargetAmount = goalTargetAmount;
    }

    public LocalDate getAllocatedUpTo() {
        return AllocatedUpTo;
    }

    public void setAllocatedUpTo(LocalDate allocatedUpTo) {
        AllocatedUpTo = allocatedUpTo;
    }

    public int getDaysOutstanding() {
        return daysOutstanding;
    }

    public void setDaysOutstanding(int daysOutstanding) {
        this.daysOutstanding = daysOutstanding;
    }
}
