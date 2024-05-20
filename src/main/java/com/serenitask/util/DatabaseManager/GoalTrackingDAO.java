package com.serenitask.util.DatabaseManager;

import com.serenitask.model.GoalTracking;
import com.serenitask.util.ErrorHandler;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * GoalTrackingDAO (Data Access Object) class for managing interactions with the 'goal_tracking' table in the database.
 * Provides CRUD operations for GoalTracking objects, mapping between the model and database representation.
 */
public class GoalTrackingDAO {

    /** Database connection object. */
    private final Connection connection;

    /**
     * Constructor for GoalTrackingDAO.
     * Initializes the database connection and creates the 'goal_tracking' table if it doesn't exist.
     */
    public GoalTrackingDAO() {
        connection = SqliteConnection.getConnection();
        createTable();
    }

    /**
     * Creates the 'goal_tracking' table in the database if it doesn't exist.
     * Defines the table schema, including columns for goal ID, date, and completion status, with a composite primary key.
     */
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
        } catch (SQLException e) {
            // Handle SQL errors
            ErrorHandler.handleException(e);
        }
    }

    /**
     * Adds a new GoalTracking record to the database.
     *
     * @param goalTracking The GoalTracking object representing the tracking information to be added.
     * @return The ID of the newly added goal tracking record, or -1 if the operation fails.
     */
    public int addGoalTracking(GoalTracking goalTracking) {
        try {
            // Create insert query
            String query = "INSERT INTO goal_tracking (goal_id, goal_date, completed) VALUES (?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            // Update new row with values from goalTracking
            statement.setInt(1, goalTracking.getId());
            statement.setDate(2, java.sql.Date.valueOf(goalTracking.getGoalDate()));
            statement.setBoolean(3, goalTracking.getCompleted());

            // Execute update
            statement.executeUpdate();
            // Set the id of the new Goal Tracking entry
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                goalTracking.setId(generatedKeys.getInt(1));
            }
        } catch (SQLException e) {
            // Handle SQL errors
            ErrorHandler.handleException(e);
        }
        // return -1 if event wasn't created
        return -1;
    }

    /**
     * Updates an existing GoalTracking record in the database.
     *
     * @param goalTracking The GoalTracking object with the updated tracking information.
     * @return True if the update was successful, false otherwise.
     */
    public boolean updateGoalTracking(GoalTracking goalTracking) {
        try {
            // Create update query
            String query = "UPDATE goal_tracking SET completed = ? WHERE goal_id = ? AND goal_date = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            // Update row with values from Goal Tracking
            statement.setInt(1, goalTracking.getId());
            statement.setDate(2, java.sql.Date.valueOf(goalTracking.getGoalDate());
            statement.setBoolean(3, goalTracking.getCompleted());
            // Execute update
            statement.executeUpdate();

            // If success
            return true;
        } catch (SQLException e) {
            // Handle SQL errors
            ErrorHandler.handleException(e);
        }
        // Return false
        return false;
    }

    /**
     * Delete a Goal Tracking entry from the database
     * @param id Goal Tracking ID
     * @param date Goal Tracking Date
     * @return True if the Goal Tracking entry was deleted successfully
     */
    public boolean deleteGoalTracking(int id, LocalDate date) {
        try {
            // Create delete query
            String query = "DELETE FROM goal_tracking WHERE id = ? AND goal_date = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            // Delete row id
            statement.setInt(1, id);
            statement.setDate(2, java.sql.Date.valueOf(date));
            // Execute update
            statement.executeUpdate();

            // If success
            return true;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Retrieves a GoalTracking record from the database by its goal ID and date.
     *
     * @param goalId The ID of the goal for which tracking information is retrieved.
     * @param date  The date for which tracking information is retrieved.
     * @return The GoalTracking object representing the tracking information, or null if not found.
     */
    public GoalTracking getGoalTrackingByIdAndDate(int goalId, LocalDate date) {
        try {
            // Select the goal tracking
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM goal_tracking WHERE id = ? AND goal_date =?");
            statement.setInt(1, goalId);
            statement.setDate(2, java.sql.Date.valueOf(date));
            ResultSet resultSet = statement.executeQuery();
            // If goal returned, return constructed
            if (resultSet.next()) {
                return constructGoalTrackingFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            // Handle SQL errors
            ErrorHandler.handleException(e);
        }
        // Return null
        return null;
    }

    /**
     * Retrieves all GoalTracking records associated with a specific goal ID.
     *
     * @param goalId The ID of the goal for which tracking records are retrieved.
     * @return A list of GoalTracking objects representing the tracking information for the specified goal.
     */
    public List<GoalTracking> getGoalTrackingByGoalId(int goalId) {
        // Create empty list
        List<GoalTracking> goalTrackingList = new ArrayList<>();
        try {
            // Create query
            String query = "SELECT * FROM goal_tracking WHERE goal_id = ?";
            // Create prepared statement
            PreparedStatement statement = connection.prepareStatement(query);
            // Insert goal ID into query
            statement.setInt(1, goalId);
            // Execute query
            ResultSet resultSet = statement.executeQuery();

            // Add all results to list
            while (resultSet.next()) {
                goalTrackingList.add(constructGoalTrackingFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            // Handle SQL errors
            ErrorHandler.handleException(e);
        }
        // Return list even if empty
        return goalTrackingList;
    }

    /**
     * Retrieves all GoalTracking records associated with a specific date.
     *
     * @param date The date for which tracking records are retrieved.
     * @return A list of GoalTracking objects representing the tracking information for the specified date.
     */
    public List<GoalTracking> getGoalTrackingByDate(String date) {
        // Create empty list
        List<GoalTracking> goalTrackingList = new ArrayList<>();

        try{
            // Create query
            String query = "SELECT * FROM goal_tracking WHERE goal_date = ?";
            // Create prepared statement
            PreparedStatement statement = connection.prepareStatement(query);
            // Insert date into query
            statement.setString(1, date);

            // Execute query
            try (ResultSet resultSet = statement.executeQuery()) {
                // Add all results to list
                while (resultSet.next()) {
                    goalTrackingList.add(constructGoalTrackingFromResultSet(resultSet));
                }
            }
        } catch (SQLException e) {
            // Handle SQL errors
            ErrorHandler.handleException(e);
        }
        // Return list even if empty
        return goalTrackingList;
    }

    /**
     * Retrieves all GoalTracking records from the database.
     *
     * @return A list of GoalTracking objects representing all goal tracking information stored in the database.
     */
    public List<GoalTracking> getAllGoalTrackingRecords() {
        // Create query
        List<GoalTracking> goalTrackingList = new ArrayList<>();

        try {
            String query = "SELECT * FROM goal_tracking";
            // Create statement
            Statement statement = connection.createStatement();
            // Execute query
            ResultSet resultSet = statement.executeQuery(query);

            // Add all results to list
            while (resultSet.next()) {
                goalTrackingList.add(constructGoalTrackingFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            // Handle SQL errors
            ErrorHandler.handleException(e);
        }
        // Return list even if empty
        return goalTrackingList;
    }

    /**
     * Constructs a GoalTracking object from a ResultSet obtained from the database.
     *
     * @param resultSet The ResultSet containing data for a goal tracking record.
     * @return The constructed GoalTracking object.
     * @throws SQLException If an error occurs while processing the ResultSet.
     */
    private GoalTracking constructGoalTrackingFromResultSet(ResultSet resultSet) throws SQLException {
        int goalId = resultSet.getInt("goal_id");
        String goalDate = resultSet.getString("goal_date");
        boolean completed = resultSet.getBoolean("completed");

        return new GoalTracking(goalId, goalDate, completed);
    }
}

/* Original Code, kept in case of requiring fazed out methods
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
}

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
}

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
*/