package controller;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.serenitask.model.Goal;
import com.serenitask.util.DatabaseManager.GoalDAO;
import com.serenitask.controller.GoalController;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class GoalControllerTest {
    // GoalController object
    private final GoalController goalController = new GoalController();
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

    @Test
    public void testLoadSimpleGoal() {
        // Add test goals to the database
        int goalId1 = goalDAO.addGoal(createTestGoal(testTime));
        int goalId2 = goalDAO.addGoal(createTestGoal(testTime));
        int goalId3 = goalDAO.addGoal(createTestGoal(testTime));

        // Call loadSimpleGoal method
        List<String> result = goalController.loadSimpleGoal();
        // Verify that the result is not null
        assertNotNull(result);

        // Verify that the result contains the correct number of goals
        assertEquals(3, result.size());
        // Verify that the result contains the correct goals
        assertEquals("Test Goal", result.get(0));
        assertEquals("Test Goal", result.get(1));
        assertEquals("Test Goal", result.get(2));

        // Delete the test goals from goalController
        goalController.deleteGoal(goalId1);
        goalController.deleteGoal(goalId2);
        goalController.deleteGoal(goalId3);
    }

    @Test
    public void testControlSimpleGoal() {
        // Call controlSimpleGoal method
        goalController.controlSimpleGoal("Test Goal");

        // Verify that the goal is added to the database
        List<Goal> goals = goalDAO.getAllGoals();
        assertEquals(1, goals.size());

        // Verify that the goal has the correct components
        assertEquals("Test Goal", goals.get(0).getTitle());
        assertEquals(15, goals.get(0).getMinChunk());
        assertEquals(60, goals.get(0).getMaxChunk());

        // Delete the test goal from goalController
        goalController.deleteGoal(goals.get(0).getId());
    }

    @Test
    public void testCheckIfEmpty_NotEmpty() {
        // Add a test goal to the database
        int goalID = goalDAO.addGoal(createTestGoal(testTime));

        // Call checkIfEmpty method
        boolean result = goalController.checkIfEmpty();
        // Verify that the result is false
        assertFalse(result);

        // Delete the test goal from the goalController
        goalController.deleteGoal(goalID);
    }

    @Test
    public void testCheckIfEmpty_Empty() {
        // Call checkIfEmpty method
        boolean result = goalController.checkIfEmpty();
        // Verify that the result is true
        assertTrue(result);
    }

    @Test
    public void testReturnFirstGoal_NotEmpty() {
        // Add test goal to the database
        int goalId = goalDAO.addGoal(new Goal(
                "Correct Test Goal",
                1,
                15,
                60,
                testTime.toLocalDate(),
                0
        ));


        // Call returnFirstGoal method
        Goal result = goalController.returnFirstGoal();
        // Verify that the result is not null
        assertNotNull(result);

        // Verify that the result is the first goal in the database
        assertEquals("Correct Test Goal", result.getTitle());
        assertEquals(15, result.getMinChunk());
        assertEquals(60, result.getMaxChunk());

        // Delete the test goal from the goalController
        goalController.deleteGoal(goalId);
    }

    @Test
    public void testReturnFirstGoal_Empty() {
        // Call returnFirstGoal method
        Goal result = goalController.returnFirstGoal();
        // Verify that the result is null
        assertNull(result);
    }

    @Test
    public void testDeleteGoal() {
        // Add a test goal to the database
        int goalID = goalDAO.addGoal(createTestGoal(testTime));

        // Call deleteGoal method
        goalController.deleteGoal(goalID);

        // Verify that the goal is deleted from the database
        Goal goal = goalDAO.getGoalById(goalID);
        assertNull(goal);
    }
}
