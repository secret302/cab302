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

import com.calendarfx.model.CalendarSource;
import com.calendarfx.view.AgendaView;
import com.calendarfx.view.DetailedDayView;
import com.calendarfx.view.DetailedWeekView;
import com.calendarfx.view.YearMonthView;

import fr.brouillard.oss.cssfx.CSSFX;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.LocalTime;

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
        Rectangle switchViewBox = new Rectangle(100, 1000);
        switchViewBox.setFill(Color.GREY);
        Text switchViewText = new Text(">");
        StackPane switchViewButton = new StackPane();
        switchViewButton.getChildren().addAll(switchViewBox, switchViewText);

        // Set date
        Text dateToday = new Text(LocalDate.now().toString());
        StackPane dateTodayPanel = new StackPane();
        dateTodayPanel.getChildren().addAll(dateToday);

        // set container for main calendar view
        VBox leftPanel = new VBox();
        leftPanel.getChildren().addAll(dateTodayPanel, calendarDayView);
        VBox.setVgrow(calendarDayView, Priority.ALWAYS);

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
        VBox.setVgrow(rightPanel, Priority.ALWAYS);
        rightPanel.setMaxWidth(800);

        // Load in goals from database
        GoalController goalController = new GoalController();
        for(String title : goalController.loadSimpleGoal())
        {
            dailygoals.getChildren().add(new javafx.scene.control.Label(title));
        }

        // Creates vertical box that can be clicked to change view
        HBox calendarDisplay = new HBox();
        calendarDisplay.getChildren().addAll(leftPanel, switchViewButton, rightPanel);
        calendarDisplay.setAlignment(Pos.CENTER_LEFT);

        // Prevents Calendar from being squished by other HBox Components
        HBox.setHgrow(leftPanel, Priority.ALWAYS);
        leftPanel.setMaxWidth(1020);


        // Switches betwenn Day view and Week View
        switchViewButton.setOnMouseClicked(event -> {
            if (leftPanel.getChildren().contains(calendarDayView)){
                leftPanel.getChildren().remove(calendarDayView);
                leftPanel.getChildren().add(1, calendarWeekView);
                VBox.setVgrow(calendarWeekView, Priority.ALWAYS);
                leftPanel.setMaxWidth(1420);
                rightPanel.setMaxWidth(400);
            }
            else {
                leftPanel.getChildren().remove(calendarWeekView);
                leftPanel.getChildren().add(1, calendarDayView);
                VBox.setVgrow(calendarDayView, Priority.ALWAYS);
                leftPanel.setMaxWidth(1020);
                rightPanel.setMaxWidth(800);
            }
        });


        Thread updateTimeThread = new Thread("Calendar: Update Time Thread") {
            @Override
            public void run() {
                while (true) {
                    Platform.runLater(() -> {
                        calendarDayView.setToday(LocalDate.now());
                        calendarDayView.setTime(LocalTime.now());
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

        Scene scene = new Scene(calendarDisplay);
        scene.focusOwnerProperty().addListener(it -> System.out.println("focus owner: " + scene.getFocusOwner()));
        CSSFX.start(scene);

        primaryStage.setTitle("SereniTask");
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
