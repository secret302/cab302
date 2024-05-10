package com.serenitask.model;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import com.serenitask.model.TimeWindow;

public class Day {


    private int priority;
    private int freeTime;


    private boolean dateSet = false;


    private LocalDate startDate;
    private LocalDate endDate;


    List<TimeWindow> timeWindows;

    public Day() {
        priority = -1;
        freeTime = 0;
        timeWindows = new ArrayList<>();
    }

    public void addWindow(LocalTime open, LocalTime close) {
        TimeWindow newWindow = new TimeWindow(open, close);
        timeWindows.add(newWindow);
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getFreeTime() {
        return freeTime;
    }

    public void setFreeTime(int freeTime) {
        this.freeTime = freeTime;
    }

    public boolean isDateSet() {
        return dateSet;
    }

    public void setDateSet(boolean dateSet) {
        this.dateSet = dateSet;
    }

    public TimeWindow getBiggestWindow() {
        return findBiggestWindow();
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    private TimeWindow findBiggestWindow() {
        int maxDiff = 0;
        TimeWindow maxWindow = null;
        for (TimeWindow window : timeWindows) {
            Duration duration = Duration.between(window.getWindowOpen(), window.getWindowClose());
            int diff = (int) duration.getSeconds();
            if (diff > maxDiff) {
                maxDiff = diff;
                maxWindow = window;
            }
        }

        return maxWindow;
    }

}
