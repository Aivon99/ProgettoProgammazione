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
    private String[] testo;

    public esercizioProva() {
        super("Prova con Toggle", new String[]{"Facile", "Medio", "Difficile"},new int[]{3, 3, 3});
      
        totalQuestions = this.getNEsercizi("Facile") + this.getNEsercizi("Medio") + this.getNEsercizi("Difficile"); // Numero totale di domande  
        this.correctAnswers = new String[9];
                correctAnswers[0] = "PROVA 1";
                correctAnswers[1] = "PROVA 5";
                correctAnswers[2] = "PROVA 9";
                correctAnswers[3] = "PROVA 13";
                correctAnswers[4] = "PROVA 17";
                correctAnswers[5] = "PROVA 21";
                correctAnswers[6] = "PROVA 25";
                correctAnswers[7] = "PROVA 29";
                correctAnswers[8] = "PROVA 33";
        this.answers = new String[totalQuestions];
       this.testo = new String[totalQuestions * 4];
            testo[0] = "PROVA 1";
            testo[1] = "PROVA 2";
            testo[2] = "PROVA 3";
            testo[3] = "PROVA 4";
            testo[4] = "PROVA 5";
            testo[5] = "PROVA 6";
            testo[6] = "PROVA 7";
            testo[7] = "PROVA 8";
            testo[8] = "PROVA 9";
            testo[9] = "PROVA 10";
            testo[10] = "PROVA 11";
            testo[11] = "PROVA 12";
            testo[12] = "PROVA 13";
            testo[13] = "PROVA 14";
            testo[14] = "PROVA 15";
            testo[15] = "PROVA 16";
            testo[16] = "PROVA 17";
            testo[17] = "PROVA 18";
            testo[18] = "PROVA 19";
            testo[19] = "PROVA 20";
            testo[20] = "PROVA 21";
            testo[21] = "PROVA 22";
            testo[22] = "PROVA 23";
            testo[23] = "PROVA 24";
            testo[24] = "PROVA 25";
            testo[25] = "PROVA 26";
            testo[26] = "PROVA 27";
            testo[27] = "PROVA 28";
            testo[28] = "PROVA 29";
            testo[29] = "PROVA 30";
            testo[30] = "PROVA 31";
            testo[31] = "PROVA 32";
            testo[32] = "PROVA 33";
            testo[33] = "PROVA 34";
            testo[34] = "PROVA 35";
            testo[35] = "PROVA 36";
    }

  
    

    @Override
    public boolean checkAllAnswers(String difficolta) {
       // System.out.println(totalQuestions);
     //   System.out.println(this.answers.length);
        
        for (int i = 0; i < getNEsercizi(difficolta); i++) {
            
            if ( answers[i] == null || answers[i].isEmpty() || !answers[i].equals(this.correctAnswers[i])) {
                    System.out.println(i);
                    System.out.println(this.answers[i]);
                    System.out.println(this.correctAnswers[i]); 
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
      
        ToggleGroup toggleGroup = new ToggleGroup();
        campoInput[0] = toggleGroup; 
        VBox toggleContainer = new VBox(10);
        int myIndex = index * 4;
        if(difficulty == "Medio"){
            myIndex += getNEsercizi("Facile") * 4;
        }
        else if(difficulty == "Difficile"){
        
            myIndex += getNEsercizi("Facile") * 4 + getNEsercizi("Medio") *4;

        }

        for(int i = myIndex; i < myIndex +4; i++){
            RadioButton bottone = new RadioButton(testo[i]);
                bottone.setStyle("-fx-text-fill: red;");
            bottone.setToggleGroup(toggleGroup);
            toggleContainer.getChildren().addAll(bottone);
        }
     
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
