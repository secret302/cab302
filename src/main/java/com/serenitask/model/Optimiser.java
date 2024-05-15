package com.serenitask.model;

import com.calendarfx.model.CalendarSource;
import com.serenitask.controller.RoutineOneController;
import com.serenitask.controller.RoutineTwoController;

import java.time.LocalTime;

/**
 * Class object handling the optimization of user calendars.
 * Made up of multiple routines that are executed sequentially.
 */
public class Optimiser {

    /**
     * Method that runs routines that make up the optimizer.
     *
     * @param calendarSource Contains all calendars used in application
     */
    public static void optimize(CalendarSource calendarSource, LocalTime userDayStart, LocalTime userDayEnd, int allocateAhead ) {
        try {
            RoutineOneController routineOne = new RoutineOneController(userDayStart, userDayEnd, allocateAhead);
            RoutineTwoController routineTwo = new RoutineTwoController(userDayStart, userDayEnd);

            routineOne.runRoutine(calendarSource);
            routineTwo.runRoutine(calendarSource);
        }
        catch(Exception e){
            System.err.println("An error occurred while running the routines for the Schedule Optimiser: " + e.getMessage());
            e.printStackTrace();
        }
    }
};

