package com.serenitask.controller;

import com.serenitask.util.DatabaseManager.SettingsDAO;
import javafx.collections.FXCollections;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.HashMap;
import java.util.Map;

/**
 * Provides a user interface component for managing application settings.
 * This class is responsible for rendering a settings dialog where users can view
 * and modify application settings. Settings are persisted to the database
 * automatically.
 */
public class SettingsController {
    /**
     * Data Access Object for interacting with the 'settings' table in the database.
     */
    private static final SettingsDAO settingsDAO = new SettingsDAO();

    /**
     * Map of setting titles to their corresponding UI controls.
     */
    private static final Map<String, Control> settingControls = new HashMap<>();

    /**
     * Flag to enable or disable live updates to the database when settings change.
     */
    private static final Boolean enableLiveUpdates = false;

    /**
     * Adds a setting to the settings UI and handles database interaction.
     *
     * @param title         The display title of the setting.
     * @param type          The data type of the setting ("text", "number", "time", "boolean", "choice").
     * @param defaultValue  The default value of the setting (as a String).
     * @param parent        The parent container to add the setting UI element to.
     */
    public static void addSetting(String title, String type, String defaultValue, VBox parent) {
        // Attempt to retrieve the setting value from the database, using the default value if not found.
        String settingValue = settingsDAO.getSetting(title, defaultValue);

        // Create a label for the setting title
        Label label = new Label(title + ":");

        // Create the appropriate UI control based on the setting type.
        Control inputControl = createInputControl(type, settingValue, title);

        // Add UI elements to the parent container with appropriate spacing and layout.
        HBox settingBox = new HBox(10);
        settingBox.getChildren().addAll(label, inputControl);
        HBox.setHgrow(inputControl, Priority.ALWAYS);
        parent.getChildren().add(settingBox);

        // Store a reference to the control for later access (e.g., retrieving the value).
        settingControls.put(title, inputControl);
    }

    // TODO: Maybe add a separate method for adding event listeners?
    /**
     * Creates and configures the appropriate UI control based on the setting type.
     *
     * @param type          The type of setting, determining the UI control to create.
     * @param settingValue  The initial value to set for the UI control.
     * @param title         The title of the setting, used for identifying the control.
     * @return The created and configured UI control.
     * @throws IllegalArgumentException if the setting type is invalid.
     */
    private static Control createInputControl(String type, String settingValue, String title) {
        // Create the appropriate UI control based on the setting type.
        switch (type) {
            case "text":
                // Create a text field for text settings
                TextField textField = new TextField(settingValue);
                // Add an event listener to save the setting value to the database when the text changes
                if (enableLiveUpdates) {
                    textField.textProperty().addListener((observable, oldValue, newValue) ->
                            settingsDAO.saveSetting(title, newValue));
                }
                return textField;
            case "number":
                // Create a spinner for number settings
                Spinner<Integer> spinner = new Spinner<>();
                // Set the spinner value factory to allow integer values
                SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(
                        Integer.MIN_VALUE, Integer.MAX_VALUE, Integer.parseInt(settingValue));
                spinner.setValueFactory(valueFactory);
                // Add an event listener to save the setting value to the database when the value changes
                if (enableLiveUpdates) {
                    spinner.valueProperty().addListener((observable, oldValue, newValue) ->
                            settingsDAO.saveSetting(title, String.valueOf(newValue)));
                }
                return spinner;
            case "time":
                // Create a ComboBox for time settings
                ComboBox<String> timeComboBox = createTimeComboBox(settingValue);
                // Add an event listener to save the setting value to the database when the value changes
                if (enableLiveUpdates) {
                    timeComboBox.valueProperty().addListener((observable, oldValue, newValue) ->
                            settingsDAO.saveSetting(title, newValue));
                }
                return timeComboBox;
            case "boolean":
                // Create a CheckBox for boolean settings
                CheckBox checkBox = new CheckBox();
                checkBox.setSelected(Boolean.parseBoolean(settingValue));
                // Add an event listener to save the setting value to the database when the value changes
                if (enableLiveUpdates) {
                    checkBox.selectedProperty().addListener((observable, oldValue, newValue) ->
                            settingsDAO.saveSetting(title, String.valueOf(newValue)));
                }
                return checkBox;
            case "choice":
                // Create a ComboBox for choice settings
                String[] choices = settingValue.split(",");
                ComboBox<String> choiceComboBox = new ComboBox<>(FXCollections.observableArrayList(choices));
                choiceComboBox.setValue(settingsDAO.getSetting(title, choices[0]));
                // Add an event listener to save the setting value to the database when the value changes
                if (enableLiveUpdates) {
                    choiceComboBox.valueProperty().addListener((observable, oldValue, newValue) ->
                            settingsDAO.saveSetting(title, newValue));
                }
                return choiceComboBox;
            default:
                // Throw an exception if the setting type is invalid, would not occur in production
                throw new IllegalArgumentException("Invalid setting type: " + type);
        }
    }

    /**
     * Retrieves the value of a setting from the UI control.
     *
     * @param title The title of the setting to retrieve the value for.
     * @return The current value of the setting, or null if the setting is not found.
     */
    public static String getSettingValue(String title) {
        // Retrieve the control for the setting title
        Control control = settingControls.get(title);
        // Return the value of the control if it exists
        if (control != null) {
            // Switch on the control type to retrieve the value
            switch (control.getClass().getSimpleName()) {
                case "TextField":
                    return ((TextField) control).getText();
                case "Spinner":
                    return ((Spinner<?>) control).getValue().toString();
                case "ComboBox":
                    // TODO: Fix this
//                    return ((ComboBox<?>) control).getValue();
                    // Temp return empty string
                    return "";
                case "CheckBox":
                    return String.valueOf(((CheckBox) control).isSelected());
            }
        }
        // Return null if the setting is not found
        return null;
    }

    /**
     * Save all settings to the database.
     */
    public static void saveSettings() {
        // Iterate over all setting controls and save their values to the database
        for (Map.Entry<String, Control> entry : settingControls.entrySet()) {
            String title = entry.getKey();
            String value = getSettingValue(title);
            settingsDAO.saveSetting(title, value);
        }
    }

    /**
     * Creates a ComboBox pre-populated with time options in 15-minute intervals.
     *
     * @param defaultTime The default time to select in the ComboBox (format: "HH:mm").
     * @return A ComboBox containing time options.
     */
    private static ComboBox<String> createTimeComboBox(String defaultTime) {
        // Create a ComboBox with time options in 15-minute intervals
        ComboBox<String> comboBox = new ComboBox<>();
        for (int hour = 0; hour < 24; hour++) {
            for (int minute = 0; minute < 60; minute += 15) {
                String time = String.format("%02d:%02d", hour, minute);
                comboBox.getItems().add(time);
            }
        }
        // Set the default value if provided
        comboBox.setValue(defaultTime != null ? defaultTime : "09:00");
        return comboBox;
    }
}