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

package com.calendarfx.app;

import com.calendarfx.model.Calendar;
import com.calendarfx.model.Calendar.Style;
import com.calendarfx.model.CalendarSource;
import com.calendarfx.view.DetailedDayView;

import fr.brouillard.oss.cssfx.CSSFX;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.LocalTime;

public class CalendarApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        DetailedDayView calendarView = new DetailedDayView();
        
        calendarView.setEnableTimeZoneSupport(true);

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
        calendarView.getCalendarSources().setAll(mainCalendarSource);
        calendarView.setRequestedTime(LocalTime.now());
        calendarView.setMaxWidth(600);

        Rectangle switchViewBox = new Rectangle(100, 1000);
        switchViewBox.setFill(Color.GREY);
        Text switchViewText = new Text(">");
        StackPane switchViewButton = new StackPane();
        switchViewButton.getChildren().addAll(switchViewBox, switchViewText);

        HBox stackPane = new HBox();
        stackPane.getChildren().addAll(calendarView, switchViewButton);
        stackPane.setAlignment(Pos.CENTER);

        // Prevents Calendar from being squished by other HBox Components
        HBox.setHgrow(calendarView, Priority.ALWAYS);

        Thread updateTimeThread = new Thread("Calendar: Update Time Thread") {
            @Override
            public void run() {
                while (true) {
                    Platform.runLater(() -> {
                        calendarView.setToday(LocalDate.now());
                        calendarView.setTime(LocalTime.now());
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

        Scene scene = new Scene(stackPane);
        scene.focusOwnerProperty().addListener(it -> System.out.println("focus owner: " + scene.getFocusOwner()));
        CSSFX.start(scene);

        primaryStage.setTitle("SereniTask");
        primaryStage.setScene(scene);
        primaryStage.setWidth(1300);
        primaryStage.setHeight(1000);
        primaryStage.centerOnScreen();
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
