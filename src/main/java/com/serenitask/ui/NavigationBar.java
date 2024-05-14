package com.serenitask.ui;

import com.calendarfx.view.DateControl;
import com.calendarfx.view.Messages;
import impl.com.calendarfx.view.NavigateDateView;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class NavigationBar extends DateControl {

private final BooleanProperty showNavigation = new SimpleBooleanProperty(this, "showNavigation", true);

public final BooleanProperty showNavigationProperty() {
    return showNavigation;}

    public NavigateDateView createButton()
    {
        NavigateDateView navBar = new NavigateDateView();
        navBar.getTodayButton().setText(Messages.getString("Today"));
        navBar.setOnBackward(() -> goBack());
        navBar.setOnToday(() -> goToday());
        navBar.setOnForward(() -> goForward());
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