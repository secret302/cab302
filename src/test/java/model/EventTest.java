package model;

import com.calendarfx.model.Interval;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.serenitask.model.Event;
import java.time.LocalDateTime;

/* Methods:
    getId()
    setId(String id)
    getTitle()
    setTitle(String title)
    getLocation()
    setLocation(String location)
    getInterval()
    setInterval(Interval interval)
    getFullDay()
    setFullDay(boolean fullDay)
    getStaticPos()
    setStaticPos(boolean staticPos)
    getCalendar()
    setCalendar(String calendar)
    getRecurrenceRules() - TO BE REMOVED?
    setRecurrenceRules(String recurrenceRules) - TO BE REMOVED?
    getAllocatedUntil() - TO BE REMOVED?
    setAllocatedUntil(LocalDate allocatedUntil) - TO BE REMOVED?
 */

/* Test Cases:
    testEventConstructor()
    testEventSetters()
 */

// EventTest class tests the Event class
public class EventTest {
    // LocalDateTime for testing
    private final LocalDateTime testTime = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);

    /**
     * Create a test event with a specific start time
     * @param startTime LocalDateTime object
     * @return Event object
     */
    private Event createTestEvent(LocalDateTime startTime) {
        // Create entry for the event and return it
        return new Event(
                "Test ID",
                "Test Event",
                "Test location",
                new Interval(startTime, startTime.plusHours(2)),
                false,
                false,
                "testing",
                "",
                startTime.toLocalDate()
        );
    }

    @Test // Test the getters of the Event class
    public void testEventConstructor() {
        // Test the constructor of the Event class
        Event event = createTestEvent(testTime);

        // Verify the values
        assertEquals("Test ID", event.getId(), "Event ID should match");
        assertEquals("Test Event", event.getTitle(), "Event title should match");
        assertEquals("Test location", event.getLocation(), "Event location should match");
        assertEquals(new Interval(testTime, testTime.plusHours(2)).toString(), event.getInterval().toString(), "Event interval should match");
        assertFalse(event.getFullDay(), "Event should not be full day");
        assertFalse(event.getStaticPos(), "Event should not have static position");
        assertEquals("testing", event.getCalendar(), "Event calendar should match");

        // Test the constructor with invalid values
        assertThrows(IllegalArgumentException.class, () -> {
            new Event(
                    "Test ID",
                    "", // empty title
                    "Test location",
                    new Interval(testTime, testTime.plusHours(2)),
                    false,
                    false,
                    "testing",
                    "",
                    testTime.toLocalDate()
            );
        }, "Title cannot be empty");

        // Test the constructor with null values
        assertThrows(IllegalArgumentException.class, () -> {
            new Event(
                    null, // null ID
                    "Test Event",
                    "Test location",
                    new Interval(testTime, testTime.plusHours(2)),
                    false,
                    false,
                    "testing",
                    "",
                    testTime.toLocalDate()
            );
        }, "ID cannot be null");

        // Test the empty constructor of the Event class
        Event emptyEvent = new Event();
        assertNull(emptyEvent.getId(), "Event ID should be null");
        assertNull(emptyEvent.getTitle(), "Event title should be null");
        assertNull(emptyEvent.getLocation(), "Event location should be null");
        assertNull(emptyEvent.getInterval(), "Event interval should be null");
        assertNull(emptyEvent.getFullDay(), "Event full day should be null");
        assertNull(emptyEvent.getStaticPos(), "Event static position should be null");
        assertNull(emptyEvent.getCalendar(), "Event calendar should be null");
    }

    @Test
    public void testEventSetters() {
        // Test the setters of the Event class
        Event event = createTestEvent(testTime);

        // Set new values
        event.setId("New ID");
        event.setTitle("New Event");
        event.setLocation("New location");
        event.setInterval(new Interval(testTime.plusHours(1), testTime.plusHours(3)));
        event.setFullDay(true);
        event.setStaticPos(true);
        event.setCalendar("new calendar");

        // Verify the new values
        assertEquals("New ID", event.getId(), "Event ID should match");
        assertEquals("New Event", event.getTitle(), "Event title should match");
        assertEquals("New location", event.getLocation(), "Event location should match");
        assertEquals(new Interval(testTime.plusHours(1), testTime.plusHours(3)).toString(), event.getInterval().toString(), "Event interval should match");
        assertTrue(event.getFullDay(), "Event should be full day");
        assertTrue(event.getStaticPos(), "Event should have static position");
        assertEquals("new calendar", event.getCalendar(), "Event calendar should match");

        // Test the setters with invalid values
        assertThrows(IllegalArgumentException.class, () -> {
            event.setTitle(""); // empty title
        }, "Title cannot be empty");
        assertThrows(IllegalArgumentException.class, () -> {
            event.setId(""); // empty title
        }, "ID cannot be empty");

        // Test the setters with null values
        assertThrows(IllegalArgumentException.class, () -> {
            event.setTitle(null); // null title
        }, "Title cannot be null");
        assertThrows(IllegalArgumentException.class, () -> {
            event.setId(null); // null ID
        }, "ID cannot be null");
    }
}
