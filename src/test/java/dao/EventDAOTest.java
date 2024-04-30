package dao;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

import com.calendarfx.model.Entry;

import com.serenitask.model.Event;
import com.serenitask.util.DatabaseManager.EventDAO;
import org.mockito.internal.matchers.Null;

// EventDAOTest class tests the EventDAO class
public class EventDAOTest {
    private final EventDAO eventDAO = new EventDAO(); // Assuming you have a constructor for EventDAO
    private String eventId; // Example event ID for testing

    private Event createTestEvent() {
        // Create entry for the event
        Entry<?> newEntry = new Entry<>();
        String entryID = newEntry.getId();

        // Create new event and return it
        return new Event(
                entryID,
                "Test Event",
                "Test description",
                "Test location",
                "Test start_time",
                500,
                false,
                false,
                "default",
                "",
                ""
        );
    }

    @AfterEach
    public void tearDown() {
        // Delete the event after testing
        eventDAO.deleteEvent(eventId);
        eventId = "";
    }

    @Test
    public void testCreateEvent() {
        // Create an event and check if the event ID exists
        eventId = eventDAO.addEvent(createTestEvent());
        assertNotNull(eventId, "Event ID should not be null");

        // Verify details of the created event
        Event createdEvent = eventDAO.getEventById(eventId);
        assertNotNull(createdEvent, "Created event should not be null");
        assertEquals(eventId, createdEvent.getId(), "Event ID should match");

        // To be implemented, check other attributes
    }

    @Test
    public void testGetEventById() {
        // Create an event for testing
        eventId = eventDAO.addEvent(createTestEvent());
        assertNotNull(eventId, "Event ID should not be null");

        // Get an event by ID and check if it is not null
        Event event = eventDAO.getEventById(eventId);
        assertNotNull(event, "Event should not be null");
    }

    @Test
    public void testUpdateEvent() {
        // Create an event for testing
        eventId = eventDAO.addEvent(createTestEvent());
        assertNotNull(eventId, "Event ID should not be null");

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

        // To be implemented, check other attributes
    }

    @Test
    public void testDeleteEvent() {
        // Create an event for testing
        eventId = eventDAO.addEvent(createTestEvent());
        assertNotNull(eventId, "Event ID should not be null");

        // Delete an event and check if successful
        boolean success = eventDAO.deleteEvent(eventId);
        assertTrue(success, "Event should be deleted successfully");

        // Check if the event is deleted
        Event event = eventDAO.getEventById(eventId);
        assertNull(event, "Event should not exist");
    }

    @Test
    public void testGetEvents() {
        // Create more events for testing
        String eventID1 = eventDAO.addEvent(createTestEvent());
        String eventID2 = eventDAO.addEvent(createTestEvent());
        String eventID3 = eventDAO.addEvent(createTestEvent());

        // Get all events at date and check if the list is equal to 2
        List<Event> events = eventDAO.getEvents(date);
        assertEquals(2, events.size(), "List should contain 2 events (1&2)");
        // Check correct events are returned (1&2)
        assertEquals(eventID1, events.get(0).getId(), "Event ID should match: 1");
        assertEquals(eventID2, events.get(1).getId(), "Event ID should match: 2");

        // Get all events between two dates and check if the list is equal to 2
        events = eventDAO.getEvents(startDate, endDate);
        assertEquals(2, events.size(), "List should contain 2 events (2&3)");
        // Check correct events are returned (2&3)
        assertEquals(eventID2, events.get(0).getId(), "Event ID should match: 2");
        assertEquals(eventID3, events.get(1).getId(), "Event ID should match: 3");

        // Get all events and check if the list is equal to 3
        events = eventDAO.getEvents();
        assertEquals(3, events.size(), "List should contain 3 events (1&2&3)");
        // Check correct events are returned (1&2&3)
        assertEquals(eventID1, events.get(0).getId(), "Event ID should match: 1");
        assertEquals(eventID2, events.get(1).getId(), "Event ID should match: 2");
        assertEquals(eventID3, events.get(2).getId(), "Event ID should match: 3");

        // Delete the events after testing
        eventDAO.deleteEvent(eventID1);
        eventDAO.deleteEvent(eventID2);
        eventDAO.deleteEvent(eventID3);
        // Confirm deletion
        assertNull(eventDAO.getEventById(eventID1), "Event 1 should not exist");
        assertNull(eventDAO.getEventById(eventID2), "Event 2 should not exist");
        assertNull(eventDAO.getEventById(eventID3), "Event 3 should not exist");
    }

    @Test
    public void testUniqueEventID() {
        // Create two identical events
        Event event1 = createTestEvent();
        Event event2 = createTestEvent();
        String eventID1 = eventDAO.addEvent(event1);
        String eventID2 = eventDAO.addEvent(event2);
        // Check if the events exist
        assertNotNull(eventID1, "Event ID 1 should not be null");
        assertNotNull(eventID2, "Event ID 2 should not be null");

        // Check if the event IDs are different
        assertNotEquals(eventID1, eventID2, "Event IDs should be different");

        // Delete the events after testing
        eventDAO.deleteEvent(eventID1);
        eventDAO.deleteEvent(eventID2);
        // Confirm deletion
        assertNull(eventDAO.getEventById(eventID1), "Event 1 should not exist");
        assertNull(eventDAO.getEventById(eventID2), "Event 2 should not exist");
    }

    @Test
    public void testAddInvalidEvent() {
        // Create an invalid event and check if the goal ID is 0
        Event invalidEvent = new Event(
                "",
                "Test title",
                "Test description",
                "Test location",
                "Test start_time",
                500,
                false,
                false,
                "",
                "",
                ""
        );
        String invalidEventEntry = eventDAO.addEvent(invalidEvent);
        assertNull(invalidEventEntry, "Adding an invalid event (Null) should return null");

        // To be implemented, check other invalid attributes
    }

    @Test
    public void testUpdateInvalidEvent() {
        // Create an event
        eventId = eventDAO.addEvent(createTestEvent());
        // Confirm event exists
        assertNotNull(eventId, "EventId should not be null");

        // Get event class
        Event event = eventDAO.getEventById(eventId);
        // Confirm event is not null
        assertNotNull(event, "Event should not be null");

        // Update the event with invalid title
        event.setTitle(""); // Invalid title
        boolean success = eventDAO.updateEvent(event);
        assertFalse(success, "Updating an invalid event should fail");

        // To be implemented, check other update attributes
    }

    @Test
    public void testDeleteNonExistentGoal() {
        // Try to delete a non-existent event
        boolean success = eventDAO.deleteEvent("");
        assertFalse(success, "Deleting a non-existent event should fail");
    }
}