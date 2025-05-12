package dev.playerblair.workout_tracker.user.controller;

import dev.playerblair.workout_tracker.user.model.Token;
import dev.playerblair.workout_tracker.user.service.UserService;
import dev.playerblair.workout_tracker.user.dto.LoginRequest;
import dev.playerblair.workout_tracker.user.dto.SignUpRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<Token> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(userService.login(request));
    }

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody SignUpRequest request) {
        return ResponseEntity.ok(userService.signup(request));
    }
}
