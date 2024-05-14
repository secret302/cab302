package com.serenitask.ui;

import com.calendarfx.view.DateControl;
import com.calendarfx.view.DetailedDayView;
import com.calendarfx.view.DetailedWeekView;
import com.calendarfx.view.Messages;
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
            calendarDayView.setDate(calendarDayView.getDate().minusDays(1));
            calendarWeekView.setDate(calendarWeekView.getStartDate().minusWeeks(1));
        });
        navBar.setOnForward(() -> {
            calendarDayView.setDate(calendarDayView.getDate().plusDays(1));
            calendarWeekView.setDate(calendarWeekView.getStartDate().plusWeeks(1));
        });
        navBar.setOnToday(() -> {
            calendarDayView.setDate(LocalDate.now());
            calendarWeekView.setDate(LocalDate.now());
        });
        navBar.visibleProperty().bind(showNavigationProperty());
        return navBar;
    }

    /**
     * Moves the view to the previous day.
     */
    public void goBack() {
        setDate(getDate().minusDays(1));
    }

    /**
     * Sets the view to today's date.
     */
    public void goToday() {
        setDate(getToday());
    }

    /**
     * Moves the view to the next day.
     */
    public void goForward() {
        setDate(getDate().plusDays(1));
    }


}