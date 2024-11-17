package com.example.quizapp.service;

import com.example.quizapp.auth.AuthService;
import com.example.quizapp.dto.AnswerRequest;
import com.example.quizapp.model.Question;
import com.example.quizapp.model.User;
import com.example.quizapp.repository.QuestionRepository;
import com.example.quizapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AnswerService {
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AuthService authService;

    public String checkAnswer(AnswerRequest answerRequest) {
        Question question = questionRepository.findById(answerRequest.getQuestionId())
                .orElseThrow(() -> new IllegalArgumentException("Question not found"));

        User user = authService.getCurrentUser();
        if (question.getCorrectAnswer().equalsIgnoreCase(answerRequest.getUserAnswer())) {
            user.setStreak(user.getStreak() + 1);
            int pointsToAdd = 1;
            if (user.getStreak() >= 5) {
                pointsToAdd = 2;
            }


            user.setScore(user.getScore() + pointsToAdd);
            userRepository.save(user);
            return "Correct answer! Your score: " + user.getScore() + " Streak: " + user.getStreak();
        } else {
            user.setStreak(0);
            if (user.getScore() > 0) {
                user.setScore(user.getScore() - 1);
            }
            userRepository.save(user);
            return "Wrong answer! Correct answer was: " + question.getCorrectAnswer()
                    + " . Your score " + user.getScore() + "You loose your streak ";
        }
    }

}
