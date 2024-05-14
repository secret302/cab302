package com.serenitask.controller;

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

/**
 * Class representing Routine 1 of the Optimizer pipeline.
 * This routine handles the allocation of goals that require x time per repeating period y.
 */
public class RoutineOneController {

    // Dummy value of 7, will be replaced with 28 for 1 month or ~4 weeks
    private final int allocationThreshold;
    private final int blockSize = 7;
    private final LocalTime DayStart;
    private final LocalTime DayEnd;
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
     */
    public void runRoutine(CalendarSource mainSource) {
        try {
            EventDAO eventDAO = new EventDAO();

            List<Goal> goalList = getGoalList();
            List<Goal> parsedList = parseGoalList(goalList);

            Calendar goalCalendar = OptimizerUtil.getGoalCalendar(mainSource.getCalendars(), TargetCalendar);

            for (Goal goal : parsedList) {
                List<Event> eventList = eventDAO.getAllEvents();
                System.out.println("eventList size: " + eventList.size());
                System.out.println("Starting goal " + goal.getTitle());
                allocateGoal(goal, eventList, goalCalendar);
            }
        }
        catch(Exception e){
            System.err.println("An error occurred while allocating goals in routine 1 of the optimizer: " + e.getMessage());
            e.printStackTrace();
        }

    }


    // 1. Pull list of time base-based goals
    // NOT IMPLEMENTED AS OF YET

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


        // only add goals that need allocating, drop the rest
        for (Goal goal : goalList) {
            // If difference between targetDate and allocated is positive
            // then there are unallocated days
            int difference = targetDate.compareTo(goal.getAllocatedUntil());
            if (difference > 0) {
                parsedList.add(goal);
                goal.setDaysOutstanding(difference);
            }
        }
        System.out.println("Goal List Parsed ");
        return parsedList;
    }


    /**
     * Allocates goals based on their required time per period. Accounts for partial and full allocation periods.
     *
     * @param goal      A goal object that requires calendar entry allocations
     * @param eventList A list containing all events draw from the database to be parsed.
     */
    private void allocateGoal(Goal goal, List<Event> eventList, Calendar goalCalendar) {
        System.out.println("\nStarting goal Allocation ");

        int firstBlock = getDifferenceSunday(goal.getAllocatedUntil());

        List<Entry<?>> entries;

        System.out.println("Partial Block Start ");
        if (firstBlock != blockSize) {
            int blockTarget = (int) Math.floor(goal.getTargetAmount() * ((double) firstBlock / blockSize));
            System.out.println("Partial Block target: " + blockTarget);
            entries = allocateBlock(blockTarget, goal, eventList);
            goal.subtractDaysOutstanding(firstBlock);
            System.out.println("Partial Entries: " + entries.size());
            OptimizerUtil.commitEntries(entries, goalCalendar);
        }
        System.out.println("Partial Block Complete \n");
        System.out.println("Full Block Complete ");
        while (goal.getDaysOutstanding() > blockSize) {
            int blockTarget = goal.getTargetAmount();
            entries = allocateBlock(blockTarget, goal, eventList);
            goal.subtractDaysOutstanding(blockSize);
            OptimizerUtil.commitEntries(entries, goalCalendar);
            System.out.println("Full Entries: " + entries.size());
            System.out.println("Full Block Lap Complete ");
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
        System.out.println("\nAllocate Block Start");
        int buffer = 5;
        LocalDate allocationStart = goal.getAllocatedUntil().plusDays(1); //!! dummy currently !!
        LocalDate allocationEnd = OptimizerUtil.getNextSunday(allocationStart).plusDays(1);

        System.out.println("Allocate Block date range: " + allocationStart + " to " + allocationEnd);

        List<List<Event>> rawDaysLists = OptimizerUtil.splitDays(allocationStart, allocationEnd, eventList);

        System.out.println("Raw Day List loaded");

        List<Day> orderedDays = getDaysList(rawDaysLists, allocationStart);
        List<Day> prioritizedDays = prioritizeDays(orderedDays);

        System.out.println("Days Validation Start\n");
        for (Day day : prioritizedDays) {
            day.validate();
        }
        System.out.println("Days Validation End\n");

        System.out.println("Days Prioritized\n");

        List<Entry<?>> entriesToAdd = new ArrayList<>();

        while (blockTarget > buffer) {
            boolean hasWindows = false;
            for (Day day : prioritizedDays) {
                System.out.println("BlockTarget Loop Start: Setup; Date: " + day.getStartDate());
                TimeWindow window = day.getBiggestWindow();
                Duration duration = Duration.between(window.getWindowOpen(), window.getWindowClose());
                int windowMins = (int) (duration.getSeconds() / 60);
                System.out.println("windowMins: "+windowMins);

                if (windowMins > 0 ) {
                    hasWindows = true;
                }
                System.out.println("BlockTarget Loop: Setup complete");

                if (hasWindows) {
                    if (windowMins > goal.getMinChunk()) {
                        if (windowMins > goal.getMaxChunk()) {
                            LocalTime startTime = window.getWindowOpen();
                            LocalTime endTime = window.getWindowOpen().plusMinutes(goal.getMaxChunk());
                            day.addWindow(endTime, window.getWindowClose());
                            Entry<?> newEntry = new Entry<>(goal.getTitle());
                            newEntry.setInterval(new Interval(day.getStartDate(), startTime, day.getEndDate(), endTime));
                            newEntry.setFullDay(false);
                            entriesToAdd.add(newEntry);
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

        System.out.println("Days List Start");
        List<Day> daysList = new ArrayList<>();
        for (List<Event> list : rawDaysLists) {
            Day newDay = OptimizerUtil.createDay(list, DayStart, DayEnd,date);
            daysList.add(newDay);
            date = date.plusDays(1);
        }
        System.out.println("Days List End");
        return daysList;
    }


    /**
     * Takes in an unsorted List of day objects and sorts them based on the amount of unallocated time. returns a sorted
     * list of days from most free time to least. Additionally, assigns priority to the day objects based on ordering.
     *
     * @param rawDays Raw day objects in list form. Unsorted, unprioritized.
     * @return Sorted Day List, from most free time to least.
     */
    private List<Day> prioritizeDays(List<Day> rawDays) {

        System.out.println("prioritizeDays Start");
        // sorting by free time;
        List<Day> sorted = new ArrayList<>();
        int highestPrio = rawDays.size();


        System.out.println("count of days: " + rawDays.size());

        while (!rawDays.isEmpty()) {
            int freeMinutes = 0;
            int index = 0;
            int indexRemove = 0;
            Day newDay = new Day();

            for (Day day : rawDays) {

                if (day.getFreeTime() > freeMinutes) {
                    newDay = day;
                    indexRemove = index;
                }
                index++;
            }

            // set the priority for that day
            newDay.setPriority(highestPrio);
            highestPrio--;

            // add to sorted and remove from unsortedlist
            sorted.add(newDay);
            rawDays.remove(indexRemove);

        }
        return sorted;
    }


    /**
     * Takes in a LocalDate value and returns the integer difference between the date given
     * and the closest following sunday.
     *
     * @param date any LocalDate object
     * @return int difference
     */
    private int getDifferenceSunday(LocalDate date) {
        LocalDate targetDate = date;

        while (targetDate.getDayOfWeek().getValue() != 7) {
            targetDate = targetDate.plusDays(1);
        }

        return targetDate.compareTo(date);
    }


}
