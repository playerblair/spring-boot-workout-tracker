package dev.playerblair.workout_tracker.workout;

import dev.playerblair.workout_tracker.exercise.model.Category;
import dev.playerblair.workout_tracker.exercise.model.Exercise;
import dev.playerblair.workout_tracker.exercise.exception.ExerciseNotFoundException;
import dev.playerblair.workout_tracker.exercise.repository.ExerciseRepository;
import dev.playerblair.workout_tracker.user.model.User;
import dev.playerblair.workout_tracker.user.repository.UserRepository;
import dev.playerblair.workout_tracker.workout.dto.ExercisePlanRequest;
import dev.playerblair.workout_tracker.workout.dto.WorkoutRequest;
import dev.playerblair.workout_tracker.workout.exception.UnauthorizedWorkoutAccessException;
import dev.playerblair.workout_tracker.workout.exception.WorkoutNotFoundException;
import dev.playerblair.workout_tracker.workout.model.ExercisePlan;
import dev.playerblair.workout_tracker.workout.model.Status;
import dev.playerblair.workout_tracker.workout.model.Workout;
import dev.playerblair.workout_tracker.workout.dto.WorkoutDTO;
import dev.playerblair.workout_tracker.workout.repository.WorkoutRepository;
import dev.playerblair.workout_tracker.workout.service.WorkoutService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssertions.thenThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class WorkoutServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private WorkoutRepository workoutRepository;

    @Mock
    private ExerciseRepository exerciseRepository;

    @InjectMocks
    private WorkoutService workoutService;

    private User user;
    private Exercise exercise;
    private ExercisePlan exercisePlan;
    private Workout workout;
    private ExercisePlanRequest exercisePlanRequest;
    private WorkoutRequest workoutRequest;

    private ArgumentCaptor<Workout> workoutCaptor = ArgumentCaptor.forClass(Workout.class);

    @BeforeEach
    public void setUp() {
        // user1
        user = new User(
                1L,
                "user",
                "pass"
        );
        exercise = new Exercise(
                1L,
                "Running",
                "Running at a steady pace on a flat surface or treadmill",
                Category.CARDIO,
                null
        );
        workout = new Workout(
                1L,
                "Full Body Workout",
                LocalDateTime.now(),
                Status.PENDING,
                "no comment",
                user
        );
        exercisePlan = new ExercisePlan(
                exercise,
                12,
                3,
                135,
                workout
        );
        workout.setExercises(List.of(exercisePlan));

        // request
        exercisePlanRequest = new ExercisePlanRequest(
                1L,
                12,
                3,
                135
        );
        workoutRequest = new WorkoutRequest(
                1L,
                "Full Body Workout",
                List.of(exercisePlanRequest),
                LocalDateTime.now(),
                Status.PENDING,
                "no comment"
        );

    }

    @AfterEach
    public void tearDown() {
        SecurityContextHolder.clearContext();
    }

    private void setUpSecurityContext(User user) {
        Authentication authentication = mock(Authentication.class);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        given(authentication.isAuthenticated()).willReturn(true);
        given(authentication.getName()).willReturn(user.getUsername());
        given(userRepository.getUserByUsername(user.getUsername())).willReturn(Optional.of(user));
    }

    private void setUpSecurityContext() {
        setUpSecurityContext(user);
    }

    @Test
    public void whenCreateWorkoutIsCalled_givenValidWorkoutRequest_thenCreateAndReturnWorkout() {
        // given
        given(exerciseRepository.findById(exercisePlanRequest.exerciseId()))
                .willReturn(Optional.of(exercise));

        given(workoutRepository.save(any(Workout.class)))
                .willReturn(workout);

        setUpSecurityContext();

        // when
        WorkoutDTO workoutDTO = workoutService.createWorkout(workoutRequest);

        // then
        verify(workoutRepository).save(workoutCaptor.capture());
        Workout capturedWorkout = workoutCaptor.getValue();
        then(capturedWorkout)
                .usingRecursiveComparison()
                .ignoringFields("id", "dateTime", "exercises.workout")
                .isEqualTo(workout);

        // and
        then(workoutDTO.name()).isEqualTo(capturedWorkout.getName());
        then(workoutDTO.exercises().getFirst().exercise()).isEqualTo(capturedWorkout.getExercises().getFirst().getExercise());
        then(workoutDTO.status()).isEqualTo(capturedWorkout.getStatus());
        then(workoutDTO.comment()).isEqualTo(capturedWorkout.getComment());
        then(workoutDTO.userId()).isEqualTo(capturedWorkout.getUser().getId());
    }

    @Test
    public void whenCreateWorkoutIsCalled_givenInvalidExerciseId_thenThrowException() {
        // given
        given(exerciseRepository.findById(exercisePlanRequest.exerciseId()))
                .willReturn(Optional.empty());

        // when & then
        thenThrownBy(() -> workoutService.createWorkout(workoutRequest))
                .isInstanceOf(ExerciseNotFoundException.class)
                .hasMessage("Exercise not found with ID: " + exercise.getId());
    }

    @Test
    public void whenUpdateWorkoutIsCalled_givenValidWorkoutRequest_thenUpdateAndReturnWorkout() {
        // given
        LocalDateTime dateTime = LocalDateTime.now();
        workoutRequest = new WorkoutRequest(
                1L,
                "Full Body Workout",
                List.of(exercisePlanRequest),
                dateTime,
                Status.PENDING,
                "updated exercise plan"
        );
        Workout updatedWorkout = new Workout(
                1L,
                "Full Body Workout",
                dateTime,
                Status.PENDING,
                "updated exercise plan",
                user
        );
        ExercisePlan updatedExercisePlan = new ExercisePlan(
                exercise,
                12,
                3,
                135,
                updatedWorkout
        );
        updatedWorkout.setExercises(List.of(updatedExercisePlan));

        setUpSecurityContext();

        given(workoutRepository.findById(workoutRequest.id()))
                .willReturn(Optional.of(workout));

        given(exerciseRepository.findById(exercisePlanRequest.exerciseId()))
                .willReturn(Optional.of(exercise));

        given(workoutRepository.save(any(Workout.class)))
                .willReturn(updatedWorkout);

        // when
        WorkoutDTO workoutDTO = workoutService.updateWorkout(workoutRequest);

        // then
        verify(workoutRepository).save(workoutCaptor.capture());
        Workout capturedWorkout = workoutCaptor.getValue();
        then(capturedWorkout)
                .usingRecursiveComparison()
                .isEqualTo(updatedWorkout);

        // and
        then(workoutDTO.id()).isEqualTo(capturedWorkout.getId());
        then(workoutDTO.name()).isEqualTo(capturedWorkout.getName());
        then(workoutDTO.exercises().getFirst().exercise()).isEqualTo(capturedWorkout.getExercises().getFirst().getExercise());
        then(workoutDTO.dateTime()).isEqualTo(capturedWorkout.getDateTime());
        then(workoutDTO.status()).isEqualTo(capturedWorkout.getStatus());
        then(workoutDTO.comment()).isEqualTo(capturedWorkout.getComment());
        then(workoutDTO.userId()).isEqualTo(capturedWorkout.getUser().getId());
    }

    @Test
    public void whenUpdateWorkoutIsCalled_givenInvalidId_thenThrowException() {
        // given
        given(workoutRepository.findById(workoutRequest.id()))
                .willReturn(Optional.empty());

        // when & then
        thenThrownBy(() -> workoutService.updateWorkout(workoutRequest))
                .isInstanceOf(WorkoutNotFoundException.class)
                .hasMessage("Workout not found with ID: " + workoutRequest.id());
    }

    @Test
    public void whenUpdateWorkoutIsCalled_givenUnauthorisedAccess_thenThrowException() {
        // given
        User differentUser = new User(
                2L,
                "john",
                "pass"
        );

        setUpSecurityContext(differentUser);

        given(workoutRepository.findById(workoutRequest.id()))
                .willReturn(Optional.of(workout));

        // when & then
        thenThrownBy(() -> workoutService.updateWorkout(workoutRequest))
                .isInstanceOf(UnauthorizedWorkoutAccessException.class)
                .hasMessage("User is not authorized to modify workout with ID: " + workout.getId());
    }

    @Test
    public void whenUpdateWorkoutIsCalled_givenInvalidExercise_thenThrowException() {
        // given
        setUpSecurityContext();

        given(workoutRepository.findById(workoutRequest.id()))
                .willReturn(Optional.of(workout));

        given(exerciseRepository.findById(exercisePlanRequest.exerciseId()))
                .willReturn(Optional.empty());

        // when & then
        thenThrownBy(() -> workoutService.updateWorkout(workoutRequest))
                .isInstanceOf(ExerciseNotFoundException.class)
                .hasMessage("Exercise not found with ID: " + exercisePlanRequest.exerciseId());
    }

    @Test
    public void whenDeleteWorkoutIsCalled_givenValidId_thenDeleteAndReturnWorkout() {
        // given
        Long id = workout.getId();

        setUpSecurityContext();

        given(workoutRepository.findById(id))
                .willReturn(Optional.of(workout));

        // when
        WorkoutDTO workoutDTO = workoutService.deleteWorkout(id);

        // then
        verify(workoutRepository).delete(workoutCaptor.capture());
        Workout capturedWorkout = workoutCaptor.getValue();
        then(capturedWorkout)
                .usingRecursiveComparison()
                .isEqualTo(workout);

        // and
        then(workoutDTO.id()).isEqualTo(capturedWorkout.getId());
        then(workoutDTO.name()).isEqualTo(capturedWorkout.getName());
        then(workoutDTO.exercises().getFirst().exercise()).isEqualTo(exercise);
        then(workoutDTO.dateTime()).isEqualTo(capturedWorkout.getDateTime());
        then(workoutDTO.status()).isEqualTo(capturedWorkout.getStatus());
        then(workoutDTO.comment()).isEqualTo(capturedWorkout.getComment());
    }

    @Test
    public void whenDeleteWorkoutIsCalled_givenUnauthorisedAccess_thenThrowException() {
        // given
        Long id = workout.getId();
        User differentUser = new User(
                2L,
                "john",
                "pass"
        );

        setUpSecurityContext(differentUser);

        given(workoutRepository.findById(id)).willReturn(Optional.of(workout));

        // when & then
        thenThrownBy(() -> workoutService.deleteWorkout(id))
                .isInstanceOf(UnauthorizedWorkoutAccessException.class)
                .hasMessage("User is not authorized to modify workout with ID: " + workout.getId());
    }

    @Test
    public void whenDeleteWorkoutIsCalled_givenInvalidId_thenThrowException() {
        // given
        Long id = 3L;

        given(workoutRepository.findById(id)).willReturn(Optional.empty());

        // when & then
        thenThrownBy(() -> workoutService.deleteWorkout(id))
                .isInstanceOf(WorkoutNotFoundException.class)
                .hasMessage("Workout not found with ID: " + id);
    }

    @Test
    public void whenScheduleWorkoutIsCalled_givenValidId_thenUpdateScheduleAndReturnWorkout() {
        // given
        workoutRequest = new WorkoutRequest(1L, LocalDateTime.now());
        Workout updatedWorkout = workout;
        updatedWorkout.setDateTime(workoutRequest.dateTime());

        setUpSecurityContext();

        given(workoutRepository.findById(workoutRequest.id()))
                .willReturn(Optional.of(workout));

        given(workoutRepository.save(any(Workout.class)))
                .willReturn(updatedWorkout);

        // when
        WorkoutDTO workoutDTO = workoutService.scheduleWorkout(workoutRequest);

        // then
        verify(workoutRepository).save(workoutCaptor.capture());
        Workout capturedWorkout = workoutCaptor.getValue();
        then(capturedWorkout)
                .usingRecursiveComparison()
                .isEqualTo(updatedWorkout);

        // and
        then(workoutDTO.id()).isEqualTo(capturedWorkout.getId());
        then(workoutDTO.name()).isEqualTo(capturedWorkout.getName());
        then(workoutDTO.exercises().getFirst().exercise()).isEqualTo(exercise);
        then(workoutDTO.dateTime()).isEqualTo(capturedWorkout.getDateTime());
        then(workoutDTO.status()).isEqualTo(capturedWorkout.getStatus());
        then(workoutDTO.comment()).isEqualTo(capturedWorkout.getComment());
    }

    @Test
    public void whenScheduleWorkoutIsCalled_givenUnauthorisedAccess_thenThrowException() {
        // given
        workoutRequest = new WorkoutRequest(1L, LocalDateTime.now());
        User differentUser = new User(
                2L,
                "john",
                "pass"
        );

        setUpSecurityContext(differentUser);

        given(workoutRepository.findById(workoutRequest.id())).willReturn(Optional.of(workout));

        // when & then
        thenThrownBy(() -> workoutService.scheduleWorkout(workoutRequest))
                .isInstanceOf(UnauthorizedWorkoutAccessException.class)
                .hasMessage("User is not authorized to modify workout with ID: " + workout.getId());
    }

    @Test
    public void whenScheduleWorkoutIsCalled_givenInvalidId_thenThrowException() {
        // setup test data
        workoutRequest = new WorkoutRequest(3L, LocalDateTime.now());

        // mock repository behaviour
        given(workoutRepository.findById(workoutRequest.id()))
                .willReturn(Optional.empty());

        // verify interactions + assertions
        assertThatThrownBy(() -> workoutService.scheduleWorkout(workoutRequest))
                .isInstanceOf(WorkoutNotFoundException.class)
                .hasMessage("Workout not found with ID: " + workoutRequest.id());
    }

    @Test
    public void whenGetWorkoutsIsCalled_thenReturnWorkouts() {
        // given
        setUpSecurityContext();

        given(workoutRepository.findByUserId(user.getId()))
                .willReturn(List.of(workout));

        // when
        List<WorkoutDTO> workouts = workoutService.getWorkouts();

        // then
        then(workouts).hasSize(1);
    }

    @Test
    public void whenGetWorkoutIsCalled_givenValidStatus_thenReturnWorkouts() {
        // given
        Status status = Status.PENDING;
        setUpSecurityContext();

        given(workoutRepository.findByUserIdAndStatus(user.getId(), status))
                .willReturn(List.of(workout));

        // when
        List<WorkoutDTO> workouts = workoutService.getWorkouts(status);

        // then
        then(workouts).hasSize(1);
    }

    @Test
    public void whenGenerateReportIsCalled_givenUserHasNoWorkouts_returnReport() {
        // given
        setUpSecurityContext();

        given(workoutRepository.findByUserId(user.getId()))
                .willReturn(List.of());

        // when
        String report = workoutService.generateReport();

        // then
        then(report).contains(List.of(user.getUsername(), "No workouts found."));
    }

    @Test
    public void whenGenerateReportIsCalled_givenUserHasWorkouts_returnReport() {
        // given
        setUpSecurityContext();

        given(workoutRepository.findByUserId(user.getId()))
                .willReturn(List.of(workout));

        // when
        String report = workoutService.generateReport();

        // then
        then(report).contains(List.of(user.getUsername(), "Workout", workout.getName(), "Exercise", exercise.getName()));
    }
}
