package model;

import com.calendarfx.model.Interval;
import com.serenitask.model.Day;
import com.serenitask.model.TimeWindow;
import org.junit.jupiter.api.Test;
import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.time.LocalTime;

/* Methods:
    addWindow(LocalTime open, LocalTime close)
    calculateFreeTime() - PRIVATE
    getPriority()
    setPriority(int priority)
    getFreeTime()
    setFreeTime(int freeTime)
    isDateSet()
    setDateSet(boolean dateSet)
    getBiggestWindow()
    getStartDate()
    getWorkingTime()
    getHealthTime()
    setStartDate(LocalDate startDate)
    getEndDate()
    setEndDate(LocalDate endDate)
    findBiggestWindow() - PRIVATE
    addWork(int work)
    addHealth(int health)
    getHealthNeeded(int ratio)
 */

/* Test Cases:
    testDayConstructor()
    testDaySetters()
 */

public class DayTest {
    // LocalDateTime for testing
    private final LocalDateTime testTime = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);

    @Test // Test the getters of the Day class
    public void testDayConstructor() {
        // Test the constructor of the Day class
        Day day = new Day();

        // Verify the values
        assertEquals(-1, day.getPriority(), "Day priority should be -1");
        assertEquals(-1, day.getFreeTime(), "Day free time should be -1");
        assertEquals(-1, day.getWorkingTime(), "Day working time should be -1");
    }

    @Test
    public void testDaySetters() {
        // Test the setters of the Day class
        Day day = new Day();

        // TimeWindow objects
        TimeWindow window1 = new TimeWindow(LocalTime.of(9, 0), LocalTime.of(10, 0));
        TimeWindow window2 = new TimeWindow(LocalTime.of(10, 0), LocalTime.of(20, 0));

        // Set new values
        day.setPriority(5);
        day.setFreeTime(240);
        day.setDateSet(true);
        day.setStartDate(testTime.toLocalDate());
        day.setEndDate(testTime.toLocalDate());
        // Special cases
        day.addWindow(window1.getWindowOpen(), window1.getWindowClose());
        day.addWindow(window2.getWindowOpen(), window2.getWindowClose());
        day.addWork(120);
        day.addHealth(60);

        // Verify the new values
        assertEquals(5, day.getPriority(), "Day priority should be 5");
        assertEquals(660, day.getFreeTime(), "Day free time should be 660");
        assertTrue(day.isDateSet(), "Day date should be set");
        assertEquals(testTime.toLocalDate(), day.getStartDate(), "Day start date should match");
        assertEquals(testTime.toLocalDate(), day.getEndDate(), "Day end date should match");
        assertEquals(120, day.getWorkingTime(), "Day working time should be 120");
        assertEquals(60, day.getHealthTime(), "Day health time should be 60");
        assertEquals(day.getWorkingTime() / 2, day.getHealthNeeded(2), "Day health needed should be 60");
        assertEquals(window2.getWindowOpen(), day.getBiggestWindow().getWindowOpen(), "Day biggest window should match");
    }
}
