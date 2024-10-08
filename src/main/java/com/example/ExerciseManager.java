package com.example;

import java.io.IOException;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextInputControl;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;



public class ExerciseManager {

    private Account account;
    private Exercise[] exercises;
    private PauseTransition timerUpdate;

    public ExerciseManager(Account account) {
        this.account = account;
        this.exercises = new Exercise[]{
            new CompleteTheCode(),
            new PrevediOutput(),
            new esercizioEfficienza()
             // Aggiungi il nuovo esercizio qui
        };

        // Carica i progressi dell'utente per tutti gli esercizi
        for (Exercise exercise : exercises) {
            try {
                exercise.loadProgress(account.getNome());
            } catch (IOException e) {
                System.out.println("Errore nel caricamento del progresso per " + exercise.getName() + ": " + e.getMessage());
            }
        }
    }

    public void show(Stage primaryStage) {
        primaryStage.setTitle("Benvenuto");
    
        // Benvenuto Label
        Label welcomeLabel = new Label("Benvenuto, " + account.getNome() + "!");
        welcomeLabel.setFont(Font.font("Arial", FontWeight.BOLD, 36));
        welcomeLabel.setTextFill(Color.web("#FFD700"));
        welcomeLabel.setAlignment(Pos.CENTER);
        welcomeLabel.setPadding(new Insets(10));
    
        // ListView per gli esercizi
        ListView<Exercise> exerciseListView = new ListView<>();
        exerciseListView.getItems().addAll(exercises);
        exerciseListView.setStyle("-fx-background-color: #2e2e2e; -fx-text-fill: #61dafb;");
    
        exerciseListView.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Exercise item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("-fx-background-color: #282c34; -fx-text-fill: #61dafb;");
                } else {
                    setText(item.getName());
                    setStyle(isSelected() ? 
                            "-fx-background-color: #61dafb; -fx-text-fill: #282c34; -fx-font-weight: bold;" :
                            "-fx-background-color: #282c34; -fx-text-fill: #61dafb;");
                }
            }
        });
    
        // ComboBox per la difficoltà
        ComboBox<String> difficultyComboBox = new ComboBox<>();
        difficultyComboBox.setPromptText("Seleziona difficoltà");
        difficultyComboBox.setStyle("-fx-background-color: #61dafb; -fx-text-fill: white; -fx-font-size: 14px;");
        exerciseListView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                difficultyComboBox.getItems().clear();
                for (String difficulty : newSelection.getDifficulties()) {
                    if (newSelection.canAccessDifficulty(difficulty)) {
                        difficultyComboBox.getItems().add(difficulty);
                    }
                }
            }
        });
    
        // Bottone per visualizzare l'esercizio
        Button viewExerciseButton = new Button("Visualizza Esercizio");
        viewExerciseButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 14px;");
    
        viewExerciseButton.setOnAction(e -> {
            Exercise selectedExercise = exerciseListView.getSelectionModel().getSelectedItem();
            if (selectedExercise != null) {
                String selectedDifficulty = difficultyComboBox.getValue();
                if (selectedDifficulty != null) {
                    showDescriptionWindow(selectedExercise, selectedDifficulty, primaryStage);
                } else {
                    showAlert("Errore", "Seleziona un livello di difficoltà.");
                }
            } else {
                showAlert("Errore", "Seleziona un esercizio.");
            }
        });
    
        // Bottone per logout
        Button logoutButton = new Button("Logout");
        logoutButton.setStyle("-fx-background-color: #FF5722; -fx-text-fill: white; -fx-font-size: 14px;");
    
        logoutButton.setOnAction(e -> {
            primaryStage.close();
            LoginPage reloginPage = new LoginPage();
            reloginPage.show(primaryStage);
        });
    
        // Layout principale
        VBox layout = new VBox(20);
        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-background-color: #2e2e2e;");
        layout.getChildren().addAll(welcomeLabel, exerciseListView, difficultyComboBox, viewExerciseButton);
    
        // Aggiungi i progress bar degli esercizi
        for (Exercise exercise : exercises) {
            layout.getChildren().add(exercise.getExerciseProgressBar());
        }
    
        layout.getChildren().add(logoutButton);
    
        // Crea e mostra la scena
        Scene scene = new Scene(layout, 600, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    

    private void showDescriptionWindow(Exercise exercise, String difficulty, Stage primaryStage) {
        Stage descriptionStage = new Stage();
        descriptionStage.setTitle("Descrizione dell'Esercizio");

        Label descriptionLabel = new Label(exercise.getDescription(difficulty));
        descriptionLabel.setWrapText(true);
            descriptionLabel.setFont(Font.font("Comic Sans MS", 14));
            descriptionLabel.setTextFill(Color.web("#FFFFFF"));
            
        Button backButton = new Button("Indietro");
        Button proceedButton = new Button("Prosegui");
            backButton.setStyle("-fx-background-color: #FF5722; -fx-text-fill: white;");
            proceedButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");

        backButton.setOnAction(e -> descriptionStage.close());

        proceedButton.setOnAction(e -> {
            descriptionStage.close();
            showExerciseWindow(exercise, difficulty, primaryStage);
        });

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));
            layout.setStyle("-fx-background-color: #282c34;");
        layout.getChildren().addAll(descriptionLabel, backButton, proceedButton);

        Scene scene = new Scene(layout, 400, 200);
        descriptionStage.setScene(scene);
        descriptionStage.show();
    }

    private void showExerciseWindow(Exercise exercise, String difficulty, Stage primaryStage) {
        Stage exerciseStage = new Stage();
        exerciseStage.setTitle(exercise.getName());
        int[] currentQuestionIndex = {0};
        
        //Intestazione Eserciizio
        Label exerciseLabel = new Label("Esercizio: " + exercise.getName());
            exerciseLabel.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 24));
            exerciseLabel.setTextFill(Color.web("#FFFFFF"));
        
        //Difficoltà
        Label difficultyLabel = new Label("Difficoltà: " + difficulty);
            difficultyLabel.setFont(Font.font("Comic Sans MS", 18));
            difficultyLabel.setTextFill(Color.web("#FFD700")); 

        
        //Bottoni e stile
        Button submitButton = new Button("Invia Risposta");
        Button exitButton = new Button("Esci dall’Esercizio");
        Label timerLabel = new Label();
            submitButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10px; -fx-cursor: hand;");
            exitButton.setStyle("-fx-background-color: #FF5722; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10px; -fx-cursor: hand;");
            timerLabel.setFont(Font.font("Comic Sans MS", 16));
            timerLabel.setTextFill(Color.web("#FFFFFF")); 

            //Layout Schermata Esercizio
        VBox layout = new VBox(20);
            layout.setPadding(new Insets(20));
            layout.setStyle("-fx-background-color: #2e2e2e;"); 
       
         VBox core = new VBox(); //contiene testo, contesto e descrizione esercizio. Da modificare a seconda delle esigenze nella classe di ciascun esercizio tramite metodo finestraEsercizio() 
           
         Object[] campoInput = new Object[1]; //Per gestire diversi tipi di input scelta multipla, testo etc
            
            // Chiamare finestraEsercizio per visualizzare la domanda iniziale
            exercise.finestraEsercizio(currentQuestionIndex[0], difficulty, core, campoInput);
           
            
        layout.getChildren().addAll(exerciseLabel, difficultyLabel, core,  
                                      submitButton, exitButton, timerLabel);
    
        Scene scene = new Scene(layout, 850, 750);
        exerciseStage.setScene(scene);
        exerciseStage.show();
    
        // Inizializzare il timer
        startTimer(timerLabel, difficulty, exercise, exerciseStage);
       
        submitButton.setOnAction(e -> {
           

            try {
                String userInput = letturaInput(campoInput); //metodo separato per leggere input (differenzia se risposta multipla o testo) pulisce anche i campi (deseleziona togglebar e .clear() per textArea())
               
                exercise.registraRisposta(userInput, difficulty);
    
                if (currentQuestionIndex[0] < exercise.getNEsercizi(difficulty)-1) { //Da codice originale, non sono sicuro di perchè 2, probabilmente serve var per numero esercizi in ogni livello di difficoltà?
                    currentQuestionIndex[0]++;
                        core.getChildren().clear(); //pulisco ciò che viene disegnato dall'esercizio
                    exercise.finestraEsercizio(currentQuestionIndex[0], difficulty, core, campoInput);
                    //       updateQuestion(exercise, currentQuestionIndex[0], questionLabel); --> Da modificare 
                   
                } else {
                    if (timerUpdate != null) {
                        timerUpdate.stop();  // Aggiungi questa riga per fermare il timer
                    }
                    boolean success = exercise.checkAllAnswers(difficulty);
                    if (success) {
                        exercise.completeDifficulty(difficulty, account.getNome());
                        showAlert("Successo", "Hai completato correttamente tutte le domande! Livello completato.");
                    } else {
                        showAlert("Errore", "Non tutte le risposte sono corrette. Riprova.");
                    }
                    try {
                        exercise.saveResult(account.getNome(), success, difficulty);
                    } catch (IOException ex) {
                        showAlert("Errore", "Impossibile salvare il risultato.");
                    }
                    
                    exerciseStage.close();
            }} 
            catch (NumberFormatException ex) {
                showAlert("Errore", "Inserisci una risposta valida."); ///TODO: mettere metodo per sanificare input (eliminarespazi, virgole, tab e a capo)
            }
        
        });
    
        exitButton.setOnAction(e -> {

            if (timerUpdate != null) {
                timerUpdate.stop();  // Aggiungi questa riga per fermare il timer
            }
    
            if (!exercise.checkAllAnswers(difficulty)) {
                showAlert("Fallimento", "Hai abbandonato l'esercizio. Sarà considerato come fallito.");
                try {
                    exercise.saveResult(account.getNome(), false, difficulty);
                } catch (IOException ex) {
                    showAlert("Errore", "Impossibile salvare il risultato.");
                }
            }
                       
            exerciseStage.close();
        });
    }

    private String letturaInput(Object[] campoInput){
        String userInput = null;

        if (campoInput[0] instanceof TextInputControl) {  // per il testo
           
            userInput = ((TextInputControl) campoInput[0]).getText().replaceAll("[^a-zA-Z]", "").toLowerCase(); //pulisce il testo 
           ((TextInputControl) campoInput[0]).clear();

        } else if (campoInput[0] instanceof ToggleGroup) { // per scelta multipla
            ToggleGroup group = (ToggleGroup) campoInput[0];

            RadioButton selectedButton = (RadioButton) group.getSelectedToggle();
                userInput = selectedButton.getText();
               // System.out.println(userInput);
              ((ToggleGroup) campoInput[0]).getSelectedToggle().setSelected(false);
                }
        return userInput;
    }

    private void startTimer(Label timerLabel, String difficulty, Exercise exercise, Stage exerciseStage) {
    int timeLimit;

    switch (difficulty) {
        case "Facile":
            timeLimit = 100;
            break;
        case "Medio":
            timeLimit = 120;
            break;
        case "Difficile":
            timeLimit = 180;
            break;
        default:
            timeLimit = 60;
            break;
    }

    final int[] timeRemaining = {timeLimit}; // Tempo rimanente

    // Inizializza la variabile timerUpdate
    timerUpdate = new PauseTransition(Duration.seconds(1));
    timerUpdate.setOnFinished(event -> {
        if (timeRemaining[0] > 0) {
            timeRemaining[0]--;
            timerLabel.setText("Tempo rimanente: " + timeRemaining[0] + " secondi");
            timerUpdate.playFromStart(); // Riavvia il timer
        } else {
            // Tempo scaduto
            Platform.runLater(() -> { 
                showAlert("Tempo Scaduto", "Il tempo per completare l'esercizio è scaduto.");
                exerciseStage.close();
            });

            try {
                // Salva il risultato come fallito
                exercise.saveResult(account.getNome(), false, difficulty);
            } catch (IOException e) {
                showAlert("Errore", "Impossibile salvare il risultato.");
            }
        }
    });

    // Avvia il timer
    timerUpdate.play();
    // Ferma il timer quando l'esercizio è completato
    exerciseStage.setOnCloseRequest(event -> {
        timerUpdate.stop(); // Ferma il timer se la finestra viene chiusa
    });
}

      
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
