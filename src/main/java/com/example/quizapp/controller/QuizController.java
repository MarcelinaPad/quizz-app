package com.example.quizapp.controller;

import com.example.quizapp.dto.QuizResponse;
import com.example.quizapp.exception.InvalidQuestionException;
import com.example.quizapp.mapper.QuizMapper;
import com.example.quizapp.model.Question;
import com.example.quizapp.model.Quiz;
import com.example.quizapp.service.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/quizzes")
public class QuizController {
    @Autowired
    private QuizService quizService;

    @GetMapping
    public List<Quiz> getAllQuizzes() {
        return quizService.getAllQuizzes();
    }

    @GetMapping("/page")
    public Page<Quiz> getAllQuizzes(Pageable pageable) {
        return quizService.getAllQuizzes(pageable);
    }

    @PostMapping
    public ResponseEntity<QuizResponse> createQuiz(@RequestBody Quiz quiz) {
        List<Question> questions = quiz.getQuestions();
        for (Question question : questions) {
            if (question.getIncorrectAnswers() == null || question.getIncorrectAnswers().size() != 3) {
                throw new InvalidQuestionException("Each question must have exactly 3 incorrect answers.");
            }
        }
        Quiz createdQuiz = quizService.createQuiz(quiz.getQuizName(), quiz.getDescription(), questions );
        QuizResponse quizResponse = QuizMapper.mapToQuizResponse(createdQuiz);
        return ResponseEntity.status(HttpStatus.CREATED).body(quizResponse);
    }
    @PutMapping("/{quizId}")
    public ResponseEntity<Quiz> updateQuiz(@PathVariable Long quizId, @RequestBody Quiz quizDetails) {
        Quiz updatedQuiz = quizService.updateQuiz(quizId, quizDetails);
        return ResponseEntity.ok(updatedQuiz);
    }
    @DeleteMapping("/{quizId}")
    public ResponseEntity<Void> deleteQuiz(@PathVariable Long quizId) {
        quizService.deleteQuiz(quizId);
        return ResponseEntity.noContent().build();
    }
}
