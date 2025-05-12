package dev.playerblair.workout_tracker.exercise.repository;

import dev.playerblair.workout_tracker.exercise.model.Exercise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExerciseRepository extends JpaRepository<Exercise, Long> {
}
