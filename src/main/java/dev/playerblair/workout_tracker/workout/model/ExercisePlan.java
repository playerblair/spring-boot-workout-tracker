package dev.playerblair.workout_tracker.workout.model;

import dev.playerblair.workout_tracker.exercise.model.Exercise;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ExercisePlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "exercise_id")
    private Exercise exercise;

    private Integer reps;
    private Integer sets;
    private Integer weights;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "workout_id", nullable = false)
    private Workout workout;

    public ExercisePlan(Exercise exercise, Integer reps, Integer sets, Integer weights, Workout workout) {
        this.exercise = exercise;
        this.reps = reps;
        this.sets = sets;
        this.weights = weights;
        this.workout = workout;
    }
}
