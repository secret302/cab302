package com.serenitask.util.DatabaseManager;


import com.serenitask.model.GoalTracking;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class GoalTrackingDAO {
    private Connection connection;

    public GoalTrackingDAO() {
        connection = SqliteConnection.getConnection();
        createTable();
        // Used for debugging
        // addSampleEntries();
    }

    private void addSampleEntries() {
        try {
            // Clear before inserting
            Statement clearStatement = connection.createStatement();
            String clearQuery = "DELETE FROM goal_tracking";
            clearStatement.execute(clearQuery);
            Statement insertStatement = connection.createStatement();
            // goal_date values are strings, placeholder for DATE
            String insertQuery =
                    "INSERT INTO goal_tracking (goal_id, goal_date, completed) VALUES "
                            + "(1, 'A', TRUE),"
                            + "(1, 'B', FALSE ),"
                            + "(2, 'A', TRUE ),"
                            + "(2, 'B', FALSE ),"
                            + "(3, 'C', TRUE )";
            insertStatement.execute(insertQuery);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createTable() {
        try {
            // Create table if it doesn't exist
            String query = "CREATE TABLE IF NOT EXISTS goal_tracking ("
                    + "goal_id INTEGER  REFERENCES goals (id) NOT NULL,"
                    + "goal_date DATE NOT NULL,"
                    + "completed BOOLEAN NOT NULL DEFAULT (false),"
                    + "PRIMARY KEY ( goal_id, goal_date)"
                    + ");";
            Statement statement = connection.createStatement();
            statement.execute(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int addEvent(GoalTracking goalTracking) {
        try {
            // Create insert query
            String query = "INSERT INTO events (goal_id, goal_date, completed) VALUES" +
                    "(?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            // Update new row with values from goalTracking
            statement.setInt(1, goalTracking.getId());
            statement.setString(2, goalTracking.getGoalDate());
            statement.setBoolean(3, goalTracking.getCompleted());

            // Execute update
            statement.executeUpdate();
            // Set the id of the new Goal Tracking entry
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                goalTracking.setId(generatedKeys.getInt(1));
            }
            return goalTracking.getId();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // return -1 if event wasn't created
        return -1;
    }


    public boolean updateGoalTracking(GoalTracking goalTracking) {
        try {
            // Create update query
            String query = "UPDATE events SET " +
                    "completed = ?" +
                    "WHERE id = ? AND goal_date = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            // Update row with values from Goal Tracking
            statement.setInt(1, goalTracking.getId());
            statement.setString(2, goalTracking.getGoalDate());
            statement.setBoolean(3, goalTracking.getCompleted());
            // Execute update
            statement.executeUpdate();

            // If success
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // To be brought pack as an optional.
    /*
    public boolean deleteGoalTracking(int id, string date) {
        try {
            // Create delete query
            String query = "DELETE FROM goal_tracking WHERE id = ? AND goal_date = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            // Delete row id
            statement.setInt(1, id);
            statement.setInt(1, date);
            // Execute update
            statement.executeUpdate();

            // If success
            return true;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
     */

    // Goal Track
    public GoalTracking getSingleTrackedGoal(int id, String date) {
        try {
            // Select the goal tracking
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM goal_tracking WHERE id = ? AND goal_date =?");
            statement.setInt(1, id);
            statement.setString(2, date);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int goal_id = resultSet.getInt("goal_id");
                String goal_date = resultSet.getString("goal_date");
                Boolean completed = resultSet.getBoolean("completed");

                GoalTracking goalTracking = new GoalTracking(goal_id, goal_date, completed);

                return goalTracking;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Returns a list containing all unique IDs from a given list
    private List<Integer> getIdList(List<GoalTracking> unsorted)
    {
        List<Integer> goalIds = new ArrayList<>();
        for (GoalTracking goal : unsorted)
        {
            int id = goal.getId();
            if(goalIds.contains(id) == false)
            {
                goalIds.add(id);
            }
        }
        return goalIds;
    };

    // Returns a list containing a list of each goaltracked and its respective entries from the database
    private List<List<GoalTracking>> sortTrackedGoals(List<GoalTracking> unsorted)
    {
        // Setup return list and list of unique IDs
        List<List<GoalTracking>> AllGoals = new ArrayList<>();
        List<Integer> IdList = getIdList(unsorted);

        // For each unique ID, add all entries to a list and append to the AllGoals List
        for (Integer ID : IdList)
        {
            List<GoalTracking> goalTracked = new ArrayList<>();

            for (GoalTracking goal : unsorted)
            {
                if(ID == goal.getId())
                {
                    goalTracked.add(goal);
                }
            }

            AllGoals.add(goalTracked);
        }

        // Return list of lists
        return AllGoals;
    };

    // Returns a list of all entries related to a goal ID
    public List<GoalTracking> getTrackedGoalById(int id) {
        List<GoalTracking> goals = new ArrayList<>();
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM goal_tracking WHERE id = ? ");
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                // Retrieve data from the result set
                int goal_id = resultSet.getInt("goal_id");
                String goal_date = resultSet.getString("goal_date");
                Boolean completed = resultSet.getBoolean("completed");
                // Create a new event object
                GoalTracking goalTracking = new GoalTracking(goal_id, goal_date, completed);
                goals.add(goalTracking);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return goals;
    }

    // Create a list of all goal tracking by Date
    public List<GoalTracking> getTrackedGoalByDate(String date) {
        List<GoalTracking> goals = new ArrayList<>();
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM goal_tracking WHERE goal_date = ? ");
            statement.setString(1, date);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                // Retrieve data from the result set
                int goal_id = resultSet.getInt("goal_id");
                String goal_date = resultSet.getString("goal_date");
                Boolean completed = resultSet.getBoolean("completed");
                // Create a new event object
                GoalTracking goalTracking = new GoalTracking(goal_id, goal_date, completed);
                goals.add(goalTracking);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return goals;
    }

    // Create list of all tracked goals
    public List<List<GoalTracking>> getAllTrackedGoals() {
        List<List<GoalTracking>> AllGoals = new ArrayList<>();
        List<GoalTracking> unsorted = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            String query = "SELECT * FROM goal_tracking";
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                // Retrieve data from the result set
                int goal_id = resultSet.getInt("goal_id");
                String goal_date = resultSet.getString("goal_date");
                Boolean completed = resultSet.getBoolean("completed");
                // Create a new event object
                GoalTracking goalTracking = new GoalTracking(goal_id, goal_date, completed);
                unsorted.add(goalTracking);
            }
            AllGoals = sortTrackedGoals(unsorted);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return AllGoals;
    }


}