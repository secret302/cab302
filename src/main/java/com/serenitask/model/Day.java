package com.serenitask.model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import com.serenitask.model.TimeWindow;

public class Day {


    private int priority;
    private int freeTime;

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

    public TimeWindow getBiggestWindow() {
        return findBiggestWindow();
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
