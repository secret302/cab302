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
 
 import com.serenitask.controller.GoalController;
 import fr.brouillard.oss.cssfx.CSSFX;
 import javafx.application.Application;
 import javafx.application.Platform;
 import javafx.geometry.Insets;
 import javafx.geometry.Pos;
 import javafx.scene.Scene;
 import javafx.scene.control.Button;
 import javafx.scene.control.TextField;
 import javafx.scene.layout.*;
 import javafx.scene.paint.Color;
 import javafx.scene.shape.Rectangle;
 import javafx.scene.text.Font;
 import javafx.scene.text.FontWeight;
 import javafx.scene.text.Text;
 import javafx.stage.Stage;
 
 import java.time.LocalDate;
 import java.time.LocalTime;
 import java.util.concurrent.atomic.AtomicBoolean;
 
 import com.serenitask.controller.*;
 
 
 public class CalendarApp extends Application {
 
     @Override
     public void start(Stage primaryStage) {
 
        
         // Create the base views for the calendar and set timeZone
         DetailedDayView calendarDayView = new DetailedDayView();
         DetailedWeekView calendarWeekView = new DetailedWeekView();
         calendarDayView.setEnableTimeZoneSupport(true);
         calendarWeekView.setEnableTimeZoneSupport(true);

         // Call to Database and check entries;
         // Run this block if there are no entries returned;
         EventLoader eventLoader = new EventLoader();
         CalendarSource mainCalendarSource = eventLoader.loadEventsFromDatabase();
 
         // populate Day and Week views based on CalendarSource
         calendarDayView.getCalendarSources().setAll(mainCalendarSource);
         calendarDayView.setRequestedTime(LocalTime.now());
         calendarWeekView.getCalendarSources().setAll(mainCalendarSource);
         calendarWeekView.setRequestedTime(LocalTime.now());

         // Set appearence parameters and set style
         calendarWeekView.setMaxWidth(1600);

         
         StackPane switchViewButton = new StackPane();
         Rectangle switchViewBox = new Rectangle(120, 50);
         switchViewBox.setFill(Color.GREY);
         switchViewBox.setArcWidth(10);
         switchViewBox.setArcHeight(10);
         Text dailyText = new Text("Daily");
         Text weeklyText = new Text("Weekly");
         dailyText.setFont(Font.font(30));
         weeklyText.setFont(Font.font(30));
         dailyText.setFill(Color.WHITE);
         weeklyText.setFill(Color.WHITE);
         AtomicBoolean isWeeklyView = new AtomicBoolean(false); // Initial state is Daily View
         switchViewButton.getChildren().addAll(switchViewBox, isWeeklyView.get() ? weeklyText : dailyText);
 
         // Set date
         Text dateToday = new Text(LocalDate.now().toString());
         dateToday.setFont(Font.font(40));

         // Setup Right container
         HBox dateTodayPanel = new HBox();
         Region spacer = new Region();
         dateTodayPanel.getChildren().addAll(dateToday, spacer, switchViewButton);
         HBox.setHgrow(dateToday, Priority.ALWAYS);
         StackPane.setAlignment(dateToday, Pos.CENTER_LEFT);
         spacer.setMinWidth(1000);
         StackPane.setAlignment(switchViewButton, Pos.CENTER_RIGHT);
 
         // set container for main calendar view
         VBox leftPanel = new VBox();
         leftPanel.getChildren().addAll(dateTodayPanel, calendarDayView);
         leftPanel.setMinHeight(700);
 
         // Setup agenda view and style
         AgendaView agenda = new AgendaView();
         agenda.setEnableTimeZoneSupport(true);
         agenda.getCalendarSources().setAll(mainCalendarSource);
         agenda.setRequestedTime(LocalTime.now());
         agenda.lookAheadPeriodInDaysProperty().set(3);
         agenda.setPadding(new Insets(10));
 
         // Set container for goals view
         VBox dailygoals = new VBox();
         dailygoals.setSpacing(10);
         dailygoals.setPadding(new Insets(10));
         TextField goalTextField = new TextField();
         goalTextField.setPromptText("Enter your goal here");
 
         // Create button for goals container
         Button createGoalButton = new Button("Create Goal");
         createGoalButton.setOnAction(event -> {
             String goal = goalTextField.getText().trim();
             if (!goal.isEmpty()) {
                 dailygoals.getChildren().add(new javafx.scene.control.Label(goal));
                 goalTextField.clear();
             }
             // Commit goal to database
             GoalController goalController = new GoalController();
             goalController.controlSimpleGoal(goal);
         });
 
         // Setup right hand side Vbox elements
         dailygoals.getChildren().addAll(new javafx.scene.control.Label("I want to"), goalTextField, createGoalButton);
         YearMonthView heatmap = new YearMonthView();
         heatmap.showUsageColorsProperty().set(true);
         VBox rightPanel = new VBox();
         rightPanel.getChildren().addAll(heatmap, agenda, dailygoals);
         rightPanel.setMinHeight(700);
         rightPanel.setMaxWidth(800);
 
         // Load in goals from database
         GoalController goalController = new GoalController();
         for(String title : goalController.loadSimpleGoal())
         {
             dailygoals.getChildren().add(new javafx.scene.control.Label(title));
         }
 
         // Creates vertical box that can be clicked to change view
         HBox calendarDisplay = new HBox();

         calendarDisplay.getChildren().addAll(leftPanel, rightPanel);
         //calendarDisplay.setAlignment(Pos.CENTER);
         //calendarDisplay.setMaxHeight(700);
         //calendarDisplay.setPadding(new Insets(25,0,0,0));
 
         // Prevents Calendar from being squished by other HBox Components
         HBox.setHgrow(leftPanel, Priority.ALWAYS);
         leftPanel.setMaxWidth(1420);
         calendarDayView.setMinHeight(900);
         calendarDayView.setMaxHeight(900);
         calendarDayView.setPadding(new Insets(62,0,0,0));


         StackPane calendarDisplay2 = new StackPane();

         Rectangle shadowPanel = new Rectangle();
         shadowPanel.setWidth(1920);
         shadowPanel.setHeight(1080);
         shadowPanel.setOpacity(0.8);

         Rectangle taskPopupPanel = new Rectangle();
         taskPopupPanel.setWidth(400);
         taskPopupPanel.setHeight(250);
         taskPopupPanel.setArcWidth(20);
         taskPopupPanel.setArcHeight(20);
         taskPopupPanel.setFill(Color.WHITE);

         // Create VBox for content
         VBox contentVBox = new VBox();
         contentVBox.setPadding(new Insets(20));
         contentVBox.setSpacing(20);

         // Text for the goal completion question
         Text goalText = new Text("Have you completed " +
                 (goalController.returnFirstGoal() != null ? goalController.returnFirstGoal().getTitle() : "no goal") +
                 " goal?");
         goalText.setFont(Font.font("Arial", FontWeight.BOLD, 18));

         // Switches betwenn Day view and Week View
         switchViewButton.setOnMouseClicked(event -> {
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
         });

         HBox buttonBox = new HBox();
         buttonBox.setSpacing(20);
         buttonBox.setPadding(new Insets(100, 20, 20, 20)); // Padding for buttons

         // Adding the VBox to the rectangle
         StackPane taskPopup = new StackPane();
         taskPopup.getChildren().addAll(taskPopupPanel, contentVBox);



         // Buttons
         Button noButton = new Button("No");
         noButton.setPrefWidth(80);
         noButton.setOnAction(e ->
         {
             goalController.deleteGoal(goalController.returnFirstGoal().getId());
             dailygoals.getChildren().remove(dailygoals.getChildren().get(3));
             calendarDisplay2.getChildren().removeAll(shadowPanel, taskPopup);
         });

         Button yesButton = new Button("Yes");
         yesButton.setPrefWidth(80);
         yesButton.setOnAction(e ->
         {
             goalController.deleteGoal(goalController.returnFirstGoal().getId());
             dailygoals.getChildren().remove(dailygoals.getChildren().get(3));
             calendarDisplay2.getChildren().removeAll(shadowPanel, taskPopup);
         });

         // Adding buttons to the HBox
         buttonBox.getChildren().addAll(noButton, yesButton);

         buttonBox.setAlignment(Pos.CENTER);


         // Adding elements to the VBox
         contentVBox.getChildren().addAll(goalText, buttonBox);

         contentVBox.setAlignment(Pos.CENTER);

         // Center the VBox inside the rectangle
         StackPane.setAlignment(contentVBox, javafx.geometry.Pos.CENTER);



         calendarDisplay2.getChildren().addAll(calendarDisplay);

 

 

         // Update Clock
         Thread updateTimeThread = new Thread("Calendar: Update Time Thread") {
             @Override
             public void run() {
                 while (true) {
                     Platform.runLater(() -> {
                         calendarDayView.setToday(LocalDate.now());
                         calendarDayView.setTime(LocalTime.now());
                         LocalTime startTime = LocalTime.of(16, 0);
                         LocalTime endTime = LocalTime.of(23, 59, 59);

                         if (LocalTime.now().isAfter(startTime) && LocalTime.now().isBefore(endTime)) {
                             if (!goalController.checkIfEmpty())
                             {
                                 if (!calendarDisplay2.getChildren().contains(shadowPanel))
                                 {
                                     goalController.loadSimpleGoal();
                                     calendarDisplay2.getChildren().addAll(shadowPanel, taskPopup);
                                 }
                             }
                         }

                         else {
                             calendarDisplay2.getChildren().removeAll(shadowPanel, taskPopup);
                         }
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
            
         Scene scene = new Scene(calendarDisplay2);
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
 