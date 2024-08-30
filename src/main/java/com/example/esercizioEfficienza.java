package com.example;

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
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;


public class esercizioEfficienza extends Exercise {

    private String[] answers;
    private String[] correctAnswers;
    private int currentQuestionIndex; //TODO: controlla se viene azzerato ecc
    private int totalQuestions;

    public esercizioEfficienza() {
        super("Selezione dell'algoritmo più efficiente", new String[]{"Facile", "Medio", "Difficile"});
      
        totalQuestions = 3; // Numero totale di domande  
        this.correctAnswers = new String[3];
                correctAnswers[0] = "a";
                correctAnswers[1] = "a";
                correctAnswers[2] = "a";
        this.answers = new String[totalQuestions];
        resetExercise();
    }

    @Override
    public void resetExercise() {  //TODO: da rivedere 
        answers = new String[totalQuestions]; // Array per le risposte dell'utente
        correctAnswers = new String[totalQuestions]; // Array per le risposte corrette
        currentQuestionIndex = 0;
    }

    

    @Override
    public boolean checkAllAnswers() {
        
        for (int i = 0; i < totalQuestions; i++) {
            if (answers[i] != correctAnswers[i]) {
                    System.out.println(i);
                    System.out.println(answers[i]);
                    System.out.println(correctAnswers[i]); //da null
                return false; // Restituisce false se almeno una risposta è sbagliata
            }
        }
        return true; // Restituisce true se tutte le risposte sono corrette
    }

    @Override
    public void registraRisposta(String risposta) {
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
         campoInput[0] = new TextArea();   
            
        //TODO modifica tutto
            // layout.getChildren().clear(); //Questo è quello che cancellava contatori ecc
    
        //Label exerciseLabel = new Label("Esercizio: " + getName());
        //Label difficultyLabel = new Label("Difficoltà: " + difficulty);
    
        String fileName = getTextFile(difficulty);
        String[] Potentialsnippets = readSnippetsFromFile(fileName);
         String[] snippets = {Potentialsnippets[index], Potentialsnippets[index+1]};

         /*
        if (snippets == null ){
            System.out.println("snippets sono null");
        //if(snippets.length != 4){
          //  System.out.println("Ci sono " + snippets.length +"snippet invece che i 4 attesi");
            //}
        
        }*/
        if (snippets != null ) {
           HBox hBox = new HBox(); //Lo utilizziamo per posizionare gli snippet uno a fianco all-altro invece che impilati 
        hBox.setSpacing(10);  
        
        int n;
            if(difficulty == "Facile") n = 2;
            else n=2;
        int i = 0;
        while (i < n) {    
        Label codice = new Label("Metodo "+ (i+1) +":\n" + snippets[i]);
           // codice.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 30));
            codice.setTextFill(Color.web("#FFD700")); 
        i++;
        hBox.getChildren().addAll(codice);
        }
        layout.getChildren().add(hBox); 
        //layout.getChildren().add((Node) campoInput[0]);
      

        } else {
            System.out.println("Errore: Snippets is null or not correctly formatted.");
            Label errorLabel = new Label("Errore nel caricamento del testo dell'esercizio.");
            layout.getChildren().addAll( errorLabel);

        }

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
