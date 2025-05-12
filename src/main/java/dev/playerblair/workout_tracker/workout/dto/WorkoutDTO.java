package dev.playerblair.workout_tracker.workout.dto;

import dev.playerblair.workout_tracker.workout.model.Status;

import java.time.LocalDateTime;
import java.util.List;

public record WorkoutDTO(
        Long id,
        String name,
        List<ExercisePlanDTO> exercises,
        LocalDateTime dateTime,
        Status status,
        String comment,
        Long userId
        ) {
}
