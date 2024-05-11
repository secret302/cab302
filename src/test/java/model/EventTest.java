package model;

import com.calendarfx.model.Interval;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import com.serenitask.model.Event;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class EventTest {
    public Event createTestEvent(LocalDateTime startTime) {
        // Create entry for the event and return it
        return new Event(
                "Test ID",
                "Test Event",
                "Test location",
                new Interval(startTime, startTime.plusHours(2)), // Will this cause issues near 11:59 PM?
                false,
                false,
                "default",
                "",
                startTime.toLocalDate()
        );
        // Events will be cleaned up by the garbage collector
    }

    @Test
    public void testEventConstructor() {
        // Set LocalDateTime
        LocalDateTime startTime = LocalDateTime.now();
        // Test the constructor of the Event class
        Event event = createTestEvent(startTime);
        assertNotNull(event, "Event should not be null");
        assertEquals("Test Event", event.getTitle(), "Event title should match");
        assertEquals("Test location", event.getLocation(), "Event location should match");
        assertEquals(startTime, event.getInterval().getStartDateTime(), "Event start time should match");
        assertFalse(event.getFullDay(), "Event should not be full day");
        assertFalse(event.getStaticPos(), "Event should not have static position");
        assertEquals("default", event.getCalendar(), "Event calendar should match");
        assertEquals("", event.getRecurrenceRules(), "Event recurrence rules should match");
        assertEquals(startTime.toLocalDate(), event.getAllocatedUntil(), "Event allocated until should match");
    }

    @Test
    public void testEventSetters() {
        // Set LocalDateTime
        LocalDateTime startTime = LocalDateTime.now();
        // Test the setters of the Event class
        Event event = createTestEvent(startTime);
        event.setTitle("New Title");
        event.setLocation("New Location");
        event.setInterval(new Interval(startTime, startTime.plusHours(3)));
        event.setFullDay(true);
        event.setStaticPos(true);
        event.setCalendar("New Calendar");
        event.setRecurrenceRules("New Recurrence Rules");
        event.setAllocatedUntil(startTime.toLocalDate().plusDays(1));

        // Verify the changes
        assertEquals("New Title", event.getTitle(), "Event title should match");
        assertEquals("New Location", event.getLocation(), "Event location should match");
        assertEquals(startTime, event.getInterval().getStartDateTime(), "Event start time should match");
        assertTrue(event.getFullDay(), "Event should be full day");
        assertTrue(event.getStaticPos(), "Event should have static position");
        assertEquals("New Calendar", event.getCalendar(), "Event calendar should match");
        assertEquals("New Recurrence Rules", event.getRecurrenceRules(), "Event recurrence rules should match");
        assertEquals(startTime.toLocalDate().plusDays(1), event.getAllocatedUntil(), "Event allocated until should match");
    }

    @Test
    public void testInvalidEventConstructor() {
        // Test recurrence_rules and date_time
        // To be implemented
    }
}
