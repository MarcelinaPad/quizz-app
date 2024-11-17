package com.example.quizapp.service;

import com.example.quizapp.model.User;
import com.example.quizapp.repository.UserRepository;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

import org.slf4j.Logger;

import java.util.regex.Pattern;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);


    public User registerUser(String name, String password) {
        if (password.length() < 8) {
            throw new IllegalArgumentException("Your password is too short");
        }
        String passwordPattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#\\$%^&*(),.?\":{}|<>]).{8,}$";
        if (!Pattern.matches(passwordPattern, password)) {
            throw new IllegalArgumentException("The password must contain at least one uppercase letter, lowercase letter, number, and special character");
        }

        String hashedPassword = passwordEncoder.encode(password); // Hash the password
        User user = new User(name, hashedPassword);
        return userRepository.save(user);
    }

    public Optional<User> findByName(String name) {
        return userRepository.findByName(name);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.debug("Attempting to load user: {}", username);

        com.example.quizapp.model.User appUser = userRepository.findByName(username)
                .orElseThrow(() -> {
                    logger.warn("User not found: {}", username);
                    return new UsernameNotFoundException("User not found: " + username);
                });

        logger.debug("User found: {}", appUser.getName());
        return org.springframework.security.core.userdetails.User.builder()
                .username(appUser.getName())
                .password(appUser.getPassword()) // Must be hashed
                .roles("USER") // Default role
                .build();
    }
}
