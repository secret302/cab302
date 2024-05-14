package com.serenitask.ui.WindowComponents;

import com.calendarfx.model.CalendarSource;
import com.calendarfx.model.Entry;
import com.calendarfx.model.Calendar;
import com.calendarfx.model.Interval;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import com.serenitask.model.Event;


import com.serenitask.util.DatabaseManager.EventDAO;

public class AddEvent {
    public static void displayAddEventView(CalendarSource calendarSource) {
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
        saveButton.setOnAction(e -> {
            String title = titleInput.getText();
            LocalDate startDate = startDateInput.getValue();
            LocalDate endDate = endDateInput.getValue();
            LocalTime startTime = LocalTime.parse(startTimeInput.getValue());
            LocalTime endTime = LocalTime.parse(endTimeInput.getValue());
            boolean fullDay = allDayCheckBox.isSelected();
            String calendarName = calendarSelectInput.getValue();
            Interval interval = new Interval(startDate, startTime, endDate, endTime);

            Calendar selectedCalendar = null;

            for (Calendar calendar : calendarSource.getCalendars()) {
                if (calendar.getName().equals(calendarName)) {
                    selectedCalendar = calendar;
                    break;
                }
            }

            if (selectedCalendar == null) {
                System.err.println("Selected calendar not found!");
                return;
            }

            Event event = new Event();
            event.setTitle(title);
            event.setInterval(interval);
            event.setFullDay(fullDay);
            event.setStaticPos(true);
            event.setCalendar(calendarName);

            // REFACTOR OUT
            event.setAllocatedUntil(endDate);

            Entry<?> entry = new Entry<>(event.getTitle());
            entry.setInterval(new Interval(startDate, startTime, endDate, endTime));
            entry.setFullDay(fullDay);

            selectedCalendar.addEntry(entry);

            popOutStage.close();
        });

        Button backButton = new Button("Back");
        backButton.setOnAction(e -> popOutStage.close());

        VBox layout = new VBox(10);
        layout.getChildren().addAll(titleLabel, titleInput, startDateLabel, startDateInput, endDateLabel,
                endDateInput, startTimeLabel, startTimeInput, endTimeLabel, endTimeInput, allDayCheckBox,
                calendarSelectLabel, calendarSelectInput, saveButton, backButton);
        layout.setAlignment(Pos.BASELINE_LEFT);

        Scene popOutScene = new Scene(layout, 500, 500);
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
