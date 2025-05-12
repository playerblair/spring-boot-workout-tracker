package dev.playerblair.workout_tracker.workout.exception;

public class WorkoutNotFoundException extends RuntimeException {
    public WorkoutNotFoundException(Long id) {
        super("Workout not found with ID: " + id);
    }
}
