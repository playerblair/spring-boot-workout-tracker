package dev.playerblair.workout_tracker.workout.dto;

import dev.playerblair.workout_tracker.exercise.model.Exercise;

public record ExercisePlanDTO(
        Long id,
        Exercise exercise,
        Integer reps,
        Integer sets,
        Integer weights,
        Long workoutId
) {
}
