package com.serenitask.ui;

import com.serenitask.controller.GoalController;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class DailyGoalsComponent {
    public static void goalView(VBox dailygoals, TextField goalTextField, Button createGoalButton){
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
