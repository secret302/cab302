package model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import com.serenitask.model.GoalTracking;
import com.serenitask.model.Goal;
import java.time.LocalDate;
import java.util.Date;

public class GoalTrackingTest {
    public Goal createTestGoal() {
        // Create entry for the goal and return it
        return new Goal(
                "Test Goal",
                "Test description",
                120,
                230,
                0,
                "Test endDate",
                ""
        );
        // Goals will be cleaned up by the garbage collector
    }
    @Test
    public void testGoalTrackingConstructor() {
        // LocalDate date
        LocalDate date = LocalDate.now();
        // Create a Goal object
        Goal goal = createTestGoal();
        assertNotNull(goal, "Goal should not be null");

        // Test valid GoalTracking constructor
        GoalTracking goalTracking = new GoalTracking(goal, date, false);
        assertNotNull(goalTracking, "GoalTracking should not be null");
        assertEquals(goal.getId(), goalTracking.getGoal(), "Goal should match");
        //assertEquals(date, goalTracking.getGoalDate(), "Goal date should match"); // To be done when we finalise the date format
        assertFalse(goalTracking.isCompleted(), "Goal should not be completed");
    }

    @Test
    public void testGoalTrackingSetters() {
        // LocalDate date
        LocalDate date = LocalDate.now();
        // Create a Goal object
        Goal goal = createTestGoal();
        assertNotNull(goal, "Goal should not be null");

        // Test the setters of the GoalTracking class
        GoalTracking goalTracking = new GoalTracking(goal.getId(), date.toString(), false);
        goalTracking.setCompleted(true);
        assertTrue(goalTracking.isCompleted(), "Goal should be completed");
    }

    @Test
    public void testInvalidGoalTrackingConstructor() {
        // LocalDate date
        LocalDate date = LocalDate.now();
        // Create a Goal object
        Goal goal = createTestGoal();
        assertNotNull(goal, "Goal should not be null");

        // Test invalid GoalTracking constructor, null Goal
        GoalTracking invalidGoalTracking = new GoalTracking(0, date.toString(), false);
        assertNull(invalidGoalTracking, "GoalTracking should be null");

        // Test invalid GoalTracking constructor, old date
        GoalTracking invalidGoalTracking2 = new GoalTracking(goal.getId(), new Date(1984, 1, 1).toString(), false); // Date is deprecated pwease fwix uwu
        assertNull(invalidGoalTracking2, "GoalTracking should be null");

        // Test invalid GoalTracking constructor, null date
        GoalTracking invalidGoalTracking3 = new GoalTracking(goal.getId(), null, false);
        assertNull(invalidGoalTracking3, "GoalTracking should be null");

        // Test invalid GoalTracking constructor, not unique date
        GoalTracking invalidGoalTracking4 = new GoalTracking(goal.getId(), date.toString(), true);
        assertNull(invalidGoalTracking4, "GoalTracking should be null");
    }
}
