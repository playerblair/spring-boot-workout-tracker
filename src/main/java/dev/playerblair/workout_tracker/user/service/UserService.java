package dev.playerblair.workout_tracker.user.service;

import dev.playerblair.workout_tracker.user.dto.LoginRequest;
import dev.playerblair.workout_tracker.user.dto.SignUpRequest;
import dev.playerblair.workout_tracker.user.exception.UserAlreadyExistsException;
import dev.playerblair.workout_tracker.user.model.Token;
import dev.playerblair.workout_tracker.user.model.User;
import dev.playerblair.workout_tracker.user.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    public UserService(AuthenticationManager authenticationManager, UserRepository userRepository, PasswordEncoder passwordEncoder, TokenService tokenService) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
    }

    public Token login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.username(),
                        request.password()
                )
        );

        return tokenService.generateToken(authentication);
    }

    public String signup(SignUpRequest request) {
        Optional<User> existingUser = userRepository.getUserByUsername(request.username());

        if (existingUser.isPresent()) {
            throw new UserAlreadyExistsException(request.username());
        }

        userRepository.save(new User(
                request.username(),
                passwordEncoder.encode(request.password())
        ));

        return "User signup successful!";
    }

}
