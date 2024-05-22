package com.serenitask.model;

import java.time.Duration;
import java.time.LocalDate;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static java.time.temporal.ChronoUnit.MINUTES;

/**
 * Class representing a single day as an object. A day has a date, and a series of time windows of available time
 * for event allocation.
 */
public class Day {
    /**
     * Priority of the day when ordered based on available free time.
     * Higher priority means more free time.
     */
    private int priority;

    /**
     * Total free time in minutes available for the day.
     */
    private int freeTime;

    /**
     * Total time in minutes allocated to work-related activities.
     */
    private int workingTime;

    /**
     * Total time in minutes allocated to health-related activities.
     */
    private int healthTime;

    /**
     * Flag indicating whether the date has been set for the day.
     */
    private boolean dateSet;

    /**
     * Start date of the day.
     */
    private LocalDate startDate;

    /**
     * End date of the day.
     */
    private LocalDate endDate;

    /**
     * The largest time window available for the day.
     */
    private TimeWindow biggestWindow;

    /**
     * List of time windows representing free time slots during the day.
     */
    private List<TimeWindow> timeWindows;

    /**
     * Base Constructor; represents a single day on a date containing windows of free time
     */
    public Day() {
        this.priority = -1;
        this.freeTime = 0;
        this.workingTime = 0;
        this.healthTime = 0;
        this.timeWindows = new ArrayList<>();
    }

    /**
     * Adds a window of time to the day. A window has a start and an end. The free time of the add will be totaled
     * and added to the Day's free time.
     *
     * @param open Localtime object representing the start of the window
     * @param close Localtime object representing the end of the window
     */
    public void addWindow(LocalTime open, LocalTime close) {
        // Check if the open time is after the close time, throw an exception if it is
        if (open.isAfter(close)) {
            throw new IllegalArgumentException("Open time cannot be after close time.");
        }
        // Create a new TimeWindow object and add it to the list of time windows
        TimeWindow newWindow = new TimeWindow(open, close);
        timeWindows.add(newWindow);
        // Calculate the free time for the day
        freeTime = timeWindows.stream().mapToInt(window -> (int) MINUTES.between(window.getWindowOpen(), window.getWindowClose())).sum();
    }

    /**
     * Returns the priority of the day.
     *
     * @return Priority of the day.
     */
    public int getPriority() {
        return priority;
    }

    /**
     *  Sets the priority of the day.
     *
     * @param priority Priority to set for the day.
     */
    public void setPriority(int priority) {
        this.priority = priority;
    }

    /**
     * Returns the total free time available for the day.
     *
     * @return Total free time in minutes.
     */
    public int getFreeTime() {
        return freeTime;
    }

    /**
     * Sets the total free time available for the day.
     * Note: It's generally not recommended to set freeTime directly.
     * Use the addWindow method to manage time windows and let freeTime be calculated automatically.
     *
     * @param freeTime Total free time in minutes.
     */
    public void setFreeTime(int freeTime) {
        this.freeTime = freeTime;
    }

    /**
     * Checks whether the date has been set for the day.
     *
     * @return True if the date has been set, false otherwise.
     */
    public boolean isDateSet() {
        return dateSet;
    }

    /**
     * Sets the flag indicating whether the date has been set for the day.
     *
     * @param dateSet True if the date has been set, false otherwise.
     */
    public void setDateSet(boolean dateSet) {
        this.dateSet = dateSet;
    }

    /**
     * Returns the largest time window available for the day.
     *
     * @return Largest time window.
     */
    public TimeWindow getBiggestWindow() {
//        try {
//            biggestWindow = findBiggestWindow();
//            return biggestWindow;
//        }
//        catch(Exception e){
//            System.err.println("An error occurred while trying to calculate the largest window of the day: " + e.getMessage());
//            e.printStackTrace();
//        }
//        // returns null if the calculations for the biggest window of the day cannot be completed
//        return new TimeWindow(LocalTime.of(0,0),LocalTime.of(0,0));
        if (biggestWindow == null) {
            biggestWindow = findBiggestWindow();
        }
        return biggestWindow;
    }

    /**
     * Returns the start date of the day.
     *
     * @return Start date of the day.
     */
    public LocalDate getStartDate() {
        return startDate;
    }

    /**
     * Getter for workingTime parameter
     *
     * @return int object representing total working time for day
     */
    public int getWorkingTime() {
        return workingTime;
    }

    /**
     * Getter for healthTime parameter
     *
     * @return int object representing total health based time for day
     */
    public int getHealthTime() {
        return healthTime;
    }

    /**
     * Setter for StartDate parameter
     *
     * @param startDate LocalDate object to set as StartDate
     */
    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    /**
     * Getter for EndDate parameter
     *
     * @return LocalDate object representing EndDate
     */
    public LocalDate getEndDate() {
        return endDate;
    }

    /**
     * Setter for EndDate parameter
     *
     * @param endDate LocalDate object to set as endDate
     */
    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    /**
     * Function calculates the biggest window of the day. Parses all windows and returns the window with the largest
     * difference
     *
     * @return TimeWindow object representing the largest window of the day
     */
    private TimeWindow findBiggestWindow() {
        TimeWindow maxWindow = null;
        long maxDuration = 0;
        int maxIndex = -1;

        for (int i = 0; i < timeWindows.size(); i++) {
            TimeWindow window = timeWindows.get(i);
            Duration duration = Duration.between(window.getWindowOpen(), window.getWindowClose());
            if (duration.toMinutes() > maxDuration) {
                maxDuration = duration.toMinutes();
                maxWindow = window;
                maxIndex = i;
            }
        }

        if (maxIndex != -1) {
            timeWindows.remove(maxIndex);
        }
        return maxWindow;
    }

    /**
     * Prints out validation data to ensure days are generating as required.
     * Debug only, will be removed for final release
     */
    public void validate() {
        System.out.println("Priority: " + priority);
        System.out.println("Free time: " + freeTime);
        System.out.println("Date Start: " + startDate);
        System.out.println("Date End: " + endDate);
        System.out.println("Date Set: " + dateSet);
        System.out.println("Windows: ");
        int index = 1;
        for (TimeWindow window : timeWindows) {
            System.out.println("Window " + index + " - start: " + window.getWindowOpen() + " - to close: " + window.getWindowClose());
        }
    }

    /**
     * Adds work based activity time to total sum for day
     *
     * @param work integer representing minutes of work based events
     */
    public void addWork(int work) {
        workingTime += work;
    }

    /**
     * Adds health based activity time to total sum for day
     *
     * @param health integer representing minutes of health based events
     */
    public void addHealth(int health) {

        healthTime += health;
    }

    /**
     * Returns an integer of total minutes needed to achieve target health ratio
     *
     * @param ratio integer value of ratio target
     * @return minutes required to achieve target ratio
     */
    public int getHealthNeeded(int ratio) {
        if(workingTime != 0) {
            return (workingTime / ratio);
        }
        return 0;

    }
}