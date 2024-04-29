package com.serenitask.util.DatabaseManager;


import com.serenitask.model.Goal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class GoalDAO {
    private Connection connection;
    public GoalDAO() {
        connection = SqliteConnection.getConnection();
        createTable();
        // Used for debugging
        // addSampleEntries();
    }

// Adding sample entries
    private void addSampleEntries() {
        try {
            // Clear before inserting
            Statement clearStatement = connection.createStatement();
            String clearQuery = "DELETE FROM goals";
            clearStatement.execute(clearQuery);
            Statement insertStatement = connection.createStatement();

            // Sample Insert Query
            String insertQuery =
                    "INSERT INTO goals (title, description, minChunk, maxChunk, periodicity, endDate, recurrenceRules) VALUES "
                            + "('Goal 1', 'description of goal 1', 900,7200, 'weekly', 'placeholder for DATETIME', 'recurr rule'),"
                            + "('Goal 2', 'description of goal 2', 900,7200, 'daily', 'placeholder for DATETIME', 'recurr rule'),"
                            + "('Goal 3', 'description of goal 2', 900,7200, 'bi weekly', 'placeholder for DATETIME', 'recurr rule')";
            insertStatement.execute(insertQuery);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

// Creating table if it doesn't exist
    private void createTable() {
        try {
            String query = "CREATE TABLE IF NOT EXIST goals ("
                    + "id INTEGER  PRIMARY KEY AUTOINCREMENT,"
                    + "title TEXT NOT NULL,"
                    + "description TEXT,"
                    + "min_chunk INTEGER,"
                    + "max_chunk INTEGER,"
                    + "periodicity INTEGER NOT NULL,"
                    + "end_date TEXT,"
                    + "recurrence_rules TEXT"
                    + ");";
            Statement statement = connection.createStatement();
            statement.execute(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

// Adding a goal
    public int addGoal(Goal goal) {
        try {
            // Create insert query
            String query = "INSERT INTO goals (title, description, minChunk, maxChunk, periodicity, endDate, recurrenceRules) VALUES" +
                    "(?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            // Update new row with values from goal
            statement.setString(1, goal.getTitle());
            statement.setString(2, goal.getDescription());
            statement.setInt(3, goal.getMinChunk());
            statement.setInt(4, goal.getMaxChunk());
            statement.setInt(5, goal.getPeriodicity());
            statement.setString(6, goal.getEndDate());
            statement.setString(7, goal.getRecurrenceRules());

            // Execute update
            statement.executeUpdate();
            // Set the id of the new goal
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                goal.setId(generatedKeys.getInt(1));
            }
            return goal.getId();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // return -1 if goal wasn't created
        return -1;
    }

    public boolean updateGoal(Goal goal) {
        try {
            // Create update query
            String query = "UPDATE goals SET " +
                    "title = ?," +
                    "description = ?," +
                    "minChunk = ?," +
                    "maxChunk = ?," +
                    "periodicity = ?," +
                    "endDate = ?," +
                    "recurrenceRules = ?" +
                    "WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query);

            // Update row with values from goal
            statement.setString(1, goal.getTitle());
            statement.setString(2, goal.getDescription());
            statement.setInt(3, goal.getMinChunk());
            statement.setInt(4, goal.getMaxChunk());
            statement.setInt(5, goal.getPeriodicity());
            statement.setString(6, goal.getEndDate());
            statement.setString(7, goal.getRecurrenceRules());
            statement.setInt(8, goal.getId());

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
    public void deleteGoal(Goal goal) {
        try {
            // Create delete query
            String query = "DELETE FROM goals WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            // Delete row id
            statement.setInt(1, goal.getId());
            // Execute update
            statement.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
     */

// Get goal by ID
    public Goal getGoalById(int id) {
        try {
            // Selecting goals
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM goals WHERE id = ?");
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            // Create variables for results
            if (resultSet.next()) {
                String title = resultSet.getString("title");
                String description = resultSet.getString("description");
                int minChunk = resultSet.getInt("minChunk");
                int maxChunk = resultSet.getInt("maxChunk");
                int periodicity = resultSet.getInt("periodicity");
                String endDate = resultSet.getString("endDate");
                String recurrenceRules = resultSet.getString("recurrenceRules");

                Goal goal = new Goal(title, description, minChunk, maxChunk, periodicity, endDate, recurrenceRules);
                goal.setId(id);
                return goal;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

// Getting all goals
    public List<Goal> getAllGoals() {
        List<Goal> goals = new ArrayList<>();
        try {
            // Select Query
            Statement statement = connection.createStatement();
            String query = "SELECT * FROM goals";

            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                // Retrieve data from the result set
                int id = resultSet.getInt("id");
                String title = resultSet.getString("title");
                String description = resultSet.getString("description");
                int minChunk = resultSet.getInt("minChunk");
                int maxChunk = resultSet.getInt("maxChunk");
                int periodicity = resultSet.getInt("periodicity");
                String endDate = resultSet.getString("endDate");
                String recurrenceRules = resultSet.getString("recurrenceRules");
                // Create a new goal object
                Goal goal = new Goal(title, description, minChunk, maxChunk, periodicity, endDate, recurrenceRules);
                goal.setId(id);
                goals.add(goal);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return goals;
    }


}
