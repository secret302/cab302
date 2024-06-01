package com.serenitask.ui;

import java.util.concurrent.atomic.AtomicBoolean;

import com.calendarfx.model.CalendarSource;
import com.calendarfx.view.AgendaView;

import com.serenitask.ui.WindowComponents.AddEvent;
import com.serenitask.ui.WindowComponents.AddGoal;
import com.serenitask.model.Optimiser;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;


/**
 * Contains static methods to manage and manipulate the right panel of the application's UI,
 * which includes action components like switching views, adding goals, and optimizing events.
 */
public class RightPanelComponent {
    /**
     * Sets up and populates the right panel with various functional buttons such as switching views,
     * optimizing schedules, adding goals, and adding events.
     *
     * @param rightPanelObjects The VBox that holds all interactive elements of the right panel.
     * @param dailyText         Text control for accessing the daily view.
     * @param weeklyText        Text control for accessing the weekly view.
     * @param switchViewBox     Rectangle background for the switch view button.
     * @param switchViewButton  StackPane acting as the switch view button.
     * @param isWeeklyView      Flag indicating whether the weekly view is currently displayed.
     * @param optimiseButton    StackPane acting as the optimise button.
     * @param optimiseText      Text displayed on the optimise button.
     * @param optimiseViewBox   Rectangle background for the optimise button.
     * @param addGoalButton     StackPane acting as the add goal button.
     * @param addGoalText       Text displayed on the add goal button.
     * @param addGoalViewBox    Rectangle background for the add goal button.
     * @param addEventButton    StackPane acting as the add event button.
     * @param addEventText      Text displayed on the add event button.
     * @param addEventViewBox   Rectangle background for the add event button.
     */
    public static void actionsComponent(VBox rightPanelObjects, Text dailyText, Text weeklyText, Rectangle switchViewBox, StackPane switchViewButton, AtomicBoolean isWeeklyView, StackPane optimiseButton, Text optimiseText, Rectangle optimiseViewBox, StackPane addGoalButton, Text addGoalText, Rectangle addGoalViewBox
            , StackPane addEventButton, Text addEventText, Rectangle addEventViewBox) {
        switchViewBox.setWidth(170.0);
        switchViewBox.setFill(Color.GREY);

        switchViewBox.setArcWidth(10);
        switchViewBox.setArcHeight(10);
        dailyText.setFont(Font.font(30));
        weeklyText.setFont(Font.font(30));
        dailyText.setFill(Color.WHITE);
        weeklyText.setFill(Color.WHITE);
        //dateToday.setFont(Font.font(40));

        optimiseViewBox.setWidth(170.0);
        optimiseViewBox.setFill(Color.GREY);
        optimiseViewBox.setArcWidth(10);
        optimiseViewBox.setArcHeight(10);
        optimiseText.setFont(Font.font(30));
        optimiseText.setFill(Color.WHITE);

        addGoalViewBox.setWidth(170.0);
        addGoalViewBox.setFill(Color.GREY);
        addGoalViewBox.setArcWidth(10);
        addGoalViewBox.setArcHeight(10);
        addGoalText.setFont(Font.font(30));
        addGoalText.setFill(Color.WHITE);
        addGoalButton.setPadding(new Insets(0, 20, 0, 20));

        addEventViewBox.setWidth(170.0);
        addEventViewBox.setFill(Color.GREY);
        addEventViewBox.setArcWidth(10);
        addEventViewBox.setArcHeight(10);
        addEventText.setFont(Font.font(30));
        addEventText.setFill(Color.WHITE);
        addEventButton.setPadding(new Insets(0, 20, 0, 20));

        switchViewButton.getChildren().addAll(switchViewBox, isWeeklyView.get() ? weeklyText : dailyText);
        optimiseButton.getChildren().addAll(optimiseViewBox, optimiseText);
        addGoalButton.getChildren().addAll(addGoalViewBox, addGoalText);
        addEventButton.getChildren().addAll(addEventViewBox, addEventText);

        rightPanelObjects.getChildren().addAll(switchViewButton, addEventButton, optimiseButton);
        rightPanelObjects.setSpacing(20);
    }

    /**
     * Toggles between the actions view and the default view containing the agenda and daily goals.
     *
     * @param rightPanelObjects The VBox containing the action components.
     * @param isActionsView     AtomicBoolean indicating whether the actions view is currently displayed.
     * @param rightPanel        The main right panel VBox that switches between displaying actions and agenda/daily goals.
     * @param agenda            The AgendaView component.
     */
    public static void switchRight(VBox rightPanelObjects, AtomicBoolean isActionsView, VBox rightPanel, AgendaView agenda, StackPane addGoalButton, Rectangle leftButtonPanelSwitchViewBox, Rectangle rightButtonPanelSwitchViewBox, Text leftButtonPanelText, Text rightButtonPanelText,
                                   VBox staticgoals) {
        //boolean added = false;
        //VBox goals = DailyGoalsComponent.goalView(dailygoals, goalTextField, createGoalButton);
        

        if(!isActionsView.get()){
            isActionsView.set(!isActionsView.get());
            leftButtonPanelSwitchViewBox.setFill(Color.web("#b2b3b7")); // Light red
            rightButtonPanelSwitchViewBox.setFill(Color.web("#e84d3e")); // Light gray
            rightButtonPanelText.setFill(Color.WHITE);
            leftButtonPanelText.setFill(Color.BLACK);


                rightPanel.getChildren().remove(rightPanelObjects);
                rightPanel.getChildren().remove(agenda);
                rightPanel.getChildren().add(2, addGoalButton);
                rightPanel.getChildren().add(3, staticgoals);
                
            }
        }

        
    

    public static void switchLeft(VBox rightPanelObjects, AtomicBoolean isActionsView, VBox rightPanel, AgendaView agenda, StackPane addGoalButton, Rectangle leftButtonPanelSwitchViewBox, Rectangle rightButtonPanelSwitchViewBox, Text leftButtonPanelText, Text rightButtonPanelText,
                                  VBox staticgoals) {

        //VBox goals = DailyGoalsComponent.goalView(dailygoals, goalTextField, createGoalButton);

        if(isActionsView.get()){
            isActionsView.set(!isActionsView.get());
            leftButtonPanelSwitchViewBox.setFill(Color.web("#e84d3e")); // Light red
            rightButtonPanelSwitchViewBox.setFill(Color.web("#b2b3b7")); // Light gray
            rightButtonPanelText.setFill(Color.BLACK);
            leftButtonPanelText.setFill(Color.WHITE);
            rightPanel.getChildren().remove(addGoalButton);
            rightPanel.getChildren().remove(staticgoals);
            rightPanel.getChildren().add(2, rightPanelObjects);
            rightPanel.getChildren().add(3, agenda);
            }
        }

        
    



            //
            


    /**
     * Handles the click event for the "Add Goal" button.
     * This method displays the interface for adding a new goal.
     */
    public static void addGoalClick(VBox dailygoals) {
        AddGoal.displayAddGoalView(dailygoals);
    }

    /**
     * Handles the click event for the "Optimise" button.
     * This method invokes the Optimiser to optimise the event scheduling based on user-defined constraints.
     *
     * @param calendarSource The source of calendars to be optimized.
     */
    public static void addOptimiseClick(CalendarSource calendarSource) {

        try {
            Optimiser.optimize(calendarSource);
        } catch (Exception e) {
            System.err.println("An error occurred while trying to add a window of time to the day: " + e.getMessage());
            e.printStackTrace();
        }


        // throw new UnsupportedOperationException("Unimplemented method 'addOptimiseClick'");
    }

    /**
     * Handles the click event for the "Add Event" button.
     * This method displays the interface for adding a new event to the calendar.
     *
     * @param calendarSource The source of calendars where the event will be added.
     */
    public static void addEventClick(CalendarSource calendarSource) {
        AddEvent.displayAddEventView(calendarSource);
    }
}
