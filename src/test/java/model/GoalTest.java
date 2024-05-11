package model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import com.serenitask.model.Goal;

import java.time.LocalDateTime;

public class GoalTest {
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
        // Goals will be cleaned up by the garbage collector
    }

    @Test
    public void testGoalConstructor() {
        // Set LocalDateTime
        LocalDateTime startTime = LocalDateTime.now();
        // Test the constructor of the Goal class
        Goal goal = createTestGoal(startTime);
        assertNotNull(goal, "Goal should not be null");
        assertEquals("Test Goal", goal.getTitle(), "Goal title should match");
        assertEquals(1, goal.getTargetAmount());
        assertEquals(15, goal.getMinChunk());
        assertEquals(60, goal.getMaxChunk());
        assertEquals(startTime.toLocalDate(), goal.getAllocatedUntil(), "Goal allocated until should match");
        assertEquals(0, goal.getDaysOutstanding());
    }

    @Test
    public void testGoalSetters() {
        // Set LocalDateTime
        LocalDateTime startTime = LocalDateTime.now();
        // Test the setters of the Goal class
        Goal goal = createTestGoal(startTime);
        goal.setTitle("New Title");
        goal.setTargetAmount(2);
        goal.setMinChunk(30);
        goal.setMaxChunk(90);
        goal.setAllocatedUntil(startTime.toLocalDate().plusDays(1));
        goal.setDaysOutstanding(1);

        // Verify the changes
        assertEquals("New Title", goal.getTitle(), "Goal title should match");
        assertEquals(2, goal.getTargetAmount());
        assertEquals(30, goal.getMinChunk());
        assertEquals(90, goal.getMaxChunk());
        assertEquals(startTime.toLocalDate().plusDays(1), goal.getAllocatedUntil(), "Goal allocated until should match");
        assertEquals(1, goal.getDaysOutstanding());
    }

    @Test
    public void testInvalidGoalConstructor() {
        // To be implemented
    }
}
