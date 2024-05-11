package com.serenitask.util.DatabaseManager;

import com.serenitask.model.Goal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * GoalDAO class is used to interact with the database for goals
 */
public class GoalDAO {
    // Connection to the database
    private Connection connection;

    /**
     * GoalDAO constructor
     * @constructor
     */
    public GoalDAO() {
        // Get connection to the database
        connection = SqliteConnection.getConnection();
        // Create table if it doesn't exist
        createTable();
        // Used for debugging
        // addSampleEntries();
    }

    // Private
    /**
     * Add sample entries to the database
     */
    private void addSampleEntries() {
        try {
            // Create and execute clear query
            Statement clearStatement = connection.createStatement();
            String clearQuery = "DELETE FROM goals";
            clearStatement.execute(clearQuery);
            // Create and execute insert query
            Statement insertStatement = connection.createStatement();
            String insertQuery =
                    "INSERT INTO goals "
                            + "(title, goalTargetAmount, min_chunk, max_chunk, allocatedUntil, daysOutstanding) VALUES "
                            + "('Goal 1', 3, 15, 15, " + java.sql.Date.valueOf(LocalDate.now().plusDays(1)) + ", 1),"
                            + "('Goal 2', 5, 30, 30, " + java.sql.Date.valueOf(LocalDate.now().plusDays(2)) + ", 2),"
                            + "('Goal 3', 7, 45, 45, " + java.sql.Date.valueOf(LocalDate.now().plusDays(3)) + ", 3)";
            insertStatement.execute(insertQuery);
        } catch (Exception e) {
            // Print error if sample entries fail
            e.printStackTrace();
        }
    }

    // Private
    /**
     * Create Goals SQLite table if it doesn't exist
     */
    private void createTable() {
        try {
            // Table Create Query
            String query = "CREATE TABLE IF NOT EXISTS goals ("
                    + "id               INTEGER     PRIMARY KEY AUTOINCREMENT,"
                    + "title            TEXT        NOT NULL,"
                    + "goalTargetAmount INTEGER     NOT NULL,"
                    + "min_chunk        INTEGER     NOT NULL,"
                    + "max_chunk        INTEGER     NOT NULL,"
                    + "allocatedUntil   DATE        NOT NULL,"
                    + "daysOutstanding  INTEGER     NOT NULL);";
            // Create and execute statement
            Statement statement = connection.createStatement();
            statement.execute(query);
        } catch (Exception e) {
            // Print error if table creation fails
            e.printStackTrace();
        }
    }

    /**
     * Add a goal to the database
     * @param goal Goal to add
     * @return ID of the new goal, -1 if failed
     */
    public int addGoal(Goal goal) {
        try {
            // Create insert query
            String query = "INSERT INTO goals ("
                    + "title,"
                    + "goalTargetAmount,"
                    + "min_chunk,"
                    + "max_chunk,"
                    + "allocatedUntil,"
                    + "daysOutstanding) "
                    + "VALUES (?, ?, ?, ?, ?, ?)";
            // Create prepared statement
            PreparedStatement statement = connection.prepareStatement(query);
            // Insert values from event
            statement.setString(1, goal.getTitle());
            statement.setInt(2, goal.getTargetAmount());
            statement.setInt(3, goal.getMinChunk());
            statement.setInt(4, goal.getMaxChunk());
            statement.setDate(5, java.sql.Date.valueOf(goal.getAllocatedUntil()));
            statement.setInt(6, goal.getDaysOutstanding());
            // Execute update
            statement.executeUpdate();

            // Retrieve the ID of the new goal
            ResultSet generatedKeys = statement.getGeneratedKeys();
            // If the ID exists,
            if (generatedKeys.next()) {
                goal.setId(generatedKeys.getInt(1));
                return goal.getId();
            }
        } catch (Exception e) {
            // Print error if goal creation fails
            e.printStackTrace();
        }
        // return -1 if goal wasn't created
        return -1;
    }

    // Add goal simple to be brought back if required.

    /**
     * Update a goal in the database
     * @param goal Goal to update
     * @return True if successful, false otherwise
     */
    public boolean updateGoal(Goal goal) {
        try {
            // Create update query
            String query = "UPDATE goals SET "
                    + "title             = ?,"
                    + "goalTargetAmount  = ?,"
                    + "min_chunk         = ?,"
                    + "max_chunk         = ?,"
                    + "allocatedUntil    = ?,"
                    + "daysOutstanding   = ? "
                    + "WHERE id          = ?";
            // Create prepared statement
            PreparedStatement statement = connection.prepareStatement(query);
            // Update row with values from goal
            statement.setString(1, goal.getTitle());
            statement.setInt(2, goal.getTargetAmount());
            statement.setInt(3, goal.getMinChunk());
            statement.setInt(4, goal.getMaxChunk());
            statement.setDate(5, java.sql.Date.valueOf(goal.getAllocatedUntil()));
            statement.setInt(6, goal.getDaysOutstanding());
            statement.setInt(7, goal.getId());
            // Execute update
            statement.executeUpdate();

            // If success
            return true;
        } catch (Exception e) {
            // Print error if update fails
            e.printStackTrace();
        }
        // Return false if update fails
        return false;
    }

    /**
     * Delete a goal from the database
     * @param id ID of the goal to delete
     * @return True if successful, false otherwise
     */
    public Boolean deleteGoal(int id) {
        try {
            // Create delete query
            String query = "DELETE FROM goals WHERE id = ?";
            // Create prepared statement
            PreparedStatement statement = connection.prepareStatement(query);
            // Delete row id
            statement.setInt(1, id);
            // Execute update
            int rowsDeleted = statement.executeUpdate();

            // Return if goal was deleted
            return rowsDeleted > 0;
        } catch (Exception e) {
            // Print error if delete fails
            e.printStackTrace();
        }
        // Return false if delete fails
        return false;
    }


    /**
     * Get a goal from the database by ID
     * @param id ID of the goal to get
     * @return Goal object, null if not found
     */
    public Goal getGoalById(int id) {
        try {
            // Create Query
            String query = "SELECT * FROM goals WHERE id = ?";
            // Create prepared statement
            PreparedStatement statement = connection.prepareStatement(query);
            // Insert ID into query
            statement.setInt(1, id);
            // Execute query
            ResultSet resultSet = statement.executeQuery();

            // If found, return goal
            if (resultSet.next()) {
                int goalId = resultSet.getInt("id");
                String title = resultSet.getString("title");
                int goalTargetAmount = resultSet.getInt("goalTargetAmount");
                int minChunk = resultSet.getInt("min_chunk");
                int maxChunk = resultSet.getInt("max_chunk");
                LocalDate allocatedUntil = resultSet.getDate("allocatedUntil").toLocalDate();
                int daysOutstanding = resultSet.getInt("daysOutstanding");

                // Create new Goal object
                Goal goal = new Goal(title, goalTargetAmount, minChunk, maxChunk, allocatedUntil, daysOutstanding);
                goal.setId(goalId);

                // Return goal
                return goal;
            }
        } catch (Exception e) {
            // Print error if goal not found
            e.printStackTrace();
        }
        // Return null if goal not found
        return null;
    }

    /**
     * Get all goals from the database
     * @return List of all goals
     */
    public List<Goal> getAllGoals() {
        // Create empty list of events to return
        List<Goal> goals = new ArrayList<>();

        try {
            // Create statement
            Statement statement = connection.createStatement();
            // Create get all query
            String query = "SELECT * FROM goals";
            // Execute query
            ResultSet resultSet = statement.executeQuery(query);

            // For each event in the result set
            while (resultSet.next()) {
                // Retrieve data from the result set
                int id = resultSet.getInt("id");
                String title = resultSet.getString("title");
                int goalTargetAmount = resultSet.getInt("goalTargetAmount");
                int minChunk = resultSet.getInt("min_chunk");
                int maxChunk = resultSet.getInt("max_chunk");
                LocalDate allocatedUntil = resultSet.getDate("allocatedUntil").toLocalDate();
                int daysOutstanding = resultSet.getInt("daysOutstanding");

                // Create a new goal object
                Goal goal = new Goal(title, goalTargetAmount, minChunk, maxChunk, allocatedUntil, daysOutstanding);
                // Add event to list
                goals.add(goal);
            }
        } catch (Exception e) {
            // Print error if event retrieval fails
            e.printStackTrace();
        }
        // return list of events (regardless of none found)
        return goals;
    }
}
