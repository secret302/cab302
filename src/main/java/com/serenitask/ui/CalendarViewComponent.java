package com.serenitask.ui;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.concurrent.atomic.AtomicBoolean;

import com.calendarfx.view.DetailedDayView;
import com.calendarfx.view.DetailedWeekView;
import com.serenitask.controller.GoalController;

import impl.com.calendarfx.view.NavigateDateView;
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
    public static void calendarView(Text dateToday, HBox dateTodayPanel, Region spacer, NavigateDateView navigateView)
    {

         dateToday.setFont(Font.font(40));
         dateTodayPanel.getChildren().addAll(dateToday, spacer, navigateView);
         dateTodayPanel.setPadding(new Insets(20,0,0,30));
         HBox.setHgrow(dateToday, Priority.ALWAYS);
         StackPane.setAlignment(dateToday, Pos.CENTER_LEFT);
         spacer.setMinWidth(1100);
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
                 calendarWeekView.setAdjustToFirstDayOfWeek(true);

             } else {
                 leftPanel.getChildren().remove(calendarWeekView);
                 leftPanel.getChildren().add(1, calendarDayView);
                 calendarDayView.setMinHeight(900);
                 calendarDayView.setMaxHeight(900);
             }
    }
}
