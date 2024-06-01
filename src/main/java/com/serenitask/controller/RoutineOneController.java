package com.serenitask.controller;

import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.Comparator;
import com.calendarfx.model.Calendar;
import com.calendarfx.model.CalendarSource;
import com.calendarfx.model.Entry;
import com.calendarfx.model.Interval;
import com.serenitask.model.*;
import com.serenitask.util.DatabaseManager.EventDAO;
import com.serenitask.util.DatabaseManager.GoalDAO;
import com.serenitask.util.routine.OptimizerUtil;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

import java.util.List;
import java.util.Random;

/**
 * Class representing Routine 1 of the Optimizer pipeline.
 * This routine handles the allocation of goals that require x time per repeating period y.
 */
public class RoutineOneController {

    /**
     * The minimum threshold for allocation.
     */
    private final int allocationThreshold;

    /**
     * The size of each block in the allocation process, set to 7 by default.
     */
    private final int blockSize = 7;

    /**
     * The start time of the day for goal allocation.
     */
    private final LocalTime DayStart;

    /**
     * The end time of the day for goal allocation.
     */
    private final LocalTime DayEnd;

    /**
     * The name of the target calendar for goal allocation, set to "Goals" by default.
     */
    private final String TargetCalendar = "Goals";


    /**
     * Constructor for RoutineOne, Represents the complex routine used for allocating goals into events.
     *
     * @param dayStart            LocalTime object representing the Start of users day
     * @param dayEnd              LocalTime object representing the End of users day
     * @param allocationThreshold integer value of days to allocate ahead. 7 = 1 week;
     */
    public RoutineOneController(LocalTime dayStart, LocalTime dayEnd, int allocationThreshold) {
        this.allocationThreshold = allocationThreshold;
        this.DayStart = dayStart;
        this.DayEnd = dayEnd;
    }


    /**
     * Simple execution function that executes routine 1 of the optimizer.
     * This routine handles the allocation of goals that require x time per repeating period y.
     * @param mainSource the Calendar Source that provides access to the calendars main source.
     */
    public void runRoutine(CalendarSource mainSource) {
        try {
            EventDAO eventDAO = new EventDAO();

            // Get and prepare goal lists
            List<Goal> goalList = getGoalList();
            List<Goal> parsedList = parseGoalList(goalList);
            Calendar goalCalendar = OptimizerUtil.getGoalCalendar(mainSource.getCalendars(), TargetCalendar);

            // Allocate each goal in parsed list.
            for (Goal goal : parsedList) {
                System.out.println("Starting goal " + goal.getTitle());
                List<Event> eventList = eventDAO.getAllEvents();
                allocateGoal(goal, eventList, goalCalendar);
            }
        }
        catch(Exception e){
            System.err.println("An error occurred while allocating goals in routine 1 of the optimizer: " + e.getMessage());
            e.printStackTrace();
        }

    }


    // 1. Pull list of time base-based goals
    // NOT IMPLEMENTED AS YET

    /**
     * Extracts all goals from the database and returns them as a list. Goals extracted are of the type:
     * requires x time per repeating period of y.
     *
     * @return List of all goals from the database
     */
    private List<Goal> getGoalList() {
        GoalDAO goalDAO = new GoalDAO();
        return goalDAO.getAllGoals();
    }


    // 2. Parse list and remove any that exceed the allocation data threshold today + ~4weeks

    /**
     * Parses the goal list and determines which goals require allocation. Goals allocated to or beyond the allocation
     * threshold are not required to be allocated.
     *
     * @param goalList List of all goals requiring x time per y period
     * @return List of goals requiring allocation
     */
    private List<Goal> parseGoalList(List<Goal> goalList) {
        LocalDate targetDate = OptimizerUtil.getTargetDate(allocationThreshold);
        List<Goal> parsedList = new ArrayList<>();

        for (Goal goal : goalList) {
            if (goal == null || goal.getAllocatedUntil() == null) continue; // Skip goals with missing data

            LocalDate allocationStart = calcAllocatedUntil(goal);
            // Calculate the number of days until the target date
            long daysUntilTarget = ChronoUnit.DAYS.between(allocationStart, targetDate)+1;

            // If the target date is after the allocated date, include the goal
            if (daysUntilTarget > 0) {
                goal.setDaysOutstanding((int) daysUntilTarget);
                parsedList.add(goal);
            }
        }
        return parsedList;
    }


    /**
     * Allocates goals based on their required time per period. Accounts for partial and full allocation periods.
     *
     * @param goal      A goal object that requires calendar entry allocations
     * @param goalCalendar is the Calendar object that holds all goals.
     * @param eventList A list containing all events draw from the database to be parsed.
     */
    private void allocateGoal(Goal goal, List<Event> eventList, Calendar goalCalendar) {

        int firstBlock = getDifferenceSunday(goal.getAllocatedUntil());
        GoalDAO goalDAO = new GoalDAO();
        LocalDate blockStart = calcAllocatedUntil(goal);

        List<Entry<?>> entries;

        if (firstBlock != blockSize) {
            int blockTarget = (int) Math.floor(goal.getTargetAmount() * ((double) firstBlock / blockSize));
            entries = allocateBlock(blockTarget, goal, eventList);
            goal.subtractDaysOutstanding(firstBlock);
            OptimizerUtil.commitEntries(entries, goalCalendar);
            goal.setAllocatedUntil(blockStart.plusDays(getDifferenceSunday(blockStart)-1));
            goalDAO.updateGoal(goal);
        }

        while (goal.getDaysOutstanding() >= blockSize) {
            System.out.println("beep");
            LocalDate blockCurrent = calcAllocatedUntil(goal);
            int blockTarget = goal.getTargetAmount();
            entries = allocateBlock(blockTarget, goal, eventList);
            goal.subtractDaysOutstanding(blockSize);
            OptimizerUtil.commitEntries(entries, goalCalendar);
            goal.setAllocatedUntil(blockCurrent.plusDays(blockSize-1));
            goalDAO.updateGoal(goal);
        }

    }

    /**
     * Calculates the next date for allocation based on the current allocatedUntil value of a goal.
     *
     * @param goal The goal for which the next allocation date is calculated.
     * @return The next allocation date. If the allocatedUntil date is in the past or today,
     *         returns tomorrow's date. If allocatedUntil is in the future, returns allocatedUntil plus one day.
     */
    private LocalDate calcAllocatedUntil(Goal goal)
    {
        LocalDate today = LocalDate.now();
        LocalDate allocatedUntil = goal.getAllocatedUntil();

        if (!allocatedUntil.isAfter(today)) {
            // If allocatedUntil is today or in the past, return tomorrow's date
            return today.plusDays(1);
        } else {
            // If allocatedUntil is in the future, return allocatedUntil plus one day
            return allocatedUntil.plusDays(1);
        }
    }



    /**
     * Sophisticated allocation algorithm that takes in a blockTarget value, a goal and an eventList. Processes these
     * given parameters to allocate period based goals into events and store them in the calendar. Used to generate
     * period based goals: I want to do "goal" for x hours per y period.
     *
     * @param blockTarget int value representing minutes of time required to be allocated
     * @param goal        current goal having time allocated
     * @param eventList   raw list of events drawn from database
     * @return returns a list of entries to be added to calendar
     */
    private List<Entry<?>> allocateBlock(int blockTarget, Goal goal, List<Event> eventList) {
        int minAllocation = 15;
        int buffer = 5;

        System.out.println("AllocateBlock Start");
        LocalDate allocationStart = calcAllocatedUntil(goal);
        LocalDate allocationEnd = OptimizerUtil.getNextSunday(allocationStart);

        System.out.println("Allocate Block date range: " + allocationStart + " to " + allocationEnd);

        List<List<Event>> rawDaysLists = OptimizerUtil.splitDays(allocationStart, allocationEnd, eventList);
        List<Day> orderedDays = getDaysList(rawDaysLists, allocationStart);
        List<Day> prioritizedDays = prioritizeDays(orderedDays);


        List<Entry<?>> entriesToAdd = new ArrayList<>();

        while (blockTarget > buffer) {
            boolean hasWindows = false;
            for (Day day : prioritizedDays) {

                TimeWindow window = day.getBiggestWindow();
                if (window != null) {
                    Duration duration = Duration.between(window.getWindowOpen(), window.getWindowClose());
                    int windowMins = (int) (duration.getSeconds() / 60);

                    if (windowMins > 0  ) {
                        hasWindows = true;
                    }

                    if (hasWindows) {
                        if (windowMins > goal.getMinChunk()) {
                            if (windowMins > goal.getMaxChunk()) {
                                if(windowMins > goal.getMaxChunk() + 2*minAllocation)
                                {
                                    int eventOffsetValue = OptimizerUtil.calcOffsetMins(windowMins, goal.getMaxChunk());
                                    LocalTime startTime = window.getWindowOpen().plusMinutes(eventOffsetValue);
                                    LocalTime endTime = window.getWindowOpen().plusMinutes(goal.getMaxChunk()+eventOffsetValue);
                                    day.addWindow(endTime, window.getWindowClose());
                                    Entry<?> newEntry = new Entry<>(goal.getTitle());
                                    newEntry.setInterval(new Interval(day.getStartDate(), startTime, day.getEndDate(), endTime));
                                    newEntry.setFullDay(false);
                                    entriesToAdd.add(newEntry);
                                }
                                else
                                {
                                    LocalTime startTime = window.getWindowOpen();
                                    LocalTime endTime = window.getWindowOpen().plusMinutes(goal.getMaxChunk());
                                    day.addWindow(endTime, window.getWindowClose());
                                    Entry<?> newEntry = new Entry<>(goal.getTitle());
                                    newEntry.setInterval(new Interval(day.getStartDate(), startTime, day.getEndDate(), endTime));
                                    newEntry.setFullDay(false);
                                    entriesToAdd.add(newEntry);
                                }
                                blockTarget -= goal.getMaxChunk();


                            } else {
                                Entry<?> newEntry = new Entry<>(goal.getTitle());
                                newEntry.setInterval(new Interval(day.getStartDate(), window.getWindowOpen(), day.getEndDate(), window.getWindowClose()));
                                newEntry.setFullDay(false);
                                entriesToAdd.add(newEntry);
                                blockTarget -= windowMins;
                            }
                        }
                        if (blockTarget <= buffer) {
                            break;
                        }
                    }
                }

            }

            if (!hasWindows) {
                System.out.println("out of windows");
                break;
            }


        }

        return entriesToAdd;
    }


    /**
     * Takes in a List of Lists containing events sorted by day. Uses events per day to evaluate available TimeWindows
     * available for allocation. Each list of events is converted to a Day which contains a List of Windows.
     *
     * @param rawDaysLists List of lists containing events, sorted by date
     * @param date Localdate object representing starting date of allocation
     * @return List of Day objects
     */
    private List<Day> getDaysList(List<List<Event>> rawDaysLists, LocalDate date) {

        List<Day> daysList = new ArrayList<>();

        for (List<Event> list : rawDaysLists) {
            Day newDay = OptimizerUtil.createDay(list, DayStart, DayEnd,date);
            daysList.add(newDay);
            date = date.plusDays(1);
        }
        return daysList;
    }


    /**
     * Takes in an unsorted List of day objects and sorts them based on the amount of unallocated time. returns a sorted
     * list of days from most free time to least. Additionally, assigns priority to the day objects based on ordering.
     *
     * @param rawDays Raw day objects in list form. Unsorted, unprioritied.
     * @return Sorted Day List, from most free time to least.
     */
    private List<Day> prioritizeDays(List<Day> rawDays) {

        // Make a copy of the list
        List<Day> sorted = new ArrayList<>(rawDays);

        // Sort the list
        sorted.sort(Comparator.comparingInt(Day::getFreeTime).reversed());

        // Assign priority based on ordering, from most free time to least
        for (int i = 0; i < sorted.size(); i++) {
            sorted.get(i).setPriority(sorted.size() - i);
        }

        return sorted;
    }


    /**
     * Takes in a LocalDate value, and returns the integer difference between the date given
     * and the closest following sunday. If the given date is in the past, the present difference is calculated
     *
     * @param date any LocalDate object
     * @return int difference
     */
    private int getDifferenceSunday(LocalDate date) {

        // Start from tomorrow if the given date is today or in the past
        LocalDate today = LocalDate.now();
        LocalDate targetDate = (date.isBefore(today) || date.isEqual(today)) ? today.plusDays(1) : date;

        // Get the next Sunday
        int daysCount = 0; // Start counting from the next day or the given date

        do {
            // If it is Sunday, add 7 to ensure the full next week is included
            if (targetDate.getDayOfWeek().getValue() == 7) {
                if (daysCount == 0) { // If it's already Sunday when the method starts
                    return 1; // Only count the 7 days of the next week
                }
                daysCount++;
                break; // Otherwise, break as we found the next Sunday
            }
            targetDate = targetDate.plusDays(1);
            daysCount++;
        } while (true);

        return daysCount;
    }


}
