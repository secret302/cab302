package com.serenitask.controller;

import com.serenitask.util.DatabaseManager.GoalDAO;
import com.serenitask.model.Goal;

import java.util.ArrayList;
import java.util.List;

/**
 * Goal controller class handles the interface between GoalDAO and the calendars. Performs goal CRUD operations.
 */
public class GoalController {

    /**
     * Loads a list of titles of all goals from the database.
     *
     * @return A List of Strings where each string is the title of a Goal.
     */
    public List<String> loadSimpleGoal() {
        try {
            GoalDAO goalDAO = new GoalDAO();

            List<Goal> goalList = goalDAO.getAllGoals();
            List<String> stringList = new ArrayList<>();
            for (Goal goal : goalList) {
                stringList.add(goal.getTitle());
            }
            return stringList;
        }
        catch(Exception e){
            System.err.println("An error occured while trying to load simple goals: " + e.getMessage());
            e.printStackTrace();
        }
        // if the simple goal cannot be loaded, an empty list is returned
        return new ArrayList<>();
    }

    /**
     * Creates a new Goal and adds it to the database.
     *
     * @param title The title of the Goal to be added.
     */
    public void controlSimpleGoal(String title) {
        try {
            Goal goal = new Goal(title, 15, 60);
            GoalDAO goalDAO = new GoalDAO();
            goalDAO.addGoal(goal);
        }
        catch(Exception e){
            System.err.println("An error occurred while trying to create a new goal and add it to the database: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Checks if there are any Goals in the database.
     *
     * @return true if there are no Goals stored, false otherwise.
     */
    public boolean checkIfEmpty() {
        try {
            GoalDAO goalDAO = new GoalDAO();
            List<Goal> goalList = goalDAO.getAllGoals();
            return goalList.isEmpty();
        }
        catch(Exception e){
            System.err.println("An error has occurred while checking if there are any Goals in the Database: " + e.getMessage());
            e.printStackTrace();
        }
        // returns false if there are Goals stored in the Database
        return false;
    }

    /**
     * Retrieves the first Goal from the database if available.
     *
     * @return the first Goal if the list is not empty, null otherwise.
     */
    public Goal returnFirstGoal() {
        try {
            GoalDAO goalDAO = new GoalDAO();
            List<Goal> goalList = goalDAO.getAllGoals();
            if (!goalList.isEmpty()) {
                return goalList.get(0); // Access first element
            } else {
                return null;
            }
        }
        catch(Exception e){
            System.err.println("An error has occurred while retrieving the first Goal from the database: " + e.getMessage());
            e.printStackTrace();
        }
        // returns null if the list is not empty
        return null;
    }

    /**
     * Deletes a Goal from the database based on the given ID.
     *
     * @param id The ID of the Goal to be deleted.
     */
    public void deleteGoal(int id) {
        try {
            GoalDAO goalDAO = new GoalDAO();
            goalDAO.deleteGoal(id);
        }
        catch(Exception e){
            System.err.println("An error has occurred while trying to delete a Goal from the Database: " + e.getMessage());
            e.printStackTrace();
        }
    }

}