package dev.playerblair.workout_tracker.workout.dto;

public record ExercisePlanRequest(
        Long exerciseId,
        Integer reps,
        Integer sets,
        Integer weights
) {
}
