package com.serenitask.ui;

import java.time.LocalTime;
import java.util.concurrent.atomic.AtomicBoolean;

import com.calendarfx.model.CalendarSource;
import com.calendarfx.view.AgendaView;

import com.serenitask.ui.WindowComponents.AddEvent;
import com.serenitask.ui.WindowComponents.AddGoal;
import com.serenitask.model.Optimiser;
import javafx.geometry.Insets;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;


public class RightPanelComponent {
    public static void actionsComponent(VBox rightPanelObjects, Text dailyText, Text weeklyText, Rectangle switchViewBox, StackPane switchViewButton, AtomicBoolean isWeeklyView, VBox actionsPanel, StackPane optimiseButton, Text optimiseText, Rectangle optimiseViewBox, StackPane addGoalButton, Text addGoalText, Rectangle addGoalViewBox
    , StackPane addEventButton, Text addEventText, Rectangle addEventViewBox){
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
         addGoalButton.setPadding(new Insets(0,20,0,20));

         addEventViewBox.setWidth(170.0);
         addEventViewBox.setFill(Color.GREY);
         addEventViewBox.setArcWidth(10);
         addEventViewBox.setArcHeight(10);
         addEventText.setFont(Font.font(30));
         addEventText.setFill(Color.WHITE);
         addEventButton.setPadding(new Insets(0,20,0,20));

         switchViewButton.getChildren().addAll(switchViewBox, isWeeklyView.get() ? weeklyText : dailyText);
         optimiseButton.getChildren().addAll(optimiseViewBox, optimiseText);
         addGoalButton.getChildren().addAll(addGoalViewBox, addGoalText);
         addEventButton.getChildren().addAll(addEventViewBox, addEventText);

         rightPanelObjects.getChildren().addAll(switchViewButton, optimiseButton, addGoalButton, addEventButton);
    }

    public static void switchRightPanel(VBox rightPanelObjects,AtomicBoolean isActionsView, VBox rightPanel, AgendaView agenda, VBox dailyGoals){
        isActionsView.set(!isActionsView.get());
        
             if (isActionsView.get()) {
                 rightPanel.getChildren().remove(agenda);
                 rightPanel.getChildren().remove(dailyGoals);
                 rightPanel.getChildren().add(2, rightPanelObjects);
                 } 
            else {
                 rightPanel.getChildren().remove(rightPanelObjects);
                 rightPanel.getChildren().add(2, agenda);
                 rightPanel.getChildren().add(3, dailyGoals);
                }
    }

    public static void addGoalClick() {
        AddGoal.displayAddGoalView();
    }

    public static void addOptimiseClick(CalendarSource calendarSource) {

        try {
            LocalTime userDayStart = LocalTime.of(8, 0, 0);
            LocalTime userDayEnd = LocalTime.of(18, 30, 0);
            int allocateAhead = 7;

            Optimiser.optimize(calendarSource, userDayStart, userDayEnd, allocateAhead);
        }
        catch(Exception e){
            System.err.println("An error occurred while trying to add a window of time to the day: " + e.getMessage());
            e.printStackTrace();
        }


       // throw new UnsupportedOperationException("Unimplemented method 'addOptimiseClick'");
    }

    public static void addEventClick(CalendarSource calendarSource) {
        AddEvent.displayAddEventView(calendarSource);
    }
}
