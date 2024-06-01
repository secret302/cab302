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
     * The minimum threshold for allocation.
     */
    private final int allocationThreshold;



    /**
     * Constructor for RoutineTwo, Represents the complex routine used for allocating balanced health focused habits.
     *
     * @param dayStart            LocalTime object representing the Start of users day
     * @param dayEnd              LocalTime object representing the End of users day
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
     * @param mainSource Calendar Source Object containing all calendars
     */
    public void runRoutine(CalendarSource mainSource) {
        try {
            Calendar healthCalendar = OptimizerUtil.getGoalCalendar(mainSource.getCalendars(), TargetCalendar);

            EventDAO eventDAO = new EventDAO();
            List<Event> eventList = eventDAO.getAllEvents();

            LocalDate allocationStart = LocalDate.now();
            LocalDate allocationEnd = OptimizerUtil.getTargetDate(allocationThreshold);
            List<List<Event>> rawDaysLists = OptimizerUtil.splitDays(allocationStart, allocationEnd, eventList);
            List<Day> preparedDays = prepareDays(rawDaysLists,allocationStart);
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
        int minAllocation = 15;
        List<Entry<?>> entriesToAdd = new ArrayList<>();
        Random random = new Random();

        boolean hasWindows;
        for (Day day : days) {
            while (checkRatio(day)) {
                int targetChange = day.getHealthNeeded(healthRatio);
                if (targetChange > 30) {
                    targetChange = 30;
                }
                if (targetChange < 15)
                {
                    targetChange = 15;
                }

                TimeWindow window = day.getBiggestWindow();
                if(window != null){
                    Duration duration = Duration.between(window.getWindowOpen(), window.getWindowClose());
                    int windowMins = (int) (duration.getSeconds() / 60);

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
                                startTime = window.getWindowClose().minusMinutes(targetChange);
                                LocalTime endTime = window.getWindowClose();
                                day.addWindow(DayStart, startTime);
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
                else
                {
                    break;
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
        return switch (index) {
            case 1 -> "Quick Walk";
            case 2 -> "Stretching";
            case 3 -> "Power Nap";
            case 4 -> "Meditation";
            case 5 -> "Yoga";
            case 6 -> "Jogging";
            case 7 -> "Plan Meals";
            case 8 -> "Breathing Exercises";
            case 9 -> "Free Time";
            case 10 -> "Healthy Snack Preparation";
            case 11 -> "Hydration Break";
            case 12 -> "Positive Affirmations";
            case 13 -> "Herbal Tea Break";
            case 14 -> "Listen to Music";
            case 15 -> "Watch a Motivational Video";
            case 16 -> "Write in a Journal";
            case 17 -> "Acupressure";
            case 18 -> "Biking";
            case 19 -> "Mindfulness Coloring";
            case 20 -> "Balance Exercises";
            case 21 -> "Light Gardening";
            default -> "Invalid activity";
        };

    }

    /**
     * Computes the ratio between work and health based activities. For every 4 hours of work,
     * there should be 30 minutes of health based activities
     *
     * @param day Day object representing a single day
     * @return Boolean, true if day needs more health activities, false if balanced.
     */
    private boolean checkRatio(Day day) {
        int work = day.getWorkingTime();
        int health = day.getHealthTime();

        if (work == 0 ) {
            return false;
        }
        else if (health == 0)
        {
            return true;
        }
        else if ((work / health) > healthRatio) {
            int ratio = (work/health);
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
        List<Day> daysList = new ArrayList<>();

        for (List<Event> list : rawDaysLists) {
            List<Event> createList = new ArrayList<>(list);
            Day newDay = OptimizerUtil.createDay(createList, DayStart, DayEnd, date);
            newDay = appendEvents(newDay, list);
            daysList.add(newDay);
        }
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
