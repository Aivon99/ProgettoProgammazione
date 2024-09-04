package com.example;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;



import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;


public class esercizioProva extends Exercise {

    private String[] answers;
    private String[] correctAnswers;
    private int currentQuestionIndex; //TODO: controlla se viene azzerato ecc
    private int totalQuestions;

    public esercizioProva() {
        super("Prova con Toggle", new String[]{"Facile", "Medio", "Difficile"},new int[]{3, 3, 3});
      
        totalQuestions = 3; // Numero totale di domande  
        this.correctAnswers = new String[3];
                correctAnswers[0] = "a";
                correctAnswers[1] = "a";
                correctAnswers[2] = "a";
        this.answers = new String[totalQuestions];
        
    }

   

    

    @Override
    public boolean checkAllAnswers(String difficulty) {
        for (int i = 0; i < this.totalQuestions; i++) {
            if (answers[i].equals(this.correctAnswers[i])) {
                    System.out.println(i);
                    System.out.println(this.answers[i]);
                    System.out.println(this.correctAnswers[i]); //da null
                return false; // Restituisce false se almeno una risposta è sbagliata
            }
        }
        return true; // Restituisce true se tutte le risposte sono corrette
    }
    @Override
    public void registraRisposta(String risposta, String difficulty) {
        if (currentQuestionIndex < totalQuestions) {
            answers[currentQuestionIndex] = risposta; // Registra la risposta dell'utente
            currentQuestionIndex++; //rimane locale
        }
    }

    @Override
    public void handleExerciseSuspension() {
        // Se l'esercizio è sospeso prima di completare tutte le domande, lo consideriamo come fallito.
        // Questo metodo può essere personalizzato ulteriormente in base alle necessità.
    }

    /* Messo a commento il 28 agosto, insieme a dichiarazione abstract in Exercise.java
    @Override
    public String getExerciseDetails(int index) {
       
            return "Domanda non valida.";
        
    }
     */
    @Override
    public String getDescription(String difficolta) {
        switch (difficolta) {
            case "Facile":
                return "Descrizione Facile";
               
            case "Medio":
                return "descrizione Medio";
             
            case "Difficile":
                return "Descrizione Difficile ";
             
            default:
                return " "; // mi sembrava carino
             
        }
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
    public void finestraEsercizio(int index, String difficulty, VBox layout, Object[] campoInput) {
        // Create a ToggleGroup
        ToggleGroup toggleGroup = new ToggleGroup();
        campoInput[0] = toggleGroup; // Still store the ToggleGroup if needed elsewhere
    
        // Create ToggleButtons
        RadioButton toggleButton1 = new RadioButton("PROVA 1");
        RadioButton toggleButton2 = new RadioButton("Prova 2");
        RadioButton toggleButton3 = new RadioButton("Prova 3");
        RadioButton toggleButton4 = new RadioButton("Prova 4");
        toggleButton1.setStyle("-fx-text-fill: red;");
        toggleButton2.setStyle("-fx-text-fill: red;");
        toggleButton3.setStyle("-fx-text-fill: red;");
        toggleButton4.setStyle("-fx-text-fill: red;");
        
        // Assign the ToggleGroup to each RadioButton
        toggleButton1.setToggleGroup(toggleGroup);
        toggleButton2.setToggleGroup(toggleGroup);
        toggleButton3.setToggleGroup(toggleGroup);
        toggleButton4.setToggleGroup(toggleGroup);
        
        // Create an HBox or VBox to hold the ToggleButtons
        VBox toggleContainer = new VBox(10); // 10 is the spacing between buttons
        toggleContainer.getChildren().addAll(toggleButton1, toggleButton2, toggleButton3, toggleButton4);
        
        // Add the HBox to the layout instead of the ToggleGroup
        layout.getChildren().add(toggleContainer);
    }
    
    
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
    protected String[] readSnippetsFromFile(String fileName) { //TODO: modifica per importare diversi numeri di snippet di codice, numeri di esercizi per livello di difficoltà ecc. 
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
