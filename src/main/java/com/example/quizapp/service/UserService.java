package com.example.quizapp.service;

import com.example.quizapp.model.User;
import com.example.quizapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User registerUser(String name, String password) {
        if(password.length() < 8) {
            throw new IllegalArgumentException("Your password is too short");
        }
        String passwordPattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#\\$%^&*(),.?\":{}|<>]).{8,}$";
        if (!Pattern.matches(passwordPattern, password)) {
            throw new IllegalArgumentException("The password must contain at least one uppercase letter, lowercase letter, number and special character ");
        }
        User user = new User(name, password);
        return userRepository.save(user);
    }

    public Optional<User> findByName(String name) {
        return userRepository.findByName(name);
    }
}