package com.serenitask.model;

import com.calendarfx.model.CalendarSource;
import com.serenitask.controller.RoutineOneController;
import com.serenitask.controller.RoutineTwoController;

import java.time.LocalTime;

/**
 * Class object handling the optimization of user calendars.
 * Made up of multiple routines that are executed sequentially.
 */
public class Optimizer {

    private final LocalTime userDayStart = LocalTime.of(8, 0, 0);
    private final LocalTime userDayEnd = LocalTime.of(18, 30, 0);
    private final int allocateAhead = 7;


    /**
     * Method that runs routines that make up the optimizer.
     *
     * @param calendarSource Contains all calendars used in application
     */
    public void optimize(CalendarSource calendarSource) {
        RoutineOneController routineOne = new RoutineOneController(userDayStart, userDayEnd, allocateAhead);
        RoutineTwoController routineTwo = new RoutineTwoController(userDayStart, userDayEnd, allocateAhead);

        routineOne.runRoutine(calendarSource);
        routineTwo.runRoutine(calendarSource);
    }

    ;


};

