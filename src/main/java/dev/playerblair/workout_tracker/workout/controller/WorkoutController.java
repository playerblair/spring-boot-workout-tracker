package dev.playerblair.workout_tracker.workout.controller;

import dev.playerblair.workout_tracker.workout.dto.WorkoutDTO;
import dev.playerblair.workout_tracker.workout.dto.WorkoutRequest;
import dev.playerblair.workout_tracker.workout.service.WorkoutService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/workouts")
public class WorkoutController {

    private final WorkoutService workoutService;

    public WorkoutController(WorkoutService workoutService) {
        this.workoutService = workoutService;
    }

    @PostMapping
    public ResponseEntity<WorkoutDTO> createWorkout(@RequestBody WorkoutRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(workoutService.createWorkout(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<WorkoutDTO> updateWorkout(@PathVariable Long id, @RequestBody WorkoutRequest request) {
        return ResponseEntity.ok(workoutService.updateWorkout(request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<WorkoutDTO> deleteWorkout(@PathVariable Long id) {
        return ResponseEntity.ok(workoutService.deleteWorkout(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<WorkoutDTO> scheduleWorkout(@PathVariable Long id, @RequestBody WorkoutRequest request) {
        return ResponseEntity.ok(workoutService.scheduleWorkout(request));
    }

    @GetMapping
    public ResponseEntity<List<WorkoutDTO>> getWorkouts() {
        return ResponseEntity.ok(workoutService.getWorkouts());
    }

    @GetMapping("/report")
    public ResponseEntity<String> generateReport() {
        return ResponseEntity.ok(workoutService.generateReport());
    }

}
