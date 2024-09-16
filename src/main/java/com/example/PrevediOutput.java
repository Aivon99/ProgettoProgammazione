package com.example;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class PrevediOutput extends Exercise {

    private Map<String, String[]> questions; // Contiene le domande
    private Map<String, String[]> answers; // Contiene le risposte corrette
    private Map<String, String[]> userAnswers; // Contiene le risposte dell'utente
    private int currentQuestionIndex;

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
    "Qual è l'output del seguente codice?\n\nint a = 5;\nif(a > 3) {\n    a *= 2;\n}\nSystem.out.println(a);\n\nA) 5    B) 10    C) 0    D) Errore di compilazione",
    "Qual è l'output del seguente codice?\n\nint a = 2;\nint b = 3;\nif (a < b) {\n    b = b - a;\n}\nSystem.out.println(a + b);\n\nA) 23    B) 3    C) 5    D) 2",
    "Qual è l'output del seguente codice?\n\nString s = \"Ciao\";\nint len = s.length();\nSystem.out.println(len);\n\nA) 3    B) 4    C) 5    D) Errore di compilazione"
});

// Domande Medie
questions.put("Medio", new String[]{
    "Qual è l'output del seguente codice?\n\nfor(int i = 0; i < 3; i++) {\n    for(int j = 0; j < 2; j++) {\n        System.out.println(i + \" \" + j);\n    }\n}\n\nA) 0 0  0 1  1 0  1 1  2 0  2 1    B) 0 0  0 1  1 0  1 1  2 0    C) 0 0  1 1  2 2  3 3    D) Nessun output",
    "Qual è l'output del seguente codice?\n\nfor(int i = 1; i <= 3; i++) {\n    for(int j = 1; j <= 4; j++) {\n        System.out.print(i * j + \" \");\n    }\n    System.out.println();\n}\n\nA) 1 2 3  2 4 6  3 6 9    B) 1 2 3 4  2 4 6 8  3 6 9 12    C) 2 4 6 8  3 6 9 12    D) 3 6 9 12 15 18",
    "Qual è l'output del seguente codice?\n\nint sum = 0;\nfor(int i = 1; i <= 4; i++) {\n    for(int j = 1; j <= 3; j++) {\n        sum += i * j;\n    }\n}\nSystem.out.println(sum);\n\nA) 30    B) 50    C) 60    D) 100"
});

// Domande Difficili
questions.put("Difficile", new String[]{
    "Qual è l'output del seguente codice?\n\nint[][] matrix = {{1, 2}, {3, 4}, {5, 6}};\nint sum = 0;\nfor(int i = 0; i < matrix.length; i++) {\n    for(int j = 0; j < matrix[i].length; j++) {\n        sum += matrix[i][j];\n    }\n}\nSystem.out.println(sum);\n\nA) 15    B) 21    C) 12    D) 10",
    "Qual è l'output del seguente codice?\n\nList<Integer> list = Arrays.asList(1, 2, 3, 4);\nint product = 1;\nfor(int i : list) {\n    product *= i;\n}\nSystem.out.println(product);\n\nA) 24    B) 120    C) 15    D) 10",
    "Qual è l'output del seguente codice?\n\nint[] arr = {2, 3, 5, 7};\nint result = 1;\nfor(int i = 0; i < arr.length; i++) {\n    result *= arr[i];\n}\nSystem.out.println(result);\n\nA) 210    B) 105    C) 384    D) 256"
});

        
    }

    private void initializeAnswers() {
        answers = new HashMap<>();

        // Risposte Facili
answers.put("Facile", new String[]{"B", "C", "B"});

// Risposte Medie
answers.put("Medio", new String[]{"A", "B", "C"});

// Risposte Difficili
answers.put("Difficile", new String[]{"B", "A", "A"});
    }

    @Override
    public String getDescription(String difficolta) {
        return "In questo esercizio, dovrai prevedere l'output del codice fornito, se rispondi correttamente a tutte e 3 le domanda potrai accedere alla difficoltà successiva, altrimenti dovrai rifare l'esercizio. Attenzione al tempo rimanente " + difficolta + ".";
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
    public void saveResult(String username, boolean success, String difficulty) throws IOException {
        String exerciseName = "PrevediOutput";  // Nome dell'esercizio (es. "Prevedi Output")
        String status = success ? "Superato" : "Fallito";  // Stato del risultato (superato/fallito)

        // Costruisci la stringa che rappresenta il risultato
        String result = username + ", Esercizio " +  ": " + exerciseName + ", " + status + ", Difficoltà: " + difficulty;

        // Crea una cartella per l'utente, se non esiste
        File userDirectory = new File(username);
        if (!userDirectory.exists()) {
            userDirectory.mkdir();  // Crea la cartella per l'utente
        }

        // Specifica il percorso del file all'interno della cartella dell'utente
        String filePath = username + "/risultati.txt";

        // Salva il risultato su file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.write(result);
            writer.newLine();
            System.out.println("Risultato salvato con successo: " + result);
        } catch (IOException e) {
            System.err.println("Errore durante il salvataggio del risultato: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public void finestraEsercizio(int index, String difficulty, VBox layout, Object[] campoInput) {
        // Domanda
        Label questionLabel = new Label(questions.get(difficulty)[index]);
        questionLabel.setWrapText(true);
    
        // Imposta una dimensione fissa per il testo del Label
        questionLabel.setStyle("-fx-font-size: 14px; " +  // Modifica la dimensione del testo come desiderato
                               "-fx-text-fill: #FFD700; " +  // Colore oro per il testo
                               "-fx-font-weight: bold; " +   // Grassetto
                               "-fx-effect: dropshadow(gaussian, black, 5, 0.5, 1, 1);");  // Effetto ombra
    
        // Gruppo di Toggle per le risposte
        ToggleGroup group = new ToggleGroup();
    
        // Opzioni di risposta con legame sulla dimensione del testo
        RadioButton optionA = createResponsiveRadioButton("A", "#FF6347", layout); // Rosso
        RadioButton optionB = createResponsiveRadioButton("B", "#32CD32", layout); // Verde lime
        RadioButton optionC = createResponsiveRadioButton("C", "#1E90FF", layout); // Blu
        RadioButton optionD = createResponsiveRadioButton("D", "#FFD700", layout); // Oro
    
        // Aggiungi le opzioni al gruppo Toggle
        optionA.setToggleGroup(group);
        optionB.setToggleGroup(group);
        optionC.setToggleGroup(group);
        optionD.setToggleGroup(group);
    
        // HBox per disporre le opzioni affiancate
        HBox optionsBox = new HBox(10); // 10 è lo spazio tra i pulsanti
        optionsBox.setAlignment(Pos.BOTTOM_LEFT); // Centra gli elementi
        optionsBox.getChildren().addAll(optionA, optionB, optionC, optionD);
    
        // Layout del quiz: sfondo colorato per migliorare la visibilità
        layout.setStyle("-fx-background-color: #2F4F4F; " +  // Sfondo grigio scuro
                        "-fx-padding: 20px; " +              // Padding interno
                        "-fx-border-color: #FFD700; " +      // Bordo dorato
                        "-fx-border-width: 3px; " +          // Spessore del bordo
                        "-fx-border-radius: 10px;");         // Angoli arrotondati
    
        // Rendi il VBox ridimensionabile
        VBox.setVgrow(questionLabel, javafx.scene.layout.Priority.ALWAYS);
        VBox.setVgrow(optionsBox, javafx.scene.layout.Priority.ALWAYS);
    
        layout.getChildren().clear();
        layout.getChildren().addAll(questionLabel, optionsBox);
    
        campoInput[0] = group;
    
        // Imposta l'indice della domanda corrente
        setQuestionIndex(index);
    }
    
    // Funzione per creare un RadioButton reattivo con colore fisso
    private RadioButton createResponsiveRadioButton(String text, String color, VBox layout) {
        RadioButton radioButton = new RadioButton(text);
        radioButton.setStyle("-fx-font-size: 14px; " + // Modifica la dimensione del testo come desiderato
                             "-fx-text-fill: " + color + ";");
        return radioButton;
    }
    
    public void setQuestionIndex(int index) {
        currentQuestionIndex = index;
        // Debugging: Verifica che l'indice della domanda venga impostato correttamente
        System.out.println("Indice della domanda impostato su: " + currentQuestionIndex);
    }
    
    public void showResult(String username, boolean success, String difficulty) {
        String status = success ? "Superato" : "Fallito";
        String message = "Utente: " + username + "\n" +
                         "Esercizio: Prevedi Output\n" +
                         "Stato: " + status + "\n" +
                         "Difficoltà: " + difficulty;
    
        // Crea una finestra di dialogo per mostrare il risultato
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Risultato dell'esercizio");
        alert.setHeaderText("Risultato finale");
        alert.setContentText(message);
        alert.showAndWait();
    }
}
    
