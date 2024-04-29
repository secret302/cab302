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
import java.time.format.DateTimeFormatter;
import java.util.Stack;
import java.util.concurrent.atomic.AtomicBoolean;

public class CalendarApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        DetailedDayView calendarDayView = new DetailedDayView();
        DetailedWeekView calendarWeekView = new DetailedWeekView();
        calendarDayView.setEnableTimeZoneSupport(true);
        calendarWeekView.setEnableTimeZoneSupport(true);
        // Users can categories events to seperate work from life
        // This helps create work-life balance, thus mental wellbeing
        Calendar personal = new Calendar("Personal Events");
        Calendar study = new Calendar("Study");
        Calendar work = new Calendar("Work");
        personal.setShortName("P");
        study.setShortName("S");
        work.setShortName("W");

        // Colours can be specified to meet colour blind needs
        personal.setStyle(Style.STYLE5);
        study.setStyle(Style.STYLE7);
        work.setStyle(Style.STYLE6);

        CalendarSource mainCalendarSource = new CalendarSource("Main");
        mainCalendarSource.getCalendars().addAll(personal, study, work);
        calendarDayView.getCalendarSources().setAll(mainCalendarSource);
        calendarDayView.setRequestedTime(LocalTime.now());
        calendarWeekView.getCalendarSources().setAll(mainCalendarSource);
        calendarWeekView.setRequestedTime(LocalTime.now());
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


        Text dateToday = new Text(LocalDate.now().format(DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy")));
        dateToday.setFont(Font.font(40));

        HBox dateTodayPanel = new HBox();
        Region spacer = new Region();
        dateTodayPanel.getChildren().addAll(dateToday, spacer, switchViewButton);
        HBox.setHgrow(dateToday, Priority.ALWAYS);
        StackPane.setAlignment(dateToday, Pos.CENTER_LEFT);
        spacer.setMinWidth(1000);
        StackPane.setAlignment(switchViewButton, Pos.CENTER_RIGHT);



        VBox leftPanel = new VBox();
        leftPanel.getChildren().addAll(dateTodayPanel, calendarDayView);
        leftPanel.setMinHeight(700);
    

        AgendaView agenda = new AgendaView();
        agenda.setEnableTimeZoneSupport(true);
        agenda.getCalendarSources().setAll(mainCalendarSource);
        agenda.setRequestedTime(LocalTime.now());
        agenda.lookAheadPeriodInDaysProperty().set(3);
        agenda.setPadding(new Insets(10));


        VBox dailygoals = new VBox();
        dailygoals.setSpacing(10);
        dailygoals.setPadding(new Insets(10));

        TextField goalTextField = new TextField();
        goalTextField.setPromptText("Enter your goal here");

        Button createGoalButton = new Button("Create Goal");
        createGoalButton.setOnAction(event -> {
            String goal = goalTextField.getText().trim();
            if (!goal.isEmpty()) {
                dailygoals.getChildren().add(new javafx.scene.control.Label(goal));
                goalTextField.clear();
            }
            // Integrate SQL goal INSERT INTO statement here
        });

        dailygoals.getChildren().addAll(new javafx.scene.control.Label("I want to"), goalTextField, createGoalButton);
        YearMonthView heatmap = new YearMonthView();
        heatmap.showUsageColorsProperty().set(true);
        VBox rightPanel = new VBox();
        rightPanel.getChildren().addAll(heatmap, agenda, dailygoals);
        rightPanel.setMinHeight(700);
        rightPanel.setMaxWidth(800);


        HBox calendarDisplay = new HBox();

        calendarDisplay.getChildren().addAll(leftPanel, rightPanel);
        calendarDisplay.setAlignment(Pos.CENTER);
        calendarDisplay.setMaxHeight(700);
        calendarDisplay.setPadding(new Insets(25,0,0,0));

        // Prevents Calendar from being squished by other HBox Components
        HBox.setHgrow(leftPanel, Priority.ALWAYS);
        leftPanel.setMaxWidth(1420);
        calendarDayView.setMinHeight(900);
        calendarDayView.setMaxHeight(900);
        calendarDayView.setPadding(new Insets(62,0,0,0));



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
