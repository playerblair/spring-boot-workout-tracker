package dev.playerblair.workout_tracker.workout.dto;

import dev.playerblair.workout_tracker.workout.model.Status;

import java.time.LocalDateTime;
import java.util.List;

public record WorkoutRequest(
        Long id,
        String name,
        List<ExercisePlanRequest> exercises,
        LocalDateTime dateTime,
        Status status,
        String comment
) {

    public WorkoutRequest(String name, List<ExercisePlanRequest> exercises, LocalDateTime dateTime, Status status, String comment) {
        this(null, name, exercises, dateTime, status, comment);
    }

    public WorkoutRequest(Long id, LocalDateTime dateTime) {
        this(id, null, null, dateTime, null, null);
    }
}

