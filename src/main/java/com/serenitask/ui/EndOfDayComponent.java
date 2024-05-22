package com.serenitask.ui;

import java.time.LocalDate;
import java.time.LocalTime;


import com.calendarfx.view.DetailedDayView;
import com.serenitask.controller.GoalController;
import javafx.application.Platform;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

/**
 * Provides static methods for managing end-of-day notifications and updates within the calendar UI.
 * This class is responsible for handling time-based events and displaying pop-up reminders or actions at the end of the day.
 */
public class EndOfDayComponent {
    /**
     * Monitors the current time and updates the calendar's day and time settings accordingly.
     * This method also manages displaying or hiding the end-of-day pop-up based on the time of day.
     *
     * @param calendarDisplay2 The main StackPane that displays the calendar and any pop-ups.
     * @param calendarDayView  The DetailedDayView component that shows the current day's schedule.
     * @param shadowPanel      A Rectangle that serves as an overlay for dimming the background during pop-ups.
     * @param taskPopup        The StackPane that contains the end-of-day task confirmation pop-up.
     * @param goalText         The Text that displays the current goal for confirmation.
     * @param goalController   The controller managing goal data and interactions.
     */
    public static void checkTime(StackPane calendarDisplay2, DetailedDayView calendarDayView,
                                 Rectangle shadowPanel, StackPane taskPopup, Text goalText, GoalController goalController) {
        Thread updateTimeThread = new Thread("Calendar: Update Time Thread") {
             @Override
             public void run() {
                 while (true) {
                     Platform.runLater(() -> {
                         calendarDayView.setToday(LocalDate.now());
                         calendarDayView.setTime(LocalTime.now());
                     });
 
                     try {
                         // update every 10 seconds
                         sleep(10000);
                     } catch (InterruptedException e) {
                         e.printStackTrace();
                     }
 
                 }
             }
         };
 
         updateTimeThread.setPriority(Thread.MIN_PRIORITY);
         updateTimeThread.setDaemon(true);
         updateTimeThread.start();
    }

}
