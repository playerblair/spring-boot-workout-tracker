package dev.playerblair.workout_tracker.workout.service;

import dev.playerblair.workout_tracker.exercise.exception.ExerciseNotFoundException;
import dev.playerblair.workout_tracker.exercise.repository.ExerciseRepository;
import dev.playerblair.workout_tracker.user.model.User;
import dev.playerblair.workout_tracker.user.repository.UserRepository;
import dev.playerblair.workout_tracker.workout.dto.ExercisePlanDTO;
import dev.playerblair.workout_tracker.workout.dto.ExercisePlanRequest;
import dev.playerblair.workout_tracker.workout.dto.WorkoutRequest;
import dev.playerblair.workout_tracker.workout.exception.UnauthorizedWorkoutAccessException;
import dev.playerblair.workout_tracker.workout.exception.WorkoutNotFoundException;
import dev.playerblair.workout_tracker.workout.model.ExercisePlan;
import dev.playerblair.workout_tracker.workout.model.Status;
import dev.playerblair.workout_tracker.workout.model.Workout;
import dev.playerblair.workout_tracker.workout.dto.WorkoutDTO;
import dev.playerblair.workout_tracker.workout.repository.WorkoutRepository;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class WorkoutService {

    private final UserRepository userRepository;
    private final WorkoutRepository workoutRepository;
    private final ExerciseRepository exerciseRepository;

    public WorkoutService(UserRepository userRepository, WorkoutRepository workoutRepository, ExerciseRepository exerciseRepository) {
        this.userRepository = userRepository;
        this.workoutRepository = workoutRepository;
        this.exerciseRepository = exerciseRepository;
    }

    public WorkoutDTO createWorkout(WorkoutRequest request) {
        Workout workout = new Workout(
                request.name(),
                request.dateTime(),
                request.status(),
                request.comment(),
                getCurrentUser()
        );
        workout.setExercises(generateExercisePlans(workout, request.exercises()));

        return convertToDto(workoutRepository.save(workout));
    }

    public WorkoutDTO updateWorkout(WorkoutRequest request) {
        return workoutRepository.findById(request.id())
                .map(workout -> {
                    if (!getCurrentUser().getId().equals(workout.getUser().getId())) {
                        throw new UnauthorizedWorkoutAccessException(request.id());
                    }
                    workout.setName(request.name());
                    workout.getExercises().clear();
                    workout.getExercises().addAll(generateExercisePlans(workout, request.exercises()));
                    workout.setDateTime(request.dateTime());
                    workout.setStatus(request.status());
                    workout.setComment(request.comment());
                    return convertToDto(workoutRepository.save(workout));
                })
                .orElseThrow(() -> new WorkoutNotFoundException(request.id()));
    }

    public WorkoutDTO deleteWorkout(Long id) {
        return workoutRepository.findById(id)
                .map(workout -> {
                    if (!getCurrentUser().getId().equals(workout.getUser().getId())) {
                        throw new UnauthorizedWorkoutAccessException(id);
                    }
                    workoutRepository.delete(workout);
                    return convertToDto(workout);
                })
                .orElseThrow(() -> new WorkoutNotFoundException(id));
    }

    public WorkoutDTO scheduleWorkout(WorkoutRequest request) {
        return workoutRepository.findById(request.id())
                .map(workout -> {
                    if (!getCurrentUser().getId().equals(workout.getUser().getId())) {
                        throw new UnauthorizedWorkoutAccessException(request.id());
                    }
                    workout.setDateTime(request.dateTime());
                    return convertToDto(workoutRepository.save(workout));
                })
                .orElseThrow(() -> new WorkoutNotFoundException(request.id()));
    }

    public List<WorkoutDTO> getWorkouts() {
        return workoutRepository.findByUserId(getCurrentUser().getId()).stream()
                .map(this::convertToDto)
                .toList();
    }

    public List<WorkoutDTO> getWorkouts(Status status) {
        return workoutRepository.findByUserIdAndStatus(getCurrentUser().getId(), status).stream()
                .map(this::convertToDto)
                .toList();
    }

    public String generateReport() {
        User user = getCurrentUser();
        List<Workout> workouts = workoutRepository.findByUserId(user.getId());

        StringBuilder report = new StringBuilder();
        report.append("Workout Report: ").append(user.getUsername()).append("\n\n");

        if (workouts.isEmpty()) {
            report.append("No workouts found.");
            return report.toString();
        }

        report.append("Total Workouts: ").append(workouts.size()).append("\n\n");
        for (Workout workout: workouts) {
            report.append("Workout: ").append(workout.getName()).append("\n");
            report.append("Date & Time: ").append(workout.getDateTime()).append("\n");
            report.append("Status: ").append(workout.getStatus()).append("\n");
            report.append("Comment: ").append(workout.getComment()).append("\n");

            report.append("Exercises: ").append("\n");
            for (ExercisePlan exercise: workout.getExercises()) {
                report.append("\t").append("Name: ").append(exercise.getExercise().getName()).append(",");
                report.append("Reps: ").append(exercise.getReps()).append(",");
                report.append("Sets: ").append(exercise.getSets()).append(",");
                report.append("Weights: ").append(exercise.getWeights());
            }
        }

        return report.toString();
    }

    private List<ExercisePlan> generateExercisePlans(Workout workout, List<ExercisePlanRequest> requests) {
        List<ExercisePlan> exercisePlans = new ArrayList<>();
        requests.forEach( exercisePlanRequest -> {
            exerciseRepository.findById(exercisePlanRequest.exerciseId())
                    .map(exercise -> exercisePlans.add(
                            new ExercisePlan(
                                    exercise,
                                    exercisePlanRequest.reps(),
                                    exercisePlanRequest.sets(),
                                    exercisePlanRequest.weights(),
                                    workout
                            )
                    ))
                    .orElseThrow(() -> new ExerciseNotFoundException(exercisePlanRequest.exerciseId()));
        });

        return exercisePlans;
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() ||
                authentication instanceof AnonymousAuthenticationToken) {
            return null;
        }

        Optional<User> optionalUser = userRepository.getUserByUsername(authentication.getName());

        return optionalUser.orElse(null);

    }

    private WorkoutDTO convertToDto(Workout workout) {
        List<ExercisePlanDTO> exercises = workout.getExercises().stream()
                .map(exercisePlan -> new ExercisePlanDTO(
                        exercisePlan.getId(),
                        exercisePlan.getExercise(),
                        exercisePlan.getReps(),
                        exercisePlan.getSets(),
                        exercisePlan.getWeights(),
                        exercisePlan.getWorkout().getId())
                )
                .toList();

        return new WorkoutDTO(
                workout.getId(),
                workout.getName(),
                exercises,
                workout.getDateTime(),
                workout.getStatus(),
                workout.getComment(),
                workout.getUser().getId()
        );
    }
}
