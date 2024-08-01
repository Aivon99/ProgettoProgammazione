package Log;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class MultiplicationExercise extends Exercise {

    private int[] answers;
    private int[] correctAnswers;
    private int currentQuestionIndex;
    private int totalQuestions;

    public MultiplicationExercise() {
        super("Moltiplicazione di Numeri", new String[]{"Facile", "Medio", "Difficile"});
        totalQuestions = 3; // Numero totale di domande
        resetExercise();
    }

    @Override
    public void resetExercise() {
        answers = new int[totalQuestions]; // Array per le risposte dell'utente
        correctAnswers = new int[totalQuestions]; // Array per le risposte corrette
        currentQuestionIndex = 0;
        generateQuestions();
    }

    private void generateQuestions() {
        for (int i = 0; i < totalQuestions; i++) {
            int num1 = (int) (Math.random() * 10) + 1; // Genera numeri casuali tra 1 e 10
            int num2 = (int) (Math.random() * 10) + 1;
            correctAnswers[i] = num1 * num2; // Calcola il prodotto e lo memorizza
        }
    }

    @Override
    public boolean checkAllAnswers() {
        for (int i = 0; i < totalQuestions; i++) {
            if (answers[i] != correctAnswers[i]) {
                return false; // Restituisce false se almeno una risposta è sbagliata
            }
        }
        return true; // Restituisce true se tutte le risposte sono corrette
    }

    @Override
    public void submitAnswer(int answer) {
        if (currentQuestionIndex < totalQuestions) {
            answers[currentQuestionIndex] = answer; // Registra la risposta dell'utente
            currentQuestionIndex++;
        }
    }

    @Override
    public void handleExerciseSuspension() {
        // Se l'esercizio è sospeso prima di completare tutte le domande, lo consideriamo come fallito.
        // Questo metodo può essere personalizzato ulteriormente in base alle necessità.
    }

    @Override
    public String getExerciseDetails(int index) {
        if (index >= 0 && index < totalQuestions) {
            int num1 = correctAnswers[index] / ((int) (Math.random() * 10) + 1); // Calcola uno dei numeri
            int num2 = correctAnswers[index] / num1; // Calcola l'altro numero
            return "Quanto fa " + num1 + " x " + num2 + "?";
        } else {
            return "Domanda non valida.";
        }
    }

    @Override
    public String getDescription() {
        return "In questo esercizio, dovrai moltiplicare numeri casuali. Ogni livello di difficoltà aumenta la complessità dei numeri da moltiplicare.";
    }

    @Override
    public void saveResult(String username, boolean success, String difficulty) throws IOException {
        String fileName = username + "_results.txt";
        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName, true))) {
            writer.println("Nome Utente: " + username);
            writer.println("Esercizio: Moltiplicazione di Numeri");
            writer.println("Difficoltà: " + difficulty);
            writer.println("Risultato: " + (success ? "Superato" : "Fallito"));
            writer.println();
        }
    }
}
