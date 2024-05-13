package model;

import com.serenitask.model.TimeWindow;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDateTime;
import java.time.LocalTime;

/* Methods:
    getWindowOpen()
    setWindowOpen(LocalTime windowOpen)
    getWindowClose()
    setWindowClose(LocalTime windowClose)
 */

/* Test Cases:
    testDayConstructor()
    testDaySetters()
 */

public class TimeWindowTest {
    // LocalDateTime for testing
    private final LocalDateTime testTime = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);

    @Test // Test the getters of the Day class
    public void testDayConstructor() {
        // Test the constructor of the TimeWindow class
        TimeWindow window = new TimeWindow(LocalTime.of(9, 0), LocalTime.of(10, 0));

        // Verify the values
        assertEquals(LocalTime.of(9, 0), window.getWindowOpen(), "Window open time should be 9:00");
        assertEquals(LocalTime.of(10, 0), window.getWindowClose(), "Window close time should be 10:00");
    }

    @Test
    public void testDaySetters() {
        // Test the setters of the TimeWindow class
        TimeWindow window = new TimeWindow(LocalTime.of(9, 0), LocalTime.of(10, 0));

        // Set new values
        window.setWindowOpen(LocalTime.of(10, 0));
        window.setWindowClose(LocalTime.of(11, 0));

        // Verify the new values
        assertEquals(LocalTime.of(10, 0), window.getWindowOpen(), "Window open time should be 10:00");
        assertEquals(LocalTime.of(11, 0), window.getWindowClose(), "Window close time should be 11:00");
    }
}
