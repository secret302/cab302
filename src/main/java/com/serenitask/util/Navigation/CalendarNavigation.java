package com.serenitask.util.Navigation;

import com.calendarfx.view.DateControl;
import com.calendarfx.view.DetailedDayView;
import com.calendarfx.view.DetailedWeekView;


import java.time.LocalDate;

/**
 * The NavigationBar class extends DateControl to provide navigation controls
 * for DetailedDayView and DetailedWeekView. It allows the user to navigate
 * between days and weeks via backward, forward and today navigation options.
 */
public class CalendarNavigation extends DateControl {

    /**
     * Moves the view to the previous day.
     */
    public static void goBack(DetailedDayView calendarDayView, DetailedWeekView calendarWeekView) {
        calendarDayView.setDate(calendarDayView.getDate().minusDays(1));
        calendarWeekView.setDate(calendarWeekView.getStartDate().minusWeeks(1));
    }

    /**
     * Sets the view to today's date.
     */
    public static void goToday(DetailedDayView calendarDayView, DetailedWeekView calendarWeekView) {
        calendarDayView.setDate(LocalDate.now());
        calendarWeekView.setDate(LocalDate.now());
    }

    /**
     * Moves the view to the next day.
     */
    public static void goForward(DetailedDayView calendarDayView, DetailedWeekView calendarWeekView) {
        calendarDayView.setDate(calendarDayView.getDate().plusDays(1));
        calendarWeekView.setDate(calendarWeekView.getStartDate().plusWeeks(1));
    }
}