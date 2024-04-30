package dao;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

import com.serenitask.model.GoalTracking;
import com.serenitask.util.DatabaseManager.GoalTrackingDAO;

import com.serenitask.model.Goal;
import com.serenitask.util.DatabaseManager.GoalDAO;

public class GoalTrackingDAOTest {
    private GoalTrackingDAO goalTrackingDAO = new GoalTrackingDAO(); // Assuming constructor exists
    private GoalDAO goalDAO = new GoalDAO(); // Assuming constructor exists
    private Goal exampleGoal = new Goal("Test Goal", "Test description", 120, 230,
            0, "Test emdDate", "");
    private LocalDate goalDate;

    private GoalTracking createTestGoalTracking(boolean completed) {
        // Create a goal and get its ID
        int goalId = goalDAO.addGoal(exampleGoal);
        // Generate a unique goal ID if needed (e.g., using a counter or timestamp)
        return new GoalTracking(goalId, LocalDate.now().toString(), completed);
    }

    @AfterEach
    public void tearDown() {
        // Delete goal tracking entries and the associated goal (If it has one) after testing
        goalTrackingDAO.deleteGoalTracking(goalId, goalDate);
        goalId = 0;
        goalDate = null;
    }

    @Test
    public void testAddGoalTracking() {
        // Create a goal tracking entry
        GoalTracking newTracking = createTestGoalTracking(false);
        boolean success = goalTrackingDAO.addGoalTracking(newTracking);
        assertTrue(success, "Goal tracking creation should be successful");

        // Retrieve the created entry and verify its details
        GoalTracking retrievedTracking = goalTrackingDAO.getGoalTracking(goalId, goalDate);
        assertNotNull(retrievedTracking.getID(), "Retrieved tracking should not be null");
        assertEquals(newTracking.getGoalId(), retrievedTracking.getGoalId(), "Goal ID should match");
        assertEquals(newTracking.getGoalDate(), retrievedTracking.getGoalDate(), "Goal date should match"); // Format needed? To be decided later
        assertEquals(newTracking.isCompleted(), retrievedTracking.isCompleted(), "Completed status should match");
    }

    @Test
    public void testUpdateGoalTracking() {
        // Create a goal tracking entry
        GoalTracking newTracking = createTestGoalTracking(false);
        goalTrackingDAO.addGoalTracking(newTracking);

        // Update the completed status
        newTracking.setCompleted(true);
        boolean success = goalTrackingDAO.updateGoalTracking(newTracking);
        assertTrue(success, "Goal tracking update should be successful");

        // Verify the updated status
        GoalTracking updatedTracking = goalTrackingDAO.getGoalTracking(goalId, goalDate);
        assertTrue(updatedTracking.isCompleted(), "Completed status should be updated to true");
    }

    @Test
    public void testDeleteGoalTracking() {
        // Create a goal tracking entry
        GoalTracking newTracking = createTestGoalTracking(false);
        goalTrackingDAO.addGoalTracking(newTracking);

        // Delete the goal tracking entry
        boolean success = goalTrackingDAO.deleteGoalTracking(goalId, goalDate);
        assertTrue(success, "Goal tracking deletion should be successful");

        // Verify that the entry is deleted
        GoalTracking deletedTracking = goalTrackingDAO.getGoalTracking(goalId, goalDate);
        assertNull(deletedTracking, "Deleted tracking should be null");
    }

    @Test
    public void testGetGoalTrackings() {
        // Get today at 6am
        Date localDate = new Date( // Assuming Date is imported
            LocalDate.now().getYear(),
            LocalDate.now().getMonthValue(),
            LocalDate.now().getDayOfMonth(),
            6,
            0);
        // Create goal
        Goal goal = new Goal("Test Goal", "Test description");
        int goalId = goalDAO.addGoal(goal);
        // Create goal tracking entries
        GoalTracking tracking1 = new GoalTracking(goalId, localDate.setHours(7), true);
        GoalTracking tracking2 = new GoalTracking(goalId, localDate.setHours(8), false);
        GoalTracking tracking3 = new GoalTracking(goalId, new Date(localDate.getTime()+24*60*60*1000), true);
        // Add goal tracking entries
        goalTrackingDAO.addGoalTracking(tracking1);
        goalTrackingDAO.addGoalTracking(tracking2);
        goalTrackingDAO.addGoalTracking(tracking3);

        // Get goal tracking entries
        List<GoalTracking> trackings = goalTrackingDAO.getGoalTrackings(localDate.now()); // Pass it day to get all trackings for that day
        assertEquals(2, trackings.size(), "Number of trackings should be 2");
    }
    
    @Test
    public void testAddInvalidGoalTracking() {
        // Create a goal tracking entry with an invalid goal ID
        GoalTracking invalidTracking = new GoalTracking(-1, LocalDate.now(), false);
        boolean success = goalTrackingDAO.addGoalTracking(invalidTracking);
        assertFalse(success, "Goal tracking creation should fail with invalid goal ID");
        // Create a goal tracking entry with an invalid date
        GoalTracking invalidTracking2 = new GoalTracking(1, null, false);
        success = goalTrackingDAO.addGoalTracking(invalidTracking2);
        assertFalse(success, "Goal tracking creation should fail with invalid date");
        // Create a goal tracking entry with a long-past date
        GoalTracking invalidTracking3 = new GoalTracking(1, LocalDate.of(1987, 1, 1), false);
        success = goalTrackingDAO.addGoalTracking(invalidTracking3);
        assertFalse(success, "Goal tracking creation should fail with long-past date / old date");
    }

    @Test
    public void testUpdateInvalidGoalTracking() {
        // Create a goal tracking entry
        GoalTracking newTracking = createTestGoalTracking(false);
        goalTrackingDAO.addGoalTracking(newTracking);

        // Update the goal ID to an invalid value
        newTracking.setGoalId(-1);
        boolean success = goalTrackingDAO.updateGoalTracking(newTracking);
        assertFalse(success, "Goal tracking update should fail with invalid goal ID");

        // Update the goal date to an invalid value
        newTracking.setGoalDate(null);
        success = goalTrackingDAO.updateGoalTracking(newTracking);
        assertFalse(success, "Goal tracking update should fail with invalid date");
    }

    @Test
    public void testDeleteNonExistentGoalTracking() {
        // Create a goal tracking entry
        GoalTracking newTracking = createTestGoalTracking(false);
        goalTrackingDAO.addGoalTracking(newTracking);
        // Attempt to delete a goal tracking entry with an invalid goal ID
        boolean success = goalTrackingDAO.deleteGoalTracking(-1, newTracking.getGoalDate());
        assertFalse(success, "Goal tracking deletion should fail with invalid goal ID");
        // Attempt to delete a goal tracking entry with an invalid date
        success = goalTrackingDAO.deleteGoalTracking(1, null);
        assertFalse(success, "Goal tracking deletion should fail with invalid date");
    }
}