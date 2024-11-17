package com.example.quizapp.controller;

import com.example.quizapp.dto.AnswerRequest;
import com.example.quizapp.service.AnswerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/answers")
public class AnswerController {
    @Autowired
    private AnswerService answerService;

    @PostMapping
    public ResponseEntity<String> submitAnswer(@Valid @RequestBody AnswerRequest answerRequest) {
        String response = answerService.checkAnswer(answerRequest);
        return ResponseEntity.ok(response);
    }

}
