package com.example.zadanie_2;

public class Question {
    private int questionId; // Id pytania
    private boolean trueAnswer; // Poprawna odpowiedź na pytanie
    public Question(int questionId, boolean trueAnswer){
        this.questionId=questionId;
        this.trueAnswer=trueAnswer;
    }
    public boolean isTrueAnswer(){
        return this.trueAnswer;
    }
    public int getQuestionId(){
        return this.questionId;
    }
}
