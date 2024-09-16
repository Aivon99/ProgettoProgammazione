package com.example;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
 
public class CompleteTheCode extends Exercise {
    private Map<String, String[]> framCodice; // Frammenti di codice con parti mancanti
    private Map<String, String[]> risposteEsatte; // Soluzioni corrette
    private Map<String, String[]> risposteUtente; // Contiene le risposte dell'utente
    private Map<String, String[][]> opzioniPerDomanda; // Opzioni per ogni frammento
    private Map<String, String[]> descrizioni;
    private int indiceDomanda;
 
    public CompleteTheCode() {
        super("Completa il Codice", new String[]{"Facile", "Medio", "Difficile"}, new int[]{3, 3, 3});
        indiceDomanda = 0;
        inizializzaEsercizio();
    }
 
    public void caricaFrammentiDaFile(String percorsoFile, String difficolta) {
        try (Scanner scanner = new Scanner(new FileReader(percorsoFile))) {
            StringBuilder frammento = new StringBuilder();
            List<String> frammenti = new ArrayList<>();
 
            while (scanner.hasNextLine()) {
                String linea = scanner.nextLine();
            
                if (linea.trim().isEmpty()) {
                    if (frammento.length() > 0) {
                        frammenti.add(frammento.toString());
                        frammento.setLength(0);  // Ripulisci per il prossimo frammento
                    }
                } else {
                    frammento.append(linea).append("\n");
                }
            }
 
            if (frammento.length() > 0) {
                frammenti.add(frammento.toString());
            }
 
            // Salva i frammenti nel Map
            framCodice.put(difficolta, frammenti.toArray(new String[0]));
 
        } catch (IOException e) {
            showAlert("Errore", "Errore durante il caricamento del codice dal file: " + e.getMessage());
        }
    }
 
 
    public void caricaOpzioniDaFile(String percorsoFile, String difficolta) {
        try (Scanner scanner = new Scanner(new FileReader(percorsoFile))) {
            List<String[]> opzioniList = new ArrayList<>();
            StringBuilder opzioni = new StringBuilder();
    
            while (scanner.hasNextLine()) {
                String linea = scanner.nextLine();
                
                if (linea.trim().isEmpty()) {
                    if (opzioni.length() > 0) {
                        opzioniList.add(opzioni.toString().split(","));
                        opzioni.setLength(0);
                    }
                } else {
                    opzioni.append(linea).append(",");
                }
            }
            if (opzioni.length() > 0) {
                opzioniList.add(opzioni.toString().split(","));
            }
            opzioniPerDomanda.put(difficolta, opzioniList.toArray(new String[0][]));
    
        } catch (IOException e) {
            showAlert("Errore", "Errore durante il caricamento delle opzioni dal file: " + e.getMessage());
        }
    }
    
    public void caricaDescrizioniDaFile(String percorsoFile, String difficolta) {
        try (Scanner scanner = new Scanner(new FileReader(percorsoFile))) {
            StringBuilder descrizione = new StringBuilder();
            List<String> descrizioniList = new ArrayList<>();
            
            while (scanner.hasNextLine()) {
                String linea = scanner.nextLine();
                
                if (linea.trim().isEmpty()) {
                    if (descrizione.length() > 0) {
                        descrizioniList.add(descrizione.toString().trim());
                        descrizione.setLength(0);
                    }
                } else {
                    descrizione.append(linea).append("\n");
                }
            }
            if (descrizione.length() > 0) {
                descrizioniList.add(descrizione.toString().trim());
            }
            descrizioni.put(difficolta, descrizioniList.toArray(new String[0]));
            
        } catch (IOException e) {
            showAlert("Errore", "Errore durante il caricamento delle descrizioni dal file: " + e.getMessage());
        }
    }
    
    
    public void inizializzaEsercizio() {
        framCodice = new HashMap<>();
        risposteEsatte = new HashMap<>();
        risposteUtente = new HashMap<>();
        opzioniPerDomanda = new HashMap<>();
        descrizioni = new HashMap<>();
        
        
        caricaFrammentiDaFile("FileTestualiMassi/Facile.txt", "Facile");
        caricaFrammentiDaFile("FileTestualiMassi/Media.txt", "Medio");
        caricaFrammentiDaFile("FileTestualiMassi/Difficile.txt", "Difficile");
        caricaOpzioniDaFile("FileTestualiMassi/OpzioniFacile.txt", "Facile");
        caricaOpzioniDaFile("FileTestualiMassi/OpzioniMedio.txt", "Medio");
        caricaOpzioniDaFile("FileTestualiMassi/OpzioniDifficile.txt", "Difficile");
        caricaDescrizioniDaFile("FileTestualiMassi/DescrizioniFacile.txt", "Facile");
        caricaDescrizioniDaFile("FileTestualiMassi/DescrizioniMedio.txt", "Medio");
        caricaDescrizioniDaFile("FileTestualiMassi/DescrizioniDifficile.txt", "Difficile");
 
        
        risposteEsatte.put("Facile", new String[]{"String/System.out.println","int / a + b / println / sum","for / i / ++ / System.out.println(i);"});
 
        risposteEsatte.put("Medio", new String[]{
            "0 / length / j / 9 / i / 0 / 0 / i / j",
            "1 / factorial(n - 1) / factorial / result",
            "7 / false / 0 / i / target / true / trovato / non trovato"});
   
        risposteEsatte.put("Difficile", new String[]{
            "String / name / extends / super(name) / return",
            "import / Scanner / try / Int / catch / Exception",
            "ListIterator / String / Iterator / Next / ListIterator / Banana / set / :"});
    }
 
    @Override
    public boolean checkAllAnswers(String difficulty) {
        String[] correctAnswers = risposteEsatte.get(difficulty);
        String[] givenAnswers = risposteUtente.get(difficulty);
 
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
        if (risposteUtente.get(difficulty) == null) {
            risposteUtente.put(difficulty, new String[getNEsercizi(difficulty)]);
        }
        risposteUtente.get(difficulty)[indiceDomanda] = risposta;
        
        // Debugging: Verifica che la risposta venga registrata correttamente
        System.out.println("Registrata risposta: " + risposta + " all'indice della domanda: " + indiceDomanda);
    }
 
 
    @Override
    public String getDescription(String description) {
        return "In questo esercizio, dovrai completare il codice seguendo le specifiche fornite. Ogni livello di difficoltà aumenta la complessità dei problemi di programmazione.";
    }
    @Override
    public void saveResult(String username, boolean success, String difficulty) throws IOException {
        String exerciseName = "Completa il Codice";  
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
        layout.getChildren().clear();
        String[] codice = framCodice.get(difficulty);
        if (codice == null || codice.length <= index) {
            showAlert("Errore", "Frammento di codice non trovato per l'indice specificato.");
            return;
        }
 
        String[] descrizioniPerDifficolta = descrizioni.get(difficulty);
        String descrizione = descrizioniPerDifficolta != null && descrizioniPerDifficolta.length > index  ? descrizioniPerDifficolta[index] : "Descrizione non disponibile";
        Label descrizioneLabel = new Label(descrizione);
        descrizioneLabel.setTextFill(Color.WHITE);
        descrizioneLabel.setFont(Font.font("Comic Sans MS", 14));
        descrizioneLabel.setWrapText(true); // testo su più righe
       
        
        
        
        TextArea codeSnippetArea = new TextArea(codice[index]);
        codeSnippetArea.setEditable(false);
        codeSnippetArea.setWrapText(true);
        codeSnippetArea.setPrefHeight(200);
        codeSnippetArea.setStyle("-fx-background-color: #333333; -fx-text-fill: #000000;-fx-font-size: 16px;");
        
 
        ToggleGroup optionsGroup = new ToggleGroup();
        campoInput[0] = optionsGroup;
 
        String[][] opzioni = opzioniPerDomanda.get(difficulty);
        String[] opzioniCorrenti = opzioni[index];
 
        RadioButton optionA = new RadioButton(opzioniCorrenti[0]);
        RadioButton optionB = new RadioButton(opzioniCorrenti[1]);
        RadioButton optionC = new RadioButton(opzioniCorrenti[2]);
 
        optionA.setToggleGroup(optionsGroup);
        optionB.setToggleGroup(optionsGroup);
        optionC.setToggleGroup(optionsGroup);
 
        optionA.setTextFill(Color.WHITE);
        optionB.setTextFill(Color.WHITE);
        optionC.setTextFill(Color.WHITE);
 
        optionA.setFont(Font.font("Consolas", 14));
        optionB.setFont(Font.font("Consolas", 14));
        optionC.setFont(Font.font("Consolas", 14));
 
        VBox optionsBox = new VBox(10);
        optionsBox.getChildren().addAll(optionA, optionB, optionC);
        optionsBox.setAlignment(Pos.CENTER_LEFT);
        optionsBox.setPadding(new Insets(10, 0, 0, 20));
 
        optionsGroup.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
            if (newToggle != null) {
                RadioButton selected = (RadioButton) newToggle;
                registraRisposta(selected.getText(), difficulty);
            }
        });
 
        layout.getChildren().addAll(descrizioneLabel,codeSnippetArea, optionsBox);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.TOP_LEFT);
        layout.setStyle("-fx-background-color: #2e2e2e;");
 
        // Aggiorna l'indice della domanda
        setQuestionIndex(index);
    }
 
    public void setQuestionIndex(int index) {
        indiceDomanda = index;
        // Debugging: Verifica che l'indice della domanda venga impostato correttamente
        System.out.println("Indice della domanda impostato su: " + indiceDomanda);
    }
 
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}