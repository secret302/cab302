package com.serenitask.controller;

import com.calendarfx.model.Calendar;
import com.calendarfx.model.CalendarSource;
import com.calendarfx.model.Entry;
import com.calendarfx.model.Interval;
import com.serenitask.model.*;
import com.serenitask.util.DatabaseManager.EventDAO;
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
public class RoutineTwoController {

    // Dummy value of 7, will be replaced with 28 for 1 month or ~4 weeks
    private final int healthRatio = 8;
    private final int allocationThreshold;
    private final String TargetCalendar = "Health";
    private final LocalTime DayStart;
    private final LocalTime DayEnd;

    /**
     * Constructor for RoutineTwo, Represents the complex routine used for allocating balanced health focused habits.
     *
     * @param dayStart            LocalTime object representing the Start of users day
     * @param dayEnd              LocalTime object representing the End of users day
     * @param allocationThreshold integer value of days to allocate ahead. 7 = 1 week;
     */
    public RoutineTwoController(LocalTime dayStart, LocalTime dayEnd, int allocationThreshold) {
        this.allocationThreshold = allocationThreshold;
        this.DayStart = dayStart;
        this.DayEnd = dayEnd;
    }

    /**
     * Simple execution function that executes routine 2 of the optimizer.
     * This routine handles the minimum ratio required for work:health is above the required amount.
     * For every 4 hours of work there must be 30 minutes of health based activity.
     *
     * @param mainSource CalandarSource Object containing all calendars
     */
    public void runRoutine(CalendarSource mainSource) {

        Calendar healthCalendar = OptimizerUtil.getGoalCalendar(mainSource.getCalendars(), TargetCalendar);

        EventDAO eventDAO = new EventDAO();
        List<Event> eventList = eventDAO.getAllEvents();

        LocalDate allocationStart = LocalDate.now();
        LocalDate allocationEnd = OptimizerUtil.getTargetDate(allocationThreshold);

        List<List<Event>> rawDaysLists = OptimizerUtil.splitDays(allocationStart, allocationEnd, eventList);

        List<Day> preparedDays = prepareDays(rawDaysLists);

        List<Entry<?>> entriesToAdd = improveDays(preparedDays);

        OptimizerUtil.commitEntries(entriesToAdd, healthCalendar);
    }


    /**
     * Complex function used to improve a day object by inject health based activities into it to
     * achieve a target level of balance
     *
     * @param days List of Day objects
     * @return List of entries to be added to calendar
     */
    private List<Entry<?>> improveDays(List<Day> days) {
        List<Entry<?>> entriesToAdd = new ArrayList<>();
        Random random = new Random();

        boolean hasWindows = false;
        for (Day day : days) {

            while (checkRatio(day)) {
                int targetChange = day.getHealthNeeded(healthRatio);
                if (targetChange > 30) {
                    targetChange = 30;
                }


                TimeWindow window = day.getBiggestWindow();
                Duration duration = Duration.between(window.getWindowOpen(), window.getWindowClose());
                int windowMins = (int) (duration.getSeconds() / 60);
                if (windowMins > 0) {
                    hasWindows = true;
                }

                if (hasWindows) {
                    int activityNumber = random.nextInt(10) + 1;

                    if (windowMins > targetChange) {

                        LocalTime startTime = window.getWindowOpen();
                        LocalTime endTime = window.getWindowOpen().plusMinutes(targetChange);
                        day.addWindow(endTime, window.getWindowClose());
                        day.addHealth(targetChange);
                        Entry<?> newEntry = new Entry<>(getHealthEvent(activityNumber));
                        newEntry.setInterval(new Interval(day.getStartDate(), startTime, day.getEndDate(), endTime));
                        newEntry.setFullDay(false);
                        entriesToAdd.add(newEntry);
                    } else {
                        Entry<?> newEntry = new Entry<>(getHealthEvent(activityNumber));
                        newEntry.setInterval(new Interval(day.getStartDate(), window.getWindowOpen(), day.getEndDate(), window.getWindowClose()));
                        newEntry.setFullDay(false);
                        int difference = (int) Duration.between(window.getWindowOpen(), window.getWindowClose()).toMinutes();
                        day.addHealth(difference);
                        entriesToAdd.add(newEntry);
                    }
                }
            }
        }

        return entriesToAdd;
    }


    /**
     * Draws a random health based event from a list of possible events.
     *
     * @param index Integer used for retrieving random event
     * @return String object containing health based event name
     */
    private String getHealthEvent(int index) {
        switch (index) {
            case 1:
                return "Quick Walk";
            case 2:
                return "Stretching";
            case 3:
                return "Call Family";
            case 4:
                return "Meditation";
            case 5:
                return "Yoga";
            case 6:
                return "Jogging";
            case 7:
                return "Plan Meals";
            case 8:
                return "Deep Breathing Exercises";
            case 9:
                return "Dance";
            case 10:
                return "Healthy Snack Preparation";
            default:
                return "Invalid activity";
        }

    }

    /**
     * Computes the ratio between work and health based activies. For every 4 hours of work,
     * there should be 30 minutes of health based activies
     *
     * @param day Day object representing a single day
     * @return Boolean, true if day needs more health activities, false if balanced.
     */
    private boolean checkRatio(Day day) {
        int work = day.getWorkingTime();
        int health = day.getHealthTime();
        if (work / health > healthRatio) {
            return true;
        }
        return false;
    }


    /**
     * Takes in a List of Lists containing events sorted by day. Uses events per day to evaluate available TimeWindows
     * available for allocation. Each list of events is converted to a Day which contains a List of Windows.
     *
     * @param rawDaysLists List of lists containing events, sorted by date
     * @return List of Day objects
     */
    private List<Day> prepareDays(List<List<Event>> rawDaysLists) {
        System.out.println("Days List Start");
        List<Day> daysList = new ArrayList<>();
        for (List<Event> list : rawDaysLists) {
            Day newDay = OptimizerUtil.createDay(list, DayStart, DayEnd);
            newDay = appendEvents(newDay, list);
        }
        System.out.println("Days List End");
        return daysList;
    }

    /**
     * Appends event duration values to either health or work based tallies
     *
     * @param day    Day object representing a day
     * @param events List of events for the given day
     * @return Returns updated Day object
     */
    private Day appendEvents(Day day, List<Event> events) {
        for (Event event : events) {
            if (event.getCalendar().equals("Health")) {
                day.addHealth((int) event.getInterval().getDuration().toMinutes());
            } else {
                day.addWork((int) event.getInterval().getDuration().toMinutes());
            }
        }
        return day;
    }

}
