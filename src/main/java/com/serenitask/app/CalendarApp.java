/*
 *  Copyright (C) 2017 Dirk Lemmermann Software & Consulting (dlsc.com)
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

 package com.serenitask.app;

 import com.calendarfx.model.Calendar;
 import com.calendarfx.model.Calendar.Style;
 import com.calendarfx.model.CalendarSource;
 import com.calendarfx.view.AgendaView;
 import com.calendarfx.view.DetailedDayView;
 import com.calendarfx.view.DetailedWeekView;
 import com.calendarfx.view.YearMonthView;

import fr.brouillard.oss.cssfx.CSSFX;
 import javafx.application.Application;
 import javafx.geometry.Insets;
 import javafx.scene.Scene;
 import javafx.scene.control.Button;
 import javafx.scene.control.TextField;
 import javafx.scene.layout.*;
 import javafx.scene.shape.Rectangle;
 import javafx.scene.text.Font;
 import javafx.scene.text.FontWeight;
 import javafx.scene.text.Text;
 import javafx.stage.Stage;
 
 import java.time.LocalDate;
 import java.time.LocalTime;
 import java.util.concurrent.atomic.AtomicBoolean;
 
 import com.serenitask.controller.*;
 import com.serenitask.ui.*;
 
 
 public class CalendarApp extends Application {
 
     @Override
     public void start(Stage primaryStage) {
 
         DetailedDayView calendarDayView = new DetailedDayView();
         DetailedWeekView calendarWeekView = new DetailedWeekView();
         EventLoader eventLoader = new EventLoader();
         CalendarSource mainCalendarSource = eventLoader.loadEventsFromDatabase();
         CalendarComponent.updateCalendar(calendarWeekView, calendarDayView, mainCalendarSource);


         Text dateToday = new Text(LocalDate.now().toString());
         Text dailyText = new Text("Daily");
         Text weeklyText = new Text("Weekly");
         StackPane switchViewButton = new StackPane();
         Rectangle switchViewBox = new Rectangle(120, 50);

         Text optimiseText = new Text("Optimise");
         StackPane optimiseButton = new StackPane();
         Rectangle optimiseViewBox = new Rectangle(120, 50);

         Text addGoalText = new Text("Add Goal");
         StackPane addGoalButton = new StackPane();
         Rectangle addGoalViewBox = new Rectangle(120, 50);

         AtomicBoolean isWeeklyView = new AtomicBoolean(false);
         HBox dateTodayPanel = new HBox();
         Region spacer = new Region();
         CalendarViewComponent.calendarView(dailyText, weeklyText, dateToday, switchViewBox, switchViewButton, isWeeklyView, dateTodayPanel, spacer, optimiseButton, optimiseText, optimiseViewBox, addGoalButton, addGoalText, addGoalViewBox);
         

         VBox leftPanel = new VBox();
         leftPanel.getChildren().addAll(dateTodayPanel, calendarDayView);
         leftPanel.setMinHeight(700);
 

         VBox dailygoals = new VBox();
         TextField goalTextField = new TextField();
         Button createGoalButton = new Button("Create Goal");
         DailyGoalsComponent.goalView(dailygoals, goalTextField, createGoalButton);

         YearMonthView heatmap = new YearMonthView();
         heatmap.showUsageColorsProperty().set(true);

         AgendaView agenda = new AgendaView();
         agenda.setEnableTimeZoneSupport(true);
         agenda.getCalendarSources().setAll(mainCalendarSource);
         agenda.setRequestedTime(LocalTime.now());
         agenda.lookAheadPeriodInDaysProperty().set(3);
         agenda.setPadding(new Insets(10));

         VBox rightPanel = new VBox();
         rightPanel.getChildren().addAll(heatmap, agenda, dailygoals);
         rightPanel.setMinHeight(700);
         rightPanel.setMaxWidth(800);


         GoalController goalController = new GoalController();
         for(String title : goalController.loadSimpleGoal())
         {
             dailygoals.getChildren().add(new javafx.scene.control.Label(title));
         }
 
          // Switches betwenn Day view and Week View
          switchViewButton.setOnMouseClicked(event -> {
            CalendarViewComponent.switchView(isWeeklyView, dailyText, weeklyText, switchViewButton, leftPanel, calendarDayView, calendarWeekView);
        });

         // Creates vertical box that can be clicked to change view
         HBox calendarDisplay = new HBox();
         calendarDisplay.getChildren().addAll(leftPanel, rightPanel);
 
         // Prevents Calendar from being squished by other HBox Components
         HBox.setHgrow(leftPanel, Priority.ALWAYS);
         leftPanel.setMaxWidth(1420);
         calendarDayView.setMinHeight(900);
         calendarDayView.setMaxHeight(900);
         calendarDayView.setPadding(new Insets(62,0,0,0));


         // Text for the goal completion question
         Text goalText = new Text("Have you completed " +
                 (goalController.returnFirstGoal() != null ? goalController.returnFirstGoal().getTitle() : "no goal") +
                 " goal?");
         goalText.setFont(Font.font("Arial", FontWeight.BOLD, 18));

         StackPane calendar = new StackPane();
         Rectangle shadowPanel = new Rectangle();
         Rectangle taskPopupPanel = new Rectangle();
         VBox contentVBox = new VBox();
         HBox buttonBox = new HBox();
         StackPane taskPopup = new StackPane();
         Button noButton = new Button("No");
         Button yesButton = new Button("Yes");

         EndOfDayComponent.EndOfDayPopup(goalText, calendar, goalController, dailygoals, shadowPanel, taskPopupPanel, contentVBox, buttonBox, taskPopup, noButton, yesButton);

         calendar.getChildren().addAll(calendarDisplay);

         // Update Clock
         EndOfDayComponent.checkTime(calendar, calendarDayView, shadowPanel, taskPopup, goalText, goalController);
            
         Scene scene = new Scene(calendar);
         scene.focusOwnerProperty().addListener(it -> System.out.println("focus owner: " + scene.getFocusOwner()));
         CSSFX.start(scene);

         primaryStage.setTitle("SereniTask");
         primaryStage.setResizable(false);
         primaryStage.setScene(scene);
         primaryStage.setWidth(1920);
         primaryStage.setHeight(1080);
         primaryStage.centerOnScreen();
         primaryStage.show();
         primaryStage.setMaximized(true);
     }
 
     public static void main(String[] args) {
         launch(args);
     }
 }
 