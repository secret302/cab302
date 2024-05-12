package com.serenitask.ui;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.concurrent.atomic.AtomicBoolean;

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

public class CalendarViewComponent {
    public static void calendarView(Text dailyText, Text weeklyText, Text dateToday, Rectangle switchViewBox, StackPane switchViewButton, AtomicBoolean isWeeklyView, HBox dateTodayPanel, Region spacer, 
    StackPane optimiseButton, Text optimiseText, Rectangle optimiseViewBox, StackPane addGoalButton, Text addGoalText, Rectangle addGoalViewBox)
    {
        switchViewBox.setWidth(170.0);
        switchViewBox.setFill(Color.GREY);
         switchViewBox.setArcWidth(10);
         switchViewBox.setArcHeight(10);
         dailyText.setFont(Font.font(30));
         weeklyText.setFont(Font.font(30));
         dailyText.setFill(Color.WHITE);
         weeklyText.setFill(Color.WHITE);
         dateToday.setFont(Font.font(40));

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

         dateTodayPanel.getChildren().addAll(dateToday, spacer, optimiseButton, addGoalButton , switchViewButton);
         HBox.setHgrow(dateToday, Priority.ALWAYS);
         StackPane.setAlignment(dateToday, Pos.CENTER_LEFT);
         spacer.setMinWidth(700);
         StackPane.setAlignment(switchViewButton, Pos.CENTER_RIGHT);
    }

    public static void switchView(AtomicBoolean isWeeklyView, Text dailyText, Text weeklyText, StackPane switchViewButton,
                            VBox leftPanel, DetailedDayView calendarDayView, DetailedWeekView calendarWeekView){
        isWeeklyView.set(!isWeeklyView.get());
             switchViewButton.getChildren().remove(isWeeklyView.get() ? dailyText : weeklyText);
             switchViewButton.getChildren().add(isWeeklyView.get() ? weeklyText : dailyText);
             if (isWeeklyView.get()) {
                 leftPanel.getChildren().remove(calendarDayView);
                 leftPanel.getChildren().add(1, calendarWeekView);
                 calendarWeekView.setMinHeight(900);
                 calendarWeekView.setMaxHeight(900);
                 calendarWeekView.setPadding(new Insets(40,0,0,0));
             } else {
                 leftPanel.getChildren().remove(calendarWeekView);
                 leftPanel.getChildren().add(1, calendarDayView);
                 calendarDayView.setMinHeight(900);
                 calendarDayView.setMaxHeight(900);
             }
    }
}
