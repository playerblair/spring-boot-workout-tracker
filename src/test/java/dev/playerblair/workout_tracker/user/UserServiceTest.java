package dev.playerblair.workout_tracker.user;

import dev.playerblair.workout_tracker.user.dto.LoginRequest;
import dev.playerblair.workout_tracker.user.dto.SignUpRequest;
import dev.playerblair.workout_tracker.user.exception.UserAlreadyExistsException;
import dev.playerblair.workout_tracker.user.model.Token;
import dev.playerblair.workout_tracker.user.model.User;
import dev.playerblair.workout_tracker.user.repository.UserRepository;
import dev.playerblair.workout_tracker.user.service.TokenService;
import dev.playerblair.workout_tracker.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TokenService tokenService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private User user;
    private LoginRequest loginRequest;
    private SignUpRequest signUpRequest;
    private Authentication authentication;

    @BeforeEach
    public void setUp() {
        user = new User(
                1L,
                "user",
                "pass",
                "USER"
        );

        loginRequest = new LoginRequest(
                "user",
                "pass"
        );

        signUpRequest = new SignUpRequest(
                "user",
                "pass"
        );

        authentication = mock(Authentication.class);
    }

    @Test
    public void whenLoginIsCalled_givenValidLoginRequest_generateToken() {
        // setup test data
        Token mockToken = new Token("mock-token-value");

        // mock authentication manager behaviour
        given(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.username(), loginRequest.password())))
                .willReturn(authentication);
        given(tokenService.generateToken(authentication)).willReturn(mockToken);

        // execute the method under test
        Token generatedToken = userService.login(loginRequest);

        // verify token service interaction
        verify(tokenService).generateToken(authentication);

        // verify response
        assertThat(generatedToken).isEqualTo(mockToken);
    }

    @Test
    public void whenLoginIsCalled_givenBadCredentials_throwException() {
        // mock authentication manager behaviour
        given(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.username(), loginRequest.password())))
                .willThrow(new BadCredentialsException("Bad credentials"));

        // assert exception thrown
        assertThatThrownBy(() -> userService.login(loginRequest))
                .isInstanceOf(BadCredentialsException.class);
    }

    @Test
    public void whenSignupIsCalled_givenValidSignUpRequest_createUser() {
        // mock repository behaviour
        given(userRepository.getUserByUsername(signUpRequest.username()))
                .willReturn(Optional.empty());

        given(passwordEncoder.encode(signUpRequest.password())).willReturn(signUpRequest.password());

        given(userRepository.save(any(User.class)))
                .willReturn(user);

        // execute the method under test
        String response = userService.signup(signUpRequest);

        // verify repository interaction
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());
        User capturedUser = userCaptor.getValue();

        // verify saved user
        assertThat(capturedUser.getUsername()).isEqualTo(signUpRequest.username());

        // verify response
        assertThat(response).isEqualTo("User signup successful!");
    }

    @Test
    public void whenSignupIsCalled_givenExistingUser_throwException() {
        // mock repository behaviour
        given(userRepository.getUserByUsername(signUpRequest.username()))
                .willReturn(Optional.of(user));

        // assert exception thrown
        assertThatThrownBy(() -> userService.signup(signUpRequest))
                .isInstanceOf(UserAlreadyExistsException.class)
                .hasMessage("User already exists with username: " + signUpRequest.username());
    }
}
