package dev.playerblair.workout_tracker.user.controller;

import dev.playerblair.workout_tracker.user.service.TokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    private static final Logger LOG = LoggerFactory.getLogger(AuthController.class);

    private final TokenService tokenService;

    public AuthController(TokenService tokenService) {
        this.tokenService = tokenService;
    }

//    @PostMapping("/token")
//    public String token(Authentication authentication) {
//        LOG.debug("Token requested for user: '{}'", authentication.getName());
//        Token token = tokenService.generateToken(authentication);
//        LOG.debug("Token granted: {}", token);
//        return token;
//    }
}