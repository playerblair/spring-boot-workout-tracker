package dev.playerblair.workout_tracker.exercise.exception;

public class ExerciseNotFoundException extends RuntimeException {
    public ExerciseNotFoundException(Long id) {
        super("Exercise not found with ID: " + id);
    }
}
