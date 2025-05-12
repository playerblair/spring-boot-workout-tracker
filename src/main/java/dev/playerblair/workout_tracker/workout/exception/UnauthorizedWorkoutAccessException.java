package dev.playerblair.workout_tracker.workout.exception;

public class UnauthorizedWorkoutAccessException extends RuntimeException {
    public UnauthorizedWorkoutAccessException(Long id) {
        super("User is not authorized to modify workout with ID: " + id);
    }
}
