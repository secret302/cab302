package com.serenitask.ui.WindowComponents;

import java.time.LocalDate;
import java.util.stream.IntStream;

import com.serenitask.model.Goal;
import com.serenitask.util.DatabaseManager.GoalDAO;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AddGoal {
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

        Label minChunkLabel = new Label("Min chunk:");
        ComboBox<Integer> minChunkInput = new ComboBox<>();
        minChunkInput.getItems().addAll(15, 30, 45, 60);

        Label maxChunkLabel = new Label("Max chunk:");
        ComboBox<Integer> maxChunkInput = new ComboBox<>();
        maxChunkInput.getItems().addAll(15, 30, 45, 60);

        Label goalEndDateLabel = new Label("Enter the date you wish to complete your goal by:");
        DatePicker goalEndDateInput = new DatePicker(LocalDate.now());

        ComboBox<String> periodInput = new ComboBox<>();
        // REFACTOR THIS
        periodInput.getItems().addAll("Day", "Week", "Month", "Year");

        Button saveButton = new Button("Save");
        saveButton.setOnAction(e -> {
            String title = wantInput.getText();
            LocalDate goalEndDate = goalEndDateInput.getValue();

            Integer minChunkValue = minChunkInput.getValue();
            Integer maxChunkValue = maxChunkInput.getValue();

            if (minChunkValue == null || maxChunkValue == null) {
                System.err.println("Please select values for minChunk and maxChunk.");
                return;
            }

            int minChunk = minChunkInput.getValue();
            int maxChunk = maxChunkInput.getValue();

            Goal goal = new Goal(title, minChunk, maxChunk);
            GoalDAO goalDAO = new GoalDAO();
            goalDAO.addGoal(goal);

            popOutStage.close();
                });
        Button backButton = new Button("Back");
        backButton.setOnAction(e -> popOutStage.close());

        VBox layout = new VBox(10);
        layout.getChildren().addAll(wantLabel, wantInput, forLabel, hourInput, hourLabel,
                andLabel, minuteInput, minuteLabel, perLabel, periodInput, minChunkLabel, minChunkInput, maxChunkLabel, maxChunkInput, backButton, saveButton);
        layout.setAlignment(Pos.BASELINE_LEFT);
        Scene popOutScene = new Scene(layout, 500, 500);
        popOutStage.setScene(popOutScene);
        popOutStage.showAndWait();
    }
}
