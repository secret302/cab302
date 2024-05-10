package com.serenitask.controller;


import com.calendarfx.model.Entry;
import com.calendarfx.model.Interval;
import com.serenitask.model.*;
import com.serenitask.util.DatabaseManager.EventDAO;


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
    private final int allocationThreshold = 7;
    private final int blockSize = 7;
    private LocalTime DayStart = LocalTime.of(8, 0, 0);
    private LocalTime DayEnd = LocalTime.of(18, 0, 0);

    // Dummy variables setup

    // dummy goals
    DummyGoal goalOne = new DummyGoal("Workout", "null", 30, 120, 0, "never", "weekly");
    DummyGoal goalTwo = new DummyGoal("Study", "null", 30, 120, 0, "never", "weekly");
    DummyGoal goalThree = new DummyGoal("Read", "null", 30, 60, 0, "never", "weekly");

    /*
    This routine currently operates on dummy values;
    Routine requires updating pending changes to Event, Goal and GoalDAO
    Event: requires constructor to create blank object.
    goal and GoalDAO: Addition of an "AllocatedUpTo" value. Will store the date the optimizer has allocated up until.
     */

    /**
     * Simple execution function that executes routine 1 of the optimizer.
     * This routine handles the allocation of goals that require x time per repeating period y.
     */
    public void runRoutine() {
        EventDAO eventDAO = new EventDAO();
        List<Event> eventList = eventDAO.getAllEvents();

        List<DummyGoal> goalList = getGoalList();
        List<DummyGoal> parsedList = parseGoalList(goalList);

        for (DummyGoal goal : parsedList) {
            allocateGoal(goal, eventList);
        }
    }

    // 1. Pull list of time base-based goals

    /**
     * Extracts all goals from the database and returns them as a list. Goals extracted are of the type:
     * requires x time per repeating period of y.
     *
     * @return List of all goals from the database
     */
    private List<DummyGoal> getGoalList() {
        List<DummyGoal> goalList = new ArrayList<>();
        goalList.add(goalOne);
        goalList.add(goalTwo);

        return goalList;
    }


    // 2. Parse list and remove any that exceed the allocation data threshold today + ~4weeks

    /**
     * Parses the goal list and determines which goals require allocation. Goals allocated to or beyond the allocation
     * threshold are not required to be allocated.
     *
     * @param goalList List of all goals requiring x time per y period
     * @return List of goals requiring allocation
     */
    private List<DummyGoal> parseGoalList(List<DummyGoal> goalList) {
        LocalDate targetDate = getTargetDate();
        List<DummyGoal> parsedList = new ArrayList<>();

        // only add goals that need allocating, drop the rest
        for (DummyGoal goal : goalList) {
            // If difference between targetDate and allocated is positive
            // then there are unallocated days
            int difference = targetDate.compareTo(goal.getAllocatedUpTo());
            if (difference > 0) {
                parsedList.add(goal);
                goal.setDaysOutstanding(difference);
            }
        }
        return parsedList;
    }


    // 3. Will now have a list of time based goals that need to be implemented
    // from (stored_date -> threshold_date)
    // use a ceiling type method, must allocate up until a sunday. so could be (4 weeks -- 4 weeks + 6 days)
    // allocate time based goals to fill windows

    /**
     * Allocates goals based on their required time per period. Accounts for partial and full allocation periods.
     *
     * @param goal      A goal object that requires calendar entry allocations
     * @param eventList A list containing all events draw from the database to be parsed.
     */
    private void allocateGoal(DummyGoal goal, List<Event> eventList) {

        int firstBlock = getDifferenceSunday(goal.getAllocatedUpTo());

        // TO DO !!!
        // This works, but now the entries returned via allocateBlock must be attached to calendar.
        // TO DO !!!!

        List<Entry<?>> entries = new ArrayList<>();


        if (firstBlock != blockSize) {
            int blockTarget = (int) Math.floor(goal.getGoalTargetAmount() * ((double) firstBlock / blockSize));
            entries = allocateBlock(blockTarget, goal, eventList);
            goal.subtractDaysOutstanding(firstBlock);
            // Commit entries here
        }

        while (goal.getDaysOutstanding() > blockSize) {
            int blockTarget = goal.getGoalTargetAmount();
            entries = allocateBlock(blockTarget, goal, eventList);
            goal.subtractDaysOutstanding(blockSize);
            // Commit entries here
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
    private List<Entry<?>> allocateBlock(int blockTarget, DummyGoal goal, List<Event> eventList) {
        int buffer = 5;
        LocalDate allocationStart = goal.getAllocatedUpTo().plusDays(1);
        LocalDate allocationEnd = getNextSunday(allocationStart);
        List<List<Event>> rawDaysLists = splitDays(allocationStart, allocationEnd, eventList);
        List<Day> prioritizedDays = getDaysList(rawDaysLists);
        List<Entry<?>> entriesToAdd = new ArrayList<>();

        while (blockTarget > buffer) {
            for (Day day : prioritizedDays) {
                TimeWindow window = day.getBiggestWindow();
                Duration duration = Duration.between(window.getWindowOpen(), window.getWindowClose());
                int windowMins = (int) (duration.getSeconds() / 60);

                if (windowMins > goal.getMinChunk()) {
                    if (windowMins > goal.getMaxChunk()) {
                        int middlePoint = (int) (duration.getSeconds() / 2);
                        LocalTime middleTime = window.getWindowOpen().plusSeconds(middlePoint);
                        LocalTime startTime = middleTime.minusMinutes(60);
                        LocalTime endTime = middleTime.plusMinutes(60);

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

        return entriesToAdd;


    }

    /**
     * Takes in a List of Lists containing events sorted by day. Uses events per day to evaluate available TimeWindows
     * available for allocation. Each list of events is converted to a Day which contains a List of Windows.
     *
     * @param rawDaysLists List of lists containing events, sorted by date
     * @return List of Day objects
     */
    private List<Day> getDaysList(List<List<Event>> rawDaysLists) {
        List<Day> daysList = new ArrayList<>();
        for (List<Event> list : rawDaysLists) {
            Day newDay = new Day();
            List<Event> sortedList = getSortedList(list);

            LocalTime windowStart = DayStart;

            for (Event event : sortedList) {

                if (windowStart.isBefore(DayEnd)) {
                    if (windowStart.compareTo(event.getStartTime().toLocalTime()) > 0) {
                        newDay.addWindow(windowStart, event.getStartTime().toLocalTime());
                    } else {
                        windowStart = event.getStartTime().toLocalTime().plusSeconds(event.getDuration());
                    }
                }
                if (!newDay.isDateSet()) {
                    newDay.setStartDate(event.getStartDate());
                    newDay.setEndDate(event.getEndDate());
                    newDay.setDateSet(true);
                }
            }
            daysList.add(newDay);
        }
        return prioritizeDays(daysList);
    }


    /**
     * Takes in an unsorted List of day objects and sorts them based on the amount of unallocated time. returns a sorted
     * list of days from most free time to least. Additionally, assigns priority to the day objects based on ordering.
     *
     * @param rawDays Raw day objects in list form. Unsorted, unprioritized.
     * @return Sorted Day List, from most free time to least.
     */
    private List<Day> prioritizeDays(List<Day> rawDays) {

        // sorting by free time;
        List<Day> sorted = new ArrayList<>();
        int highestPrio = rawDays.size();

        while (!rawDays.isEmpty()) {
            int freeMinutes = 0;
            Day newDay = new Day();

            for (Day day : rawDays) {

                if (day.getFreeTime() > freeMinutes) {
                    newDay = day;
                }
            }

            // set the priority for that day
            newDay.setPriority(highestPrio);
            highestPrio--;

            // add to sorted and remove from unsortedlist
            sorted.add(newDay);
            rawDays.remove(newDay);
        }
        return sorted;
    }

    /**
     * Takes in a single event list and sorts it based on start times. returns the list sorted in chronological order
     *
     * @param unsortedlist An unsorted list of event objects
     * @return Chronologically sorted list of events
     */
    private List<Event> getSortedList(List<Event> unsortedlist) {
        List<Event> sorted = new ArrayList<>();

        while (!unsortedlist.isEmpty()) {
            LocalTime minTime = LocalTime.of(23, 59);
            Event earliestEvent = new Event();

            for (Event event : unsortedlist) {
                if (event.getStartTime().toLocalTime().compareTo(minTime) >= 0) {
                    earliestEvent = event;
                }
            }
            // add to sorted and remove from unsortedlist
            sorted.add(earliestEvent);
            unsortedlist.remove(earliestEvent);
        }
        return sorted;
    }

    /**
     * Function takes in a start and end date spanning a period of time. Parses the given eventList from start date
     * to end date and creates a list of events per date. Returns a list of these day lists.
     *
     * @param start     LocalDate object representing the start date of the allocation period
     * @param end       LocalDate object representing the end date of the allocation period
     * @param eventList List object containing all given events.
     * @return List of Lists. Splits all events into lists of lists
     */
    private List<List<Event>> splitDays(LocalDate start, LocalDate end, List<Event> eventList) {
        List<List<Event>> dayLists = new ArrayList<>();
        while (end.compareTo(start) > 0) {
            List<Event> dayList = new ArrayList<>();
            for (Event event : eventList) {
                // What if a day
                if (event.getStartDate() == start || event.getEndDate() == start) {
                    dayList.add(event);
                }
            }
            start.plusDays(1);
            dayLists.add(dayList);
        }

        return dayLists;
    }


    /**
     * Gets the current date and returns a future date based on the allocationThreshold class parameter
     * if current date is a sunday, will return date + (allocationThreshold) days
     * if current date is not sunday, will return date + (allocationThreshold + n days) days
     *
     * @return new LocalDate used for target date
     */
    private LocalDate getTargetDate() {
        LocalDate targetDate = LocalDate.now().plusDays(allocationThreshold);

        return getNextSunday(targetDate);
    }

    /**
     * Takes in a LocalDate value and returns the date of the nearest sunday.
     *
     * @param startDate any LocalDate object
     * @return LocalDate of the closest following sunday
     */
    private LocalDate getNextSunday(LocalDate startDate) {
        LocalDate sunday = startDate;

        while (sunday.getDayOfWeek().getValue() != 7) {
            sunday = sunday.plusDays(1);
        }
        return sunday;
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
