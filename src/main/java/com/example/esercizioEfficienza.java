package com.example;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class esercizioEfficienza extends Exercise {

    private Map<String, String[]> Testo; // Contiene le domande
    private Map<String, String[]> Risposte; // Contiene le risposte corrette
    private String[] risposteUtente; // Contiene le risposte dell'utente
   
    private int currentQuestionIndex = 0; //TODO: controlla se viene azzerato ecc
    private int totalQuestions;

    public esercizioEfficienza() {
        super("Selezione dell'algoritmo più efficiente", new String[]{"Facile", "Medio", "Difficile"}, new int[]{3, 3, 3});
        totalQuestions = getNEsercizi("Facile") + getNEsercizi("Medio") + getNEsercizi("Difficile");
        
        int n = Math.max(getNEsercizi("Facile"), Math.max(getNEsercizi("Medio"), getNEsercizi("Difficile"))); //come lunghezza del vettore risposte date imposto il numero più alto di domande che c'è
        risposteUtente = new String[n];
        Inizializzazione();
    }

    @Override
    public boolean checkAllAnswers(String Difficolta) {
        String[] giuste = Risposte.get(Difficolta);
        for (int i = 0; i < getNEsercizi(Difficolta); i++) {
            if (risposteUtente[i] == null) return false; 
            else if (risposteUtente[i].isEmpty()) return false; 
            else if (!risposteUtente[i].equals(giuste[i])) {
                return false;
            }
        }
        return true; // Restituisce true se tutte le risposte sono corrette
    }

    @Override
    public String getDescription(String difficolta) {
        switch (difficolta) {
            case "Facile":
                return "Dato un problema e due metodi che lo risolvono con implementazioni differenti (una più efficiente e una meno), ordinare i codici dal più al meno efficiente in termini di tempo. Es. a,b ";
               
            case "Medio":
                return "Dato un problema e tre metodi con implementazioni differenti, sia per istruzioni sia per strutture dati utilizzate, ordinare i codici dal più al meno efficiente in termini di tempo ed efficienza nello spazio. Es. bca";
             
            case "Difficile":
                return "Dato un problema ed il relativo contesto, considerare quattro metodi con implementazioni differenti per istruzioni e/o per strutture dati utilizzate, ordinare i codici dal più al meno efficiente in termini di tempo e spazio, tenendo conto del contesto. Es. cdab";
             
            default:
                return " "; // mi sembrava carino
        }
    }    

    @Override
    public void saveResult(String username, boolean success, String difficulty) throws IOException { ///TODO: si potrebbe mettere comune a tutti?
        String fileName = username + "_results.txt";
        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName, true))) {
            writer.println("Nome Utente: " + username);
            writer.println("Esercizio:" + this.getName());
            writer.println("Difficoltà: " + difficulty);
            writer.println("Risultato: " + (success ? "Superato" : "Fallito"));
            writer.println();
        }
    }
   
    public void setQuestionIndex(int index) { 
        currentQuestionIndex = index;
       
        System.out.println("Indice della domanda impostato su: " + currentQuestionIndex);
    }

    @Override
    public void registraRisposta(String risposta, String difficulty) {
        risposteUtente[currentQuestionIndex] = risposta;
    }

    @Override
    public void finestraEsercizio(int index, String difficulty, VBox layout, Object[] campoInput) {
        TextArea textArea = new TextArea();
        textArea.setMaxHeight(80);
        textArea.setMinHeight(30);
        campoInput[0] = textArea;  
                
        int n;
        if (difficulty.equals(getDifficulties()[0])) n = 3; 
        else if (difficulty.equals(getDifficulties()[1])) n = 4;
        else if (difficulty.equals(getDifficulties()[2])) n = 5;
        else n = 0;

        int puntatore = index * n;

        String[] testoArray = Testo.get(difficulty);
        if (testoArray == null || testoArray.length == 0) {
            showAlert("Errore", "Nessun testo disponibile per la difficoltà " + difficulty + ".");
            return;
        }
        if (puntatore >= testoArray.length) {
            showAlert("Errore", "Indice della domanda fuori dai limiti.");
            return;
        }

        Label contesto = new Label(testoArray[puntatore]);
        contesto.setTextFill(Color.web("#FFD700"));
        layout.getChildren().add(contesto);

        ScrollPane conteiner = new ScrollPane();
        HBox contenitoreCodice = new HBox();

        contenitoreCodice.setSpacing(30);  
        Label codice;
        contesto.setWrapText(true);
        
        for (int i = 1; i < n; i++) {
            if ((puntatore + i) >= testoArray.length) {
                showAlert("Errore", "Errore nel caricamento delle opzioni di codice.");
                return;
            }
            codice = new Label(testoArray[puntatore + i]); 
            codice.setWrapText(true);
            contenitoreCodice.getChildren().add(codice);
        }
        conteiner.setContent(contenitoreCodice);
        layout.setSpacing(10);
        layout.getChildren().add(conteiner); 
        layout.getChildren().add(((Node) campoInput[0]));
        
        setQuestionIndex(index); // Potevamo o fare così o aggiungere un parametro a registraRisposta. Abbiamo preferito così
    }

    private void Inizializzazione() {
        Risposte = new HashMap<>();
        Testo = new HashMap<>();
        String[] difficolta = getDifficulties();
        int[] nElementi = {3, 4, 5};
        String[] file;
        int cont;
        int q;
        for (int i = 0; i < 3; i++) {
            file = Lettore(difficolta[i]); // leggo il file per una data difficolta
            if (file == null || file.length == 0) {
                showAlert("Errore", "Impossibile caricare i dati per la difficoltà " + difficolta[i] + ".");
                continue;
            }
            System.out.println("N vett: " + file.length);
        
            cont = 0;
            StringBuilder testo = new StringBuilder();
            StringBuilder risposte = new StringBuilder();
            while (cont < file.length) {
                risposte.append(file[cont].replaceAll("\\s+", "")).append("\n").append("\n"); 
                cont++;
                q = 0;
                while (q < nElementi[i]) { // leggo gli elementi che devono appartenere al testo e li aggiungo 
                    if (cont >= file.length) {
                        showAlert("Errore", "Formato del file non valido per la difficoltà " + difficolta[i] + ".");
                        break;
                    }
                    testo.append(file[cont]).append("\n").append("\n");
                    q++;  
                    cont++;
                }           
            }

            System.out.println("N risposte in STRINGBUILDER: "  + risposte.toString().split("\n\n").length);
            System.out.println("N Testo in STRINGBUILDER: " + testo.toString().split("\n\n").length); 
                         
            Risposte.put(difficolta[i], risposte.toString().split("\n\n")); // carico le risposte e il testo
            Testo.put(difficolta[i], testo.toString().split("\n\n")); 
            System.out.println("N risposte in HashMap: " + Risposte.get(difficolta[i]).length);
            System.out.println("N testo in HashMap: " + Testo.get(difficolta[i]).length); 
        }
    }

    private String[] Lettore(String difficolta) { 
        String percorso = "TestoEfficienza/";
        switch (difficolta) {
            case "Facile":
                percorso = percorso + "Facile";
                break;
            case "Medio":
                percorso = percorso + "Medio";
                break;
            case "Difficile":
                percorso = percorso + "Difficile";
                break;
            default:
                showAlert("Errore", "Difficoltà non valida: " + difficolta);
                return new String[0];
        }

        File file = new File(percorso);
        if (!file.exists()) {
            showAlert("Errore", "Il file " + percorso + " non esiste.");
            return new String[0];
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String linea;
            StringBuilder contenuto = new StringBuilder();
            while ((linea = br.readLine()) != null) {
                contenuto.append(linea).append("\n");
            }
            return contenuto.toString().split("\n\n"); 
        } catch (IOException e) {
            System.out.println("Errore in lettura Snippet, StackTrace segue");
            mostraEccezione(e); // Variabile aggiunta per gestire l'eccezione
            return new String[0];
        }
    }

    private void showAlert(String titolo, String messaggio) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titolo);
        alert.setHeaderText(null);
        alert.setContentText(messaggio);
        alert.showAndWait();
    }

    private void mostraEccezione(Exception eccezione) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Errore");
        alert.setHeaderText("Si è verificato un errore");
        alert.setContentText(eccezione.getMessage());
        alert.showAndWait();
        eccezione.printStackTrace();
    }
}
