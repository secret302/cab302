package com.serenitask.model;

import java.time.LocalTime;

/**
 * Class representing a window of time. A window of time has an open and a close time.
 */
public class TimeWindow {

    private LocalTime windowOpen;
    private LocalTime windowClose;

    /**
     * Constructor for TimeWindow. Takes in a LocalTime object for open and close and sets the fields.
     *
     * @param open  LocalTime Object representing the start time of window
     * @param close LocalTime Object representing the end time of window
     */
    public TimeWindow(LocalTime open, LocalTime close) {
        windowOpen = open;
        windowClose = close;
    }

    /**
     * Getter for WindowOpen Parameter
     *
     * @return LocalTime object representing the start time of the window
     */
    public LocalTime getWindowOpen() {
        return windowOpen;
    }

    /**
     * Setter for WindowOpen parameter
     *
     * @param windowOpen LocalTime object used to set WindowOpen
     */
    public void setWindowOpen(LocalTime windowOpen) {
        this.windowOpen = windowOpen;
    }

    /**
     * Getter for WindowClose Parameter
     *
     * @return LocalTime object representing the end time of the window
     */
    public LocalTime getWindowClose() {
        return windowClose;
    }

    /**
     * Setter for WindowClose Parameter
     *
     * @param windowClose LocalTime object used to set WindowOpen
     */
    public void setWindowClose(LocalTime windowClose) {
        this.windowClose = windowClose;
    }

}
