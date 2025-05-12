package dev.playerblair.workout_tracker.exercise.controller;

import dev.playerblair.workout_tracker.exercise.exception.ExerciseNotFoundException;
import dev.playerblair.workout_tracker.exercise.repository.ExerciseRepository;
import dev.playerblair.workout_tracker.exercise.model.Exercise;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/exercises")
public class ExerciseController {

    private ExerciseRepository exerciseRepository;

    public ExerciseController(ExerciseRepository exerciseRepository) {
        this.exerciseRepository = exerciseRepository;
    }

    @GetMapping
    public ResponseEntity<List<Exercise>> getAllExercises() {
        return ResponseEntity.ok(exerciseRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Exercise> getExercise(@PathVariable Long id) {
        return exerciseRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ExerciseNotFoundException(id));
    }
}
