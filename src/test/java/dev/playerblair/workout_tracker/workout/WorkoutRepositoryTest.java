package dev.playerblair.workout_tracker.workout;

import dev.playerblair.workout_tracker.exercise.model.Exercise;
import dev.playerblair.workout_tracker.exercise.repository.ExerciseRepository;
import dev.playerblair.workout_tracker.user.model.User;
import dev.playerblair.workout_tracker.user.repository.UserRepository;
import dev.playerblair.workout_tracker.workout.model.ExercisePlan;
import dev.playerblair.workout_tracker.workout.model.Status;
import dev.playerblair.workout_tracker.workout.model.Workout;
import dev.playerblair.workout_tracker.workout.repository.WorkoutRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.BDDAssertions.then;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource("classpath:test.properties")
public class WorkoutRepositoryTest {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ExerciseRepository exerciseRepository;

    @Autowired
    private WorkoutRepository workoutRepository;


    private Exercise exercise1;
    private Exercise exercise2;
    private User user1;
    private User user2;
    private Workout workout1;
    private Workout workout2;
    private ExercisePlan exercisePlan1;
    private ExercisePlan exercisePlan2;

    @BeforeEach
    public void setUp() {
        // exercises
        exercise1 = exerciseRepository.findById(1L).get();
        exercise2 = exerciseRepository.findById(2L).get();

        // user1
        user1 = userRepository.save(new User(
                "user1",
                "pass"
        ));
        workout1 = new Workout(
                "Test Workout 1",
                LocalDateTime.now(),
                Status.ACTIVE,
                "no comment",
                user1
        );
        exercisePlan1 = new ExercisePlan(
                exercise1,
                null,
                null,
                null,
                 workout1
        );
        workout1.setExercises(List.of(exercisePlan1));

        // user2
        user2 = userRepository.save(new User(
                "user2",
                "pass"
        ));
        workout2 = new Workout(
                "Test Workout 2",
                LocalDateTime.now(),
                Status.ACTIVE,
                "no comment",
                user2
        );
        exercisePlan2 = new ExercisePlan(
                exercise2,
                3,
                2,
                50,
                workout2
        );
        workout2.setExercises(List.of(exercisePlan2));

        workoutRepository.saveAll(List.of(workout1, workout2));
    }

    @AfterEach
    public void tearDown() {
        workoutRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Transactional
    @Test
    public void whenFindByIdIsCalled_givenValidId_thenReturnWorkout() {
        // given
        Long id = workoutRepository.findAll().getFirst().getId();

        // when
        Optional<Workout> optionalWorkout = workoutRepository.findById(id);

        // then
        then(optionalWorkout).isPresent();
        then(optionalWorkout.get())
                .usingRecursiveComparison()
                .isEqualTo(workout1);
    }

    @Test
    public void whenFindByIsCalled_givenInvalidId_returnEmpty() {
        // given
        Long id = workoutRepository.findAll().getFirst().getId();
        workoutRepository.deleteById(id);

        // when
        Optional<Workout> optionalWorkout = workoutRepository.findById(id);

        // then
        then(optionalWorkout).isEmpty();
    }

    @Transactional
    @Test
    public void whenFindByUserIdIsCalled_givenValidUserId_returnWorkouts() {
        // given
        Long id = user1.getId();

        // when
        List<Workout> workouts = workoutRepository.findByUserId(id);

        // then
        then(workouts)
                .usingRecursiveComparison()
                .isEqualTo(List.of(workout1));
    }

    @Test
    public void whenFindByUserIdIsCalled_givenInvalidUserId_returnNothing() {
        // when
        Long id = user1.getId() + user2.getId();

        // given
        List<Workout> workouts = workoutRepository.findByUserId(id);

        // then
        then(workouts).isEmpty();
    }

    @Transactional
    @Test
    public void whenFindByUserIdAndStatusIsCalled_givenValidUserIdAndStatus_returnWorkouts() {
        // given
        Long id = user1.getId();
        Status status = Status.ACTIVE;

        // when
        List<Workout> workouts = workoutRepository.findByUserIdAndStatus(id, status);

        // then
        then(workouts).hasSize(1);

        // and
        workouts = workoutRepository.findByUserIdAndStatus(id, Status.PENDING);
        then(workouts).isEmpty();
    }

    @Test
    public void whenFindByUserIdAndStatusIsCalled_givenInvalidUserId_returnNothing() {
        // given
        Long id = user1.getId() + user2.getId();
        Status status = Status.ACTIVE;

        // when
        List<Workout> workouts = workoutRepository.findByUserIdAndStatus(id, status);

        // then
        then(workouts).isEmpty();
    }
}
