package com.serenitask.controller;

import com.calendarfx.model.CalendarSource;
import com.calendarfx.view.DetailedDayView;
import com.calendarfx.view.DetailedWeekView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.Scene;
import com.serenitask.ui.WindowComponents.AddEvent;
import com.serenitask.ui.WindowComponents.AddGoal;
import com.serenitask.model.Optimiser;
import com.serenitask.ui.CalendarViewComponent;
import com.serenitask.util.Navigation.CalendarNavigation;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.time.LocalTime;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Creates listeners and attaches them to a scene. Implements shortcuts into the scene and their actions when triggered
 */
public class ShortcutController {
    /**
     * Creates listeners to handle key press events and attaches them to the scene.
     * @param scene Current scene contained in Stage of javaFx
     * @param mainCalendarSource Object containing all calendars
     */
    public static void setupShortcuts(Scene scene, CalendarSource mainCalendarSource, AtomicBoolean isWeeklyView, Text dailyText, Text weeklyText, StackPane switchViewButton,
                                      VBox leftPanel, DetailedDayView calendarDayView, DetailedWeekView calendarWeekView, VBox dailygoals) {

        KeyCombination goalCombo = new KeyCodeCombination(KeyCode.G, KeyCombination.CONTROL_DOWN);
        KeyCombination eventCombo = new KeyCodeCombination(KeyCode.E, KeyCombination.CONTROL_DOWN);
        KeyCombination optimiserCombo = new KeyCodeCombination(KeyCode.SPACE, KeyCombination.CONTROL_DOWN);
        KeyCombination toggleViewCombo = new KeyCodeCombination(KeyCode.W, KeyCombination.CONTROL_DOWN);

        KeyCombination toggleGoBackCombo = new KeyCodeCombination(KeyCode.B, KeyCombination.CONTROL_DOWN);
        KeyCombination toggleTodayCombo = new KeyCodeCombination(KeyCode.T, KeyCombination.CONTROL_DOWN);
        KeyCombination toggleForwardCombo = new KeyCodeCombination(KeyCode.F, KeyCombination.CONTROL_DOWN);

        scene.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if (goalCombo.match(event)) {
                openGoalAddMenu(dailygoals);
                event.consume();

            } else if (eventCombo.match(event)) {
                openEventAddMenu(mainCalendarSource);
                event.consume();

            } else if (optimiserCombo.match(event)) {
                openOptimiserMenu(mainCalendarSource);
                event.consume();
            }
            else if (toggleGoBackCombo.match(event)) {
                goBackToggle(calendarDayView, calendarWeekView);
                event.consume();
            }
            else if (toggleViewCombo.match(event)) {
                changeViewToggle(isWeeklyView, dailyText, weeklyText, switchViewButton, leftPanel, calendarDayView, calendarWeekView);
                event.consume();
            }
            else if (toggleTodayCombo.match(event)) {
                goTodayToggle(calendarDayView, calendarWeekView);
                event.consume();
            }
            else if (toggleForwardCombo.match(event)) {
                goForwardToggle(calendarDayView, calendarWeekView);
                event.consume();
            }
        });
    }

    /**
     * Shortcut event action that opens the Add Goal menu
     */
    private static void openGoalAddMenu(VBox dailygoals) {
        AddGoal.displayAddGoalView(dailygoals);
    }

    /**
     * Shortcut event action that opens the Add Event menu
     * @param mainCalendarSource Calendarsource containing calendars to add event
     */
    private static void openEventAddMenu(CalendarSource mainCalendarSource) {
        AddEvent.displayAddEventView(mainCalendarSource);
    }

    /**
     * Shortcut event action that runs the optimiser
     * @param calendarSource CalendarSource containing the calendars to be optimised
     */
    private static void openOptimiserMenu(CalendarSource calendarSource) {
        // Gross duplicate dummy values in 2 areas. Make a database table or object to hold this.
        LocalTime userDayStart = LocalTime.of(8, 0, 0);
        LocalTime userDayEnd = LocalTime.of(18, 30, 0);
        int allocateAhead = 7;
        Optimiser.optimize(calendarSource, userDayStart, userDayEnd, allocateAhead);
    }

    private static void changeViewToggle(AtomicBoolean isWeeklyView, Text dailyText, Text weeklyText, StackPane switchViewButton,
                                         VBox leftPanel, DetailedDayView calendarDayView, DetailedWeekView calendarWeekView) {
        CalendarViewComponent.switchView(isWeeklyView, dailyText, weeklyText, switchViewButton, leftPanel, calendarDayView, calendarWeekView);
    }

    public static void goBackToggle(DetailedDayView calendarDayView, DetailedWeekView calendarWeekView) {
        CalendarNavigation.goBack(calendarDayView, calendarWeekView);
    }

    public static void goTodayToggle(DetailedDayView calendarDayView, DetailedWeekView calendarWeekView) {
        CalendarNavigation.goToday(calendarDayView, calendarWeekView);
    }

    public static void goForwardToggle(DetailedDayView calendarDayView, DetailedWeekView calendarWeekView) {
        CalendarNavigation.goForward(calendarDayView, calendarWeekView);
    }
}
