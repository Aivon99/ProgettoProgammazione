package com.example;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;

public class PrevediOutput extends Exercise {

    private Map<String, String[]> questions; // Contiene le domande
    private Map<String, String[]> answers; // Contiene le risposte corrette
    private Map<String, String[]> userAnswers; // Contiene le risposte dell'utente
    private int currentQuestionIndex;
    private String currentDifficulty;

    public PrevediOutput() {
        super("Prevedi Output", new String[]{"Facile", "Medio", "Difficile"}, new int[]{3, 3, 3});
        initializeQuestions();
        initializeAnswers();
        userAnswers = new HashMap<>();
        currentQuestionIndex = 0; // Inizializza a 0
    }

    private void initializeQuestions() {
        questions = new HashMap<>();

        // Domande Facili
        questions.put("Facile", new String[]{
            "Qual è l'output del seguente codice?\n\nint a = 5;\nSystem.out.println(a);\n\nA) 5\nB) 10\nC) 0\nD) Errore di compilazione",
            "Qual è l'output del seguente codice?\n\nint a = 2;\nint b = 3;\nSystem.out.println(a + b);\n\nA) 23\nB) 5\nC) 6\nD) 8",
            "Qual è l'output del seguente codice?\n\nString s = \"Ciao\";\nSystem.out.println(s);\n\nA) Hello\nB) Ciao\nC) Cioa\nD) 123"
        });

        // Domande Medie
        questions.put("Medio", new String[]{
            "Qual è l'output del seguente codice?\n\nfor(int i = 0; i < 2; i++) {\n    for(int j = 0; j < 2; j++) {\n        System.out.println(i + \" \" + j);\n    }\n}\n\nA) 0 0\n   0 1\n   1 0\n   1 1\nB) 0 0\n   0 1\n   1 0\nC) 0 1\n   1 0\n   1 1\n   2 2\nD) Nessun output",
            "Qual è l'output del seguente codice?\n\nfor(int i = 1; i <= 3; i++) {\n    for(int j = 1; j <= 3; j++) {\n        System.out.print(i * j + \" \");\n    }\n    System.out.println();\n}\n\nA) 1 2 3\n   2 4 6\n   3 6 9\nB) 1 2 3 4 5 6\nC) 2 4 6 8 10 12\nD) 3 6 9 12 15 18",
            "Qual è l'output del seguente codice?\n\nint sum = 0;\nfor(int i = 1; i <= 5; i++) {\n    for(int j = 1; j <= 2; j++) {\n        sum += i * j;\n    }\n}\nSystem.out.println(sum);\n\nA) 15\nB) 20\nC) 30\nD) 60"
        });

        // Domande Difficili
        questions.put("Difficile", new String[]{
            "Qual è l'output del seguente codice?\n\nint[][] matrix = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};\nint sum = 0;\nfor(int i = 0; i < matrix.length; i++) {\n    for(int j = 0; j < matrix[i].length; j++) {\n        sum += matrix[i][j];\n    }\n}\nSystem.out.println(sum);\n\nA) 45\nB) 50\nC) 15\nD) 30",
            "Qual è l'output del seguente codice?\n\nList<Integer> list = Arrays.asList(1, 2, 3, 4);\nint product = 1;\nfor(int i : list) {\n    product *= i;\n}\nSystem.out.println(product);\n\nA) 24\nB) 10\nC) 120\nD) 15",
            "Qual è l'output del seguente codice?\n\nint[] arr = {2, 4, 6, 8};\nint result = 1;\nfor(int i = 0; i < arr.length; i++) {\n    result *= arr[i];\n}\nSystem.out.println(result);\n\nA) 384\nB) 3840\nC) 24\nD) 256"
        });
    }

    private void initializeAnswers() {
        answers = new HashMap<>();

        // Risposte Facili
        answers.put("Facile", new String[]{"A", "B", "B"});

        // Risposte Medie
        answers.put("Medio", new String[]{"A", "A", "C"});

        // Risposte Difficili
        answers.put("Difficile", new String[]{"A", "A", "A"});
    }

    @Override
    public String getDescription(String difficolta) {
        return "In questo esercizio, dovrai prevedere l'output del codice fornito per la difficoltà " + difficolta + ".";
    }

    @Override
    public boolean checkAllAnswers(String difficulty) {
        String[] correctAnswers = answers.get(difficulty);
        String[] givenAnswers = userAnswers.get(difficulty);

        if (givenAnswers == null || givenAnswers.length != correctAnswers.length) {
            System.out.println("Risposte date non valide o lunghezza non corrispondente.");
            return false;
        }

        for (int i = 0; i < correctAnswers.length; i++) {
            String correctAnswer = correctAnswers[i];
            String givenAnswer = givenAnswers[i];
            
            // Debugging: Verifica le risposte corrette e quelle date
            System.out.println("Indice della domanda: " + i);
            System.out.println("Risposta corretta: " + correctAnswer + ", Risposta data: " + givenAnswer);
            
            if (!correctAnswer.equalsIgnoreCase(givenAnswer)) {
                System.out.println("Risposta non corretta trovata: " + givenAnswer);
                return false;
            }
        }
        return true;
    }

    @Override
    public void registraRisposta(String risposta, String difficulty) {
        if (userAnswers.get(difficulty) == null) {
            userAnswers.put(difficulty, new String[getNEsercizi(difficulty)]);
        }
        userAnswers.get(difficulty)[currentQuestionIndex] = risposta;
        
        // Debugging: Verifica che la risposta venga registrata correttamente
        System.out.println("Registrata risposta: " + risposta + " all'indice della domanda: " + currentQuestionIndex);
    }

    @Override
    public void handleExerciseSuspension() {
        // Implementa azioni da fare se l'esercizio viene sospeso, se necessario
    }

    @Override
    public void saveResult(String username, boolean success, String difficulty) throws IOException {
        // Implementa il salvataggio del risultato dell'utente su file, se necessario
    }

    @Override
    public void finestraEsercizio(int index, String difficulty, VBox layout, Object[] campoInput) {
        Label questionLabel = new Label(questions.get(difficulty)[index]);
        questionLabel.setWrapText(true);
        questionLabel.setStyle("-fx-text-fill: white;");

        ToggleGroup group = new ToggleGroup();
        RadioButton optionA = new RadioButton("A");
        RadioButton optionB = new RadioButton("B");
        RadioButton optionC = new RadioButton("C");
        RadioButton optionD = new RadioButton("D");

        optionA.setToggleGroup(group);
        optionB.setToggleGroup(group);
        optionC.setToggleGroup(group);
        optionD.setToggleGroup(group);

        layout.getChildren().clear();
        layout.getChildren().addAll(questionLabel, optionA, optionB, optionC, optionD);
        campoInput[0] = group;
        
        // Aggiorna l'indice della domanda
        setQuestionIndex(index);
    }

    public void setQuestionIndex(int index) {
        currentQuestionIndex = index;
        // Debugging: Verifica che l'indice della domanda venga impostato correttamente
        System.out.println("Indice della domanda impostato su: " + currentQuestionIndex);
    }
}
