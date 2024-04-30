package dao;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

import com.serenitask.model.Goal;
import com.serenitask.util.DatabaseManager.GoalDAO;
import org.mockito.internal.matchers.Null;

// GoalDAOTest class tests the GoalDAO class
public class GoalDAOTest {
    private final GoalDAO goalDAO = new GoalDAO(); // Assuming you have a constructor for GoalDAO
    private Integer goalId; // Example goal ID for testing

    private Goal createTestGoal() {
        // Generate a unique title
        String uniqueTitle = "Test Goal " + System.currentTimeMillis();
        // Create new goal and return it
        return new Goal(uniqueTitle,
                "Test description",
                120,
                230,
                0,
                "Test endDate",
                ""
        );
    }

    @AfterEach
    public void tearDown() {
        // Delete the goal after testing
        goalDAO.deleteGoal(goalId);
        goalId = null;
    }

    @Test
    public void testCreateGoal() {
        // Create an goal and check if the goal ID exists
        goalId = goalDAO.addGoal(createTestGoal());
        assertNotNull(goalId, "Goal ID should not be null");

        // Verify details of the created goal
        Goal createdGoal = goalDAO.getGoalById(goalId);
        assertNotNull(createdGoal, "Created goal should not be null");
        assertEquals(goalId, createdGoal.getId(), "Goal ID should match");

        // To be implemented, check other attributes
    }

    @Test
    public void testGetGoalById() {
        // Create an goal for testing
        goalId = goalDAO.addGoal(createTestGoal());
        assertNotNull(goalId, "Goal ID should not be null");

        // Get an goal by ID and check if it is not null
        Goal goal = goalDAO.getGoalById(goalId);
        assertNotNull(goal, "Goal should not be null");
    }

    @Test
    public void testUpdateGoal() {
        // Create an goal for testing
        goalId = goalDAO.addGoal(createTestGoal());
        assertNotNull(goalId, "Goal ID should not be null");

        // Update an goal and check if successful
        Goal goal = goalDAO.getGoalById(goalId);
        goal.setTitle("New Title");
        goal.setDescription("New Description");
        boolean success = goalDAO.updateGoal(goal);
        assertTrue(success, "Goal should be updated successfully");

        // Check if the changes are reflected
        goal = goalDAO.getGoalById(goalId);
        assertEquals("New Title", goal.getTitle(), "Title should be updated");
        assertEquals("New Description", goal.getDescription(), "Description should be updated");

        // To be implemented, check other attributes
    }

    @Test
    public void testDeleteGoal() {
        // Create an goal for testing
        goalId = goalDAO.addGoal(createTestGoal());
        assertNotNull(goalId, "Goal ID should not be null");

        // Delete an goal and check if successful
        boolean success = goalDAO.deleteGoal(goalId);
        assertTrue(success, "Goal should be deleted successfully");

        // Check if the goal is deleted
        Goal goal = goalDAO.getGoalById(goalId);
        assertNull(goal, "Goal should not exist");
    }

    @Test
    public void testGetGoals() {
        // Create more goals for testing
        Integer goalID1 = goalDAO.addGoal(createTestGoal());
        Integer goalID2 = goalDAO.addGoal(createTestGoal());
        Integer goalID3 = goalDAO.addGoal(createTestGoal());

        // Get all goals at date and check if the list is equal to 2
        List<Goal> goals = goalDAO.getGoals(date);
        assertEquals(2, goals.size(), "List should contain 2 goals (1&2)");
        // Check correct goals are returned (1&2)
        assertEquals(goalID1, goals.get(0).getId(), "Goal ID should match: 1");
        assertEquals(goalID2, goals.get(1).getId(), "Goal ID should match: 2");

        // Get all goals between two dates and check if the list is equal to 2
        goals = goalDAO.getGoals(startDate, endDate);
        assertEquals(2, goals.size(), "List should contain 2 goals (2&3)");
        // Check correct goals are returned (2&3)
        assertEquals(goalID2, goals.get(0).getId(), "Goal ID should match: 2");
        assertEquals(goalID3, goals.get(1).getId(), "Goal ID should match: 3");

        // Get all goals and check if the list is equal to 3
        goals = goalDAO.getGoals();
        assertEquals(3, goals.size(), "List should contain 3 goals (1&2&3)");
        // Check correct goals are returned (1&2&3)
        assertEquals(goalID1, goals.get(0).getId(), "Goal ID should match: 1");
        assertEquals(goalID2, goals.get(1).getId(), "Goal ID should match: 2");
        assertEquals(goalID3, goals.get(2).getId(), "Goal ID should match: 3");

        // Delete the goals after testing
        goalDAO.deleteGoal(goalID1);
        goalDAO.deleteGoal(goalID2);
        goalDAO.deleteGoal(goalID3);
        // Confirm deletion
        assertNull(goalDAO.getGoalById(goalID1), "Goal 1 should not exist");
        assertNull(goalDAO.getGoalById(goalID2), "Goal 2 should not exist");
        assertNull(goalDAO.getGoalById(goalID3), "Goal 3 should not exist");
    }

    @Test
    public void testUniqueGoalID() {
        // Create two identical goals
        Goal goal1 = createTestGoal();
        Goal goal2 = createTestGoal();
        Integer goalID1 = goalDAO.addGoal(goal1);
        Integer goalID2 = goalDAO.addGoal(goal2);
        // Check if the goals exist
        assertNotNull(goalID1, "Goal ID 1 should not be null");
        assertNotNull(goalID2, "Goal ID 2 should not be null");

        // Check if the goal IDs are different
        assertNotEquals(goalID1, goalID2, "Goal IDs should be different");

        // Delete the goals after testing
        goalDAO.deleteGoal(goalID1);
        goalDAO.deleteGoal(goalID2);
        // Confirm deletion
        assertNull(goalDAO.getGoalById(goalID1), "Goal 1 should not exist");
        assertNull(goalDAO.getGoalById(goalID2), "Goal 2 should not exist");
    }

    @Test
    public void testAddInvalidGoal() {
        // Create an invalid goal and check if the goal ID is 0
        Goal invalidGoal = new Goal(
                "",
                "Test description",
                120,
                230,
                0,
                "Test emdDate",
                ""
        );
        Integer invalidGoalEntry = goalDAO.addGoal(invalidGoal);
        assertNull(invalidGoalEntry, "Adding an invalid goal (Null) should return null");

        // To be implemented, check other invalid attributes
    }

    @Test
    public void testUpdateInvalidGoal() {
        // Create an goal
        goalId = goalDAO.addGoal(createTestGoal());
        // Confirm goal exists
        assertNotNull(goalId, "GoalId should not be null");

        // Get goal class
        Goal goal = goalDAO.getGoalById(goalId);
        // Confirm goal is not null
        assertNotNull(goal, "Goal should not be null");

        // Update the goal with invalid title
        goal.setTitle(""); // Invalid title
        boolean success = goalDAO.updateGoal(goal);
        assertFalse(success, "Updating an invalid goal should fail");

        // To be implemented, check other update attributes
    }

    @Test
    public void testDeleteNonExistentGoal() {
        // Try to delete a non-existent goal
        boolean success = goalDAO.deleteGoal("");
        assertFalse(success, "Deleting a non-existent goal should fail");
    }
}