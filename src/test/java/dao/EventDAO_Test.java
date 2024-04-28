package dao;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

import com.calendarfx.model.Event;
import com.calendarfx.util.EventDAO;

// EventDAOTest class tests the EventDAO class
public class EventDAOTest {
    private EventDAO eventDAO = new EventDAO(); // Assuming you have a constructor for EventDAO
    private int eventId = 0; // Example event ID for testing

    private Event createTestEvent() {
        // Generate a unique title or use other attributes to ensure uniqueness
        String uniqueTitle = "Test Event " + System.currentTimeMillis();
        return new Event(uniqueTitle, "Test description", ...); // Fill in other event details
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
        eventId = eventDAO.createEvent(createTestEvent());
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
        eventId = eventDAO.createEvent(createTestEvent());

        // Get an event by ID and check if it is not null
        Event event = eventDAO.getEventById(eventId);
        assertNotNull(event.getId(), "Event should not be null");
    }

    @Test
    public void testEditEvent() {
        // Create an event for testing
        eventId = eventDAO.createEvent(createTestEvent());

        // Edit an event and check if successful
        event = eventDAO.getEventById(eventId);
        event.setTitle("New Title");
        event.setDescription("New Description");
        boolean success = eventDAO.editEvent(event);
        assertTrue(success, "Event should be edited successfully");

        // Check if the changes are reflected
        event = eventDAO.getEventById(eventId);
        assertEquals("New Title", event.getTitle(), "Title should be updated");
        assertEquals("New Description", event.getDescription(), "Description should be updated");
        // ADD MORE HERE (e.g., check other attributes)
    }

    @Test
    public void testDeleteEvent() {
        // Create an event for testing
        eventId = eventDAO.createEvent(createTestEvent());

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
        int eventID1 = eventDAO.createEvent(createTestEvent());
        int eventID2 = eventDAO.createEvent(createTestEvent());
        int eventID3 = eventDAO.createEvent(createTestEvent());

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
    public void testDeleteNonExistentEvent() {
        int nonExistentEventId = -1; // Or any ID that doesn't exist
        boolean success = eventDAO.deleteEvent(nonExistentEventId);
        assertFalse(success, "Deleting a non-existent event should fail");
    }
}