package dev.playerblair.workout_tracker.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.playerblair.workout_tracker.user.dto.LoginRequest;
import dev.playerblair.workout_tracker.user.dto.SignUpRequest;
import dev.playerblair.workout_tracker.user.exception.UserAlreadyExistsException;
import dev.playerblair.workout_tracker.user.model.MyUserDetails;
import dev.playerblair.workout_tracker.user.model.Token;
import dev.playerblair.workout_tracker.user.model.User;
import dev.playerblair.workout_tracker.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserDetailsService userDetailsService;

    @MockitoBean
    private UserService userService;

    private User user;
    private UserDetails userDetails;
    private LoginRequest loginRequest;
    private SignUpRequest signUpRequest;

    @BeforeEach
    public void setUp() {
        user = new User(
                1L,
                "user",
                "pass",
                "USER"
        );

        userDetails = new MyUserDetails(user);

        loginRequest = new LoginRequest(
                "user",
                "pass"
        );

        signUpRequest = new SignUpRequest(
                "user",
                "pass"
        );
    }

    @Test
    public void whenLoginIsCalled_givenValidLoginRequest_return200() throws Exception {
        // setup test data
        Token token = new Token("mock-token-value");
        String requestJson = objectMapper.writeValueAsString(loginRequest);
        String responseJson = objectMapper.writeValueAsString(token);

        // mock service behaviour
        given(userService.login(loginRequest)).willReturn(token);

        // execute the method under test + assertions
        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(responseJson));
    }

    @Test
    public void whenLoginIsCalled_givenBadCredentials_return400() throws Exception {
        // setup test data
        String requestJson = objectMapper.writeValueAsString(loginRequest);

        // mock service behaviour
        given(userService.login(loginRequest)).willThrow(new BadCredentialsException("Bad credentials"));

        // execute the method under test + assertions
        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Bad credentials"));
    }

    @Test
    public void whenSignupIsCalled_givenValidLoginRequest_return200() throws Exception {
        // setup test data
        String requestJson = objectMapper.writeValueAsString(signUpRequest);

        // mock service behaviour
        given(userService.signup(signUpRequest)).willReturn("User signup successful!");

        // execute the method under test + assertions
        mockMvc.perform(post("/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk());
    }

    @Test
    public void whenSignupIsCalled_givenExistingUser_return400() throws Exception {
        // setup test data
        String requestJson = objectMapper.writeValueAsString(signUpRequest);

        // mock service behaviour
        given(userService.signup(signUpRequest)).willThrow(new UserAlreadyExistsException(signUpRequest.username()));

        // execute the method under test + assertions
        mockMvc.perform(post("/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("User already exists with username: " + signUpRequest.username()));
    }
}
