package dev.playerblair.workout_tracker.workout.model;

import dev.playerblair.workout_tracker.user.model.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Workout {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @OneToMany(mappedBy = "workout", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ExercisePlan> exercises = new ArrayList<>();
    private LocalDateTime dateTime;

    @Enumerated(EnumType.STRING)
    private Status status;
    private String comment;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    public Workout(String name, LocalDateTime dateTime, Status status, String comment) {
        this.name = name;
        this.dateTime = dateTime;
        this.status = status;
        this.comment = comment;
    }

    public Workout(Long id, String name, LocalDateTime dateTime, Status status, String comment) {
        this.id = id;
        this.name = name;
        this.dateTime = dateTime;
        this.status = status;
        this.comment = comment;
    }

    public Workout(String name, LocalDateTime dateTime, Status status, String comment, User user) {
        this.name = name;
        this.dateTime = dateTime;
        this.status = status;
        this.comment = comment;
        this.user = user;
    }

    public Workout(Long id, String name, LocalDateTime dateTime, Status status, String comment, User user) {
        this.id = id;
        this.name = name;
        this.dateTime = dateTime;
        this.status = status;
        this.comment = comment;
        this.user = user;
    }
}
