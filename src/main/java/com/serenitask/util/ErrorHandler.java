package com.serenitask.util;

/**
 * ErrorHandler class for handling exceptions and errors in the application.
 */
public class ErrorHandler {
    /**
     * Custom error handling method to log exceptions and print stack traces.
     *
     * @param e The exception to be handled.
     */
    public static void handleException(Exception e) {
        // Log the exception
        System.err.println("An error occurred: " + e.getMessage());
        // Print the stack trace
        e.printStackTrace();
        // More error handling code could be added here, such as displaying a user-friendly error message
    }
}
