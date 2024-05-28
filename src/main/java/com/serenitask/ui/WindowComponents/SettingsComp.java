package com.serenitask.ui.WindowComponents;

import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import static com.serenitask.controller.SettingsController.addSetting;
import static com.serenitask.controller.SettingsController.saveSettings;

/**
 * Provides a user interface component for settings.
 * This class is responsible for rendering a pop-up window where users can enter settings.
 */

public class SettingsComp {
    /**
     * Displays a modal window for settings, allowing the user to input settings.
     * The window contains form inputs for the day start, end, allocate ahead, select date, and select calendar.
     */

    public static void displaySettingsView() {
        Stage popOutStage = new Stage();
        popOutStage.initModality(Modality.APPLICATION_MODAL);
        popOutStage.setTitle("Settings");

        VBox layout = new VBox(20);
        // TODO: Add padding to the layout

        // Add application settings to the dialog
        addSetting("Day Start", "time", "09:00", layout);
        addSetting("Day End", "time", "18:00", layout);
        addSetting("Allocate Ahead (Days)", "number", "7", layout);
        addSetting("Selected Calendar", "choice", "Personal,Work,Goals", layout);
        addSetting("Enable Notifications", "boolean", "true", layout);

        // Buttons for saving and discarding changes.
        Button saveButton = new Button("Save");
        saveButton.setStyle("-fx-background-color: lightgreen; -fx-font-size: 20");
        saveButton.setOnAction(e -> {
            saveSettings(); // Save the settings to the database
            popOutStage.close();
        });

        Button backButton = new Button("Back");
        backButton.setStyle("-fx-background-color: #ff5f56; -fx-font-size: 20");
        backButton.setOnAction(e -> popOutStage.close());

        HBox buttonBox = new HBox(10);
        buttonBox.getChildren().addAll(backButton, saveButton);
        buttonBox.setAlignment(Pos.CENTER);
        layout.getChildren().add(buttonBox);

        Scene popOutScene = new Scene(layout, 500, 550);
        popOutStage.setScene(popOutScene);
        popOutStage.showAndWait();
    }
}
