package dao;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

import com.calendarfx.model.GoalTracking;
import com.calendarfx.util.GoalTrackingDAO;

public class GoalTrackingDAO_Test {
    private GoalTrackingDAO goalTrackingDAO = new GoalTrackingDAO(); // Assuming constructor exists
    private int goalId; // To store the ID of the goal used in tests
    private LocalDate goalDate;

    private GoalTracking createTestGoalTracking(boolean completed) {
        // Generate a unique goal ID if needed (e.g., using a counter or timestamp)
        return new GoalTracking(goalId, LocalDate.now(), completed);
    }

    @AfterEach
    public void tearDown() {
        // Delete goal tracking entries and the associated goal (If it has one) after testing
        goalTrackingDAO.deleteGoalTracking(goalId, goalDate);
        goalId = 0;
        goalDate = null;
    }

    @Test
    public void testCreateGoalTracking() {
        // Create a goal tracking entry
        GoalTracking newTracking = createTestGoalTracking(false);
        boolean success = goalTrackingDAO.createGoalTracking(newTracking);
        assertTrue(success, "Goal tracking creation should be successful");

        // Retrieve the created entry and verify its details
        GoalTracking retrievedTracking = goalTrackingDAO.getGoalTracking(goalId, goalDate);
        assertNotNull(retrievedTracking.getID(), "Retrieved tracking should not be null");
        assertEquals(newTracking.getGoalId(), retrievedTracking.getGoalId(), "Goal ID should match");
        assertEquals(newTracking.getGoalDate(), retrievedTracking.getGoalDate(), "Goal date should match");
        assertEquals(newTracking.isCompleted(), retrievedTracking.isCompleted(), "Completed status should match");
    }

    @Test
    public void testUpdateGoalTracking() {
        // Create a goal tracking entry
        GoalTracking newTracking = createTestGoalTracking(false);
        goalTrackingDAO.createGoalTracking(newTracking);

        // Update the completed status
        newTracking.setCompleted(true);
        boolean success = goalTrackingDAO.updateGoalTracking(newTracking);
        assertTrue(success, "Goal tracking update should be successful");

        // Verify the updated status
        GoalTracking updatedTracking = goalTrackingDAO.getGoalTracking(goalId, goalDate);
        assertTrue(updatedTracking.isCompleted(), "Completed status should be updated to true");
    }

    // Gonna add more tests later (e.g., delete, get by goal ID, etc.)
}