package com.serenitask.controller;

import com.serenitask.util.DatabaseManager.SqliteConnection;
import com.serenitask.util.DatabaseManager.GoalDAO;
import com.serenitask.model.Goal;



public class GoalController {
    public GoalController() {
    }

    public void controlSimpleGoal0(String title) {

        // Create goal object with constructor made earlier ( in Goal.java) (2nd Goal Constructor) - done
        // When its called, create a goal object, goalDAO object, feed GoalDAO object into goal.java - done
        Goal goal = new Goal(title);

        GoalDAO goalDAO = new GoalDAO();

        // feed little goal

        goalDAO.addGoal(goal);




        // Feed into function AddGoal (AddEntry)


    }


}
