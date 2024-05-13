package com.serenitask.ui;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.concurrent.atomic.AtomicBoolean;

import com.calendarfx.view.AgendaView;
import com.calendarfx.view.DetailedDayView;
import com.calendarfx.view.DetailedWeekView;


import com.serenitask.controller.GoalController;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class RightPanelComponent {
    public static void actionsComponent(VBox rightPanelObjects, Text dailyText, Text weeklyText, Rectangle switchViewBox, StackPane switchViewButton, AtomicBoolean isWeeklyView, VBox actionsPanel, StackPane optimiseButton, Text optimiseText, Rectangle optimiseViewBox, StackPane addGoalButton, Text addGoalText, Rectangle addGoalViewBox){
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

         switchViewButton.getChildren().addAll(switchViewBox, isWeeklyView.get() ? weeklyText : dailyText);
         optimiseButton.getChildren().addAll(optimiseViewBox, optimiseText);
         addGoalButton.getChildren().addAll(addGoalViewBox, addGoalText);

         rightPanelObjects.getChildren().addAll(switchViewButton, optimiseButton, addGoalButton);
    }

    public static void switchRightPanel(VBox rightPanelObjects,AtomicBoolean isActionsView, VBox rightPanel, AgendaView agenda, VBox dailyGoals){
        isActionsView.set(!isActionsView.get());
             //switchViewButton.getChildren().remove(isActionsView.get() ? dailyText : weeklyText);
             //switchViewButton.getChildren().add(isActionsView.get() ? weeklyText : dailyText);
             
             
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
}
