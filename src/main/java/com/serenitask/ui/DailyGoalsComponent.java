package com.serenitask.ui;

import java.util.List;

import com.serenitask.controller.GoalController;
import com.serenitask.model.Goal;
import com.serenitask.util.DatabaseManager.GoalDAO;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

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
    public static VBox goalView(VBox dailygoals, TextField goalTextField, Button createGoalButton) {
        dailygoals.setSpacing(10);
        dailygoals.setPadding(new Insets(10));


  GoalDAO goalDAO = new GoalDAO();
  List<Goal> goals = goalDAO.getAllGoals();

  for (Goal goal : goals) {
    HBox goalContainer = new HBox();

    Label goalLabel = new Label(goal.getTitle());
    Label deleteLabel = new Label(" (X)");

    deleteLabel.setTextFill(Color.RED);
    deleteLabel.setStyle("-fx-alignment: center-right;");

    int goalId = goal.getId();
    deleteLabel.setOnMouseClicked(e -> {
      dailygoals.getChildren().remove(goalContainer);
      goalDAO.deleteGoal(goalId);
    });

    goalContainer.getChildren().addAll(goalLabel, deleteLabel);
    dailygoals.getChildren().add(goalContainer);
  }
  return dailygoals;
    }
}
