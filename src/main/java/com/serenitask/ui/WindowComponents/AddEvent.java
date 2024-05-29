package com.serenitask.ui.WindowComponents;

import com.calendarfx.model.CalendarSource;
import com.calendarfx.model.Entry;
import com.calendarfx.model.Calendar;
import com.calendarfx.model.Interval;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import com.serenitask.model.Event;


/**
 * Provides a user interface for adding new events to a calendar.
 * This class is responsible for rendering a pop-up window where users can enter details about a new event
 * and save it to a selected calendar within the application.
 */
public class AddEvent {
    /**
     * Displays a modal window for adding a new event to the specified calendar source.
     * Users can input event details such as title, start and end times, and select which calendar to add the event to.
     *
     * @param calendarSource The source containing multiple calendars to which the event can be added.
     */
    public static void displayAddEventView(CalendarSource calendarSource) {
        Stage popOutStage = new Stage();
        popOutStage.initModality(Modality.APPLICATION_MODAL);
        popOutStage.setTitle("Add Event");

        Label titleLabel = new Label("Title");
        titleLabel.setFont(Font.font(30));
        TextField titleInput = new TextField();
        titleInput.setPromptText("Your title here...");
        titleInput.setMaxWidth(250);

        Label startDateLabel = new Label("Start Date");
        DatePicker startDateInput = new DatePicker(LocalDate.now());
        startDateLabel.setFont(Font.font(30));

        Label endDateLabel = new Label("End Date");
        DatePicker endDateInput = new DatePicker(LocalDate.now());
        endDateLabel.setFont(Font.font(30));

        CheckBox allDayCheckBox = new CheckBox("All Day Event");

        Label startTimeLabel = new Label("Start Time");
        ComboBox<String> startTimeInput = createTimeComboBox();
        startTimeLabel.setFont(Font.font(30));
        startDateInput.setMaxWidth(150);

        Label endTimeLabel = new Label("End Time");
        ComboBox<String> endTimeInput = createTimeComboBox();
        endTimeLabel.setFont(Font.font(30));
        endDateInput.setMaxWidth(150);

        Label calendarSelectLabel = new Label("Select Calendar");
        calendarSelectLabel.setFont(Font.font(30));
        ComboBox<String> calendarSelectInput = new ComboBox<>();
        calendarSelectInput.setMaxWidth(150);
        // PLACEHOLDER CALENDARS
        calendarSelectInput.getItems().addAll("Personal Events", "Health", "Goals");

        Button saveButton = new Button("Save");
        saveButton.setStyle("-fx-background-color: lightgreen; -fx-font-size: 20");
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

            Entry<?> entry = new Entry<>(event.getTitle());
            entry.setInterval(new Interval(startDate, startTime, endDate, endTime));
            entry.setFullDay(fullDay);

            selectedCalendar.addEntry(entry);

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
        VBox endDateVBox = new VBox(15);
        endDateVBox.getChildren().addAll(endDateLabel, endDateInput);
        endDateVBox.setAlignment(Pos.CENTER);

        VBox startTimeVBox = new VBox(15);
        startTimeVBox.getChildren().addAll(startTimeLabel, startTimeInput);
        startTimeVBox.setAlignment(Pos.CENTER);
        VBox endTimeVBox = new VBox(15);
        endTimeVBox.getChildren().addAll(endTimeLabel, endTimeInput);
        endTimeVBox.setAlignment(Pos.CENTER);

        HBox dateHBox = new HBox(20);
        dateHBox.getChildren().addAll(startDateVBox, endDateVBox);
        dateHBox.setAlignment(Pos.CENTER);

        HBox timeHBox = new HBox(20);
        timeHBox.getChildren().addAll(startTimeVBox, endTimeVBox);
        timeHBox.setAlignment(Pos.CENTER);

        VBox layout = new VBox(20);
        layout.getChildren().addAll(titleLabel, titleInput, dateHBox, timeHBox, allDayCheckBox,
                calendarSelectLabel, calendarSelectInput, buttonBox);
        layout.setAlignment(Pos.CENTER);

        Scene popOutScene = new Scene(layout, 500, 550);
        popOutStage.setScene(popOutScene);
        popOutStage.showAndWait();
    }

    /**
     * Creates a ComboBox containing time options in 15-minute increments.
     * This method generates a list of times from 00:00 to 23:45 for user selection.
     *
     * @return A ComboBox filled with time options.
     */
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
