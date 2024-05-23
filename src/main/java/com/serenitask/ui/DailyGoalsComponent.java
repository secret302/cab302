package com.serenitask.ui;

import java.util.List;

import com.serenitask.controller.GoalController;
import com.serenitask.model.Goal;
import com.serenitask.util.DatabaseManager.GoalDAO;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
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

    goalContainer.setOnMouseEntered(e -> deleteLabel.setVisible(true));
    goalContainer.setOnMouseExited(e -> deleteLabel.setVisible(false));


                

    int goalId = goal.getId();
    deleteLabelBox.setOnMouseClicked(e -> {

      Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Do you want to delete all planned events for this goal?");
      ButtonType buttonYes = new ButtonType("Yes");
      ButtonType buttonNo = new ButtonType("No");
      ButtonType buttonCancel = new ButtonType("Cancel");
      alert.getButtonTypes().setAll(buttonYes, buttonNo, buttonCancel);
      alert.showAndWait().ifPresent(response -> {
          if (response == buttonYes) {
              dailygoals.getChildren().remove(goalContainer);
              // enter backend for deleting multiple
              alert.close();
          } else if (response == buttonNo) {
              dailygoals.getChildren().remove(goalContainer);
              goalDAO.deleteGoal(goalId);
              alert.close();
          }
      });
    });

    deleteLabelText.setOnMouseClicked(e -> {

      Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Do you want to delete all planned events for this goal?");
      ButtonType buttonYes = new ButtonType("Yes");
      ButtonType buttonNo = new ButtonType("No");
      ButtonType buttonCancel = new ButtonType("Cancel");
      alert.getButtonTypes().setAll(buttonYes, buttonNo, buttonCancel);
      alert.showAndWait().ifPresent(response -> {
          if (response == buttonYes) {
              dailygoals.getChildren().remove(goalContainer);
              // enter backend for deleting multiple
              alert.close();
          } else if (response == buttonNo) {
              dailygoals.getChildren().remove(goalContainer);
              goalDAO.deleteGoal(goalId);
              alert.close();
          }
      });
    });


    goalContainer.getChildren().add(deleteLabel);
    dailygoals.getChildren().add(goalContainer);
  }
  return dailygoals;
    }
}
