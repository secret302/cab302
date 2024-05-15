package com.serenitask.ui;

import java.util.concurrent.atomic.AtomicBoolean;

import com.calendarfx.view.DetailedDayView;
import com.calendarfx.view.DetailedWeekView;

import impl.com.calendarfx.view.NavigateDateView;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 * Utility class providing static methods to manage views within a calendar UI component.
 * This class includes methods to set up date displays and to switch between daily and weekly views.
 */
public class CalendarViewComponent {
    /**
     * Configures the main display for showing the current date on the calendar view.
     *
     * @param dateToday        A Text object to display the current date.
     * @param dateTodayPanel   An HBox that holds the date display.
     * @param spacer           A Region used to manage spacing within the layout.
     * @param navigateDateView
     */
    public static void calendarView(Text dateToday, HBox dateTodayPanel, Region spacer, NavigateDateView navigateDateView) {

        dateToday.setFont(Font.font(40));
        dateTodayPanel.getChildren().addAll(dateToday, spacer, navigateDateView);
        dateTodayPanel.setPadding(new Insets(20, 0, 0, 30));
        HBox.setHgrow(dateToday, Priority.ALWAYS);
        StackPane.setAlignment(dateToday, Pos.CENTER_LEFT);
        spacer.setMinWidth(700);
    }

    /**
     * Switches between daily and weekly views of the calendar based on the user's selection.
     * This method dynamically updates the displayed calendar view and adjusts layout settings accordingly.
     *
     * @param isWeeklyView     An AtomicBoolean indicating whether the weekly view is currently displayed.
     * @param dailyText        The Text object representing the daily view option.
     * @param weeklyText       The Text object representing the weekly view option.
     * @param switchViewButton The StackPane used as a button to toggle between views.
     * @param leftPanel        The VBox containing the calendar views.
     * @param calendarDayView  The DetailedDayView component of the calendar.
     * @param calendarWeekView The DetailedWeekView component of the calendar.
     */
    public static void switchView(AtomicBoolean isWeeklyView, Text dailyText, Text weeklyText, StackPane switchViewButton,
                                  VBox leftPanel, DetailedDayView calendarDayView, DetailedWeekView calendarWeekView) {
        isWeeklyView.set(!isWeeklyView.get());
        switchViewButton.getChildren().remove(isWeeklyView.get() ? dailyText : weeklyText);
        switchViewButton.getChildren().add(isWeeklyView.get() ? weeklyText : dailyText);
        if (isWeeklyView.get()) {
            leftPanel.getChildren().remove(calendarDayView);
            leftPanel.getChildren().add(1, calendarWeekView);
            calendarWeekView.setMinHeight(900);
            calendarWeekView.setMaxHeight(900);
            calendarWeekView.setPadding(new Insets(40, 0, 0, 0));
            calendarWeekView.setAdjustToFirstDayOfWeek(true);

        } else {
            leftPanel.getChildren().remove(calendarWeekView);
            leftPanel.getChildren().add(1, calendarDayView);
            calendarDayView.setMinHeight(900);
            calendarDayView.setMaxHeight(900);
        }
    }
}
