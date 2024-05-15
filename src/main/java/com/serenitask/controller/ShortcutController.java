package com.serenitask.controller;

import com.calendarfx.model.CalendarSource;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.Scene;
import com.serenitask.ui.WindowComponents.AddEvent;
import com.serenitask.ui.WindowComponents.AddGoal;
import com.serenitask.model.Optimiser;

import java.time.LocalTime;

public class ShortcutController {
    public static void setupShortcuts(Scene scene, CalendarSource mainCalendarSource) {

        KeyCombination goalCombo = new KeyCodeCombination(KeyCode.G, KeyCombination.CONTROL_DOWN);
        KeyCombination eventCombo = new KeyCodeCombination(KeyCode.E, KeyCombination.CONTROL_DOWN);
        KeyCombination optimiserCombo = new KeyCodeCombination(KeyCode.SPACE, KeyCombination.CONTROL_DOWN);

        scene.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if (goalCombo.match(event)) {
                openGoalAddMenu();
                event.consume();

            } else if (eventCombo.match(event)) {
                openEventAddMenu(mainCalendarSource);
                event.consume();

            } else if (optimiserCombo.match(event)) {
                openOptimiserMenu(mainCalendarSource);
                event.consume();
            }
        });
    }

    private static void openGoalAddMenu() {
        AddGoal.displayAddGoalView();
    }

    private static void openEventAddMenu(CalendarSource mainCalendarSource) {
        AddEvent.displayAddEventView(mainCalendarSource);
    }

    private static void openOptimiserMenu(CalendarSource calendarSource) {
        LocalTime userDayStart = LocalTime.of(8, 0, 0);
        LocalTime userDayEnd = LocalTime.of(18, 30, 0);
        int allocateAhead = 7;
        Optimiser.optimize(calendarSource, userDayStart, userDayEnd, allocateAhead);
    }
}
