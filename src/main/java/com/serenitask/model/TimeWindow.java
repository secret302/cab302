package com.serenitask.model;

import java.time.LocalTime;

/**
 * Represents a window of time defined by its opening and closing times.
 * Ensures that the opening time is always before the closing time.
 */
public class TimeWindow {

    /** The time at which the window opens. */
    private LocalTime windowOpen;
    /** The time at which the window closes. */
    private LocalTime windowClose;

    /**
     * Constructor to create a TimeWindow object.
     *
     * @param open  The opening time of the window.
     * @param close The closing time of the window.
     * @throws IllegalArgumentException If the opening time is not before the closing time.
     */
    public TimeWindow(LocalTime open, LocalTime close) {
        // Validate that the opening time is before the closing time
        if (open.isAfter(close)) {
            throw new IllegalArgumentException("The Open time cannot appear after Close time");
        }
        // Set the opening and closing times
        this.windowOpen = open;
        this.windowClose = close;
    }

    /**
     * Gets the opening time of the window.
     * @return The time at which the window opens.
     */
    public LocalTime getWindowOpen() {
        return windowOpen;
    }

    /**
     * Sets the opening time of the window.
     * @param windowOpen The new opening time for the window.
     */
    public void setWindowOpen(LocalTime windowOpen) {
        this.windowOpen = windowOpen;
    }

    /**
     * Gets the closing time of the window.
     * @return The time at which the window closes.
     */
    public LocalTime getWindowClose() {
        return windowClose;
    }

    /**
     * Sets the closing time of the window.
     * @param windowClose The new closing time for the window.
     */
    public void setWindowClose(LocalTime windowClose) {
        this.windowClose = windowClose;
    }
}