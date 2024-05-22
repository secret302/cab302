package com.serenitask.ui.WindowComponents;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.IntStream;

import com.serenitask.model.Goal;
import com.serenitask.util.DatabaseManager.GoalDAO;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
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
    public static void displayAddGoalView(VBox dailygoals) {
        Stage popOutStage = new Stage();
        popOutStage.initModality(Modality.APPLICATION_MODAL);
        popOutStage.setTitle("Add Goal");
        Label wantLabel = new Label("I want to do:");
        wantLabel.setFont(Font.font(30));
        TextField wantInput = new TextField();
        wantInput.setMaxWidth(150);
        wantInput.setStyle("-fx-text-alignment: center");
        

        Label forLabel = new Label("For");
        forLabel.setFont(Font.font(30));
        TextField hourInput = new TextField();
        hourInput.setMaxWidth(100);
        Label hourLabel = new Label("Hours");
        hourLabel.setFont(Font.font(30));
        Label perLabel = new Label("Per");
        perLabel.setFont(Font.font(30));

        Label minChunkLabel = new Label("Minimum amount of time for a goal occurrance:");
        TextField minChunkInput = new TextField();
        minChunkInput.setMaxWidth(200);

        Label maxChunkLabel = new Label("Maximum amount of time for a goal occurrance:");
        TextField maxChunkInput = new TextField();
        maxChunkInput.setMaxWidth(200);

        Label goalEndDateLabel = new Label("Enter the date you wish to complete your goal by:");
        DatePicker goalEndDateInput = new DatePicker(LocalDate.now());

        ComboBox<String> periodInput = new ComboBox<>();
        periodInput.setStyle("-fx-text-alignment: center");
        // REFACTOR THIS
        periodInput.getItems().addAll("Week");

        Button saveButton = new Button("Save");
        saveButton.setStyle("-fx-background-color: lightgreen; -fx-font-size: 20");
        saveButton.setOnAction(e -> {
            String title = wantInput.getText();
            int minChunk = Integer.parseInt(minChunkInput.getText());
            int maxChunk = Integer.parseInt(maxChunkInput.getText());
            int targetAmount = (int) Float.parseFloat(hourInput.getText()) * 60;

            // not implemented
            //LocalDate allocatedUntil = goalEndDateInput.getValue().minusDays(1);
            LocalDate allocatedUntil = LocalDate.now().minusDays(1);

            Goal goal = new Goal(title, targetAmount, minChunk, maxChunk, allocatedUntil);
            GoalDAO goalDAO = new GoalDAO();
            goalDAO.addGoal(goal);

            // updating goal in dailygoals list

            
                    dailygoals.setSpacing(10);
        dailygoals.setPadding(new Insets(10));

        StackPane goalContainer = new StackPane();
        Rectangle addGoalViewBox = new Rectangle(160, 30);
        addGoalViewBox.setFill(Color.web("#94b7fd"));
        Label goalLabel = new Label(goal.getTitle());
        goalLabel.setFont(Font.font(15));
        goalLabel.setAlignment(Pos.CENTER);

        StackPane deleteLabel = new StackPane();
        Rectangle deleteLabelBox = new Rectangle(30, 30);
        Label deleteLabelText = new Label("X");
        deleteLabelText.setPadding(new Insets(0,7,0,0));
        deleteLabelText.setAlignment(Pos.CENTER);
        deleteLabel.getChildren().addAll(deleteLabelBox, deleteLabelText);
        deleteLabelText.setFont(Font.font(24));
        deleteLabel.setMaxWidth(160);
        deleteLabelText.setTextFill(Color.WHITE);
        deleteLabelBox.setFill(Color.RED);
        deleteLabel.setVisible(false);
        deleteLabel.setAlignment(Pos.CENTER_RIGHT);

        goalContainer.getChildren().addAll(addGoalViewBox, goalLabel);

        goalContainer.setOnMouseEntered(event -> deleteLabel.setVisible(true));
        goalContainer.setOnMouseExited(event -> deleteLabel.setVisible(false));

        int goalId = goal.getId();
        deleteLabelBox.setOnMouseClicked(event -> {
            dailygoals.getChildren().remove(goalContainer);
            goalDAO.deleteGoal(goalId);
        });

        deleteLabelText.setOnMouseClicked(event -> {
        dailygoals.getChildren().remove(goalContainer);
        goalDAO.deleteGoal(goalId);
    });


        goalContainer.getChildren().add(deleteLabel);
        dailygoals.getChildren().add(goalContainer);
  

            popOutStage.close();
        });
        
        Button backButton = new Button("Back");
        backButton.setStyle("-fx-background-color: #ff5f56; -fx-font-size: 20");
        backButton.setOnAction(e -> popOutStage.close());

        HBox buttonBox = new HBox(10);
        buttonBox.getChildren().addAll(backButton, saveButton);
        buttonBox.setAlignment(Pos.CENTER);

        VBox layout = new VBox(10);
        layout.getChildren().addAll(wantLabel, wantInput, forLabel, hourInput, hourLabel, perLabel, periodInput, minChunkLabel, minChunkInput, maxChunkLabel, maxChunkInput, buttonBox);
        layout.setAlignment(Pos.CENTER);
        Scene popOutScene = new Scene(layout, 500, 550);
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
