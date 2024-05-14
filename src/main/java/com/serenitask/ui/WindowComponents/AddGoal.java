package com.serenitask.ui.WindowComponents;

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

/**
 * Provides a user interface component for adding a new goal.
 * This class is responsible for rendering a pop-up window where users can enter details about a new goal they wish to achieve.
 */

public class AddGoal {
    /**
     * Displays a modal window for adding a new goal, allowing the user to input goal details such as duration and frequency.
     * The window contains form inputs for the goal description, hours and minutes of effort, and the recurrence period.
     */
    public static void displayAddGoalView() {
        Stage popOutStage = new Stage();
        popOutStage.initModality(Modality.APPLICATION_MODAL);
        popOutStage.setTitle("Add Goal");

        Label wantLabel = new Label("I want to do:");
        TextField wantInput = new TextField();

        Label forLabel = new Label("For");
        TextField hourInput = new TextField();
        Label hourLabel = new Label("hours");
        Label andLabel = new Label("and");
        TextField minuteInput = new TextField();
        Label minuteLabel = new Label("minutes.");
        Label perLabel = new Label("Per");
        ComboBox<String> periodInput = new ComboBox<>();
        // PLACEHOLDER VALUES?
        periodInput.getItems().addAll("Day", "Week", "Month", "Year");

        Button saveButton = new Button("Save");
        saveButton.setOnAction(e -> popOutStage.close());
        Button backButton = new Button("Back");
        backButton.setOnAction(e -> popOutStage.close());

        VBox layout = new VBox(10);
        layout.getChildren().addAll(wantLabel, wantInput, forLabel, hourInput, hourLabel,
                andLabel, minuteInput, minuteLabel, perLabel, periodInput, backButton, saveButton);
        layout.setAlignment(Pos.BASELINE_LEFT);
        Scene popOutScene = new Scene(layout, 500, 500);
        popOutStage.setScene(popOutScene);
        popOutStage.showAndWait();
    }
}
