package controller;

import com.calendarfx.model.Calendar;
import com.calendarfx.model.Entry;
import com.calendarfx.model.Interval;
import com.serenitask.controller.EventListener;
import com.serenitask.util.DatabaseManager.EventDAO;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/* Methods:
    * setUpListeners()
 */

/* Test Cases:
    * testSetUpListeners()
 */

// Currently empty / not implemented
public class EventListenerTest {
    // EventListener object
    private final EventListener eventListener = new EventListener();
    // EventDAO object
    private final EventDAO eventDAO = new EventDAO();

    @Test // Test setting up listeners
    public void testSetUpListeners() {
        // Create Calendar object
        Calendar<?> calendar = new Calendar<>("Test Calendar");
        // Check if the calendar is not null
        assertNotNull(calendar);

        // Set up listeners
        Calendar<?> result = eventListener.setupListeners(calendar);
        // Check if the result is not null
        assertNotNull(result);

        // Create entries
        Entry<?> newEntry1 = new Entry<>("Test Entry");
        Entry<?> newEntry2 = new Entry<>("Test Entry 2", new Interval(LocalDateTime.now(), LocalDateTime.now().plusHours(2)));

        // Add entries to the calendar
        calendar.addEntry(newEntry1);
        calendar.addEntry(newEntry2);

        // Check if the calendar has the entries
        assertTrue(calendar.findEntries("").contains(newEntry1));
        assertTrue(calendar.findEntries("").contains(newEntry2));

        // Check if event exists in database
        assertNotNull(eventDAO.getEventById(newEntry1.getId())); // I should make getEventById static
        assertNotNull(eventDAO.getEventById(newEntry2.getId()));

        // Update the entries
        newEntry1.setTitle("Updated Title");
        newEntry2.setTitle("Updated Title 2");

        // Check if the entries are updated
        assertEquals("Updated Title", newEntry1.getTitle());
        assertEquals("Updated Title 2", newEntry2.getTitle());

        // Check if the event is updated in the database
        assertEquals("Updated Title", eventDAO.getEventById(newEntry1.getId()).getTitle());
        assertEquals("Updated Title 2", eventDAO.getEventById(newEntry2.getId()).getTitle());

        // Remove the entries
        calendar.removeEntry(newEntry1);
        calendar.removeEntry(newEntry2);

        // Check if the entries are removed
        assertFalse(calendar.findEntries("").contains(newEntry1));
        assertFalse(calendar.findEntries("").contains(newEntry2));

        // Check if the event is removed from the database
        assertNull(eventDAO.getEventById(newEntry1.getId()));
        assertNull(eventDAO.getEventById(newEntry2.getId()));
    }
}

