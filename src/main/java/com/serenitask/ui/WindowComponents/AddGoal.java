package com.serenitask.ui.WindowComponents;

import java.time.LocalDate;
import java.util.stream.IntStream;

import com.serenitask.model.Goal;
import com.serenitask.util.DatabaseManager.GoalDAO;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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

        Label minChunkLabel = new Label("Min chunk:");
        TextField minChunkInput = new TextField();

        Label maxChunkLabel = new Label("Max chunk:");
        TextField maxChunkInput = new TextField();

        Label goalEndDateLabel = new Label("Enter the date you wish to complete your goal by:");
        DatePicker goalEndDateInput = new DatePicker(LocalDate.now());

        ComboBox<String> periodInput = new ComboBox<>();
        // REFACTOR THIS
        periodInput.getItems().addAll("Week");

        Button saveButton = new Button("Save");
        saveButton.setOnAction(e -> {
            String title = wantInput.getText();
            int minChunk = Integer.parseInt(minChunkInput.getText());
            int maxChunk = Integer.parseInt(maxChunkInput.getText());
            int targetAmount = Integer.parseInt(hourInput.getText());

            LocalDate allocatedUntil = goalEndDateInput.getValue().minusDays(1);

            Goal goal = new Goal(title, targetAmount, minChunk, maxChunk, allocatedUntil);
            GoalDAO goalDAO = new GoalDAO();
            goalDAO.addGoal(goal);

            popOutStage.close();
        });

        Button backButton = new Button("Back");
        backButton.setOnAction(e -> popOutStage.close());

        VBox layout = new VBox(10);
        layout.getChildren().addAll(wantLabel, wantInput, forLabel, hourInput, hourLabel, periodInput, minChunkLabel, minChunkInput, maxChunkLabel, maxChunkInput, backButton, saveButton);
        layout.setAlignment(Pos.BASELINE_LEFT);
        Scene popOutScene = new Scene(layout, 500, 500);
        popOutStage.setScene(popOutScene);
        popOutStage.showAndWait();

        // Listeners to make sure that the inputs are numeric only
        addNumericOnlyListener(minChunkInput);
        addNumericOnlyListener(maxChunkInput);
        addNumericOnlyListener(hourInput);
    }

    private static void addNumericOnlyListener(TextField textField) {
        textField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*")) {
                    textField.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
    }
}
