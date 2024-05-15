package com.serenitask.ui;

import com.serenitask.controller.GoalController;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

/**
 * Provides static utility methods to manage and display the daily goals component in the user interface.
 * This class is responsible for setting up the input field and submission process for new daily goals.
 */
public class DailyGoalsComponent {
    /**
     * Configures and populates the VBox with components necessary for inputting and displaying daily goals.
     * Users can enter their goals in a TextField and submit them by clicking a button, which then displays the goal in the same VBox.
     *
     * @param dailygoals       The VBox that will contain the list of goals and the input components.
     * @param goalTextField    The TextField where users enter their goals.
     * @param createGoalButton The Button that users click to submit their goals.
     */
    public static void goalView(VBox dailygoals, TextField goalTextField, Button createGoalButton) {
        dailygoals.setSpacing(10);
        dailygoals.setPadding(new Insets(10));

        goalTextField.setPromptText("Enter your goal here");

        // Create button for goals container

        createGoalButton.setOnAction(event -> {
            String goal = goalTextField.getText().trim();
            if (!goal.isEmpty()) {
                dailygoals.getChildren().add(new javafx.scene.control.Label(goal));
                goalTextField.clear();
            }
            // Commit goal to database
            GoalController goalController = new GoalController();
            goalController.controlSimpleGoal(goal);
        });

        // Setup right hand side Vbox elements
        dailygoals.getChildren().addAll(new javafx.scene.control.Label("I want to"), goalTextField, createGoalButton);
    }
}
