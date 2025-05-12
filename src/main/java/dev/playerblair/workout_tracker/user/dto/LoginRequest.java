package dev.playerblair.workout_tracker.user.dto;

public record LoginRequest(
        String username,
        String password
) {
}
