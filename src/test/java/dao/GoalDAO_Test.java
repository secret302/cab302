package dao;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

import com.serenitask.model.Goal;
import com.serenitask.util.DatabaseManager.GoalDAO;

// GoalDAOTest class tests the GoalDAO class
public class GoalDAOTest {
    private GoalDAO goalDAO = new GoalDAO(); // Assuming you have a constructor for GoalDAO
    private int goalId = 0; // Example goal ID for testing

    private Goal createTestGoal() {
        // Generate a unique title or use other attributes to ensure uniqueness
        String uniqueTitle = "Test Goal " + System.currentTimeMillis();
        return new Goal(uniqueTitle, "Test description"); // Fill in other goal details
    }

    @AfterEach
    public void tearDown() {
        // Delete the goal after testing
        goalDAO.deleteGoal(goalId);
        goalId = 0;
    }

    @Test
    public void testCreateGoal() {
        // Create an goal and check if the goal ID is greater than 0
        goalId = goalDAO.addGoal(createTestGoal());
        assertTrue(goalId > 0, "Goal ID should be greater than 0");

        // Verify details of the created goal
        Goal createdGoal = goalDAO.getGoalById(goalId);
        assertNotNull(createdGoal.getId(), "Created goal should not be null");
        assertEquals(goalId, createdGoal.getId(), "Goal ID should match");
        // ADD MORE HERE
    }

    @Test
    public void testGetGoalById() {
        // Create an goal for testing
        goalId = goalDAO.addGoal(createTestGoal());

        // Get an goal by ID and check if it is not null
        Goal goal = goalDAO.getGoalById(goalId);
        assertNotNull(goal.getId(), "Goal should not be null");
    }

    @Test
    public void testEditGoal() {
        // Create an goal for testing
        goalId = goalDAO.addGoal(createTestGoal());

        // Edit an goal and check if successful
        goal = goalDAO.getGoalById(goalId);
        goal.setTitle("New Title");
        goal.setDescription("New Description");
        boolean success = goalDAO.editGoal(goal);
        assertTrue(success, "Goal should be edited successfully");

        // Check if the changes are reflected
        goal = goalDAO.getGoalById(goalId);
        assertEquals("New Title", goal.getTitle(), "Title should be updated");
        assertEquals("New Description", goal.getDescription(), "Description should be updated");
        // ADD MORE HERE (e.g., check other attributes)
    }

    @Test
    public void testDeleteGoal() {
        // Create an goal for testing
        goalId = goalDAO.addGoal(createTestGoal());

        // Delete an goal and check if successful
        boolean success = goalDAO.deleteGoal(goalId);
        assertTrue(success, "Goal should be deleted successfully");

        // Check if the goal is deleted
        Goal goal = goalDAO.getGoalById(goalId);
        assertNull(goal.getId(), "Goal should not exist");
    }

    @Test
    public void testGetGoals() {
        // Create more goals for testing
        int goalID1 = goalDAO.addGoal(createTestGoal());
        int goalID2 = goalDAO.addGoal(createTestGoal());
        int goalID3 = goalDAO.addGoal(createTestGoal());

        // Get all goals at date and check if the list is equal to 2
        List<Goal> goals = goalDAO.getGoals(date);
        assertEquals(2, goals.size(), "List should contain 2 goals");

        // Get all goals between two dates and check if the list is equal to 2
        goals = goalDAO.getGoals(startDate, endDate);
        assertEquals(2, goals.size(), "List should contain 2 goals");

        // Get all goals and check if the list is equal to 3
        goals = goalDAO.getGoals();
        assertEquals(3, goals.size(), "List should contain 3 goals");

        // Delete the goals after testing
        goalDAO.deleteGoal(goalID1);
        goalDAO.deleteGoal(goalID2);
        goalDAO.deleteGoal(goalID3);
    }

    @Test
    public void testDeleteNonExistentGoal() {
        int nonExistentGoalId = -1; // Or any ID that doesn't exist
        boolean success = goalDAO.deleteGoal(nonExistentGoalId);
        assertFalse(success, "Deleting a non-existent goal should fail");
    }
}