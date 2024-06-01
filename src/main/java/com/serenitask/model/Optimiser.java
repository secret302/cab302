package com.serenitask.model;

import com.calendarfx.model.CalendarSource;
import com.serenitask.controller.RoutineOneController;
import com.serenitask.controller.RoutineTwoController;
import com.serenitask.util.ErrorHandler;

import java.time.LocalTime;

/**
 * The Optimizer class orchestrates the optimization of the user's calendar by
 * executing a series of predefined routines sequentially. These routines aim to
 * efficiently allocate goals and maintain a healthy balance between work and
 * personal activities.
 */
public class Optimiser {

    /**
     * Executes the optimization routines on the given calendar source.
     * Currently, this includes:
     * <ul>
     *     <li>Routine 1: Allocates goals based on required time per period.</li>
     *     <li>Routine 2: Ensures a minimum ratio between work and health activities.</li>
     * </ul>
     *
     * @param calendarSource The CalendarSource object containing the calendars to be optimized.
     */
    public static void optimize(CalendarSource calendarSource) {
        try {

            // SettingsDAO unfinished, Dummy values used as development time unavailable to make routines dynamic
            LocalTime userDayStart = LocalTime.of(8, 0, 0);
            LocalTime userDayEnd = LocalTime.of(18, 30, 0);
            int allocateAhead = 7;

            RoutineOneController routineOne = new RoutineOneController(userDayStart, userDayEnd, allocateAhead);
            RoutineTwoController routineTwo = new RoutineTwoController(userDayStart, userDayEnd, allocateAhead);

            routineOne.runRoutine(calendarSource);
            routineTwo.runRoutine(calendarSource);

        } catch (Exception e) {
            System.err.println("An error occurred while running the Schedule Optimizer routines: " + e.getMessage());
            ErrorHandler.handleException(e);
        }
    }
}