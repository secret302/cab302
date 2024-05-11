package com.serenitask.model;


import java.time.LocalDateTime;
import java.time.LocalTime;

public class TimeWindow {


    private LocalTime windowOpen;

    private LocalTime windowClose;

    public TimeWindow(LocalTime open, LocalTime close) {
        windowOpen = open;
        windowClose = close;
    }

    public LocalTime getWindowOpen() {
        return windowOpen;
    }

    public void setWindowOpen(LocalTime windowOpen) {
        this.windowOpen = windowOpen;
    }

    public LocalTime getWindowClose() {
        return windowClose;
    }

    public void setWindowClose(LocalTime windowClose) {
        this.windowClose = windowClose;
    }

}
