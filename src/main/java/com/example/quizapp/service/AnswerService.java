package com.example.quizapp.service;

import com.example.quizapp.auth.AuthService;
import com.example.quizapp.dto.AnswerRequest;
import com.example.quizapp.model.Question;
import com.example.quizapp.model.User;
import com.example.quizapp.model.UserAnswer;
import com.example.quizapp.repository.QuestionRepository;
import com.example.quizapp.repository.UserAnswerRepository;
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
    @Autowired
    private UserAnswerRepository userAnswerRepository;

    public String checkAnswer(AnswerRequest answerRequest) {
        Question question = questionRepository.findById(answerRequest.getQuestionId())
                .orElseThrow(() -> new IllegalArgumentException("Question not found"));

        User user = authService.getCurrentUser();

        if (userAnswerRepository.findByUserIdAndQuestionId(user.getId(), question.getId()).isPresent()) {
            throw new IllegalArgumentException("You have already answered this question.");
        }

        if (question.getOwner() != null && question.getOwner().getId().equals(user.getId())) {
            throw new IllegalArgumentException("You cannot answer your own question.");
        }

        boolean isCorrect = question.getCorrectAnswer().equalsIgnoreCase(answerRequest.getUserAnswer());
        UserAnswer userAnswer = new UserAnswer();
        userAnswer.setUser(user);
        userAnswer.setQuestion(question);
        userAnswer.setCorrect(isCorrect);
        userAnswerRepository.save(userAnswer);


        if (isCorrect) {

            user.setStreak(user.getStreak() + 1);
            int pointsToAdd = user.getStreak() >= 5 ? 2 : 1;
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
                    + ". Your score: " + user.getScore() + ". You lose your streak.";
        }
    }

}
