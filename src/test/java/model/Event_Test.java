package model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import com.serenitask.model.Event;

public class Event_Test {
    @Test
    public void testEventConstructor() {
        // Test the constructor of the Event class
        Event event = new Event("Test Event", "Test description"); // Add other attributes
        assertNotNull(event, "Event should not be null");
        assertEquals("Test Event", event.getTitle(), "Event title should match");
        assertEquals("Test description", event.getDescription(), "Event description should match");
    }

    @Test
    public void testEventSetters() {
        // Test the setters of the Event class
        Event event = new Event("Test Event", "Test description"); // Add other attributes
        event.setTitle("New Title");
        event.setDescription("New Description");
        assertEquals("New Title", event.getTitle(), "Event title should match");
        assertEquals("New Description", event.getDescription(), "Event description should match");
    }

    @Test
    public void testInvalidEventConstructor() {
        // Test recurrence_rules and date_time
        // To be implemented
    }
}
