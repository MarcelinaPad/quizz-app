package com.example.quizapp.service;

import com.example.quizapp.auth.AuthService;
import com.example.quizapp.model.Question;
import com.example.quizapp.model.Quiz;
import com.example.quizapp.model.User;
import com.example.quizapp.repository.QuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuizService {
    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private AuthService authService;

    public Quiz createQuiz(String quizName, String description, List<Question> questions) {
        if (questions.size() < 3) {
            throw new IllegalArgumentException("Quiz must containst 3 questions.");
        }

        User currentUser = authService.getCurrentUser();
        Quiz quiz = new Quiz();
        quiz.setQuizName(quizName);
        quiz.setDescription(description);
        quiz.setUser(currentUser);

        for (Question question : questions) {
            question.setOwner(currentUser);
            question.setQuiz(quiz);
        }

        quiz.setQuestions(questions);
        return quizRepository.save(quiz);
    }
    public Quiz updateQuiz(Long quizId, Quiz quizDetails) {
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new IllegalArgumentException("Quiz with this ID doesn't exist"));
        quiz.setQuizName(quizDetails.getQuizName());
        quiz.setDescription(quizDetails.getDescription());
        quiz.setQuestions(quizDetails.getQuestions());
        return quizRepository.save(quiz);
    }
    public void deleteQuiz(Long quizId) {
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new IllegalArgumentException("Quiz not found"));
        quizRepository.delete(quiz);
    }
    public Page<Quiz> getAllQuizzes(Pageable pageable) {
        return quizRepository.findAll(pageable);
    }

    public List<Quiz> getAllQuizzes() {
        return quizRepository.findAll();
    }
}