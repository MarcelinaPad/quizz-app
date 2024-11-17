package com.example.quizapp.mapper;

import com.example.quizapp.dto.QuestionResponse;
import com.example.quizapp.dto.QuizResponse;
import com.example.quizapp.model.Quiz;

import java.util.List;
import java.util.stream.Collectors;

public class QuizMapper {

    public static QuizResponse mapToQuizResponse(Quiz quiz) {
        QuizResponse response = new QuizResponse();
        response.setId(quiz.getId());
        response.setQuizName(quiz.getQuizName());
        response.setDescription(quiz.getDescription());

        List<QuestionResponse> questionResponses = quiz.getQuestions().stream().map(question -> {
            QuestionResponse qr = new QuestionResponse();
            qr.setId(question.getId());
            qr.setQuestionText(question.getQuestionText());
            qr.setCorrectAnswer(question.getCorrectAnswer());
            return qr;
        }).collect(Collectors.toList());

        response.setQuestions(questionResponses);
        return response;
    }
}
