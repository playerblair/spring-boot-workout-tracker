package dev.playerblair.workout_tracker.exercise;

import dev.playerblair.workout_tracker.exercise.model.Category;
import dev.playerblair.workout_tracker.exercise.model.Exercise;
import dev.playerblair.workout_tracker.exercise.repository.ExerciseRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssertions.thenThrownBy;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource("classpath:test.properties")
public class ExerciseRepositoryTest {

    @Autowired
    private ExerciseRepository exerciseRepository;

    @Test
    public void whenFindAllIsCalled_givenExerciseTableIsPopulated_thenReturnAllExercises() {
        // given
        // exercise table is pre-populate via data.sql with 66 exercises

        // when
        List<Exercise> exercises = exerciseRepository.findAll();

        // then
        then(exercises).hasSize(66);
    }

    @Test
    public void whenFindByIdIsCalled_givenValidId_thenReturnExercise() {
        // given
        Long id = 1L;

        // when
        Optional<Exercise> optionalExercise = exerciseRepository.findById(id);

        // then
        then(optionalExercise).isPresent();

        // and
        Exercise exercise = optionalExercise.get();
        then(exercise.getName()).isEqualTo("Running");
        then(exercise.getDescription()).isEqualTo("Running at a steady pace on a flat surface or treadmill");
        then(exercise.getCategory()).isEqualTo(Category.CARDIO);
        then(exercise.getMuscleGroup()).isNull();
    }

    @Test
    public void whenFindByIdIsCalled_givenInvalidId_thenReturnEmpty() {
        // given
        Long id = 67L;

        // when
        Optional<Exercise> optionalExercise = exerciseRepository.findById(id);

        // then
        then(optionalExercise).isEmpty();
        thenThrownBy(() -> optionalExercise.get())
                .isInstanceOf(NoSuchElementException.class);
    }
}
