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

public class NavigationBar extends DateControl {

private final BooleanProperty showNavigation = new SimpleBooleanProperty(this, "showNavigation", true);


public final BooleanProperty showNavigationProperty() {
    return showNavigation;}

    public NavigateDateView createButton(DetailedDayView calendarDayView, DetailedWeekView calendarWeekView)
    {
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

    public void goBack() {
        setDate(getDate().minusDays(1));
    }

    public void goToday() {
        setDate(getToday());
    }
    
    public void goForward() {
        setDate(getDate().plusDays(1));
    }



}