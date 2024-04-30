package model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import com.serenitask.model.Goal;

public class GoalTest {
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
    public void testGoalConstructor() {
        // Test the constructor of the Goal class
        Goal goal = createTestGoal();
        assertNotNull(goal, "Goal should not be null");
        assertEquals("Test Goal", goal.getTitle(), "Goal title should match");
        assertEquals("Test description", goal.getDescription(), "Goal description should match");
        assertEquals(120, goal.getMinChunk(), "Goal min chunk should match");
        assertEquals(230, goal.getMaxChunk(), "Goal max chunk should match");
        assertEquals(0, goal.getPeriodicity(), "Goal periodicity should match");
        assertEquals("Test endDate", goal.getEndDate(), "Goal end date should match");
        assertEquals("", goal.getRecurrenceRules(), "Goal recurrence rules should match");
    }

    @Test
    public void testGoalSetters() {
        // Test the setters of the Goal class
        Goal goal = createTestGoal();
        goal.setTitle("New Title");
        goal.setDescription("New Description");
        goal.setMinChunk(100);
        goal.setMaxChunk(200);
        goal.setPeriodicity(1);
        goal.setEndDate("New endDate");
        goal.setRecurrenceRules("New Recurrence Rules");

        // Verify the changes
        assertEquals("New Title", goal.getTitle(), "Goal title should match");
        assertEquals("New Description", goal.getDescription(), "Goal description should match");
        assertEquals(100, goal.getMinChunk(), "Goal min chunk should match");
        assertEquals(200, goal.getMaxChunk(), "Goal max chunk should match");
        assertEquals(1, goal.getPeriodicity(), "Goal periodicity should match");
        assertEquals("New endDate", goal.getEndDate(), "Goal end date should match");
        assertEquals("New Recurrence Rules", goal.getRecurrenceRules(), "Goal recurrence rules should match");
    }

    @Test
    public void testInvalidGoalConstructor() {
        // Test recurrence_rules and periodicity
        // To be implemented
    }
}
