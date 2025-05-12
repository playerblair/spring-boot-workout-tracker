package dev.playerblair.workout_tracker.workout;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.playerblair.workout_tracker.exercise.model.Category;
import dev.playerblair.workout_tracker.exercise.model.Exercise;
import dev.playerblair.workout_tracker.exercise.exception.ExerciseNotFoundException;
import dev.playerblair.workout_tracker.user.model.User;
import dev.playerblair.workout_tracker.workout.dto.ExercisePlanDTO;
import dev.playerblair.workout_tracker.workout.dto.ExercisePlanRequest;
import dev.playerblair.workout_tracker.workout.dto.WorkoutRequest;
import dev.playerblair.workout_tracker.workout.exception.UnauthorizedWorkoutAccessException;
import dev.playerblair.workout_tracker.workout.exception.WorkoutNotFoundException;
import dev.playerblair.workout_tracker.workout.model.ExercisePlan;
import dev.playerblair.workout_tracker.workout.model.Status;
import dev.playerblair.workout_tracker.workout.model.Workout;
import dev.playerblair.workout_tracker.workout.dto.WorkoutDTO;
import dev.playerblair.workout_tracker.workout.service.WorkoutService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class WorkoutControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private WorkoutService workoutService;

    private User user;
    private Exercise exercise;
    private ExercisePlanRequest exercisePlanRequest;
    private WorkoutRequest workoutRequest;
    private ExercisePlan exercisePlan;
    private Workout workout;
    private WorkoutDTO workoutDTO;

    @BeforeEach
    public void setUp() {
        user = new User(
                1L,
                "user",
                "pass",
                "USER"
        );

        exercisePlanRequest = new ExercisePlanRequest(
                1L,
                12,
                3,
                135
        );

        workoutRequest = new WorkoutRequest(
                "Full Body Workout",
                List.of(exercisePlanRequest),
                LocalDateTime.now(),
                Status.PENDING,
                "no comment"
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
                null,
                null,
                null,
                workout
        );
        workout.setExercises(List.of(exercisePlan));

        workoutDTO = new WorkoutDTO(
                workout.getId(),
                workout.getName(),
                List.of(new ExercisePlanDTO(
                        exercisePlan.getId(),
                        exercisePlan.getExercise(),
                        exercisePlan.getReps(),
                        exercisePlan.getSets(),
                        exercisePlan.getWeights(),
                        exercisePlan.getWorkout().getId()
                )),
                workout.getDateTime(),
                workout.getStatus(),
                workout.getComment(),
                workout.getUser().getId()
        );
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void whenCreateWorkoutIsCalled_givenValidWorkoutRequest_thenReturn201AndWorkout() throws Exception {
        // given
        String requestJson = objectMapper.writeValueAsString(workoutRequest);
        String responseJson = objectMapper.writeValueAsString(workoutDTO);

        given(workoutService.createWorkout(workoutRequest)).willReturn(workoutDTO);

        // when & then
        mockMvc.perform(post("/api/workouts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(responseJson));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void whenCreateWorkoutIsCalled_givenInvalidWorkoutRequest_thenReturn404AndErrorResponse() throws Exception {
        // given
        String requestJson = objectMapper.writeValueAsString(workoutRequest);

        given(workoutService.createWorkout(workoutRequest)).willThrow(new ExerciseNotFoundException(3L));

        // when & then
        mockMvc.perform(post("/api/workouts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Exercise not found with ID: 3"));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void whenUpdateWorkoutIsCalled_givenValidWorkoutRequest_thenReturn200AndReturnWorkout() throws Exception {
        // given
        workoutRequest = new WorkoutRequest(
                1L,
                "Full Body Workout",
                List.of(exercisePlanRequest),
                LocalDateTime.now(),
                Status.PENDING,
                "updated exercise plan"
        );
        workout = new Workout(
                1L,
                "Full Body Workout",
                LocalDateTime.now(),
                Status.PENDING,
                "updated exercise plan",
                user
        );
        workoutDTO = new WorkoutDTO(
                workout.getId(),
                workout.getName(),
                List.of(new ExercisePlanDTO(
                        exercisePlan.getId(),
                        exercisePlan.getExercise(),
                        exercisePlan.getReps(),
                        exercisePlan.getSets(),
                        exercisePlan.getWeights(),
                        exercisePlan.getWorkout().getId()
                )),
                workout.getDateTime(),
                workout.getStatus(),
                workout.getComment(),
                workout.getUser().getId()
        );
        String requestJson = objectMapper.writeValueAsString(workoutRequest);
        String responseJson = objectMapper.writeValueAsString(workoutDTO);

        given(workoutService.updateWorkout(workoutRequest)).willReturn(workoutDTO);

        // when & then
        mockMvc.perform(put("/api/workouts/" + workoutRequest.id())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(responseJson));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void whenUpdateWorkoutIsCalled_givenInvalidId_thenReturn404AndErrorResponse() throws Exception {
        // given
        workoutRequest = new WorkoutRequest(
                1L,
                "Full Body Workout",
                List.of(exercisePlanRequest),
                LocalDateTime.now(),
                Status.PENDING,
                "updated exercise plan"
        );
        String requestJson = objectMapper.writeValueAsString(workoutRequest);

        given(workoutService.updateWorkout(workoutRequest)).willThrow(new WorkoutNotFoundException(workoutRequest.id()));

        // when & then
        mockMvc.perform(put("/api/workouts/" + workoutRequest.id())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Workout not found with ID: " + workoutRequest.id()));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void whenUpdateWorkoutIsCalled_givenUnauthorisedAccess_thenReturn400AndErrorResponse() throws Exception {
        // given
        workoutRequest = new WorkoutRequest(
                1L,
                "Full Body Workout",
                List.of(exercisePlanRequest),
                LocalDateTime.now(),
                Status.PENDING,
                "updated exercise plan"
        );
        String requestJson = objectMapper.writeValueAsString(workoutRequest);

        given(workoutService.updateWorkout(workoutRequest)).willThrow(new UnauthorizedWorkoutAccessException(workoutRequest.id()));

        // when & then
        mockMvc.perform(put("/api/workouts/" + workoutRequest.id())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("User is not authorized to modify workout with ID: " +  workoutRequest.id()));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void whenUpdateWorkoutIsCalled_givenInvalidExercise_thenReturn404AndErrorResponse() throws Exception {
        // given
        workoutRequest = new WorkoutRequest(
                1L,
                "Full Body Workout",
                List.of(exercisePlanRequest),
                LocalDateTime.now(),
                Status.PENDING,
                "updated exercise plan"
        );
        String requestJson = objectMapper.writeValueAsString(workoutRequest);

        given(workoutService.updateWorkout(workoutRequest)).willThrow(new ExerciseNotFoundException(exercisePlanRequest.exerciseId()));

        // when & then
        mockMvc.perform(put("/api/workouts/" + workoutRequest.id())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Exercise not found with ID: " + exercisePlanRequest.exerciseId()));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void whenDeleteWorkoutIsCalled_givenValidId_thenReturn200AndDeletedWorkout() throws Exception {
        // given
        String responseJson = objectMapper.writeValueAsString(workoutDTO);

        given(workoutService.deleteWorkout(workout.getId())).willReturn(workoutDTO);

        // when & then
        mockMvc.perform(delete("/api/workouts/" + workout.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(responseJson));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void whenDeleteWorkoutIsCalled_givenInvalidId_thenReturn404AndErrorResponse() throws Exception {
        // given
        given(workoutService.deleteWorkout(workout.getId())).willThrow(new WorkoutNotFoundException(workout.getId()));

        // when & then
        mockMvc.perform(delete("/api/workouts/" + workout.getId()))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Workout not found with ID: " + workout.getId()));

    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void whenDeleteWorkoutIsCalled_givenUnauthorisedAccess_thenReturn400AndErrorResponse() throws Exception {
        // given
        given(workoutService.deleteWorkout(workout.getId())).willThrow(new UnauthorizedWorkoutAccessException(workout.getId()));

        // when & then
        mockMvc.perform(delete("/api/workouts/" + workout.getId()))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("User is not authorized to modify workout with ID: " +  workout.getId()));

    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void whenScheduleWorkoutIsCalled_givenValidWorkoutRequest_thenReturn200AndUpdatedWorkout() throws Exception {
        // given
        LocalDateTime dateTime = LocalDateTime.now();
        workoutRequest = new WorkoutRequest(1L, dateTime);
        workout.setDateTime(dateTime);
        workoutDTO = new WorkoutDTO(
                workout.getId(),
                workout.getName(),
                List.of(new ExercisePlanDTO(
                        exercisePlan.getId(),
                        exercisePlan.getExercise(),
                        exercisePlan.getReps(),
                        exercisePlan.getSets(),
                        exercisePlan.getWeights(),
                        exercisePlan.getWorkout().getId()
                )),
                workout.getDateTime(),
                workout.getStatus(),
                workout.getComment(),
                workout.getUser().getId()
        );
        String requestJson = objectMapper.writeValueAsString(workoutRequest);
        String responseJson = objectMapper.writeValueAsString(workoutDTO);

        given(workoutService.scheduleWorkout(workoutRequest)).willReturn(workoutDTO);

        // when & then
        mockMvc.perform(patch("/api/workouts/" + workoutRequest.id())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(responseJson));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void whenScheduleWorkoutIsCalled_givenInvalidId_thenReturn404AndErrorResponse() throws Exception {
        // given
        workoutRequest = new WorkoutRequest(1L, LocalDateTime.now());
        String requestJson = objectMapper.writeValueAsString(workoutRequest);

        given(workoutService.scheduleWorkout(workoutRequest)).willThrow(new WorkoutNotFoundException(workoutRequest.id()));

        // when & then
        mockMvc.perform(patch("/api/workouts/" + workoutRequest.id())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Workout not found with ID: " + workout.getId()));

    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void whenScheduleWorkoutIsCalled_givenUnauthorisedAccess_thenReturn400AndErrorResponse() throws Exception {
        // given
        workoutRequest = new WorkoutRequest(1L, LocalDateTime.now());
        String requestJson = objectMapper.writeValueAsString(workoutRequest);

        given(workoutService.scheduleWorkout(workoutRequest)).willThrow(new UnauthorizedWorkoutAccessException(workout.getId()));

        // when & then
        mockMvc.perform(patch("/api/workouts/" + workoutRequest.id())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("User is not authorized to modify workout with ID: " +  workout.getId()));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void whenGetWorkoutsIsCalled_thenReturn200AndWorkouts() throws Exception {
        // given
        List<WorkoutDTO> workouts = List.of(workoutDTO);
        String responseJson = objectMapper.writeValueAsString(workouts);

        given(workoutService.getWorkouts()).willReturn(workouts);

        // when & then
        mockMvc.perform(get("/api/workouts"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(responseJson));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void whenGenerateReportIsCalled_givenNoWorkouts_thenReturn200AndReport() throws  Exception {
        // given
        given(workoutService.generateReport()).willReturn("No workouts found.");

        // when & then
        mockMvc.perform(get("/api/workouts/report"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void whenGenerateReportIsCalled_givenHasWorkouts_thenReturn200AndReport() throws  Exception {
        // given
        given(workoutService.generateReport()).willReturn("Total Workouts:");

        // when & then
        mockMvc.perform(get("/api/workouts/report"))
                .andExpect(status().isOk());
    }
}
