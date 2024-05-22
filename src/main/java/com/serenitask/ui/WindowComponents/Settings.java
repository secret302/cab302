package com.serenitask.ui.WindowComponents;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import com.calendarfx.model.Calendar;
import com.calendarfx.model.Entry;
import com.calendarfx.model.Interval;
import com.serenitask.model.Event;
import com.serenitask.model.Goal;
import com.serenitask.util.DatabaseManager.GoalDAO;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;


/**
 * Provides a user interface component for settings.
 * This class is responsible for rendering a pop-up window where users can enter settings.
 */

public class Settings {
    /**
     * Displays a modal window for settings, allowing the user to input settings.
     * The window contains form inputs for the day start, end, allocate ahead, select date, and select calendar.
     */
    public static void displaySettingsView() {
        Stage popOutStage = new Stage();
        popOutStage.initModality(Modality.APPLICATION_MODAL);
        popOutStage.setTitle("Settings");



        Label startTimeLabel = new Label("Day Start:");
        ComboBox<String> startTimeInput = createTimeComboBox();
        startTimeLabel.setFont(Font.font(30));

        Label endTimeLabel = new Label("Day End:");
        ComboBox<String> endTimeInput = createTimeComboBox();
        endTimeLabel.setFont(Font.font(30));

        Label startDateLabel = new Label("Allocate Ahead:");
        DatePicker startDateInput = new DatePicker(LocalDate.now());
        startDateInput.setMaxWidth(110);
        startDateLabel.setFont(Font.font(30));

        Label calendarSelectLabel = new Label("Select Calendar");
        calendarSelectLabel.setFont(Font.font(30));
        ComboBox<String> calendarSelectInput = new ComboBox<>();
        calendarSelectInput.setMaxWidth(150);
        // PLACEHOLDER CALENDARS
        calendarSelectInput.getItems().addAll("Personal Events", "Health", "Goals");

        Button saveButton = new Button("Save");
        saveButton.setStyle("-fx-background-color: lightgreen; -fx-font-size: 20");
        saveButton.setOnAction(e -> {

            // insert things to save

            popOutStage.close();
        });

        Button backButton = new Button("Back");
        backButton.setStyle("-fx-background-color: #ff5f56; -fx-font-size: 20");
        backButton.setOnAction(e -> popOutStage.close());

        HBox buttonBox = new HBox(10);
        buttonBox.getChildren().addAll(backButton, saveButton);
        buttonBox.setAlignment(Pos.CENTER);

        VBox startDateVBox = new VBox(15);
        startDateVBox.getChildren().addAll(startDateLabel, startDateInput);
        startDateVBox.setAlignment(Pos.CENTER);

        VBox startTimeVBox = new VBox(15);
        startTimeVBox.getChildren().addAll(startTimeLabel, startTimeInput);
        startTimeVBox.setAlignment(Pos.CENTER);
        VBox endTimeVBox = new VBox(15);
        endTimeVBox.getChildren().addAll(endTimeLabel, endTimeInput);
        endTimeVBox.setAlignment(Pos.CENTER);

        HBox dateHBox = new HBox(20);
        dateHBox.getChildren().addAll(startDateVBox);
        dateHBox.setAlignment(Pos.CENTER);
    
        VBox timeVBox = new VBox(20);
        timeVBox.getChildren().addAll(startTimeVBox, endTimeVBox);
        timeVBox.setAlignment(Pos.CENTER);

        VBox layout = new VBox(20);
        layout.getChildren().addAll(dateHBox, timeVBox,
                calendarSelectLabel, calendarSelectInput, buttonBox);
        layout.setAlignment(Pos.CENTER);

        Scene popOutScene = new Scene(layout, 500, 550);
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
