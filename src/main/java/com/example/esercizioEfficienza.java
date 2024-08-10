package com.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class esercizioEfficienza extends Exercise {

    private String[] answers;
    private String[] correctAnswers;
    private int currentQuestionIndex;
    private int totalQuestions;

    public esercizioEfficienza() {
        super("Selezione dell'algoritmo più efficiente", new String[]{"Facile", "Medio", "Difficile"});

        totalQuestions = 3; // Numero totale di domande
        resetExercise();
    }

    @Override
    public void resetExercise() {  //TODO: da modificare 

    }

    private void generateQuestions() {
        
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
            answers[currentQuestionIndex] = ""+answer; // Registra la risposta dell'utente
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
       
            return "Domanda non valida.";
        
    }

    @Override
    public String getDescription() {
        return "In questo esercizio, ti verranno presentate varie soluzioni a problemi (di ordinamento, ricerca e simili), indica quella computativamente più efficiente. In caso di parità scegli quella più efficiente a livello di memoria.";
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
    @Override
    public void finestraEsercizio(int index, String difficulty, VBox layout) {
        layout.getChildren().clear();
    
        Label exerciseLabel = new Label("Esercizio: " + getName());
        Label difficultyLabel = new Label("Difficoltà: " + difficulty);
    
        String fileName = getTextFile(difficulty);
        String[] snippets = readSnippetsFromFile(fileName);
        if (snippets == null ){
            System.out.println("snippets sono null");
        if(snippets.length != 4){
            System.out.println("Ci sono " + snippets.length +"snippet invece che i 4 attesi");
        }
        }
        if (snippets != null && snippets.length == 4) {
            Label snippet1 = new Label("Metodo 1:\n" + snippets[0]);
            Label snippet2 = new Label("Metodo 2:\n" + snippets[1]);
            Label snippet3 = new Label("Metodo 3:\n" + snippets[2]);
            Label snippet4 = new Label("Metodo 4:\n" + snippets[3]);
    
            layout.getChildren().addAll(exerciseLabel, difficultyLabel, snippet1, snippet2, snippet3, snippet4);
        } else {
            System.out.println("Errore: Snippets is null or not correctly formatted.");
            Label errorLabel = new Label("Errore nel caricamento del testo dell'esercizio.");
            layout.getChildren().addAll(exerciseLabel, difficultyLabel, errorLabel);
        }
    }
    
    //TODO: Problema in gestione Input/ricerca file, errore nel caricamento dell'esercizio sistematicamente, valuta testo hardcoded. 

    protected String getTextFile(String difficulty) {
        String basePath = "";  
        switch (difficulty.toLowerCase()) {
            case "facile":
                return basePath + "testoEfficienzaFacile.txt";
            case "medio":
                return basePath + "testoEfficienzaMedio.txt";
            case "difficile":
                return basePath + "testoEfficienzaDifficile.txt";
            default:
                return null;
        }
    }
    protected String[] readSnippetsFromFile(String fileName) {
        if (fileName == null) {
            return null;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            StringBuilder content = new StringBuilder();
            while ((line = br.readLine()) != null) {
                content.append(line).append("\n");
            }
            return content.toString().split("\n\n"); 
        } catch (IOException e) {
            System.out.println("Errore in lettura Snippet, StackTrace segue");
            e.printStackTrace();
            return null;
        }
            
    }
         

}
