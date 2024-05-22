package com.serenitask.util.DatabaseManager;

import com.serenitask.model.Goal;
import com.serenitask.util.ErrorHandler;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * GoalDAO (Data Access Object) class for interacting with the 'goals' table in the database.
 * Handles CRUD operations for Goal objects, converting between Goal model and database representation.
 */
public class GoalDAO {

    /**
     * Database connection object.
     */
    private final Connection connection;

    /**
     * Constructor for GoalDAO.
     * Initializes the database connection and creates the 'goals' table if it doesn't exist.
     */
    public GoalDAO() {
        // Get connection to the database
        connection = SqliteConnection.getConnection();
        // Create table if it doesn't exist
        createTable();
    }

    /**
     * Creates the 'goals' table in the database if it doesn't exist.
     * Defines the table schema with columns for goal attributes.
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
            ErrorHandler.handleException(e);
        }
    }

    /**
     * Adds a new Goal to the database.
     *
     * @param goal The Goal object to be added to the database.
     * @return The ID of the newly added Goal, or -1 if the operation fails.
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
        } catch (SQLException e) {
            // Print error if goal creation fails
            ErrorHandler.handleException(e);
        }
        // return -1 if goal wasn't created
        return -1;
    }

    /**
     * Updates an existing Goal in the database.
     *
     * @param goal The Goal object with updated information.
     * @return True if the update was successful, false otherwise.
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
        } catch (SQLException e) {
            // Print error if update fails
            ErrorHandler.handleException(e);
        }
        // Return false if update fails
        return false;
    }

    /**
     * Deletes a Goal from the database.
     *
     * @param id The ID of the Goal to be deleted.
     * @return True if the deletion was successful, false otherwise.
     */
    public boolean deleteGoal(int id) {
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
        } catch (SQLException e) {
            // Print error if delete fails
            ErrorHandler.handleException(e);
        }
        // Return false if delete fails
        return false;
    }

    /**
     * Retrieves a Goal from the database by its ID.
     *
     * @param id The ID of the Goal to be retrieved.
     * @return The Goal object if found, or null if no matching goal is found.
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
                return constructGoalFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            // Print error if goal not found
            ErrorHandler.handleException(e);
        }
        // Return null if goal not found
        return null;
    }

    /**
     * Retrieves all Goals from the database.
     *
     * @return A list of all Goal objects stored in the database.
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

            // Add each event in the result set
            while (resultSet.next()) {
                goals.add(constructGoalFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            // Print error if event retrieval fails
            ErrorHandler.handleException(e);
        }
        // return list of events (regardless of none found)
        return goals;
    }

    /**
     * Constructs a Goal object from a ResultSet obtained from the database.
     *
     * @param resultSet The ResultSet containing data for a goal.
     * @return The constructed Goal object.
     * @throws SQLException If an error occurs while processing the ResultSet.
     */
    private Goal constructGoalFromResultSet(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");
        String title = resultSet.getString("title");
        int goalTargetAmount = resultSet.getInt("goalTargetAmount");
        int minChunk = resultSet.getInt("min_chunk");
        int maxChunk = resultSet.getInt("max_chunk");
        LocalDate allocatedUntil = resultSet.getDate("allocatedUntil").toLocalDate();
        int daysOutstanding = resultSet.getInt("daysOutstanding");

        Goal goal = new Goal(title, goalTargetAmount, minChunk, maxChunk, allocatedUntil, daysOutstanding);
        goal.setId(id);
        return goal;
    }
}