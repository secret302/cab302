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

import com.serenitask.model.Goal;
import com.serenitask.util.DatabaseManager.GoalDAO;
import fr.brouillard.oss.cssfx.CSSFX;
import impl.com.calendarfx.view.NavigateDateView;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.paint.Color;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.WeekFields;
import java.util.concurrent.atomic.AtomicBoolean;

import com.serenitask.controller.*;
import com.serenitask.ui.*;

/**
 * Main class for the Calendar Application. This class is responsible for setting up the main view of the application
 */
public class CalendarApp extends Application {

    /**
     * Main method for the Calendar Application. This method is responsible for setting up the main view of the application
     *
     * @param primaryStage The main stage for the application
     */
    @Override
    public void start(Stage primaryStage) {
        // Try to start the application
        try {
            // Create the main views for the application
            DetailedDayView calendarDayView = new DetailedDayView();
            DetailedWeekView calendarWeekView = new DetailedWeekView();
            calendarWeekView.setWeekFields(WeekFields.of(DayOfWeek.MONDAY, 1));
            // Load events from the database
            EventLoader eventLoader = new EventLoader();
            CalendarSource mainCalendarSource = eventLoader.loadEventsFromDatabase();
            // Update the calendar with the new events
            CalendarComponent.updateCalendar(calendarWeekView, calendarDayView, mainCalendarSource);

            // Create the main buttons for the application
            Text dateToday = new Text(LocalDate.now().toString());
            Text dailyText = new Text("Daily");
            Text weeklyText = new Text("Weekly");
            StackPane switchViewButton = new StackPane();
            Rectangle switchViewBox = new Rectangle(120, 50);
            // Add Optimise button
            Text optimiseText = new Text("Optimise");
            StackPane optimiseButton = new StackPane();
            Rectangle optimiseViewBox = new Rectangle(120, 50);
            optimiseButton.setOnMouseClicked(event -> {
                RightPanelComponent.addOptimiseClick(mainCalendarSource);
            });
            // Add Goal Button
            Text addGoalText = new Text("Add Goal");
            StackPane addGoalButton = new StackPane();
            Rectangle addGoalViewBox = new Rectangle(120, 50);
            addGoalButton.setOnMouseClicked(event -> {
                RightPanelComponent.addGoalClick();
            });
            // Add Event Button
            Text addEventText = new Text("Add Event");
            StackPane addEventButton = new StackPane();
            Rectangle addEventViewBox = new Rectangle(120, 50);
            addEventButton.setOnMouseClicked(event -> {
                RightPanelComponent.addEventClick(mainCalendarSource);
            });

            AtomicBoolean isWeeklyView = new AtomicBoolean(false);
            HBox dateTodayPanel = new HBox();
            VBox actionsPanel = new VBox();
            NavigationBar navigationBar = new NavigationBar();
            NavigateDateView navigateDateView = navigationBar.createButton(calendarDayView, calendarWeekView);
            CalendarViewComponent.calendarView(dateTodayPanel, navigateDateView);

            VBox leftPanel = new VBox();
            leftPanel.getChildren().addAll(dateTodayPanel, calendarDayView);
            leftPanel.setMinHeight(700);

            VBox dailygoals = new VBox();
            YearMonthView heatmap = new YearMonthView();
            heatmap.getCalendarSources().setAll(mainCalendarSource);
            heatmap.setRequestedTime(LocalTime.now());
            heatmap.setShowUsageColors(true);
            heatmap.setShowTodayButton(false);

            AgendaView agenda = new AgendaView();
            agenda.setEnableTimeZoneSupport(true);
            agenda.getCalendarSources().setAll(mainCalendarSource);
            agenda.setRequestedTime(LocalTime.now());
            agenda.lookAheadPeriodInDaysProperty().set(0);
            agenda.setPadding(new Insets(10));

            VBox rightPanel = new VBox();
            VBox rightPanelObjects = new VBox();
            rightPanelObjects.setPadding(new Insets(20, 20, 20, 19));

            StackPane panelSwitchButton = new StackPane();
            Rectangle panelSwitchViewBox = new Rectangle(212, 46);
            panelSwitchViewBox.setFill(Color.WHITE);
            panelSwitchViewBox.setArcWidth(10);
            panelSwitchViewBox.setArcHeight(10);

            Rectangle leftButtonPanelSwitchViewBox = new Rectangle(100, 40);
            Rectangle rightButtonPanelSwitchViewBox = new Rectangle(100, 40);
            leftButtonPanelSwitchViewBox.setFill(Color.web("#e84d3e"));
            rightButtonPanelSwitchViewBox.setFill(Color.web("#b2b3b7"));
            leftButtonPanelSwitchViewBox.setArcWidth(10);
            leftButtonPanelSwitchViewBox.setArcHeight(10);
            rightButtonPanelSwitchViewBox.setArcWidth(10);
            rightButtonPanelSwitchViewBox.setArcHeight(10);
            Text leftButtonPanelText = new Text("Actions");
            Text rightButtonPanelText = new Text("Goals");
            leftButtonPanelText.setFont(Font.font(20));
            rightButtonPanelText.setFont(Font.font(20));
            leftButtonPanelText.setFill(Color.WHITE);
            rightButtonPanelText.setFill(Color.BLACK);

            StackPane leftButton = new StackPane();
            leftButton.getChildren().addAll(leftButtonPanelSwitchViewBox, leftButtonPanelText);
            leftButton.setAlignment(Pos.CENTER);

            StackPane rightButton = new StackPane();
            rightButton.getChildren().addAll(rightButtonPanelSwitchViewBox, rightButtonPanelText);
            rightButton.setAlignment(Pos.CENTER);
            HBox switches = new HBox(6, leftButton, rightButton);
            switches.setAlignment(Pos.CENTER);

            panelSwitchButton.getChildren().addAll(panelSwitchViewBox, switches);
            HBox rightPanelButton = new HBox(panelSwitchButton);
            rightPanelButton.setPadding(new Insets(40, 0, 40, 0));
            rightPanelButton.setAlignment(Pos.CENTER);

            AtomicBoolean isActionsView = new AtomicBoolean(false);
            RightPanelComponent.actionsComponent(rightPanelObjects,
                    dailyText,
                    weeklyText,
                    switchViewBox,
                    switchViewButton,
                    isWeeklyView,
                    actionsPanel,
                    optimiseButton,
                    optimiseText,
                    optimiseViewBox,
                    addGoalButton,
                    addGoalText,
                    addGoalViewBox,
                    addEventButton,
                    addEventText,
                    addEventViewBox);

            rightPanel.getChildren().addAll(heatmap, rightPanelButton, rightPanelObjects, agenda);
            rightPanel.setAlignment(Pos.TOP_CENTER);
            rightPanel.setMinHeight(700);
            rightPanel.setMinWidth(260);

            leftButton.setOnMouseClicked(event -> {
                RightPanelComponent.switchLeft(rightPanelObjects, isActionsView, rightPanel, agenda, dailygoals, addGoalButton, leftButtonPanelSwitchViewBox, rightButtonPanelSwitchViewBox, leftButtonPanelText, rightButtonPanelText);
            });

            rightButton.setOnMouseClicked(event -> {
                RightPanelComponent.switchRight(rightPanelObjects, isActionsView, rightPanel, agenda, dailygoals, addGoalButton, leftButtonPanelSwitchViewBox, rightButtonPanelSwitchViewBox, leftButtonPanelText, rightButtonPanelText);
            });

            // Load the daily goals
            GoalController goalController = new GoalController();

            // Switch between Day view and Week View
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
            calendarDayView.setPadding(new Insets(62, 0, 0, 0));

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

            calendar.getChildren().addAll(calendarDisplay);

            // Update Clock
            EndOfDayComponent.checkTime(calendar, calendarDayView, shadowPanel, taskPopup, goalText, goalController);

            // Create the main scene for the application
            Scene scene = new Scene(calendar);
            scene.focusOwnerProperty().addListener(it -> System.out.println("focus owner: " + scene.getFocusOwner()));
            CSSFX.start(scene);

            // Calls the ShortcutController to allow Shortcuts to be used
            ShortcutController.setupShortcuts(scene, mainCalendarSource, isWeeklyView, dailyText, weeklyText, switchViewButton, leftPanel, calendarDayView, calendarWeekView);

            // Set the application parameters
            primaryStage.setTitle("SereniTask");
            primaryStage.setResizable(false);
            primaryStage.setScene(scene);
            primaryStage.setWidth(1700);
            primaryStage.setHeight(1000);
            primaryStage.centerOnScreen();
            primaryStage.show();
            primaryStage.setOnCloseRequest(event -> {
                // primaryStage.close();
                // // Consume the event to prevent the application from closing
                // if (goalController.loadSimpleGoal().toString() == "[]") {
                //     primaryStage.close();
                // } else {
                //     event.consume();

                //     Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Did you complete all your goals today?\nGoals: " + goalController.loadSimpleGoal().toString());
                //     ButtonType buttonYes = new ButtonType("Yes");
                //     ButtonType buttonNo = new ButtonType("No");
                //     ButtonType buttonCancel = new ButtonType("Cancel");
                //     alert.getButtonTypes().setAll(buttonYes, buttonNo, buttonCancel);
                //     alert.showAndWait().ifPresent(response -> {
                //         if (response == buttonYes) {
                //             GoalDAO goalDAO = new GoalDAO();
                //             for (Goal goal : goalDAO.getAllGoals()) {
                //                 goalController.deleteGoal(goal.getId());
                //             }
                //             primaryStage.close();
                //         } else if (response == buttonNo) {
                //             primaryStage.close();
                //         }
                //     });
                // }
            });
        } catch (Exception e) {
            // If an error occurs, print the error message
            System.err.println("An error has occurred while starting the application: " + e.getMessage());
            e.printStackTrace();
        }
    }
    

    /**
     * Main method for the Calendar Application. This method is responsible for starting the application
     *
     * @param args The arguments for the application
     */
    public static void main(String[] args) {
        launch(args);
    }
}
