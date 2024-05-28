package com.serenitask.util.DatabaseManager;

import java.sql.*;
import com.serenitask.util.ErrorHandler;

/**
 * Data Access Object for interacting with the 'settings' table in the database.
 * Handles CRUD operations for application settings.
 */
public class SettingsDAO {
    /**
     * Database connection object.
     */
    private final Connection connection;

    /**
     * Constructor for SettingsDAO.
     * Initializes the database connection and creates the 'settings' table if it doesn't exist.
     */
    public SettingsDAO() {
        // Get connection to the database
        this.connection = SqliteConnection.getConnection();
        // Create table if it doesn't exist
        createTable();
    }

    /**
     * Creates the 'settings' table in the database if it doesn't exist.
     * Defines the table schema with columns for setting key-value pairs.
     */
    private void createTable() {
        try {
            // Query to create the settings table
            String query = "CREATE TABLE IF NOT EXISTS settings ("
                    + "setting_key TEXT PRIMARY KEY,"
                    + "setting_value TEXT)";
            Statement statement = connection.createStatement();
            // Execute the create table query
            statement.execute(query);
        } catch (SQLException e) {
            // Print error if table creation fails
            ErrorHandler.handleException(e);
        }
    }

    /**
     * Retrieves the value of a setting from the database.
     * If the setting does not exist, the default value is returned.
     *
     * @param key          The key of the setting to retrieve.
     * @param defaultValue The default value to return if the setting does not exist.
     * @return The value of the setting.
     */
    public String getSetting(String key, String defaultValue) {
        try {
            // Query to retrieve the setting value
            String query = "SELECT setting_value FROM settings WHERE setting_key = ?";
            // Create prepared statement
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, key);
            // Execute query
            ResultSet resultSet = statement.executeQuery();
            // Return the setting value if it exists
            if (resultSet.next()) {
                return resultSet.getString("setting_value");
            } else {
                // If the setting doesn't exist, insert the default value.
                saveSetting(key, defaultValue);
            }
        } catch (SQLException e) {
            // Print error if setting retrieval fails
            ErrorHandler.handleException(e);
        }
        // Return the default value if the setting doesn't exist
        return defaultValue;
    }

    /**
     * Saves a setting to the database.
     * If the setting already exists, it is updated with the new value.
     * If the setting does not exist, a new setting is inserted.
     *
     * @param key   The key of the setting to save.
     * @param value The value of the setting to save.
     */
    public void saveSetting(String key, String value) {
        try {
            // Check if the setting already exists
            if (settingExists(key)) {
                // Update the existing setting
                String updateQuery = "UPDATE settings SET setting_value = ? WHERE setting_key = ?";
                // Create prepared statement
                PreparedStatement updateStatement = connection.prepareStatement(updateQuery);
                updateStatement.setString(1, value);
                updateStatement.setString(2, key);
                // Execute the update statement
                updateStatement.executeUpdate();
            } else {
                // Insert a new setting
                String insertQuery = "INSERT INTO settings (setting_key, setting_value) VALUES (?, ?)";
                // Create prepared statement
                PreparedStatement insertStatement = connection.prepareStatement(insertQuery);
                insertStatement.setString(1, key);
                insertStatement.setString(2, value);
                // Execute the insert statement
                insertStatement.executeUpdate();
            }
        } catch (SQLException e) {
            // Print error if setting save fails
            ErrorHandler.handleException(e);
        }
    }

    /**
     * Checks if a setting exists in the database.
     *
     * @param key The key of the setting to check.
     * @return True if the setting exists, false otherwise.
     */
    private boolean settingExists(String key) {
        try {
            // Query to check if the setting exists
            String query = "SELECT 1 FROM settings WHERE setting_key = ?";
            // Create prepared statement
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, key);
            // Execute query and return true if a result is found
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            // Print error if setting check fails
            ErrorHandler.handleException(e);
        }
        // Return false if the setting check fails
        return false;
    }
}