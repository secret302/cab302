package dao;

import com.calendarfx.model.Interval;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDateTime;
import java.util.List;
import com.calendarfx.model.Entry;
import com.serenitask.model.Event;
import com.serenitask.util.DatabaseManager.EventDAO;

/* Methods:
    addSampleEntries() - PRIVATE
    reloadInterval() - PRIVATE
    createTable() - PRIVATE
    addEvent(Event event)
    updateEvent(Event event)
    deleteEvent(String eventID)
    getEventById(String eventID)
    getAllEvents()
    getEvents(LocalDate date) - NOT IMPLEMENTED?
    getEvents(LocalDate startDate, LocalDate endDate) - NOT IMPLEMENTED?
 */

/* Test Cases:
    testAddEvent()
    testUpdateEvent()
    testDeleteEvent()
    testGetEventById()
    testGetAllEvents()
    testGetEventsSingleDate() - DISABLED UNTIL IMPLEMENTED?
    testGetEventsDateRange() - DISABLED UNTIL IMPLEMENTED?
 */

// EventDAOTest class tests the EventDAO class
public class EventDAOTest {
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
                "testing",
                "",
                startTime.toLocalDate()
        );
    }

    @Test // Test the addEvent method
    public void testAddEvent() {
        // Create test event
        String eventId = eventDAO.addEvent(createTestEvent(testTime));
        // Check if the event id is not null
        assertNotNull(eventId, "Event ID should not be null");

        // Retrieve the event by ID and check if it is not null
        Event event = eventDAO.getEventById(eventId);
        assertNotNull(event, "Event should not be null");

        // Verify the components of the event
        assertEquals("Test Event", event.getTitle(), "Event title should match");
        assertEquals("Test location", event.getLocation(), "Event location should match");
        assertEquals(new Interval(testTime, testTime.plusHours(2)).toString(), event.getInterval().toString(), "Event interval should match");
        assertEquals(false, event.getFullDay(), "Event full day should match");
        assertEquals(false, event.getStaticPos(), "Event static position should match");
        assertEquals("testing", event.getCalendar(), "Event calendar should match");
        // recurrenceRules to be removed
        // allocatedUntil to be removed

        // Delete the event after testing
        eventDAO.deleteEvent(eventId);

        // Try to add a null event
        eventId = eventDAO.addEvent(null);
        assertNull(eventId, "Adding a null event should fail");
    }

    @Test // Test the updateEvent method
    public void testUpdateEvent() {
        // Create test event
        String eventId = eventDAO.addEvent(createTestEvent(testTime));
        // Retrieve the event by ID
        Event event = eventDAO.getEventById(eventId);

        // Update the event
        event.setTitle("Updated Event");
        event.setLocation("Updated location");
        event.setInterval(new Interval(testTime.plusHours(1), testTime.plusHours(3)));
        event.setFullDay(true);
        event.setStaticPos(true);
        event.setCalendar("updated");
        // recurrenceRules to be removed
        // allocatedUntil to be removed

        // Update the event
        boolean success = eventDAO.updateEvent(event);
        assertTrue(success, "Event should be updated successfully");

        // Retrieve the event by ID
        event = eventDAO.getEventById(eventId);

        // Verify the components of the event
        assertEquals("Updated Event", event.getTitle(), "Event title should match");
        assertEquals("Updated location", event.getLocation(), "Event location should match");
        assertEquals(new Interval(testTime.plusHours(1), testTime.plusHours(3)).toString(), event.getInterval().toString(), "Event interval should match");
        assertEquals(true, event.getFullDay(), "Event full day should match");
        assertEquals(true, event.getStaticPos(), "Event static position should match");
        assertEquals("updated", event.getCalendar(), "Event calendar should match");
        // recurrenceRules to be removed
        // allocatedUntil to be removed

        // Delete the event after testing
        eventDAO.deleteEvent(eventId);

        // Try to update a null event
        success = eventDAO.updateEvent(null);
        assertFalse(success, "Updating a null event should fail");
    }

    @Test // Test the deleteEvent method
    public void testDeleteEvent() {
        // Create test event
        String eventId = eventDAO.addEvent(createTestEvent(testTime));

        // Delete the event
        boolean success = eventDAO.deleteEvent(eventId);
        assertTrue(success, "Event should be deleted successfully");

        // Confirm the event is deleted
        Event event = eventDAO.getEventById(eventId);
        assertNull(event, "Event should not exist");

        // Try to delete the event again
        success = eventDAO.deleteEvent(eventId);
        assertFalse(success, "Deleting a non-existent event should fail");

        // Try to delete a non-existent event
        success = eventDAO.deleteEvent("invalidEventID");
        assertFalse(success, "Deleting a non-existent event should fail");

        // Try to delete a null event
        success = eventDAO.deleteEvent(null);
        assertFalse(success, "Deleting a null event should fail");
    }

    @Test // Test the getEventById method
    public void testGetEventById() {
        // Create test events
        String eventId1 = eventDAO.addEvent(createTestEvent(testTime));
        String eventId2 = eventDAO.addEvent(createTestEvent(testTime.plusHours(1)));

        // Try to get the events by ID
        Event event1 = eventDAO.getEventById(eventId1);
        assertNotNull(event1, "Event 1 should not be null");
        Event event2 = eventDAO.getEventById(eventId2);
        assertNotNull(event2, "Event 2 should not be null");

        // Confirm the event IDs do not match
        assertNotEquals(eventId1, eventId2, "Event IDs should not match");

        // Confirm the event IDs match their respective events
        assertEquals(eventId1, event1.getId(), "Event 2 ID should match");
        assertEquals(eventId2, event2.getId(), "Event 2 ID should match");

        // Delete the events after testing
        eventDAO.deleteEvent(eventId1);
        eventDAO.deleteEvent(eventId2);

        // Try to get a non-existent event
        Event event = eventDAO.getEventById("invalidEventID");
        assertNull(event, "Event should not exist");

        // Try to get a null event (An ID of null should not exist in the database)
        event = eventDAO.getEventById(null);
        assertNull(event, "Event should not exist");
    }

    @Test // Test the getAllEvents method
    public void testGetAllEvents() {
        // Create test events
        String eventId1 = eventDAO.addEvent(createTestEvent(testTime));
        String eventId2 = eventDAO.addEvent(createTestEvent(testTime.plusHours(2)));
        String eventId3 = eventDAO.addEvent(createTestEvent(testTime.plusHours(42)));

        // Get all events
        List<Event> events = eventDAO.getAllEvents();
        assertEquals(3, events.size(), "List should contain 3 events");

        // Delete the events after testing
        eventDAO.deleteEvent(eventId1);
        eventDAO.deleteEvent(eventId2);
        eventDAO.deleteEvent(eventId3);
    }

    /* DISABLED UNTIL IMPLEMENTED
    @Test // Test the getEvents method with a single date
    public void testGetEventsSingleDate() {
        // Create test events
        String eventId1 = eventDAO.addEvent(createTestEvent(testTime));
        String eventId2 = eventDAO.addEvent(createTestEvent(testTime.plusHours(2)));
        String eventId3 = eventDAO.addEvent(createTestEvent(testTime.plusHours(42)));

        // Get events for the test date
        List<Event> events = eventDAO.getEvents(testTime.toLocalDate());
        assertEquals(2, events.size(), "List should contain 2 events");

        // Check correct events are returned
        assertEquals(eventId1, events.get(0).getId(), "Event ID should match");
        assertEquals(eventId2, events.get(1).getId(), "Event ID should match");

        // Get event for 42 hours later
        events = eventDAO.getEvents(testTime.plusHours(42).toLocalDate());
        assertEquals(1, events.size(), "List should contain 1 event");
        assertEquals(eventId3, events.get(0).getId(), "Event ID should match");

        // Delete the events after testing
        eventDAO.deleteEvent(eventId1);
        eventDAO.deleteEvent(eventId2);
        eventDAO.deleteEvent(eventId3);

        // Get events for a non-existent date
        events = eventDAO.getEvents(LocalDate.of(1970, 1, 1));
        assertEquals(0, events.size(), "List should be empty");

        // Get events for a null date
        events = eventDAO.getEvents(null);
        assertEquals(0, events.size(), "List should be empty");
    }
     */

    /* DISABLED UNTIL IMPLEMENTED
    @Test // Test the getEvents method with a date range
    public void testGetEventsDateRange() {
        // Create test events
        String eventId1 = eventDAO.addEvent(createTestEvent(testTime));
        String eventId2 = eventDAO.addEvent(createTestEvent(testTime.plusHours(2)));
        String eventId3 = eventDAO.addEvent(createTestEvent(testTime.plusHours(42)));

        // Get events for the test date range
        List<Event> events = eventDAO.getEvents(testTime.toLocalDate(), testTime.plusHours(2).toLocalDate());
        assertEquals(2, events.size(), "List should contain 2 events");

        // Check correct events are returned
        assertEquals(eventId1, events.get(0).getId(), "Event ID should match");
        assertEquals(eventId2, events.get(1).getId(), "Event ID should match");

        // Get events for the test date range
        events = eventDAO.getEvents(testTime.plusHours(2).toLocalDate(), testTime.plusHours(42).toLocalDate());
        assertEquals(2, events.size(), "List should contain 2 events");

        // Check correct events are returned
        assertEquals(eventId2, events.get(0).getId(), "Event ID should match");
        assertEquals(eventId3, events.get(1).getId(), "Event ID should match");

        // Get events for the test date range
        events = eventDAO.getEvents(testTime.toLocalDate(), testTime.plusHours(42).toLocalDate());
        assertEquals(3, events.size(), "List should contain 3 events");

        // Check correct events are returned
        assertEquals(eventId1, events.get(0).getId(), "Event ID should match");
        assertEquals(eventId2, events.get(1).getId(), "Event ID should match");
        assertEquals(eventId3, events.get(2).getId(), "Event ID should match");

        // Delete the events after testing
        eventDAO.deleteEvent(eventId1);
        eventDAO.deleteEvent(eventId2);
        eventDAO.deleteEvent(eventId3);

        // Get events for a non-existent date range
        events = eventDAO.getEvents(LocalDate.of(1970, 1, 1), LocalDate.of(1970, 1, 2));
        assertEquals(0, events.size(), "List should be empty");

        // Get events for a null date range
        events = eventDAO.getEvents(null, null);
        assertEquals(0, events.size(), "List should be empty");
    }
     */
}