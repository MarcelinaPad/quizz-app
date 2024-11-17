package com.example.quizapp.dto;

import org.antlr.v4.runtime.misc.NotNull;
import org.hibernate.annotations.NotFound;

public class AnswerRequest {
    @NotNull
    private Long questionId;
    @NotNull
    private String userAnswer;

    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    public String getUserAnswer() {
        return userAnswer;
    }

    public void setUserAnswer(String userAnswer) {
        this.userAnswer = userAnswer;
    }
}
