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

    /**
     * Target ratio value - 240 minutes / 30 minutes = 8
     */
    private final int healthRatio = 8;
    /**
     * Target calendar for health based events
     */
    private final String TargetCalendar = "Health";
    /**
     * LocalTime object representing users start of day
     */
    private final LocalTime DayStart;
    /**
     * LocalTime object representing users end of day
     */
    private final LocalTime DayEnd;
    /**
     * Integer representing the maximum amount of time a health based event can be in minutes
     */
    private final int maxChunk = 30;

    /**
     * Constructor for RoutineTwo, Represents the complex routine used for allocating balanced health focused habits.
     *
     * @param dayStart            LocalTime object representing the Start of users day
     * @param dayEnd              LocalTime object representing the End of users day
     */
    public RoutineTwoController(LocalTime dayStart, LocalTime dayEnd) {
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
        try {
            Calendar healthCalendar = OptimizerUtil.getGoalCalendar(mainSource.getCalendars(), TargetCalendar);

            EventDAO eventDAO = new EventDAO();
            List<Event> eventList = eventDAO.getAllEvents();

            LocalDate allocationStart = LocalDate.now();
            LocalDate allocationEnd = OptimizerUtil.getNextSunday(allocationStart);

            List<List<Event>> rawDaysLists = OptimizerUtil.splitDays(allocationStart, allocationEnd, eventList);
            System.out.println("run test: rawList size: " + rawDaysLists.size());
            List<Day> preparedDays = prepareDays(rawDaysLists,allocationStart);
            System.out.println("run test: preparedDays size: " + preparedDays.size());
            List<Entry<?>> entriesToAdd = improveDays(preparedDays);

            OptimizerUtil.commitEntries(entriesToAdd, healthCalendar);
        }
        catch(Exception e){
            System.err.println("An error occurred while handling the ratio between work and health: " + e.getMessage());
            e.printStackTrace();
        }
    }


    /**
     * Complex function used to improve a day object by inject health based activities into it to
     * achieve a target level of balance
     *
     * @param days List of Day objects
     * @return List of entries to be added to calendar
     */
    private List<Entry<?>> improveDays(List<Day> days) {
        System.out.println("improveDays: Days size: " + days.size());
        System.out.println("improveDays Start");
        List<Entry<?>> entriesToAdd = new ArrayList<>();
        Random random = new Random();

        boolean hasWindows = false;
        for (Day day : days) {
            System.out.println("improveDays: Day test");
            while (checkRatio(day)) {
                System.out.println("improveDays: Ratio test");
                int targetChange = day.getHealthNeeded(healthRatio);
                if (targetChange > 30) {
                    targetChange = 30;
                }

                TimeWindow window = day.getBiggestWindow();
                Duration duration = Duration.between(window.getWindowOpen(), window.getWindowClose());
                int windowMins = (int) (duration.getSeconds() / 60);

                System.out.println("windowMins test: " + windowMins);
                if (windowMins > 0) {
                    hasWindows = true;
                }
                else
                {
                    break;
                }

                if (hasWindows) {
                    int activityNumber = random.nextInt(10) + 1;

                    if (windowMins > targetChange) {

                        LocalTime startTime = window.getWindowOpen();
                        if(startTime.equals(DayStart))
                        {
                            int eventOffsetValue = OptimizerUtil.calcOffsetMins(windowMins, 30);
                            startTime = window.getWindowOpen().plusMinutes(eventOffsetValue);
                            LocalTime endTime = window.getWindowOpen().plusMinutes(targetChange+eventOffsetValue);
                            day.addWindow(endTime, window.getWindowClose());
                            day.addHealth(targetChange);
                            Entry<?> newEntry = new Entry<>(getHealthEvent(activityNumber));
                            newEntry.setInterval(new Interval(day.getStartDate(), startTime, day.getEndDate(), endTime));
                            newEntry.setFullDay(false);
                            entriesToAdd.add(newEntry);
                        }
                        else
                        {
                        LocalTime endTime = window.getWindowOpen().plusMinutes(targetChange);
                        day.addWindow(endTime, window.getWindowClose());
                        day.addHealth(targetChange);
                        Entry<?> newEntry = new Entry<>(getHealthEvent(activityNumber));
                        newEntry.setInterval(new Interval(day.getStartDate(), startTime, day.getEndDate(), endTime));
                        newEntry.setFullDay(false);
                        entriesToAdd.add(newEntry);
                        }
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
        System.out.println("improveDays End");
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

        System.out.println("work test: " + work);
        System.out.println("health test: " + health);

        if (work == 0 ) {
            return false;
        }
        else if (health == 0)
        {
            return true;
        }
        else if ((int)(work / health) > healthRatio) {
            int ratio = (int) (work/health);
            System.out.println("healthTime test: " + ratio);
            return true;
        }
        return false;
    }


    /**
     * Takes in a List of Lists containing events sorted by day. Uses events per day to evaluate available TimeWindows
     * available for allocation. Each list of events is converted to a Day which contains a List of Windows.
     *
     * @param rawDaysLists List of lists containing events, sorted by date
     * @param date Localdate object representing starting date of allocation
     * @return List of Day objects
     */
    private List<Day> prepareDays(List<List<Event>> rawDaysLists, LocalDate date) {
        System.out.println("Days List Start");
        List<Day> daysList = new ArrayList<>();


        for (List<Event> list : rawDaysLists) {
            List<Event> createList = new ArrayList<>(list);
            Day newDay = OptimizerUtil.createDay(createList, DayStart, DayEnd, date);
            newDay = appendEvents(newDay, list);
            daysList.add(newDay);
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
        System.out.println("Day - List Size: " + events.size());
        for (Event event : events) {
            if (event.getCalendar().equals("Health")) {
                System.out.println("Day - Added health: " + (int) event.getInterval().getDuration().toMinutes());
                day.addHealth((int) event.getInterval().getDuration().toMinutes());
            } else {
                System.out.println("Day - Added work: " + (int) event.getInterval().getDuration().toMinutes());
                day.addWork((int) event.getInterval().getDuration().toMinutes());
            }
        }
        return day;
    }

}
