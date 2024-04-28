package model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import com.serenitask.model.Goal;

public class Goal_Test {
    @Test
    public void testGoalConstructor() {
        // Test the constructor of the Goal class
        Goal goal = new Goal("Test Goal", "Test description"); // Add other attributes
        assertNotNull(goal, "Goal should not be null");
        assertEquals("Test Goal", goal.getTitle(), "Goal title should match");
        assertEquals("Test description", goal.getDescription(), "Goal description should match");
    }

    @Test
    public void testGoalSetters() {
        // Test the setters of the Goal class
        Goal goal = new Goal("Test Goal", "Test description"); // Add other attributes
        goal.setTitle("New Title");
        goal.setDescription("New Description");
        assertEquals("New Title", goal.getTitle(), "Goal title should match");
        assertEquals("New Description", goal.getDescription(), "Goal description should match");
    }

    @Test
    public void testInvalidGoalConstructor() {
        // Test recurrence_rules and date_time
        // To be implemented
    }
}
