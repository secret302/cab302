package dao;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDateTime;
import java.util.List;
import com.serenitask.model.Goal;
import com.serenitask.util.DatabaseManager.GoalDAO;

/* Methods:
    addSampleEntries() - PRIVATE
    createTable() - PRIVATE
    addGoal(Goal goal)
    updateGoal(Goal goal)
    deleteGoal(String goalID)
    getGoalById(String goalID)
    getAllGoals()
 */

/* Test Cases:
    testAddGoal()
    testUpdateGoal()
    testDeleteGoal()
    testGetGoalById()
    testGetAllGoals()
 */

// GoalDAOTest class tests the GoalDAO class
public class GoalDAOTest {
    // GoalDAO object
    private final GoalDAO goalDAO = new GoalDAO();
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

    @Test // Test the addGoal method
    public void testAddGoal() {
        // Create test goal
        int goalId = goalDAO.addGoal(createTestGoal(testTime));
        // Check if the goal id is not -1
        assertNotEquals(-1, goalId, "Goal ID should not be -1");

        // Retrieve the goal by ID and check if it is not null
        Goal goal = goalDAO.getGoalById(goalId);
        assertNotNull(goal, "Goal should not be null");

        // Verify the components of the goal
        assertEquals("Test Goal", goal.getTitle(), "Goal title should match");
        assertEquals(1, goal.getTargetAmount(), "Goal target amount should match");
        assertEquals(15, goal.getMinChunk(), "Goal min chunk should match");
        assertEquals(60, goal.getMaxChunk(), "Goal max chunk should match");
        assertEquals(testTime.toLocalDate(), goal.getAllocatedUntil(), "Goal allocated until should match");
        assertEquals(0, goal.getDaysOutstanding(), "Goal days outstanding should match");

        // Delete the goal after testing
        goalDAO.deleteGoal(goalId);

        // Try to add a null goal
        goalId = goalDAO.addGoal(null);
        assertEquals(-1, goalId, "Adding a null goal should fail");
    }

    @Test // Test the updateGoal method
    public void testUpdateGoal() {
        // Create test goal
        int goalId = goalDAO.addGoal(createTestGoal(testTime));
        // Retrieve the goal by ID
        Goal goal = goalDAO.getGoalById(goalId);

        // Update the goal
        goal.setTitle("Updated Goal");
        goal.setTargetAmount(2);
        goal.setMinChunk(30);
        goal.setMaxChunk(90);
        goal.setAllocatedUntil(testTime.plusDays(1).toLocalDate());
        goal.setDaysOutstanding(1);

        // Update the goal
        boolean success = goalDAO.updateGoal(goal);
        assertTrue(success, "Goal should be updated successfully");

        // Retrieve the goal by ID
        goal = goalDAO.getGoalById(goalId);

        // Verify the components of the goal
        assertEquals("Updated Goal", goal.getTitle(), "Goal title should match");
        assertEquals(2, goal.getTargetAmount(), "Goal target amount should match");
        assertEquals(30, goal.getMinChunk(), "Goal min chunk should match");
        assertEquals(90, goal.getMaxChunk(), "Goal max chunk should match");
        assertEquals(testTime.plusDays(1).toLocalDate(), goal.getAllocatedUntil(), "Goal allocated until should match");
        assertEquals(1, goal.getDaysOutstanding(), "Goal days outstanding should match");

        // Delete the goal after testing
        goalDAO.deleteGoal(goalId);

        // Try to update a null goal
        success = goalDAO.updateGoal(null);
        assertFalse(success, "Updating a null goal should fail");
    }

    @Test // Test the deleteGoal method
    public void testDeleteGoal() {
        // Create test goal
        int goalId = goalDAO.addGoal(createTestGoal(testTime));

        // Delete the goal
        boolean success = goalDAO.deleteGoal(goalId);
        assertTrue(success, "Goal should be deleted successfully");

        // Confirm the goal is deleted
        Goal goal = goalDAO.getGoalById(goalId);
        assertNull(goal, "Goal should not exist");

        // Try to delete the goal again
        success = goalDAO.deleteGoal(goalId);
        assertFalse(success, "Deleting a non-existent goal should fail");

        // Try to delete a non-existent goal
        success = goalDAO.deleteGoal(-1);
        assertFalse(success, "Deleting a non-existent goal should fail");
    }

    @Test // Test the getGoalById method
    public void testGetGoalById() {
        // Create test goals
        int goalId1 = goalDAO.addGoal(createTestGoal(testTime));
        int goalId2 = goalDAO.addGoal(createTestGoal(testTime.plusHours(1)));

        // Try to get the goals by ID
        Goal goal1 = goalDAO.getGoalById(goalId1);
        assertNotNull(goal1, "Goal 1 should not be null");
        Goal goal2 = goalDAO.getGoalById(goalId2);
        assertNotNull(goal2, "Goal 2 should not be null");

        // Confirm the goal IDs do not match
        assertNotEquals(goalId1, goalId2, "Goal IDs should not match");

        // Confirm the goal IDs match their respective goals
        assertEquals(goalId1, goal1.getId(), "Goal 2 ID should match");
        assertEquals(goalId2, goal2.getId(), "Goal 2 ID should match");

        // Delete the goals after testing
        goalDAO.deleteGoal(goalId1);
        goalDAO.deleteGoal(goalId2);

        // Try to get a non-existent goal
        Goal goal = goalDAO.getGoalById(-1);
        assertNull(goal, "Goal should not exist");
    }

    @Test // Test the getAllGoals method
    public void testGetAllGoals() {
        // Create test goals
        int goalId1 = goalDAO.addGoal(createTestGoal(testTime));
        int goalId2 = goalDAO.addGoal(createTestGoal(testTime.plusHours(2)));
        int goalId3 = goalDAO.addGoal(createTestGoal(testTime.plusHours(42)));

        // Get all goals
        List<Goal> goals = goalDAO.getAllGoals();
        assertEquals(3, goals.size(), "List should contain 3 goals");

        // Delete the goals after testing
        goalDAO.deleteGoal(goalId1);
        goalDAO.deleteGoal(goalId2);
        goalDAO.deleteGoal(goalId3);
    }
}