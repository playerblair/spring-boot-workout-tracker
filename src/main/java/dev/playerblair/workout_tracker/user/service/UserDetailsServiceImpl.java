package dev.playerblair.workout_tracker.user.service;

import dev.playerblair.workout_tracker.user.repository.UserRepository;
import dev.playerblair.workout_tracker.user.model.MyUserDetails;
import dev.playerblair.workout_tracker.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.getUserByUsername(username);

        if (user.isEmpty()) {
            throw new UsernameNotFoundException("Could not find user: " + username);
        }

        return new MyUserDetails(user.get());
    }
}
