package com.serenitask.ui;

import java.time.LocalTime;

import com.calendarfx.model.CalendarSource;
import com.calendarfx.view.DetailedDayView;
import com.calendarfx.view.DetailedWeekView;

/**
 * Contains static methods for configuring and updating calendar views within the application.
 * This class handles the setup and synchronization of day and week views with the provided calendar source.
 */
public class CalendarComponent {
    /**
     * Updates the day and week views of the calendar with the given calendar source.
     * This method sets up time zone support, synchronizes the calendar views with the calendar source,
     * and configures various appearance settings.
     *
     * @param calendarWeekView   The week view component of the calendar.
     * @param calendarDayView    The day view component of the calendar.
     * @param mainCalendarSource The main source of calendar data that should be displayed in the views.
     */
    public static void updateCalendar(DetailedWeekView calendarWeekView, DetailedDayView calendarDayView,
                                      CalendarSource mainCalendarSource) {
        // Enable time zone support for both day and week views
        calendarDayView.setEnableTimeZoneSupport(true);
        calendarWeekView.setEnableTimeZoneSupport(true);


        // populate Day and Week views based on CalendarSource
        calendarDayView.getCalendarSources().setAll(mainCalendarSource);
        calendarDayView.setRequestedTime(LocalTime.now());
        calendarWeekView.getCalendarSources().setAll(mainCalendarSource);
        calendarWeekView.setRequestedTime(LocalTime.now());

        // Set appearence parameters and set style
        calendarWeekView.setMaxWidth(1600);
    }
}
