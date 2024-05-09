package com.serenitask.ui;

import java.time.LocalTime;

import com.calendarfx.model.CalendarSource;
import com.calendarfx.view.DetailedDayView;
import com.calendarfx.view.DetailedWeekView;

public class CalendarComponent {
    public static void updateCalendar(DetailedWeekView calendarWeekView, DetailedDayView calendarDayView,
                                        CalendarSource mainCalendarSource)
    {
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
