package com.example.quizapp.controller;

import com.example.quizapp.model.Question;
import com.example.quizapp.model.Quiz;
import com.example.quizapp.service.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @PostMapping
    public ResponseEntity<Quiz> createQuiz(@RequestBody Quiz quiz) {
        List<Question> questions = quiz.getQuestions();
        Quiz createdQuiz = quizService.createQuiz(quiz.getQuizName(), quiz.getDescription(), questions );
        return ResponseEntity.status(HttpStatus.CREATED).body(createdQuiz);
    }
    @PutMapping("/{quizId}")
    public ResponseEntity<Quiz> updateQuiz(@PathVariable Long quizId, @RequestBody Quiz quizDetails) {
        Quiz updatedQuiz = quizService.updateQuiz(quizId, quizDetails);
        return ResponseEntity.ok(updatedQuiz);
    }
    @DeleteMapping("/{quizId")
    public ResponseEntity<Void> deleteQuiz(@PathVariable Long quizId)
}
