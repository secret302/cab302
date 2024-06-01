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

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Creates listeners and attaches them to a scene. Implements shortcuts into the scene and their actions when triggered
 */
public class ShortcutController {
    /**
     * Creates listeners to handle key press events and attaches them to the scene.
     * @param scene Current scene contained in Stage of javaFx
     * @param mainCalendarSource Object containing all calendars
     * @param isWeeklyView indicates what view is being shown on calendar.
     * @param dailyText Text that swaps between daily and weekly views
     * @param calendarDayView The view component for displaying daily calendar
     * @param calendarWeekView The view component for displaying weekly calendar
     * @param leftPanel The left panel in the UI layout
     * @param switchViewButton Button that toggles the view between daily and weekly
     * @param weeklyText Text that swaps between daily and weekly views
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
        Optimiser.optimize(calendarSource);
    }

    /**
     * Toggles the view between daily and weekly views in the calendar.
     *
     * @param isWeeklyView Indicates if the current view is weekly
     * @param dailyText Text element for the daily view
     * @param weeklyText Text element for the weekly view
     * @param switchViewButton Button to switch the view
     * @param leftPanel The left panel in the UI layout
     * @param calendarDayView The view component for displaying daily calendar
     * @param calendarWeekView The view component for displaying weekly calendar
     */
    private static void changeViewToggle(AtomicBoolean isWeeklyView, Text dailyText, Text weeklyText, StackPane switchViewButton,
                                         VBox leftPanel, DetailedDayView calendarDayView, DetailedWeekView calendarWeekView) {
        CalendarViewComponent.switchView(isWeeklyView, dailyText, weeklyText, switchViewButton, leftPanel, calendarDayView, calendarWeekView);
    }

    /**
     * Toggles the calendar view to the previous period (day or week).
     *
     * @param calendarDayView The view component for displaying daily calendar
     * @param calendarWeekView The view component for displaying weekly calendar
     */
    public static void goBackToggle(DetailedDayView calendarDayView, DetailedWeekView calendarWeekView) {
        CalendarNavigation.goBack(calendarDayView, calendarWeekView);
    }

    /**
     * Toggles the calendar view to the current day.
     *
     * @param calendarDayView The view component for displaying daily calendar
     * @param calendarWeekView The view component for displaying weekly calendar
     */
    public static void goTodayToggle(DetailedDayView calendarDayView, DetailedWeekView calendarWeekView) {
        CalendarNavigation.goToday(calendarDayView, calendarWeekView);
    }

    /**
     * Toggles the calendar view to the next period (day or week).
     *
     * @param calendarDayView The view component for displaying daily calendar
     * @param calendarWeekView The view component for displaying weekly calendar
     */
    public static void goForwardToggle(DetailedDayView calendarDayView, DetailedWeekView calendarWeekView) {
        CalendarNavigation.goForward(calendarDayView, calendarWeekView);
    }
}
