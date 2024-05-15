package com.serenitask.ui;

import com.calendarfx.view.DateControl;
import com.calendarfx.view.DetailedDayView;
import com.calendarfx.view.DetailedWeekView;
import com.calendarfx.view.Messages;
import com.serenitask.util.Navigation.CalendarNavigation;
import impl.com.calendarfx.view.NavigateDateView;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;


import java.time.LocalDate;

/**
 * The NavigationBar class extends DateControl to provide navigation controls
 * for DetailedDayView and DetailedWeekView. It allows the user to navigate
 * between days and weeks via backward, forward and today navigation options.
 */
public class NavigationBar extends DateControl {

    private final BooleanProperty showNavigation = new SimpleBooleanProperty(this, "showNavigation", true);

    /**
     * Returns the property that indicates whether navigation is visible.
     *
     * @return showNavigation property.
     */
    public final BooleanProperty showNavigationProperty() {
        return showNavigation;
    }

    /**
     * Creates and configures a navigation bar with handlers for moving between
     * different views based on the day or week selected.
     *
     * @param calendarDayView  the DetailedDayView to be managed.
     * @param calendarWeekView the DetailedWeekView to be managed.
     * @return the navigation bar configured for the provided views.
     */
    public NavigateDateView createButton(DetailedDayView calendarDayView, DetailedWeekView calendarWeekView) {
        NavigateDateView navBar = new NavigateDateView();
        calendarWeekView.setDate(LocalDate.now());
        calendarDayView.setDate(LocalDate.now());
        navBar.setOnBackward(() -> {
            CalendarNavigation.goBack(calendarDayView,calendarWeekView);
        });
        navBar.setOnForward(() -> {
            CalendarNavigation.goForward(calendarDayView,calendarWeekView);
        });
        navBar.setOnToday(() -> {
            CalendarNavigation.goToday(calendarDayView,calendarWeekView);
        });
        navBar.visibleProperty().bind(showNavigationProperty());
        return navBar;
    }
}