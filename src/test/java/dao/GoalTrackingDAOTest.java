package dao;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

import com.serenitask.model.GoalTracking;
import com.serenitask.util.DatabaseManager.GoalTrackingDAO;

import com.serenitask.model.Goal;
import com.serenitask.util.DatabaseManager.GoalDAO;

/* File is incomplete due to needing to datetime formats now being decided yet.

public class GoalTrackingDAOTest {
    private final GoalTrackingDAO goalTrackingDAO = new GoalTrackingDAO(); // Assuming constructor exists
    private final GoalDAO goalDAO = new GoalDAO(); // Assuming constructor exists
    private int goalTrackId; // Example goal ID for testing
    private Date goalTrackDate; // Example goal date for testing

    private final Goal exampleGoal = new Goal(
            "Test Goal",
            "Test description",
            120,
            230,
            0,
            "Test emdDate",
            ""
    ); // Example goal for testing

    private GoalTracking createTestGoalTracking(boolean completed) {
        // Create a goal and get its ID
        goalTrackId = goalDAO.addGoal(exampleGoal);
        goalTrackDate = new Date(); // Update this to be now date.

        // Create new goal tracking entry and return it
        return new GoalTracking(goalTrackId, goalTrackDate.toString(), completed);
    }

    @AfterEach
    public void tearDown() {
        // Delete goal tracking entries and the associated goal (If it has one) after testing
        goalTrackingDAO.deleteGoalTracking(goalTrackId, goalTrackDate);
        goalTrackId = 0;
        goalTrackDate = null;
    }

    @Test
    public void testAddGoalTracking() {
        // Create a goal tracking entry
        GoalTracking newTracking = createTestGoalTracking(false);
        boolean success = goalTrackingDAO.addGoalTracking(newTracking);
        assertTrue(success, "Goal tracking creation should be successful");

        // Retrieve the created entry and verify its details
        GoalTracking retrievedTracking = goalTrackingDAO.getGoalTracking(goalTrackId, goalTrackDate);
        assertNotNull(retrievedTracking, "Retrieved tracking should not be null");
        assertEquals(newTracking.getGoalId(), retrievedTracking.getGoalId(), "Goal ID should match");
        assertEquals(newTracking.getGoalDate(), retrievedTracking.getGoalDate(), "Goal date should match"); // Format needed? To be decided later
        assertEquals(newTracking.isCompleted(), retrievedTracking.isCompleted(), "Completed status should match");
    }

    @Test
    public void testUpdateGoalTracking() {
        // Create a goal tracking entry
        GoalTracking newTracking = createTestGoalTracking(false);
        goalTrackingDAO.addGoalTracking(newTracking);
        assertNotNull(newTracking, "New tracking should not be null");

        // Update the completed status
        newTracking.setCompleted(true);
        boolean success = goalTrackingDAO.updateGoalTracking(newTracking);
        assertTrue(success, "Goal tracking update should be successful");

        // Verify the updated status
        GoalTracking updatedTracking = goalTrackingDAO.getGoalTracking(goalTrackId, goalTrackDate);
        assertTrue(updatedTracking.isCompleted(), "Completed status should be updated to true");
    }

    @Test
    public void testDeleteGoalTracking() {
        // Create a goal tracking entry
        GoalTracking newTracking = createTestGoalTracking(false);
        goalTrackingDAO.addGoalTracking(newTracking);
        assertNotNull(newTracking, "New tracking should not be null");

        // Delete the goal tracking entry
        boolean success = goalTrackingDAO.deleteGoalTracking(goalTrackId, goalTrackDate);
        assertTrue(success, "Goal tracking deletion should be successful");

        // Verify that the entry is deleted
        GoalTracking deletedTracking = goalTrackingDAO.getGoalTracking(goalTrackId, goalTrackDate);
        assertNull(deletedTracking, "Deleted tracking should be null");
    }

    @Test
    public void testGetGoalTrackings() {
        // Get today at 6am
        Date localDate = new Date(); // Assuming Date is imported
        localDate.setHours(6);
        localDate.setMinutes(0);
        localDate.setSeconds(0);

        // Create goal
        Goal goal = new Goal("Test Goal",
                "Test description",
                120,
                230,
                0,
                "Test emdDate",
                ""
        );
        int goalTrackId = goalDAO.addGoal(goal);
        // Create goal tracking entries
        GoalTracking tracking1 = new GoalTracking(goalTrackId, new Date(localDate.getTime() + 7 * 3600 * 1000).toString(), true);
        GoalTracking tracking2 = new GoalTracking(goalTrackId, new Date(localDate.getTime() + 8 * 3600 * 1000).toString(), false);
        GoalTracking tracking3 = new GoalTracking(goalTrackId, new Date(localDate.getTime() + 24 * 3600 * 1000).toString(), true);
        // Add goal tracking entries
        goalTrackingDAO.addGoalTracking(tracking1);
        goalTrackingDAO.addGoalTracking(tracking2);
        goalTrackingDAO.addGoalTracking(tracking3);

        // Get goal tracking entries on a specific day
        List<GoalTracking> trackings = goalTrackingDAO.getGoalTrackings(LocalDate.now()); // Pass it day to get all tracking for that day
        assertEquals(2, trackings.size(), "Number of trackings should be 2");
        // Check if the correct entries are returned
        assertEquals(tracking1.getGoalId(), trackings.get(0).getGoalId(), "Goal ID should match 1");
        assertEquals(tracking2.getGoalId(), trackings.get(1).getGoalId(), "Goal ID should match 2");

        // Get goal tracking entries between two dates (Change this around, so it only returns 2)
        List<GoalTracking> trackings2 = goalTrackingDAO.getGoalTrackings(LocalDate.now(), LocalDate.now().plusDays(1)); // Pass it day to get all tracking for that day
        assertEquals(3, trackings2.size(), "Number of trackings should be 3");
        // Check if the correct entries are returned
        assertEquals(tracking1.getGoalId(), trackings2.get(0).getGoalId(), "Goal ID should match 1");
        assertEquals(tracking2.getGoalId(), trackings2.get(1).getGoalId(), "Goal ID should match 2");
        assertEquals(tracking3.getGoalId(), trackings2.get(2).getGoalId(), "Goal ID should match 3");

        // Delete the goal tracking entries after testing
        goalTrackingDAO.deleteGoalTracking(tracking1.getGoalId(), tracking1.getGoalDate());
        goalTrackingDAO.deleteGoalTracking(tracking2.getGoalId(), tracking2.getGoalDate());
        goalTrackingDAO.deleteGoalTracking(tracking3.getGoalId(), tracking3.getGoalDate());
        // Confirm deletion
        assertNull(goalTrackingDAO.getGoalTracking(tracking1.getGoalId(), tracking1.getGoalDate()), "Tracking 1 should not exist");
        assertNull(goalTrackingDAO.getGoalTracking(tracking2.getGoalId(), tracking2.getGoalDate()), "Tracking 2 should not exist");
        assertNull(goalTrackingDAO.getGoalTracking(tracking3.getGoalId(), tracking3.getGoalDate()), "Tracking 3 should not exist");
    }

    @Test
    public void testAddInvalidGoalTracking() {
        // Create a goal tracking entry with an invalid goal ID
        GoalTracking invalidTracking = new GoalTracking(-1, LocalDate.now().toString(), false);
        boolean success = goalTrackingDAO.addGoalTracking(invalidTracking);
        assertFalse(success, "Goal tracking creation should fail with invalid goal ID");

        // Create a goal tracking entry with an invalid date
        GoalTracking invalidTracking2 = new GoalTracking(1, null, false);
        success = goalTrackingDAO.addGoalTracking(invalidTracking2);
        assertFalse(success, "Goal tracking creation should fail with invalid date");

        // Create a goal tracking entry with a long-past date
        GoalTracking invalidTracking3 = new GoalTracking(1, LocalDate.of(1987, 1, 1).toString(), false);
        success = goalTrackingDAO.addGoalTracking(invalidTracking3);
        assertFalse(success, "Goal tracking creation should fail with long-past date / old date");
    }

    @Test
    public void testUpdateInvalidGoalTracking() {
        // Create a goal tracking entry
        GoalTracking newTracking = createTestGoalTracking(false);
        goalTrackingDAO.addGoalTracking(newTracking);

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
        success = goalTrackingDAO.deleteGoalTracking(newTracking.getGoalId(), null);
        assertFalse(success, "Goal tracking deletion should fail with invalid date");
    }
}
*/