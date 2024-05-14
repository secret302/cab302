module com.calendarfx.app {
    requires transitive javafx.graphics;
    requires fr.brouillard.oss.cssfx;
    requires javafx.controls;
    requires com.calendarfx.view;
    requires java.sql;
    requires java.desktop;

    exports com.serenitask.app;
    exports com.serenitask.controller;
    exports com.serenitask.model;
    exports com.serenitask.ui;
    exports com.serenitask.util.DatabaseManager;
    exports com.serenitask.util.routine;
}