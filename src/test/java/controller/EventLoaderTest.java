package controller;

import com.calendarfx.model.CalendarSource;
import com.calendarfx.model.Entry;
import com.calendarfx.model.Interval;
import com.serenitask.model.Event;
import com.serenitask.util.DatabaseManager.EventDAO;
import com.serenitask.controller.EventLoader;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDateTime;
import java.util.List;

/* Methods:
    loadEventsFromDatabase()
 */

/* Test Cases:
    testLoadEventsFromDatabase()
 */

public class EventLoaderTest {
    // EventLoader object
    private final EventLoader eventLoader = new EventLoader();
    // EventDAO object
    private final EventDAO eventDAO = new EventDAO();
    // LocalDateTime for testing
    private final LocalDateTime testTime = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);

    /**
     * Create a test event with a specific start time
     * @param startTime LocalDateTime object
     * @return Event object
     */
    private Event createTestEvent(LocalDateTime startTime) {
        // Create entry for the event
        Entry<?> newEntry = new Entry<>();
        String entryID = newEntry.getId();

        // Create new event and return it
        return new Event(
                entryID,
                "Test Event",
                "Test location",
                new Interval(startTime, startTime.plusHours(2)),
                false,
                false,
                "Personal Events"
        );
    }

    @Test
    public void testLoadEventsFromDatabase() {
        // Create new Events
        String eventId1 = eventDAO.addEvent(createTestEvent(testTime));
        String eventId2 = eventDAO.addEvent(createTestEvent(testTime.plusHours(1)));
        String eventId3 = eventDAO.addEvent(createTestEvent(testTime.plusHours(2)));

        // Load events from database
        CalendarSource result = eventLoader.loadEventsFromDatabase();
        // Verify that createNew method is called
        assertNotNull(result, "CalendarSource should not be null");

        // Verify that the calendar source contains the correct number of calendars
        assertEquals(3, result.getCalendars().size(), "CalendarSource should contain 3 calendars");

        // Get entry results from the calendars
        List events = result.getCalendars().get(0).findEntries("Test");

        // Verify that the correct number of events are loaded
        assertEquals(3, events.size(), "Calendar should contain 3 events");
        // Verify the components of the first event
        assertSame(Entry.class, events.get(0).getClass(), "Event should be an instance of Entry");
        assertEquals("Test Event", ((Entry<?>) events.get(0)).getTitle(), "Event title should match");
        assertEquals("Test location", ((Entry<?>) events.get(0)).getLocation(), "Event location should match");
        assertEquals(testTime.toLocalDate(), ((Entry<?>) events.get(0)).getInterval().getStartDate(), "Event start date should match");
        assertEquals(testTime.plusHours(2).toLocalDate(), ((Entry<?>) events.get(0)).getInterval().getEndDate(), "Event end date should match");
        assertEquals("Personal Events", ((Entry<?>) events.get(0)).getCalendar().getName(), "Event calendar should match");

        // Delete the events after testing
        eventDAO.deleteEvent(eventId1);
        eventDAO.deleteEvent(eventId2);
        eventDAO.deleteEvent(eventId3);
    }
}
