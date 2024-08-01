package com.example;

public class NumberSumExercise extends Exercise {

    private int num1 = 2;
    private int num2 = 2;
    private int correctAnswer = 4;

    public NumberSumExercise() {
        super("Somma di numeri", new String[]{"Facile", "Medio", "Difficile"});
    }

    public String getExerciseDetails() {
        return "Calcola " + num1 + " + " + num2;
    }

    public boolean checkAnswer(int answer) {
        return answer == correctAnswer;
    }
}
