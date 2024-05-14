package com.serenitask.ui;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import com.calendarfx.view.AgendaView;
import com.calendarfx.view.DetailedDayView;
import com.calendarfx.view.DetailedWeekView;


import com.serenitask.controller.GoalController;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;


public class RightPanelComponent {
    public static void actionsComponent(VBox rightPanelObjects, Text dailyText, Text weeklyText, Rectangle switchViewBox, StackPane switchViewButton, AtomicBoolean isWeeklyView, VBox actionsPanel, StackPane optimiseButton, Text optimiseText, Rectangle optimiseViewBox, StackPane addGoalButton, Text addGoalText, Rectangle addGoalViewBox
    , StackPane addEventButton, Text addEventText, Rectangle addEventViewBox){
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

         addEventViewBox.setWidth(170.0);
         addEventViewBox.setFill(Color.GREY);
         addEventViewBox.setArcWidth(10);
         addEventViewBox.setArcHeight(10);
         addEventText.setFont(Font.font(30));
         addEventText.setFill(Color.WHITE);
         addEventButton.setPadding(new Insets(0,20,0,20));

         switchViewButton.getChildren().addAll(switchViewBox, isWeeklyView.get() ? weeklyText : dailyText);
         optimiseButton.getChildren().addAll(optimiseViewBox, optimiseText);
         addGoalButton.getChildren().addAll(addGoalViewBox, addGoalText);
         addEventButton.getChildren().addAll(addEventViewBox, addEventText);

         rightPanelObjects.getChildren().addAll(switchViewButton, optimiseButton, addGoalButton, addEventButton);
    }

    public static void switchRightPanel(VBox rightPanelObjects,AtomicBoolean isActionsView, VBox rightPanel, AgendaView agenda, VBox dailyGoals){
        isActionsView.set(!isActionsView.get());
        
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

    public static void addGoalClick() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addGoalClick'");
    }

    public static void addOptimiseClick() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addOptimiseClick'");
    }

    public static void addEventClick() {
        Stage popOutStage = new Stage();
        popOutStage.initModality(Modality.APPLICATION_MODAL);
        popOutStage.setTitle("Add Event");

        Label titleLabel = new Label("Title:");
        TextField titleInput = new TextField();

        Label startDateLabel = new Label("Start Date:");
        DatePicker startDateInput = new DatePicker(LocalDate.now());

        Label endDateLabel = new Label("End Date:");
        DatePicker endDateInput = new DatePicker(LocalDate.now());

        CheckBox allDayCheckBox = new CheckBox("All Day Event");

        Label startTimeLabel = new Label("Start Time:");
        ComboBox<String> startTimeInput = createTimeComboBox();

        Label endTimeLabel = new Label("End Time:");
        ComboBox<String> endTimeInput = createTimeComboBox();

        Label calendarSelectLabel = new Label("Select Calendar");
        ComboBox<String> calendarSelectInput = new ComboBox<>();
        // PLACEHOLDER CALENDARS
        calendarSelectInput.getItems().addAll("Personal Events", "Health", "Goals");

        Button saveButton = new Button("Save");
        saveButton.setOnAction(e -> popOutStage.close());
        Button backButton = new Button("Back");
        backButton.setOnAction(e -> popOutStage.close());

        VBox layout = new VBox(10);
        layout.getChildren().addAll(titleLabel, titleInput, startDateLabel, startDateInput, endDateLabel,
                endDateInput, startTimeLabel, startTimeInput, endTimeLabel, endTimeInput, allDayCheckBox,
                calendarSelectLabel, calendarSelectInput, saveButton, backButton);
        layout.setAlignment(Pos.BASELINE_LEFT);

        Scene popOutScene = new Scene(layout, 300, 200);
        popOutStage.setScene(popOutScene);
        popOutStage.showAndWait();
    }
    private static ComboBox<String> createTimeComboBox() {
        List<String> timeOptions = new ArrayList<>();
        for (int hour = 0; hour < 24; hour++) {
            for (int minute = 0; minute < 60; minute += 15) {
                String time = String.format("%02d:%02d", hour, minute);
                timeOptions.add(time);
            }
        }

        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.getItems().addAll(timeOptions);
        return comboBox;
    }
}
