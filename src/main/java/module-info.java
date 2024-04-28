module com.calendarfx.app {
    requires transitive javafx.graphics;
    requires fr.brouillard.oss.cssfx;
    requires javafx.controls;
    requires com.calendarfx.view;
    requires java.sql;

    exports com.calendarfx.app;
}