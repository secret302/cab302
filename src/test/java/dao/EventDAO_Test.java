package dao;

import com.serenitask.model.Goal;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

import com.serenitask.model.Event;
import com.serenitask.util.DatabaseManager.EventDAO;

// EventDAOTest class tests the EventDAO class
public class EventDAO_Test {
    private EventDAO eventDAO = new EventDAO(); // Assuming you have a constructor for EventDAO
    private int eventId = 0; // Example event ID for testing

    private Event createTestEvent() {
        // Generate a unique title or use other attributes to ensure uniqueness
        String uniqueTitle = "Test Event " + System.currentTimeMillis();
        return new Event(uniqueTitle, "Test description"); // Fill in other event details
    }

    @AfterEach
    public void tearDown() {
        // Delete the event after testing
        eventDAO.deleteEvent(eventId);
        eventId = 0;
    }

    @Test
    public void testCreateEvent() {
        // Create an event and check if the event ID is greater than 0
        eventId = eventDAO.addEvent(createTestEvent());
        assertTrue(eventId > 0, "Event ID should be greater than 0");

        // Verify details of the created event
        Event createdEvent = eventDAO.getEventById(eventId);
        assertNotNull(createdEvent.getId(), "Created event should not be null");
        assertEquals(eventId, createdEvent.getId(), "Event ID should match");
        // ADD MORE HERE
    }

    @Test
    public void testGetEventById() {
        // Create an event for testing
        eventId = eventDAO.addEvent(createTestEvent());

        // Get an event by ID and check if it is not null
        Event event = eventDAO.getEventById(eventId);
        assertNotNull(event.getId(), "Event should not be null");
    }

    @Test
    public void testUpdateEvent() {
        // Create an event for testing
        eventId = eventDAO.addEvent(createTestEvent());

        // Update an event and check if successful
        Event event = eventDAO.getEventById(eventId);
        event.setTitle("New Title");
        event.setDescription("New Description");
        boolean success = eventDAO.updateEvent(event);
        assertTrue(success, "Event should be updated successfully");

        // Check if the changes are reflected
        event = eventDAO.getEventById(eventId);
        assertEquals("New Title", event.getTitle(), "Title should be updated");
        assertEquals("New Description", event.getDescription(), "Description should be updated");
        // ADD MORE HERE (e.g., check other attributes)
    }

    @Test
    public void testDeleteEvent() {
        // Create an event for testing
        eventId = eventDAO.addEvent(createTestEvent());

        // Delete an event and check if successful
        boolean success = eventDAO.deleteEvent(eventId);
        assertTrue(success, "Event should be deleted successfully");

        // Check if the event is deleted
        Event event = eventDAO.getEventById(eventId);
        assertNull(event.getId(), "Event should not exist");
    }

    @Test
    public void testGetEvents() {
        // Create more events for testing
        int eventID1 = eventDAO.addEvent(createTestEvent());
        int eventID2 = eventDAO.addEvent(createTestEvent());
        int eventID3 = eventDAO.addEvent(createTestEvent());

        // Get all events at date and check if the list is equal to 2
        List<Event> events = eventDAO.getEvents(date);
        assertEquals(2, events.size(), "List should contain 2 events");

        // Get all events between two dates and check if the list is equal to 2
        events = eventDAO.getEvents(startDate, endDate);
        assertEquals(2, events.size(), "List should contain 2 events");

        // Get all events and check if the list is equal to 3
        events = eventDAO.getEvents();
        assertEquals(3, events.size(), "List should contain 3 events");

        // Delete the events after testing
        eventDAO.deleteEvent(eventID1);
        eventDAO.deleteEvent(eventID2);
        eventDAO.deleteEvent(eventID3);
    }

    @Test
    public void testAddInvalidEvent() {
        // Create an invalid event and check if the goal ID is 0
        Event invalidEvent = new Event("", "Test description");
        boolean success = eventDAO.addEvent(invalidEvent);
        assertFalse(success, "Event creation should fail with invalid title");

        // Create two identical events and check if the event IDs are different
        Event event1 = createTestEvent();
        Event event2 = createTestEvent();
        int eventID1 = eventDAO.addEvent(event1);
        int eventID2 = eventDAO.addEvent(event2);
        assertNotEquals(eventID1, eventID2, "Event IDs should be different");
    }

    @Test
    public void testUpdateInvalidEvent() {
        // Create an event and try an invalid update
        eventId = eventDAO.addEvent(createTestEvent());
        Event event = eventDAO.getEventById(eventId);
        event.setTitle(""); // Invalid title
        boolean success = eventDAO.updateEvent(event);
        assertFalse(success, "Updating an invalid event should fail");
    }

    @Test
    public void testDeleteNonExistentGoal() {
        // Try to delete a non-existent event
        boolean success = eventDAO.deleteEvent(-1);
        assertFalse(success, "Deleting a non-existent event should fail");
    }
}