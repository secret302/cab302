package com.serenitask.util.routine;

import com.calendarfx.model.Calendar;
import com.calendarfx.model.Entry;
import com.serenitask.model.Day;
import com.serenitask.model.Event;
import com.serenitask.util.ErrorHandler;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

/**
 * Class containing optimizer utility functions that are static.
 */
public class OptimizerUtil {

    /**
     * Method takes a list of entries and the calendar to inset them into.
     *
     * @param entries      List of Entry objects representing calendar events
     * @param goalCalendar Target calendar to attach entries to
     */
    public static void commitEntries(List<Entry<?>> entries, Calendar goalCalendar) {
        try {
            // Add all entries to the goal calendar
            for (Entry<?> entry : entries) {
                if (entry != null) {
                    goalCalendar.addEntry(entry);
                }
            }
        }
        catch(Exception e){
            // Handle exceptions
            System.err.println("An error occurred while committing entries: " + e.getMessage());
            ErrorHandler.handleException(e);
        }
    }

    /**
     * Takes in a LocalDate value and returns the date of the nearest sunday.
     *
     * @param startDate any LocalDate object
     * @return LocalDate of the closest following sunday
     */
    public static LocalDate getNextSunday(LocalDate startDate) {
        try {
            // Get the start date
            LocalDate sunday = startDate;

            // While the day of the week is not Sunday, increment the date
            while (sunday.getDayOfWeek().getValue() != 7) {
                sunday = sunday.plusDays(1);
            }
            // Return the nearest Sunday
            return sunday;
        }
        catch(Exception e){
            // Handle exceptions
            System.err.println("An error occurred while returning the nearest Sunday: " + e.getMessage());
            ErrorHandler.handleException(e);
        }
        // returns startDate if the sunday is not retrievable
        return startDate;
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
    public static List<List<Event>> splitDays(LocalDate start, LocalDate end, List<Event> eventList) {
        try {
            // Create a list of lists to store events
            List<List<Event>> dayLists = new ArrayList<>();

            // While the end date is greater than the start date
            while (end.compareTo(start) > 0) {
                List<Event> dayList = new ArrayList<>();
                for (Event event : eventList) {
                    // If the event is on the start date, add it to the day list
                    if (event.getInterval().getStartDate().isEqual(start) || event.getInterval().getEndDate().isEqual(start)) {
                        dayList.add(event);
                    }
                }
                // Increment the start date and add the day list to the list of lists
                start = start.plusDays(1);
                dayLists.add(dayList);

            }
            // Return the list of lists
            return dayLists;
        }
        catch(Exception e){
            // Handle exceptions
            System.err.println("An error occurred while trying to split days: " + e.getMessage());
            ErrorHandler.handleException(e);
        }
        // returns null if days cannot be split
        return null;
    }

    /**
     * Takes in a list of calendars and a target calendar as a string and returns the calendar with the name Goals
     *
     * @param calendars List of calendars in CalendarSource
     * @param Target    String name of calendar
     * @return Calendar object representing Goals calendar
     */
    public static Calendar getGoalCalendar(List<Calendar> calendars, String Target) {
        try {
            // Iterate through the list of calendars
            for (Calendar calendar : calendars) {
                // If the calendar name is Goals, return the calendar
                if (Objects.equals(calendar.getName(), Target)) {
                    return calendar;
                }
            }
            // Return a new calendar if the calendar name is not found
            return new Calendar("Unknown");
        } catch(Exception e){
            // Handle exceptions
            System.err.println("An error has occurred while trying to get Goal calendars: " + e.getMessage());
            ErrorHandler.handleException(e);
        }
        // returns null if no calendar with the name Goals exists
        return null;
    }

    /**
     * Takes in a single event list and sorts it based on start times. returns the list sorted in chronological order
     *
     * @param unsortedlist An unsorted list of event objects
     * @return Chronologically sorted list of events
     */
    public static List<Event> getSortedList(List<Event> unsortedlist) {
        try {
            // Create a new list to store the sorted events
            List<Event> sorted = new ArrayList<>();

            // While the unsorted list is not empty
            while (!unsortedlist.isEmpty()) {
                int index = 0;
                int indexRemove = 0;
                LocalTime minTime = LocalTime.of(23, 59);
                Event earliestEvent = new Event();

                // Iterate through the unsorted list to find the earliest event
                for (Event event : unsortedlist) {
                    // If the event start time is earlier than the current minimum time, update the minimum time
                    if (event.getInterval().getStartTime().compareTo(minTime) < 0) {
                        minTime = event.getInterval().getStartTime();
                        earliestEvent = event;
                        indexRemove = index;
                    }
                    index++;
                }
                // add to sorted and remove from unsortedlist
                sorted.add(earliestEvent);
                unsortedlist.remove(indexRemove);
            }
            // Return the sorted list
            return sorted;
        }
        catch(Exception e){
            // Handle exceptions
            System.err.println("An error has occurred while trying to return a sorted list: " + e.getMessage());
            ErrorHandler.handleException(e);
        }
        // returns the unsorted list if the list cannot be sorted
        return unsortedlist;
    }

    /**
     * Takes in a list of events for a single date, a starting time and end time. Creates a day object
     * and allocates all free windows of time to the day.
     *
     * @param list     List of events for a single date
     * @param dayStart LocalTime object representing start of the users day
     * @param dayEnd   LocalTime object representing end of the users day
     * @param date Localdate object representing the date of the day
     * @return A Day Object representing the Date
     */
    public static Day createDay(List<Event> list, LocalTime dayStart, LocalTime dayEnd, LocalDate date) {
        try{
            Day newDay = new Day();
            List<Event> sortedList = OptimizerUtil.getSortedList(list);
            LocalTime windowStart = dayStart;

            System.out.println("sortedList size: " + sortedList.size());
            if(sortedList.isEmpty())
            {

                newDay.setStartDate(date);
                newDay.setEndDate(date);
                newDay.setDateSet(true);
            }
            else
            {
            for (Event event : sortedList) {
                if (windowStart.isBefore(dayEnd)) {

                    if (windowStart.compareTo(event.getInterval().getStartTime()) < 0) {
                        newDay.addWindow(windowStart, event.getInterval().getStartTime());
                        windowStart = event.getInterval().getEndTime();
                    } else windowStart = event.getInterval().getEndTime();
                } else {
                    windowStart = dayEnd;
                }
                if (!newDay.isDateSet()) {
                    newDay.setStartDate(event.getInterval().getStartDate());
                    newDay.setEndDate(event.getInterval().getEndDate());
                    newDay.setDateSet(true);
                }
            }}

            if (windowStart.compareTo(dayEnd) < 0) {
                newDay.addWindow(windowStart, dayEnd);
            }

            return newDay;
        }
        catch(Exception e){
            // Handle exceptions
            System.err.println("An error has occurred while trying to create a day object to allocate free windows of time in the day: " + e.getMessage());
            ErrorHandler.handleException(e);
        }
        // returns null if a day object cannot be created
        return null;
    }

    /**
     * Gets the current date and returns a future date based on the allocationThreshold class parameter
     * if current date is a sunday, will return date + (allocationThreshold) days
     * if current date is not sunday, will return date + (allocationThreshold + n days) days
     *
     * @return new LocalDate used for target date
     */
    public static LocalDate getTargetDate(int allocationThreshold) {
        try {
            LocalDate targetDate = LocalDate.now().plusDays(allocationThreshold);
            return OptimizerUtil.getNextSunday(targetDate);
        }
        catch(Exception e){
            // Handle exceptions
            System.err.println("An error has occurred while trying to get the current date and return a future date based on the allocationThreshold parameter: " + e.getMessage());
            ErrorHandler.handleException(e);
        }
        // returns null if the future date cannot be returned
        return null;
    }

    /**
     * Calculates the random offset value when generating events. Offset values will be in
     * steps of 15 minutes to avoid unsightly event allocations
     * @param windowMins Number of minutes in the given window
     * @param eventDuration Duration of the given event
     * @return Integer in steps of 15m
     */
    public static int calcOffsetMins(int windowMins, int eventDuration)
    {
        int minsAvailable =(int) Math.floor((double) (windowMins - eventDuration) / 15);
        Random random = new Random();

        return (random.nextInt(minsAvailable)+1)*15;
    }
}
