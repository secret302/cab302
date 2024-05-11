package com.serenitask.model;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import com.serenitask.model.TimeWindow;

import static java.time.temporal.ChronoUnit.MINUTES;

public class Day {


    private int priority;
    private int freeTime;


    private boolean dateSet = false;


    private LocalDate startDate;
    private LocalDate endDate;
    private TimeWindow biggestWindow;


    List<TimeWindow> timeWindows;

    public Day() {
        priority = -1;
        freeTime = 0;
        timeWindows = new ArrayList<>();
    }

    public void addWindow(LocalTime open, LocalTime close) {
        TimeWindow newWindow = new TimeWindow(open, close);
        timeWindows.add(newWindow);
        freeTime = calculateFreeTime();
        System.out.println("new Freetime: " + freeTime);
    }

    private int calculateFreeTime() {
        int sum = 0;
        for (TimeWindow window : timeWindows) {
            sum += MINUTES.between(window.getWindowOpen(), window.getWindowClose());
        }
        return sum;
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
        biggestWindow = findBiggestWindow();
        return biggestWindow;
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
        System.out.println("Windows in day: " + timeWindows.size());
        int maxDiff = 0;
        TimeWindow maxWindow = null;
        int index = 0;
        int indexer = 0;

        for (TimeWindow window : timeWindows) {
            Duration duration = Duration.between(window.getWindowOpen(), window.getWindowClose());
            System.out.println("window Duration: " + duration.getSeconds());
            int diff = (int) duration.getSeconds();
            if (diff > maxDiff) {
                index = indexer;
                maxDiff = diff;
                maxWindow = window;
            }
            indexer++;

        }
        if (!timeWindows.isEmpty()) {
            timeWindows.remove(index);
            return maxWindow;
        } else {
            return new TimeWindow(LocalTime.of(0, 0, 0), LocalTime.of(0, 0, 0));
        }

    }


    public void validate() {
        System.out.println("Priority: " + priority);
        System.out.println("Freetime: " + freeTime);
        System.out.println("Date Start: " + startDate);
        System.out.println("Date End: " + endDate);
        System.out.println("Date Set: " + dateSet);

        System.out.println("Biggest Window: " + biggestWindow);
        System.out.println("Windows: ");
        int index = 1;
        for (TimeWindow window : timeWindows) {
            System.out.println("Window " + index + " - start: " + window.getWindowOpen() + " - to close: " + window.getWindowClose());
        }


    }
}