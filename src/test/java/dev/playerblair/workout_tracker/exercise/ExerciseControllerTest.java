package dev.playerblair.workout_tracker.exercise;

import dev.playerblair.workout_tracker.exercise.controller.ExerciseController;
import dev.playerblair.workout_tracker.exercise.model.Category;
import dev.playerblair.workout_tracker.exercise.model.Exercise;
import dev.playerblair.workout_tracker.exercise.repository.ExerciseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ExerciseController.class)
public class ExerciseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ExerciseRepository exerciseRepository;

    private Exercise exercise1;
    private Exercise exercise2;

    @BeforeEach
    public void setUp() {
        exercise1 = new Exercise(
                1L,
                "Running",
                "Running at a steady pace on a flat surface or treadmill",
                Category.CARDIO,
                null
        );

        exercise2 = new Exercise(
                2L,
                "Cycling",
                "Pedaling on a stationary bike or outdoor bicycle",
                Category.CARDIO,
                null
        );
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void whenGetAllExercisesIsCalled_thenReturn200AndAllExercises() throws Exception{
        // given
        given(exerciseRepository.findAll()).willReturn(List.of(exercise1, exercise2));

        // when & then
        mockMvc.perform(get("/api/exercises"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(exercise1.getId()))
                .andExpect(jsonPath("$[0].name").value(exercise1.getName()))
                .andExpect(jsonPath("$[1].id").value(exercise2.getId()))
                .andExpect(jsonPath("$[1].name").value(exercise2.getName()));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void whenGetExerciseIsCalled_givenValidId_thenReturn200AndExercise() throws Exception {
        // given
        Long id = exercise1.getId();
        given(exerciseRepository.findById(id)).willReturn(Optional.of(exercise1));

        // when & then
        mockMvc.perform(get("/api/exercises/" + id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(exercise1.getName()));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void whenGetExerciseIsCalled_givenInvalidId_thenReturn404AndErrorResponse() throws Exception {
        // given
        Long id = 3L;
        given(exerciseRepository.findById(id)).willReturn(Optional.empty());

        // when & then
        mockMvc.perform(get("/api/exercises/" + id))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Exercise not found with ID: " + id));
    }
}
