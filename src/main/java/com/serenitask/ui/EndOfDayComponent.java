package com.serenitask.ui;

import java.time.LocalDate;
import java.time.LocalTime;


import com.calendarfx.view.DetailedDayView;
import com.serenitask.controller.GoalController;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

/**
 * Provides static methods for managing end-of-day notifications and updates within the calendar UI.
 * This class is responsible for handling time-based events and displaying pop-up reminders or actions at the end of the day.
 */
public class EndOfDayComponent {
    /**
     * Monitors the current time and updates the calendar's day and time settings accordingly.
     * This method also manages displaying or hiding the end-of-day pop-up based on the time of day.
     *
     * @param calendarDisplay2 The main StackPane that displays the calendar and any pop-ups.
     * @param calendarDayView  The DetailedDayView component that shows the current day's schedule.
     * @param shadowPanel      A Rectangle that serves as an overlay for dimming the background during pop-ups.
     * @param taskPopup        The StackPane that contains the end-of-day task confirmation pop-up.
     * @param goalText         The Text that displays the current goal for confirmation.
     * @param goalController   The controller managing goal data and interactions.
     */
    public static void checkTime(StackPane calendarDisplay2, DetailedDayView calendarDayView,
                                 Rectangle shadowPanel, StackPane taskPopup, Text goalText, GoalController goalController) {
        Thread updateTimeThread = new Thread("Calendar: Update Time Thread") {
             @Override
             public void run() {
                 while (true) {
                     Platform.runLater(() -> {
                         calendarDayView.setToday(LocalDate.now());
                         calendarDayView.setTime(LocalTime.now());
                         //LocalTime startTime = LocalTime.of(16, 0);
                         //LocalTime endTime = LocalTime.of(23, 59, 59);

                        //  if (LocalTime.now().isAfter(startTime) && LocalTime.now().isBefore(endTime)) {
                        //      if (!goalController.checkIfEmpty())
                        //      {
                        //          if (!calendarDisplay2.getChildren().contains(shadowPanel))
                        //          {
                        //              goalController.loadSimpleGoal();
                        //              calendarDisplay2.getChildren().addAll(shadowPanel, taskPopup);
                        //          }
                        //      }
                        //  }

                        //  else {
                        //      calendarDisplay2.getChildren().removeAll(shadowPanel, taskPopup);
                        //  }
                     });
 
                     try {
                         // update every 10 seconds
                         sleep(10000);
                     } catch (InterruptedException e) {
                         e.printStackTrace();
                     }
 
                 }
             }
         };
 
         updateTimeThread.setPriority(Thread.MIN_PRIORITY);
         updateTimeThread.setDaemon(true);
         updateTimeThread.start();
    }

    // public static void EndOfDayPopup(Text goalText, StackPane calendarDisplay2, GoalController goalController, VBox dailygoals, Rectangle shadowPanel, Rectangle taskPopupPanel, VBox contentVBox, HBox buttonBox, StackPane taskPopup, Button noButton, 
    // Button yesButton){
    //     shadowPanel.setWidth(1920);
    //      shadowPanel.setHeight(1080);
    //      shadowPanel.setOpacity(0.8);

    //      taskPopupPanel.setWidth(400);
    //      taskPopupPanel.setHeight(250);
    //      taskPopupPanel.setArcWidth(20);
    //      taskPopupPanel.setArcHeight(20);
    //      taskPopupPanel.setFill(Color.WHITE);

    //      // Create VBox for content
    //      contentVBox.setPadding(new Insets(20));
    //      contentVBox.setSpacing(20);
    //      buttonBox.setSpacing(20);
    //      buttonBox.setPadding(new Insets(100, 20, 20, 20)); // Padding for buttons

    //      // Adding the VBox to the rectangle
    //      taskPopup.getChildren().addAll(taskPopupPanel, contentVBox);

    //      // Buttons
    //      noButton.setPrefWidth(80);
    //      noButton.setOnAction(e ->
    //      {
    //          goalController.deleteGoal(goalController.returnFirstGoal().getId());
    //          dailygoals.getChildren().remove(dailygoals.getChildren().get(3));
    //          calendarDisplay2.getChildren().removeAll(shadowPanel, taskPopup);
    //      });

         
    //      yesButton.setPrefWidth(80);
    //      yesButton.setOnAction(e ->
    //      {
    //          goalController.deleteGoal(goalController.returnFirstGoal().getId());
    //          dailygoals.getChildren().remove(dailygoals.getChildren().get(3));
    //          calendarDisplay2.getChildren().removeAll(shadowPanel, taskPopup);
    //      });

    //      // Adding buttons to the HBox
    //      buttonBox.getChildren().addAll(noButton, yesButton);
    //      buttonBox.setAlignment(Pos.CENTER);

    //      // Adding elements to the VBox
    //      contentVBox.getChildren().addAll(goalText, buttonBox);
    //      contentVBox.setAlignment(Pos.CENTER);

    //      // Center the VBox inside the rectangle
    //      StackPane.setAlignment(contentVBox, javafx.geometry.Pos.CENTER);
    // }
}
