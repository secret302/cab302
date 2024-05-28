package com.serenitask.util.DatabaseManager;

import java.sql.*;
import com.serenitask.util.ErrorHandler;

public class SettingsDAO {
    private final Connection connection;

    public SettingsDAO() {
        this.connection = SqliteConnection.getConnection();
        createTable();
    }

    private void createTable() {
        try {
            String query = "CREATE TABLE IF NOT EXISTS settings ("
                    + "setting_key TEXT PRIMARY KEY,"
                    + "setting_value TEXT)";
            Statement statement = connection.createStatement();
            statement.execute(query);
        } catch (SQLException e) {
            ErrorHandler.handleException(e);
        }
    }

    public String getSetting(String key, String defaultValue) {
        try {
            String query = "SELECT setting_value FROM settings WHERE setting_key = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, key);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("setting_value");
            } else {
                // If the setting doesn't exist, insert the default value.
                saveSetting(key, defaultValue);
            }
        } catch (SQLException e) {
            ErrorHandler.handleException(e);
        }
        return defaultValue;
    }

    public void saveSetting(String key, String value) {
        try {
            // Check if the setting already exists
            if (settingExists(key)) {
                // Update the existing setting
                String updateQuery = "UPDATE settings SET setting_value = ? WHERE setting_key = ?";
                PreparedStatement updateStatement = connection.prepareStatement(updateQuery);
                updateStatement.setString(1, value);
                updateStatement.setString(2, key);
                updateStatement.executeUpdate();
            } else {
                // Insert a new setting
                String insertQuery = "INSERT INTO settings (setting_key, setting_value) VALUES (?, ?)";
                PreparedStatement insertStatement = connection.prepareStatement(insertQuery);
                insertStatement.setString(1, key);
                insertStatement.setString(2, value);
                insertStatement.executeUpdate();
            }
        } catch (SQLException e) {
            ErrorHandler.handleException(e);
        }
    }

    private boolean settingExists(String key) {
        try {
            String query = "SELECT 1 FROM settings WHERE setting_key = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, key);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            ErrorHandler.handleException(e);
        }
        return false;
    }
}