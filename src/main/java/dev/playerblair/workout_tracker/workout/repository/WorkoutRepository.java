package dev.playerblair.workout_tracker.workout.repository;

import dev.playerblair.workout_tracker.workout.model.Status;
import dev.playerblair.workout_tracker.workout.model.Workout;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface WorkoutRepository extends JpaRepository<Workout, Long> {

    List<Workout> findByUserId(Long id);

    @Query("SELECT workout FROM Workout workout " +
            "WHERE workout.user.id = :id AND workout.status = :status " +
            "ORDER BY workout.dateTime DESC")
    List<Workout> findByUserIdAndStatus(Long id, Status status);
}
