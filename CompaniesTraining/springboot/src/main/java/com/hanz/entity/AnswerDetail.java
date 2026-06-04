package com.hanz.entity;

import com.fasterxml.jackson.annotation.JsonAlias;

public class AnswerDetail {
    private String question;
    private String userAnswer;
    private String correctAnswer;

    @JsonAlias("correct")
    private boolean isCorrect;

    public AnswerDetail(String question, String userAnswer, String correctAnswer, boolean isCorrect) {
        this.question = question;
        this.userAnswer = userAnswer;
        this.correctAnswer = correctAnswer;
        this.isCorrect = isCorrect;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getUserAnswer() {
        return userAnswer;
    }

    public void setUserAnswer(String userAnswer) {
        this.userAnswer = userAnswer;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public boolean isCorrect() {
        return isCorrect;
    }

    public void setCorrect(boolean correct) {
        isCorrect = correct;
    }
}
