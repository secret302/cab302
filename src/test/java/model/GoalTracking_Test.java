package model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import com.serenitask.model.GoalTracking;
import com.serenitask.model.Goal;
import java.time.LocalDate;
import java.util.Date;

public class GoalTracking_Test {
    @Test
    public void testGoalTrackingConstructor() {
        // LocalDate date
        LocalDate date = LocalDate.now();
        // Create a Goal object
        Goal goal = new Goal("Test Goal", "Test description");
        assertNotNull(goal, "Goal should not be null");

        // Test valid GoalTracking constructor
        GoalTracking goalTracking = new GoalTracking(goal, date, false);
        assertNotNull(goalTracking, "GoalTracking should not be null");
        assertEquals(goal.getID(), goalTracking.getGoal(), "Goal should match");
        assertEquals(date, goalTracking.getGoalDate(), "Goal date should match");
        assertFalse(goalTracking.isCompleted(), "Goal should not be completed");
    }

    @Test
    public void testGoalTrackingSetters() {
        // LocalDate date
        LocalDate date = LocalDate.now();
        // Create a Goal object
        Goal goal = new Goal("Test Goal", "Test description");
        assertNotNull(goal, "Goal should not be null");

        // Test the setters of the GoalTracking class
        GoalTracking goalTracking = new GoalTracking(goal, date, false);
        goalTracking.setCompleted(true);
        assertTrue(goalTracking.isCompleted(), "Goal should be completed");
    }

    @Test
    public void testInvalidGoalTrackingConstructor() {
        // LocalDate date
        LocalDate date = LocalDate.now();
        // Create a Goal object
        Goal goal = new Goal("Test Goal", "Test description");
        assertNotNull(goal, "Goal should not be null");

        // Test invalid GoalTracking constructor, null Goal
        GoalTracking invalidGoalTracking = new GoalTracking(null, date, false);
        assertNull(invalidGoalTracking, "GoalTracking should be null");

        // Test invalid GoalTracking constructor, old date
        GoalTracking invalidGoalTracking2 = new GoalTracking(goal, new Date(1984, 1, 1), false);
        assertNull(invalidGoalTracking2, "GoalTracking should be null");

        // Test invalid GoalTracking constructor, null date
        GoalTracking invalidGoalTracking3 = new GoalTracking(goal, null, false);
        assertNull(invalidGoalTracking3, "GoalTracking should be null");

        // Test invalid GoalTracking constructor, not unique date
        GoalTracking invalidGoalTracking4 = new GoalTracking(goal, date, true);
        assertNull(invalidGoalTracking4, "GoalTracking should be null");
    }
}