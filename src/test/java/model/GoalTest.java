package model;

import com.calendarfx.model.Interval;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.serenitask.model.Goal;
import java.time.LocalDateTime;

/* Methods:
    getId()
    setId()
    getTitle()
    setTitle()
    getTargetAmount()
    setTargetAmount()
    getMinChunk()
    setMinChunk()
    getMaxChunk()
    setMaxChunk()
    getAllocatedUntil()
    setAllocatedUntil()
    getDaysOutstanding()
    setDaysOutstanding()
 */

/* Test Cases:
    testGoalConstructor()
    testGoalSetters()
 */

// GoalTest class tests the Goal class
public class GoalTest {
    // LocalDateTime for testing
    private final LocalDateTime testTime = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);

    /**
     * Create a test goal with a specific start time
     * @param startTime LocalDateTime object
     * @return Goal object
     */
    private Goal createTestGoal(LocalDateTime startTime) {
        // Create entry for the goal and return it
        return new Goal(
                "Test Goal",
                1,
                15,
                60,
                startTime.toLocalDate(),
                0
        );
    }

    @Test // Test the getters of the Goal class
    public void testGoalConstructor() {
        // Test the constructor of the Goal class
        Goal goal = createTestGoal(testTime);

        // Verify the values
        assertEquals("Test Goal", goal.getTitle(), "Goal title should match");
        assertEquals(1, goal.getTargetAmount(), "Target amount should match");
        assertEquals(15, goal.getMinChunk(), "Minimum chunk should match");
        assertEquals(60, goal.getMaxChunk(), "Maximum chunk should match");
        assertEquals(testTime.toLocalDate(), goal.getAllocatedUntil(), "Allocated until should match");
        assertEquals(0, goal.getDaysOutstanding(), "Days outstanding should match");

        // Test the simple constructor of the Goal class
        Goal simpleGoal = new Goal("Test Goal", 15, 60);
        assertEquals("Test Goal", simpleGoal.getTitle(), "Goal title should match");
        assertEquals(15, simpleGoal.getMinChunk(), "Minimum chunk should match");
        assertEquals(60, simpleGoal.getMaxChunk(), "Maximum chunk should match");
        assertEquals(1, simpleGoal.getTargetAmount(), "Target amount should match");
        // allocatedUntil is a LocalDate.now() object, so it will not match
        assertEquals(0, simpleGoal.getDaysOutstanding(), "Days outstanding should match");

        // Test the constructor with invalid values
        assertThrows(IllegalArgumentException.class, () -> {
            new Goal(
                    "",
                    1,
                    15,
                    60,
                    testTime.toLocalDate(),
                    0
            );
        }, "Title cannot be empty");
        assertThrows(IllegalArgumentException.class, () -> {
            new Goal(
                    "Test Goal",
                    -1,
                    15,
                    60,
                    testTime.toLocalDate(),
                    0
            );
        }, "Target amount cannot be negative");
        assertThrows(IllegalArgumentException.class, () -> {
            new Goal(
                    "Test Goal",
                    1,
                    14,
                    60,
                    testTime.toLocalDate(),
                    0
            );
        }, "Minimum chunk cannot be below 15 minutes");
        assertThrows(IllegalArgumentException.class, () -> {
            new Goal(
                    "Test Goal",
                    1,
                    15,
                    14,
                    testTime.toLocalDate(),
                    0
            );
        }, "Maximum chunk cannot be below 15 minutes");
        assertThrows(IllegalArgumentException.class, () -> {
            new Goal(
                    "Test Goal",
                    1,
                    60,
                    15,
                    testTime.toLocalDate(),
                    0
            );
        }, "Minimum chunk cannot be greater than maximum chunk");
        // Simple constructor
        assertThrows(IllegalArgumentException.class, () -> {
            new Goal(
                    "",
                    15,
                    60
            );
        }, "Title cannot be empty");
        assertThrows(IllegalArgumentException.class, () -> {
            new Goal(
                    "Test Goal",
                    14,
                    60
            );
        }, "Minimum chunk cannot be below 15 minutes");
        assertThrows(IllegalArgumentException.class, () -> {
            new Goal(
                    "Test Goal",
                    15,
                    14
            );
        }, "Maximum chunk cannot be below 15 minutes");
        assertThrows(IllegalArgumentException.class, () -> {
            new Goal(
                    "Test Goal",
                    60,
                    15
            );
        }, "Minimum chunk cannot be greater than maximum chunk");

        // Test the constructor with null values
        assertThrows(IllegalArgumentException.class, () -> {
            new Goal(
                    null,
                    1,
                    15,
                    60,
                    testTime.toLocalDate(),
                    0
            );
        }, "Title cannot be null");
    }

    @Test
    public void testGoalSetters() {
        // Test the setters of the Goal class
        Goal goal = createTestGoal(testTime);

        // Set new values
        goal.setTitle("New Goal");
        goal.setTargetAmount(2);
        goal.setMinChunk(30);
        goal.setMaxChunk(90);
        goal.setAllocatedUntil(testTime.plusDays(1).toLocalDate());
        goal.setDaysOutstanding(1);

        // Verify the new values
        assertEquals("New Goal", goal.getTitle(), "Goal title should match");
        assertEquals(2, goal.getTargetAmount(), "Target amount should match");
        assertEquals(30, goal.getMinChunk(), "Minimum chunk should match");
        assertEquals(90, goal.getMaxChunk(), "Maximum chunk should match");
        assertEquals(testTime.plusDays(1).toLocalDate(), goal.getAllocatedUntil(), "Allocated until should match");
        assertEquals(1, goal.getDaysOutstanding(), "Days outstanding should match");

        // Test the setters with invalid values
        assertThrows(IllegalArgumentException.class, () -> {
            goal.setTitle(""); // empty title
        }, "Title cannot be empty");
        assertThrows(IllegalArgumentException.class, () -> {
            goal.setId(-1); // empty title
        }, "ID cannot be invalid");

        // Test the setters with null values
        assertThrows(IllegalArgumentException.class, () -> {
            goal.setTitle(null); // null title
        }, "Title cannot be null");
    }
}
